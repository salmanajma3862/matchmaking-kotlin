package com.example.dummyapp.ui.screens.messages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.dummyapp.context.LocalAuthContext
import com.example.dummyapp.context.LocalChatContext
import com.example.dummyapp.ui.components.chat.ChatInput
import com.example.dummyapp.ui.components.chat.MessageBubble
import com.example.dummyapp.ui.components.chat.MessageBubble
import java.text.SimpleDateFormat
import java.util.*
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext

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
    
    // Find other user
    val otherUser = conversation?.participants?.find { it.id != currentUserId }
    val otherUserName = otherUser?.name ?: "Chat"
    val otherUserAvatar = otherUser?.photos?.firstOrNull()?.url
    val isOnline = otherUser?.isOnline == true
    val lastActive = otherUser?.lastSeen
    
    val listState = rememberLazyListState()
    val context = LocalContext.current
    var selectedImage by remember { mutableStateOf<String?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: android.net.Uri? ->
        uri?.let {
            chatContext.sendPhotoMessage(conversationId, it, context)
        }
    }

    val audioRecorder = remember { com.example.dummyapp.utils.AudioRecorder(context) }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission granted, user can try recording again
        }
    }

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
        containerColor = Color(0xFFF9FAFB), // Gray-50
        topBar = {
            // Custom Header
            Surface(
                shadowElevation = 1.dp,
                color = Color.White
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .height(64.dp)
                        .padding(horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.Black)
                    }
                    
                    Spacer(modifier = Modifier.width(4.dp))
                    
                    // Avatar
                    AsyncImage(
                        model = otherUserAvatar ?: "https://via.placeholder.com/150",
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.Gray),
                        contentScale = ContentScale.Crop
                    )
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = otherUserName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        
                        // Status
                        if (chatState.typingUsers.containsKey(conversationId)) {
                            Text(
                                "typing...",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        } else if (isOnline) {
                            Text(
                                "Online",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF4CAF50) // Green
                            )
                        } else if (lastActive != null) {
                            Text(
                                "Last seen ${formatLastSeen(lastActive)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                    }
                    
                    // Action Icons
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Default.Call, contentDescription = "Call", tint = Color.Gray)
                    }
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Default.Videocam, contentDescription = "Video", tint = Color.Gray)
                    }
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu", tint = Color.Gray)
                    }
                }
            }
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .navigationBarsPadding()
                    .imePadding() 
            ) {
                ChatInput(
                    onSendMessage = { text ->
                        chatContext.sendMessage(conversationId, text)
                    },
                    onTyping = { isTyping ->
                        if (isTyping) chatContext.startTyping(conversationId)
                        else chatContext.stopTyping(conversationId)
                    },
                    onImageSelected = {
                        imagePickerLauncher.launch("image/*")
                    },
                    onStartRecording = {
                        if (androidx.core.content.ContextCompat.checkSelfPermission(
                                context,
                                android.Manifest.permission.RECORD_AUDIO
                            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
                        ) {
                            audioRecorder.startRecording()
                        } else {
                            permissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
                        }
                    },
                    onStopRecording = {
                        val file = audioRecorder.stopRecording()
                        if (file != null) {
                            chatContext.sendAudioMessage(conversationId, file)
                        }
                    }
                )
            }
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
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(chatState.messages) { message ->
                        MessageBubble(
                            message = message,
                            isMe = message.sender.id == currentUserId,
                            avatarUrl = if (message.sender.id == currentUserId) null else otherUserAvatar,
                            onImageClick = { url -> selectedImage = url }
                        )
                    }
                }
            }
        }
    }


    if (selectedImage != null) {
        Dialog(
            onDismissRequest = { selectedImage = null },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .clickable { selectedImage = null },
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = selectedImage,
                    contentDescription = "Full screen image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}

private fun formatLastSeen(isoString: String): String {
    return try {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        parser.timeZone = TimeZone.getTimeZone("UTC")
        val date = parser.parse(isoString) ?: return ""
        
        val now = Date()
        val diff = now.time - date.time
        val minutes = diff / (1000 * 60)
        val hours = diff / (1000 * 60 * 60)
        val days = diff / (1000 * 60 * 60 * 24)
        
        when {
            minutes < 1 -> "just now"
            minutes < 60 -> "$minutes m ago"
            hours < 24 -> "$hours h ago"
            days < 7 -> "$days d ago"
            else -> SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(date)
        }
    } catch (e: Exception) {
        ""
    }
}
