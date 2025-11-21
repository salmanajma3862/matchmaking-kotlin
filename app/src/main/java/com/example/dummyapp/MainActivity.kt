package com.example.dummyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.dummyapp.context.AuthContextProvider
import com.example.dummyapp.data.preferences.UserPreferences
import com.example.dummyapp.data.repository.AuthRepository
import com.example.dummyapp.ui.navigation.AppNavigation
import com.example.dummyapp.ui.theme.DummyAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authRepository: AuthRepository

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
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        AppNavigation(modifier = Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }
}
