package com.example.dummyapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dummyapp.ui.screens.auth.LoginScreen
import com.example.dummyapp.ui.screens.auth.SignupScreen
import com.example.dummyapp.ui.screens.auth.VerifyEmailScreen
import com.example.dummyapp.ui.screens.profile.ProfileSetupScreen

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Signup : Screen("signup")
    object Home : Screen("home")
    object VerifyEmail : Screen("verify_email/{email}") {
        fun createRoute(email: String) = "verify_email/$email"
    }
    object ProfileSetup : Screen("profile_setup")
}

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Login.route
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToSignup = { navController.navigate(Screen.Signup.route) },
                onNavigateToForgotPassword = { /* TODO */ },
                onNavigateToHome = { 
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Signup.route) {
            SignupScreen(
                onNavigateToLogin = { navController.popBackStack() },
                onNavigateToVerifyEmail = { email -> 
                    navController.navigate(Screen.VerifyEmail.createRoute(email))
                }
            )
        }
        composable(
            route = Screen.VerifyEmail.route
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            VerifyEmailScreen(
                email = email,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToProfileSetup = {
                    navController.navigate(Screen.ProfileSetup.route) {
                        popUpTo(Screen.Signup.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.ProfileSetup.route) {
            ProfileSetupScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Home.route) {
            // Placeholder for Home
            androidx.compose.material3.Text("Home Screen")
        }
    }
}
