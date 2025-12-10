package com.salmanajmal.ziya

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.salmanajmal.ziya.context.AuthContextProvider
import com.salmanajmal.ziya.context.ChatProvider
import com.salmanajmal.ziya.data.preferences.UserPreferences
import com.salmanajmal.ziya.data.repository.AuthRepository
import com.salmanajmal.ziya.data.repository.ChatRepository
import com.salmanajmal.ziya.ui.navigation.AppNavigation
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DummyAppTheme {
                AuthContextProvider(
                    authRepository = authRepository,
                    userPreferences = userPreferences
                ) {
                    ChatProvider(chatRepository = chatRepository) {
                        val authContext = com.salmanajmal.ziya.context.LocalAuthContext.current
                        val authState by authContext.authState.collectAsState()

                        if (authState.isLoading) {
                            androidx.compose.foundation.layout.Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = androidx.compose.ui.Alignment.Center
                            ) {
                                androidx.compose.material3.CircularProgressIndicator()
                            }
                        } else {
                            val startDestination = if (authState.isAuthenticated) {
                                com.salmanajmal.ziya.ui.navigation.Screen.Main.route
                            } else {
                                com.salmanajmal.ziya.ui.navigation.Screen.Login.route
                            }

                            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                                AppNavigation(
                                    modifier = Modifier,
                                    startDestination = startDestination
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
