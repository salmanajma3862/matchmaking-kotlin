package com.salmanajmal.ziya.ui.screens.messages

import android.util.Log
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
import com.salmanajmal.ziya.ui.navigation.Screen
import com.salmanajmal.ziya.context.LocalAuthContext
import com.salmanajmal.ziya.context.LocalChatContext
import com.salmanajmal.ziya.ui.components.chat.ChatInput
import com.salmanajmal.ziya.ui.components.chat.MessageBubble
import com.salmanajmal.ziya.ui.components.chat.TypingIndicator
import java.text.SimpleDateFormat
import java.util.*
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext

private const val TAG = "ChatScreen"

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
    
    // Respect the OTHER user's privacy settings
    val otherUserHidesOnlineStatus = otherUser?.privacySettings?.hideOnlineStatus == true
    val otherUserHidesLastSeen = otherUser?.privacySettings?.hideLastSeen == true
    
    // Only show online status if the other user allows it
    val isOnline = if (otherUserHidesOnlineStatus) false else otherUser?.isOnline == true
    // Only show last active if the other user allows it
    val lastActive = if (otherUserHidesLastSeen) null else otherUser?.lastSeen
    
    val listState = rememberLazyListState()
    val context = LocalContext.current
    var selectedImage by remember { mutableStateOf<String?>(null) }
    var messageToDelete by remember { mutableStateOf<com.salmanajmal.ziya.data.models.Message?>(null) }
    var showMenu by remember { mutableStateOf(false) }
    var showReportDialog by remember { mutableStateOf(false) }
    var currentlyPlayingAudioId by remember { mutableStateOf<String?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: android.net.Uri? ->
        uri?.let {
            chatContext.sendPhotoMessage(conversationId, it, context)
        }
    }

    val audioRecorder = remember { com.salmanajmal.ziya.utils.AudioRecorder(context) }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission granted, user can try recording again
        }
    }

    LaunchedEffect(conversationId) {
        Log.d(TAG, "💬 LaunchedEffect triggered for conversationId: $conversationId")
        Log.d(TAG, "📍 Current conversation in state: ${chatState.currentConversation?.id}")
        if (chatState.currentConversation?.id != conversationId) {
            Log.d(TAG, "🔄 Selecting conversation: $conversationId")
            chatContext.selectConversation(conversationId)
        } else {
            Log.d(TAG, "✅ Already in correct conversation")
        }
    }
    
    // Scroll to bottom on new message
    LaunchedEffect(chatState.messages.size) {
        Log.d(TAG, "📊 Message count changed: ${chatState.messages.size}")
        if (chatState.messages.isNotEmpty()) {
            Log.d(TAG, "📜 Scrolling to latest message")
            listState.animateScrollToItem(0)
        }
    }

    var replyingTo by remember { mutableStateOf<com.salmanajmal.ziya.data.models.Message?>(null) }

    Scaffold(
        modifier = Modifier.imePadding(),
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
                    
                    // Avatar - Clickable to navigate to profile
                    AsyncImage(
                        model = otherUserAvatar ?: "https://via.placeholder.com/150",
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.Gray)
                            .clickable {
                                otherUser?.id?.let { userId ->
                                    navController.navigate(Screen.ProfileDetail.createRoute(userId, "match"))
                                }
                            },
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
                                formatLastSeen(lastActive),
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
                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Menu", tint = Color.Gray)
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Report User") },
                                onClick = {
                                    showMenu = false
                                    showReportDialog = true
                                }
                            )
                        }
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
            ) {
                ChatInput(
                    onSendMessage = { text ->
                        authState.user?.let { user ->
                            chatContext.sendMessage(conversationId, text, user, replyingTo?.id)
                        }
                        replyingTo = null
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
                            authState.user?.let { user ->
                                chatContext.sendAudioMessage(conversationId, file, user)
                            }
                        }
                    },
                    onCancelRecording = {
                        audioRecorder.stopRecording() // Stop and discard
                    },
                    replyToMessage = replyingTo,
                    onCancelReply = { replyingTo = null }
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
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    // Typing indicator at bottom (first in reverse layout)
                    if (chatState.typingUsers.containsKey(conversationId)) {
                        item(key = "typing_indicator") {
                            TypingIndicator(
                                avatarUrl = otherUserAvatar
                            )
                        }
                    }
                    
                    val messages = chatState.messages
                    items(messages.size) { index ->
                        val message = messages[index]
                        val isMe = message.sender.id == currentUserId
                        
                        // In reversed layout, index 0 is the newest message (bottom visually)
                        // Avatar should show on the LAST message of a consecutive sequence from same sender
                        // That means: check if the PREVIOUS message (index - 1, which appears BELOW visually) 
                        // is from a DIFFERENT sender or doesn't exist
                        val previousMessage = messages.getOrNull(index - 1)
                        val isLastInGroup = previousMessage == null || previousMessage.sender.id != message.sender.id
                        
                        MessageBubble(
                            message = message,
                            isMe = isMe,
                            avatarUrl = if (isMe) null else otherUserAvatar,
                            showAvatar = !isMe && isLastInGroup,
                            onImageClick = { url -> selectedImage = url },
                            onLongClick = { msg -> messageToDelete = msg },
                            onReply = { msg -> replyingTo = msg },
                            onAvatarClick = {
                                otherUser?.id?.let { userId ->
                                    navController.navigate(Screen.ProfileDetail.createRoute(userId, "match"))
                                }
                            },
                            currentlyPlayingMessageId = currentlyPlayingAudioId,
                            onAudioPlay = { messageId -> currentlyPlayingAudioId = messageId }
                        )
                    }
                }
            }
        }
    }


    if (messageToDelete != null) {
        AlertDialog(
            onDismissRequest = { messageToDelete = null },
            title = { Text("Delete Message") },
            text = { Text("Are you sure you want to delete this message?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        messageToDelete?.let { msg ->
                            chatContext.deleteMessage(msg.id, forEveryone = false)
                        }
                        messageToDelete = null
                    }
                ) {
                    Text("Delete for me")
                }
            },
            dismissButton = {
                if (messageToDelete?.sender?.id == currentUserId) {
                    TextButton(
                        onClick = {
                            messageToDelete?.let { msg ->
                                chatContext.deleteMessage(msg.id, forEveryone = true)
                            }
                            messageToDelete = null
                        }
                    ) {
                        Text("Delete for everyone")
                    }
                }
                TextButton(onClick = { messageToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
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

    if (showReportDialog) {
        ReportDialog(
            onDismiss = { showReportDialog = false },
            onSubmit = { reason, description ->
                otherUser?.id?.let { reportedId ->
                    chatContext.reportUser(
                        reportedUserId = reportedId,
                        reason = reason,
                        description = description,
                        onSuccess = {
                            showReportDialog = false
                        },
                        onError = { error ->
                            // Optional: Show error
                        }
                    )
                }
            }
        )
    }
}

@Composable
fun ReportDialog(
    onDismiss: () -> Unit,
    onSubmit: (String, String) -> Unit
) {
    var selectedReason by remember { mutableStateOf("spam") }
    var description by remember { mutableStateOf("") }
    val reasons = listOf(
        "spam" to "Spam",
        "harassment" to "Harassment",
        "inappropriate_content" to "Inappropriate Content",
        "fake_profile" to "Fake Profile",
        "other" to "Other"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Report User") },
        text = {
            Column {
                Text("Why are you reporting this user?", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                
                reasons.forEach { (key, label) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedReason = key }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedReason == key,
                            onClick = { selectedReason = key }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = label)
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description (Optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSubmit(selectedReason, description) }
            ) {
                Text("Submit")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

private fun formatLastSeen(isoString: String): String {
    return try {
        // Try multiple date formats since backend might send different formats
        val formats = listOf(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            "yyyy-MM-dd'T'HH:mm:ss'Z'",
            "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
            "yyyy-MM-dd'T'HH:mm:ssXXX"
        )
        
        var date: Date? = null
        for (format in formats) {
            try {
                val parser = SimpleDateFormat(format, Locale.US)
                if (format.endsWith("'Z'")) {
                    parser.timeZone = TimeZone.getTimeZone("UTC")
                }
                date = parser.parse(isoString)
                if (date != null) break
            } catch (e: Exception) {
                // Try next format
            }
        }
        
        if (date == null) return ""
        
        val now = System.currentTimeMillis()
        val diff = now - date.time
        val minutes = diff / (1000 * 60)
        val hours = diff / (1000 * 60 * 60)
        val days = diff / (1000 * 60 * 60 * 24)
        
        when {
            minutes < 1 -> "Active now"
            minutes < 60 -> "Active ${minutes}m ago"
            hours < 24 -> "Active ${hours}h ago"
            days < 7 -> "Active ${days}d ago"
            else -> {
                val outputFormat = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
                "Active ${outputFormat.format(date)}"
            }
        }
    } catch (e: Exception) {
        ""
    }
}

