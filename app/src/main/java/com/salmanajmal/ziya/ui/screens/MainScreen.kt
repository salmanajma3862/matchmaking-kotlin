package com.salmanajmal.ziya.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.salmanajmal.ziya.ui.screens.matches.LikesScreen
import com.salmanajmal.ziya.ui.screens.matches.MatchScreen
import com.salmanajmal.ziya.ui.screens.messages.ConversationListScreen
import androidx.compose.runtime.collectAsState
import com.salmanajmal.ziya.data.models.User
import com.salmanajmal.ziya.utils.NetworkResult

@Composable
fun MainScreen(
    navController: NavController,
    onNavigateToProfileDetail: (String, String) -> Unit,
    viewModel: com.salmanajmal.ziya.viewmodel.AuthViewModel = androidx.hilt.navigation.compose.hiltViewModel()
) {
    val currentUserState by viewModel.currentUserState.collectAsState()
    
    // Check user role
    val user = when (val state = currentUserState) {
        is NetworkResult.Success -> state.data
        else -> null
    }
    
    if (user?.role == "family") {
        com.salmanajmal.ziya.ui.screens.family.FamilyDashboardScreen(
            onNavigateToLogin = { 
                // Navigation handled by AuthContext
            },
            onNavigateToChat = { conversationId ->
                navController.navigate(com.salmanajmal.ziya.ui.navigation.Screen.Chat.createRoute(conversationId))
            },
            onNavigateToProfile = { userId ->
                // Navigate to profile detail in view-only mode (matchStatus = "none" hides action buttons)
                navController.navigate(com.salmanajmal.ziya.ui.navigation.Screen.ProfileDetail.createRoute(userId, "none"))
            }
        )
        return
    }
    
    if (user?.role == "family_unlinked") {
        com.salmanajmal.ziya.ui.screens.family.FamilyLinkScreen(
            onNavigateToDashboard = {
                // Refresh user to update role
                viewModel.fetchCurrentUser()
            }
        )
        return
    }

    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Feed", "Likes", "Matches", "Messages", "Profile")
    val icons = listOf(Icons.Rounded.Home, Icons.Rounded.Favorite, Icons.Rounded.Star, Icons.Rounded.Email, Icons.Rounded.Person)

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = androidx.compose.ui.graphics.Color.White
            ) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(icons[index], contentDescription = item) },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index },
                        colors = androidx.compose.material3.NavigationBarItemDefaults.colors(
                            indicatorColor = com.salmanajmal.ziya.ui.theme.LightColors.PrimaryContainer,
                            selectedIconColor = com.salmanajmal.ziya.ui.theme.LightColors.Primary,
                            selectedTextColor = com.salmanajmal.ziya.ui.theme.LightColors.Primary,
                            unselectedIconColor = androidx.compose.ui.graphics.Color.Gray,
                            unselectedTextColor = androidx.compose.ui.graphics.Color.Gray
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedItem) {
                0 -> FeedScreen(onNavigateToProfileDetail = { userId -> onNavigateToProfileDetail(userId, "none") })
                1 -> LikesScreen(onNavigateToProfileDetail = onNavigateToProfileDetail)
                2 -> MatchScreen(onNavigateToProfileDetail = onNavigateToProfileDetail)
                3 -> ConversationListScreen(
                    navController = navController,
                    onNavigateToMatches = { selectedItem = 2 }
                )
                4 -> com.salmanajmal.ziya.ui.screens.profile.ProfileScreen(
                    onViewProfile = { 
                        user?.id?.let { userId ->
                            onNavigateToProfileDetail(userId, "none")
                        }
                    },
                    onEditProfile = { navController.navigate(com.salmanajmal.ziya.ui.navigation.Screen.EditProfile.route) },
                    onSettings = { navController.navigate(com.salmanajmal.ziya.ui.navigation.Screen.Settings.route) },
                    onNotifications = { navController.navigate(com.salmanajmal.ziya.ui.navigation.Screen.Notifications.route) },
                    onPrivacy = { navController.navigate(com.salmanajmal.ziya.ui.navigation.Screen.Privacy.route) },
                    onSubscription = { navController.navigate(com.salmanajmal.ziya.ui.navigation.Screen.Subscription.route) },
                    onHelp = { navController.navigate(com.salmanajmal.ziya.ui.navigation.Screen.Help.route) },
                    onAbout = { navController.navigate(com.salmanajmal.ziya.ui.navigation.Screen.About.route) },
                    onLogout = {
                        // Navigation back to login is handled by AuthContext in MainActivity
                    },
                    onInviteFamily = { navController.navigate(com.salmanajmal.ziya.ui.navigation.Screen.InviteFamily.route) }
                )
            }
        }
    }
}
