package com.example.dummyapp.ui.screens

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
import com.example.dummyapp.ui.screens.matches.LikesScreen
import com.example.dummyapp.ui.screens.matches.MatchScreen
import com.example.dummyapp.ui.screens.messages.ConversationListScreen

@Composable
fun MainScreen(
    navController: NavController,
    onNavigateToProfileDetail: (String, String) -> Unit
) {
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
                            indicatorColor = com.example.dummyapp.ui.theme.LightColors.PrimaryContainer,
                            selectedIconColor = com.example.dummyapp.ui.theme.LightColors.Primary,
                            selectedTextColor = com.example.dummyapp.ui.theme.LightColors.Primary,
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
                4 -> com.example.dummyapp.ui.screens.profile.ProfileScreen(
                    onViewProfile = { /* TODO: Navigate to own profile detail */ },
                    onEditProfile = { navController.navigate(com.example.dummyapp.ui.navigation.Screen.EditProfile.route) },
                    onSettings = { navController.navigate(com.example.dummyapp.ui.navigation.Screen.Settings.route) },
                    onNotifications = { navController.navigate(com.example.dummyapp.ui.navigation.Screen.Notifications.route) },
                    onPrivacy = { navController.navigate(com.example.dummyapp.ui.navigation.Screen.Privacy.route) },
                    onSubscription = { navController.navigate(com.example.dummyapp.ui.navigation.Screen.Subscription.route) },
                    onHelp = { navController.navigate(com.example.dummyapp.ui.navigation.Screen.Help.route) },
                    onAbout = { navController.navigate(com.example.dummyapp.ui.navigation.Screen.About.route) },
                    onLogout = {
                        // Navigation back to login is handled by AuthContext in MainActivity
                    }
                )
            }
        }
    }
}
