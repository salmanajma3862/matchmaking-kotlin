package com.salmanajmal.ziya.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.salmanajmal.ziya.ui.screens.auth.LoginScreen
import com.salmanajmal.ziya.ui.screens.auth.SignupScreen
import com.salmanajmal.ziya.ui.screens.auth.VerifyEmailScreen
import com.salmanajmal.ziya.ui.screens.MainScreen
import com.salmanajmal.ziya.ui.screens.profile_setup.ProfileSetupScreen
import com.salmanajmal.ziya.ui.screens.messages.ChatScreen

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Signup : Screen("signup")
    object FamilySignup : Screen("family_signup")
    object Main : Screen("main")
    object VerifyEmail : Screen("verify_email/{email}") {
        fun createRoute(email: String) = "verify_email/$email"
    }
    object ProfileSetup : Screen("profile_setup")
    object ProfileDetail : Screen("profile_detail/{userId}?matchStatus={matchStatus}") {
        fun createRoute(userId: String, matchStatus: String = "none") = "profile_detail/$userId?matchStatus=$matchStatus"
    }
    object Chat : Screen("chat/{conversationId}") {
        fun createRoute(conversationId: String) = "chat/$conversationId"
    }
    object Settings : Screen("settings")
    object EditProfile : Screen("edit_profile")
    object Notifications : Screen("notifications")
    object Privacy : Screen("privacy")
    object Subscription : Screen("subscription")
    object Help : Screen("help")
    object About : Screen("about")
    object InviteFamily : Screen("invite_family")
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
                },
                onNavigateToFamilySignup = { navController.navigate(Screen.FamilySignup.route) }
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
        composable(Screen.FamilySignup.route) {
            com.salmanajmal.ziya.ui.screens.family.FamilySignupScreen(
                onNavigateToLogin = { navController.popBackStack() },
                onNavigateToLink = { 
                    // After signup, user is logged in as family_unlinked, so go to Main which redirects to Link
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
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
                navController = navController,
                onNavigateToProfileDetail = { userId, matchStatus ->
                    navController.navigate(Screen.ProfileDetail.createRoute(userId, matchStatus))
                }
            )
        }
        composable(
            route = Screen.ProfileDetail.route,
            arguments = listOf(
                navArgument("userId") { type = NavType.StringType },
                navArgument("matchStatus") { type = NavType.StringType; defaultValue = "none" }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: return@composable
            val matchStatus = backStackEntry.arguments?.getString("matchStatus") ?: "none"
            com.salmanajmal.ziya.ui.screens.profile.ProfileDetailScreen(
                userId = userId,
                matchStatus = matchStatus,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToChat = { conversationId ->
                    navController.navigate(Screen.Chat.createRoute(conversationId))
                }
            )
        }
        composable(
            route = Screen.Chat.route,
            arguments = listOf(
                navArgument("conversationId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val conversationId = backStackEntry.arguments?.getString("conversationId") ?: return@composable
            ChatScreen(
                navController = navController,
                conversationId = conversationId
            )
        }
        composable(Screen.Settings.route) { 
            com.salmanajmal.ziya.ui.screens.settings.SettingsScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.EditProfile.route) {
            val authViewModel: com.salmanajmal.ziya.viewmodel.AuthViewModel = androidx.hilt.navigation.compose.hiltViewModel()
            val currentUserState = authViewModel.currentUserState.collectAsState()
            val user = (currentUserState.value as? com.salmanajmal.ziya.utils.NetworkResult.Success)?.data
            com.salmanajmal.ziya.ui.screens.profile.EditProfileScreen(
                user = user,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Notifications.route) { com.salmanajmal.ziya.ui.screens.profile.NotificationsScreen() }
        composable(Screen.Privacy.route) {
            val authViewModel: com.salmanajmal.ziya.viewmodel.AuthViewModel = androidx.hilt.navigation.compose.hiltViewModel()
            val currentUserState = authViewModel.currentUserState.collectAsState()
            val user = (currentUserState.value as? com.salmanajmal.ziya.utils.NetworkResult.Success)?.data
            com.salmanajmal.ziya.ui.screens.profile.PrivacyScreen(
                user = user,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Subscription.route) { 
            com.salmanajmal.ziya.ui.screens.premium.PremiumPackagesScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Help.route) { com.salmanajmal.ziya.ui.screens.profile.HelpScreen() }
        composable(Screen.About.route) { com.salmanajmal.ziya.ui.screens.profile.AboutScreen() }
        composable(Screen.InviteFamily.route) {
            com.salmanajmal.ziya.ui.screens.profile.InviteFamilyScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
