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
import androidx.navigation.compose.rememberNavController
import com.salmanajmal.ziya.context.AuthContextProvider
import com.salmanajmal.ziya.context.ChatProvider
import com.salmanajmal.ziya.context.LocalAuthContext
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Handle notification intent on initial launch
        handleIntent(intent)
        
        enableEdgeToEdge()
        setContent {
            DummyAppTheme {
                AuthContextProvider(
                    authRepository = authRepository,
                    userPreferences = userPreferences
                ) {
                    ChatProvider(chatRepository = chatRepository) {
                        val authContext = LocalAuthContext.current
                        val authState by authContext.authState.collectAsState()
                        val navController = rememberNavController()
                        
                        // Track the pending conversation for navigation
                        var conversationToNavigate by remember { mutableStateOf(pendingConversationId) }

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
