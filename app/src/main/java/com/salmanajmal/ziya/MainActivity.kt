package com.salmanajmal.ziya

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.navigation.compose.rememberNavController
import com.salmanajmal.ziya.context.AuthContextProvider
import com.salmanajmal.ziya.context.ChatProvider
import com.salmanajmal.ziya.context.LocalAuthContext
import com.salmanajmal.ziya.context.LocalChatContext
import com.salmanajmal.ziya.data.preferences.UserPreferences
import com.salmanajmal.ziya.data.repository.AuthRepository
import com.salmanajmal.ziya.data.repository.ChatRepository
import com.salmanajmal.ziya.ui.navigation.AppNavigation
import com.salmanajmal.ziya.ui.navigation.Screen
import com.salmanajmal.ziya.ui.theme.DummyAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var chatRepository: ChatRepository

    @Inject
    lateinit var userPreferences: UserPreferences

    // Store pending navigation from notification
    private var pendingConversationId: String? = null
    
    // Store user ID for lifecycle reconnection
    private var currentUserId: String? = null
    
    // App lifecycle observer for background/foreground handling
    private val appLifecycleObserver = object : DefaultLifecycleObserver {
        override fun onStart(owner: LifecycleOwner) {
            // App came to foreground - reconnect socket
            currentUserId?.let { userId ->
                Log.d("MainActivity", "🟢 App came to foreground, reconnecting socket for user: $userId")
                chatRepository.connectSocket(userId)
            }
        }
        
        override fun onStop(owner: LifecycleOwner) {
            // App went to background - disconnect socket immediately
            Log.d("MainActivity", "🔴 App went to background, disconnecting socket")
            chatRepository.disconnectSocket()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Handle notification intent on initial launch
        handleIntent(intent)
        
        // Register app lifecycle observer for background/foreground detection
        ProcessLifecycleOwner.get().lifecycle.addObserver(appLifecycleObserver)
        
        enableEdgeToEdge()
        setContent {
            DummyAppTheme {
                AuthContextProvider(
                    authRepository = authRepository,
                    userPreferences = userPreferences
                ) {
                    ChatProvider(chatRepository = chatRepository) {
                        val authContext = LocalAuthContext.current
                        val chatContext = LocalChatContext.current
                        val authState by authContext.authState.collectAsState()
                        val navController = rememberNavController()
                        
                        // Track the pending conversation for navigation
                        var conversationToNavigate by remember { mutableStateOf(pendingConversationId) }

                        // Connect socket when user is authenticated
                        LaunchedEffect(authState.isAuthenticated, authState.user?.id) {
                            if (authState.isAuthenticated && authState.user?.id != null) {
                                val userId = authState.user!!.id
                                Log.d("MainActivity", "🔌 User authenticated, connecting socket for user: $userId")
                                // Store userId for lifecycle reconnection
                                currentUserId = userId
                                chatContext.connect(userId)
                            } else {
                                currentUserId = null
                            }
                        }

                        if (authState.isLoading) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        } else {
                            val startDestination = if (authState.isAuthenticated) {
                                Screen.Main.route
                            } else {
                                Screen.Login.route
                            }

                            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                                AppNavigation(
                                    modifier = Modifier,
                                    navController = navController,
                                    startDestination = startDestination
                                )
                            }
                            
                            // Use LaunchedEffect to handle navigation as a side effect
                            // This is the correct way to trigger navigation in Compose
                            LaunchedEffect(conversationToNavigate, authState.isAuthenticated) {
                                if (authState.isAuthenticated && conversationToNavigate != null) {
                                    val conversationId = conversationToNavigate!!
                                    Log.d("MainActivity", "Navigating to chat: $conversationId")
                                    
                                    navController.navigate(Screen.Chat.createRoute(conversationId)) {
                                        // Pop up to main to avoid back stack issues
                                        popUpTo(Screen.Main.route) { inclusive = false }
                                    }
                                    
                                    // Clear the pending navigation
                                    conversationToNavigate = null
                                    pendingConversationId = null
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        // Remove lifecycle observer to prevent memory leaks
        ProcessLifecycleOwner.get().lifecycle.removeObserver(appLifecycleObserver)
    }
    
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }
    
    private fun handleIntent(intent: Intent?) {
        intent?.let {
            val conversationId = it.getStringExtra("conversationId")
            val notificationType = it.getStringExtra("notificationType")
            
            if (conversationId != null) {
                Log.d("MainActivity", "Notification intent: type=$notificationType, conversationId=$conversationId")
                pendingConversationId = conversationId
            }
        }
    }
}
