package com.example.dummyapp

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
import com.example.dummyapp.context.AuthContextProvider
import com.example.dummyapp.context.ChatProvider
import com.example.dummyapp.data.preferences.UserPreferences
import com.example.dummyapp.data.repository.AuthRepository
import com.example.dummyapp.data.repository.ChatRepository
import com.example.dummyapp.ui.navigation.AppNavigation
import com.example.dummyapp.ui.theme.DummyAppTheme
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
                        val authContext = com.example.dummyapp.context.LocalAuthContext.current
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
                                com.example.dummyapp.ui.navigation.Screen.Main.route
                            } else {
                                com.example.dummyapp.ui.navigation.Screen.Login.route
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
