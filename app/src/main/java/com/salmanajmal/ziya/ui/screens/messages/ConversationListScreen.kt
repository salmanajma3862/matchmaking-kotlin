package com.salmanajmal.ziya.ui.screens.messages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.salmanajmal.ziya.context.LocalAuthContext
import com.salmanajmal.ziya.context.LocalChatContext
import com.salmanajmal.ziya.ui.components.cards.MatchCard
import com.salmanajmal.ziya.ui.components.chat.ConversationItem
import com.salmanajmal.ziya.utils.NetworkResult
import com.salmanajmal.ziya.viewmodel.MatchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationListScreen(
    navController: NavController,
    viewModel: MatchViewModel = hiltViewModel(),
    onNavigateToMatches: () -> Unit
) {
    val chatContext = LocalChatContext.current
    val authContext = LocalAuthContext.current
    
    val chatState by chatContext.chatState.collectAsState()
    val authState by authContext.authState.collectAsState()
    val matchesState by viewModel.matches.collectAsState()
    
    val currentUserId = authState.user?.id ?: return

    LaunchedEffect(Unit) {
        chatContext.fetchConversations()
        // Ensure matches are fetched
        if (matchesState !is NetworkResult.Success) {
            // Trigger fetch if needed, assuming ViewModel init or separate call
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp) // Add padding for bottom nav
    ) {
        // Header matching MatchScreen
        Surface(
            color = Color.White,
            shadowElevation = 1.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Messages",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            // New Matches Section - Commented out
            /*
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "New Matches",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF4B5563) // Gray-600
                        )
                        Text(
                            text = "See All",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color(0xFFEC4899), // Pink-500
                            modifier = Modifier.clickable { onNavigateToMatches() }
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    when (val state = matchesState) {
                        is NetworkResult.Success -> {
                            val matches = state.data ?: emptyList()
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                contentPadding = PaddingValues(bottom = 8.dp)
                            ) {
                                items(matches) { match ->
                                    match.user?.let { user ->
                                        MatchCard(
                                            user = user,
                                            isNew = true, // Logic for "new" could be refined
                                            onClick = {
                                                // Start conversation
                                                chatContext.selectConversation(user.id) // Assuming user.id maps to conversationId or creates one
                                                navController.navigate("chat/${user.id}")
                                            }
                                        )
                                    }
                                }
                            }
                        }
                        else -> {
                            // Loading or Error state for matches row, or empty
                        }
                    }
                }
                Divider(color = Color(0xFFE5E7EB), thickness = 1.dp) // Gray-200
            }
            */

            // Messages Header
            item {
                Text(
                    text = "Messages",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF4B5563), // Gray-600
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            // Conversations List
            if (chatState.isLoading && chatState.conversations.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            } else if (chatState.conversations.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text("No conversations yet")
                    }
                }
            } else {
                items(chatState.conversations) { conversation ->
                    ConversationItem(
                        conversation = conversation,
                        currentUserId = currentUserId,
                        onClick = {
                            chatContext.selectConversation(conversation.id)
                            navController.navigate("chat/${conversation.id}")
                        }
                    )
                }
            }
        }
    }
}
