package com.example.dummyapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.dummyapp.ui.screens.auth.LoginScreen
import com.example.dummyapp.ui.screens.auth.SignupScreen
import com.example.dummyapp.ui.screens.auth.VerifyEmailScreen
import com.example.dummyapp.ui.screens.MainScreen
import com.example.dummyapp.ui.screens.profile_setup.ProfileSetupScreen

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Signup : Screen("signup")
    object Main : Screen("main")
    object VerifyEmail : Screen("verify_email/{email}") {
        fun createRoute(email: String) = "verify_email/$email"
    }
    object ProfileSetup : Screen("profile_setup")
    object ProfileDetail : Screen("profile_detail/{userId}?matchStatus={matchStatus}") {
        fun createRoute(userId: String, matchStatus: String = "none") = "profile_detail/$userId?matchStatus=$matchStatus"
    }
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
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToVerifyEmail = { email ->
                    navController.navigate(Screen.VerifyEmail.createRoute(email))
                },
                onNavigateToProfileSetup = {
                    navController.navigate(Screen.ProfileSetup.route) {
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
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Main.route) {
            MainScreen(
                onNavigateToProfileDetail = { userId, matchStatus ->
                    navController.navigate(Screen.ProfileDetail.createRoute(userId, matchStatus))
                }
            )
        }
        composable(
            route = Screen.ProfileDetail.route,
            arguments = listOf(
                navArgument("userId") { type = NavType.StringType },
                navArgument("matchStatus") { 
                    type = NavType.StringType 
                    defaultValue = "none"
                }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: return@composable
            val matchStatus = backStackEntry.arguments?.getString("matchStatus") ?: "none"
            com.example.dummyapp.ui.screens.profile.ProfileDetailScreen(
                userId = userId,
                matchStatus = matchStatus,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
