package com.example.dummyapp.ui.screens.messages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dummyapp.context.LocalAuthContext
import com.example.dummyapp.context.LocalChatContext
import com.example.dummyapp.ui.components.chat.ChatInput
import com.example.dummyapp.ui.components.chat.MessageBubble

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navController: NavController,
    conversationId: String
) {
    val chatContext = LocalChatContext.current
    val authContext = LocalAuthContext.current
    
    val chatState by chatContext.chatState.collectAsState()
    val authState by authContext.authState.collectAsState()
    
    val currentUserId = authState.user?.id ?: return
    val conversation = chatState.currentConversation
    
    // Find other user name
    val otherUser = conversation?.participants?.find { it.id != currentUserId }
    val otherUserName = otherUser?.name ?: "Chat"
    
    val listState = rememberLazyListState()

    LaunchedEffect(conversationId) {
        if (chatState.currentConversation?.id != conversationId) {
            chatContext.selectConversation(conversationId)
        }
    }
    
    // Scroll to bottom on new message
    LaunchedEffect(chatState.messages.size) {
        if (chatState.messages.isNotEmpty()) {
            listState.animateScrollToItem(0)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text(otherUserName)
                        if (chatState.typingUsers.containsKey(conversationId)) {
                            Text(
                                "typing...",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            ChatInput(
                onSendMessage = { text ->
                    chatContext.sendMessage(conversationId, text)
                },
                onTyping = { isTyping ->
                    if (isTyping) chatContext.startTyping(conversationId)
                    else chatContext.stopTyping(conversationId)
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (chatState.isLoading && chatState.messages.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    state = listState,
                    reverseLayout = true, // Messages start from bottom
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 8.dp)
                ) {
                    items(chatState.messages) { message ->
                        MessageBubble(
                            message = message,
                            isMe = message.sender.id == currentUserId
                        )
                    }
                }
            }
        }
    }
}
