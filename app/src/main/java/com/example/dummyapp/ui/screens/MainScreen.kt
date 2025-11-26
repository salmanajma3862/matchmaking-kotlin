package com.example.dummyapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Message
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
import com.example.dummyapp.ui.screens.matches.MatchScreen
import com.example.dummyapp.ui.screens.messages.ConversationListScreen

@Composable
fun MainScreen(
    navController: NavController,
    onNavigateToProfileDetail: (String, String) -> Unit
) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Feed", "Matches", "Messages")
    val icons = listOf(Icons.Filled.Home, Icons.Filled.Favorite, Icons.Filled.Message)

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(icons[index], contentDescription = item) },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedItem) {
                0 -> FeedScreen(onNavigateToProfileDetail = { userId -> onNavigateToProfileDetail(userId, "none") })
                1 -> MatchScreen(onNavigateToProfileDetail = onNavigateToProfileDetail)
                2 -> ConversationListScreen(
                    navController = navController,
                    onNavigateToMatches = { selectedItem = 1 }
                )
            }
        }
    }
}
