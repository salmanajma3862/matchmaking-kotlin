package com.example.dummyapp.ui.screens.messages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.dummyapp.context.LocalAuthContext
import com.example.dummyapp.context.LocalChatContext
import com.example.dummyapp.ui.components.chat.ConversationItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationListScreen(
    navController: NavController
) {
    val chatContext = LocalChatContext.current
    val authContext = LocalAuthContext.current
    
    val chatState by chatContext.chatState.collectAsState()
    val authState by authContext.authState.collectAsState()
    
    val currentUserId = authState.user?.id ?: return

    LaunchedEffect(Unit) {
        chatContext.fetchConversations()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Messages") }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (chatState.isLoading && chatState.conversations.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (chatState.conversations.isEmpty()) {
                Text(
                    text = "No conversations yet",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn {
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
}
