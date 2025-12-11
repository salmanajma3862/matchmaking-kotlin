package com.salmanajmal.ziya.ui.components.chat

import androidx.compose.foundation.background
import androidx.compose.ui.draw.alpha
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.salmanajmal.ziya.data.models.Message
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign

@Composable
fun MessageBubble(
    message: Message,
    isMe: Boolean,
    avatarUrl: String? = null,
    showAvatar: Boolean = true,
    onImageClick: (String) -> Unit = {},
    onLongClick: (Message) -> Unit = {},
    onReply: (Message) -> Unit = {}
) {
    val pinkColor = Color(0xFFEC4899) // Pink-500
    val roseColor = Color(0xFFF43F5E) // Rose-500
    val purpleColor = Color(0xFF8B5CF6) // Violet-500 for family
    val indigoColor = Color(0xFF6366F1) // Indigo-500 for family
    var showMenu by remember { mutableStateOf(false) }
    
    // Determine if this message is from a family member
    val isFamilyMessage = message.isFamilyParticipant
    
    val bubbleBrush = when {
        isFamilyMessage && isMe -> {
            // Family member's own messages - solid purple
            androidx.compose.ui.graphics.SolidColor(purpleColor)
        }
        isMe -> {
            // Regular user's own messages - pink/rose gradient
            androidx.compose.ui.graphics.Brush.horizontalGradient(
                colors = listOf(pinkColor, roseColor)
            )
        }
        isFamilyMessage -> {
            // Family member's messages received - light purple
            androidx.compose.ui.graphics.SolidColor(Color(0xFFEDE9FE)) // Violet-100
        }
        else -> {
            // Regular messages received - gray
            androidx.compose.ui.graphics.SolidColor(Color(0xFFF3F4F6)) // Gray-100
        }
    }
    
    val textColor = when {
        isMe -> Color.White
        isFamilyMessage -> Color(0xFF4C1D95) // Violet-900 for family received
        else -> Color(0xFF111827) // Gray-900
    }
    
    val shape = if (isMe) {
        RoundedCornerShape(topStart = 16.dp, topEnd = 4.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
    } else {
        RoundedCornerShape(topStart = 4.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 0.dp, vertical = 1.dp),
        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        if (!isMe) {
            if (showAvatar) {
                AsyncImage(
                    model = avatarUrl ?: "https://via.placeholder.com/150",
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Color.Gray),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(8.dp))
            } else {
                // Invisible spacer to maintain alignment when avatar is hidden
                Spacer(modifier = Modifier.width(36.dp))
            }
        }

            Box(
                modifier = Modifier
                    .background(bubbleBrush, shape)
                    .widthIn(max = 280.dp)
                    .alpha(if (message.isSending) 0.7f else 1f)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onLongPress = { showMenu = true }
                        )
                    }
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
            Box {
                    Column(
                        modifier = Modifier.widthIn(min = 60.dp)
                    ) {
                    // Family member label
                    if (isFamilyMessage && message.familyRelation != null) {
                        Text(
                            text = message.familyRelation.replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isMe) Color.White.copy(alpha = 0.8f) else purpleColor,
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                    
                    // Replied Message Context
                    if (message.replyTo != null) {
                        Row(
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .background(Color.Black.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                                .padding(8.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(2.dp)
                                    .height(24.dp)
                                    .background(if (isMe) Color.White else pinkColor, RoundedCornerShape(1.dp))
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = message.replyTo.sender.name,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (isMe) Color.White.copy(alpha = 0.9f) else pinkColor,
                                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                                )
                                Text(
                                    text = if (message.replyTo.text.isNotEmpty()) message.replyTo.text else "Media",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (isMe) Color.White.copy(alpha = 0.7f) else Color.Gray,
                                    maxLines = 1,
                                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                )
                            }
                        }
                    }

                    if (message.isDeletedForEveryone) {
                        Row(
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(
                                text = "This message was deleted",
                                color = textColor.copy(alpha = 0.6f),
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                modifier = Modifier.weight(1f, fill = false)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = formatTime(message.createdAt),
                                color = if (isMe) Color.White.copy(alpha = 0.7f) else Color(0xFF9CA3AF),
                                fontSize = 10.sp
                            )
                        }
                    } else {
                        if (message.media?.imageUrl != null) {
                            // Photo Message
                            Row(
                                modifier = Modifier
                                    .clickable { onImageClick(message.media.imageUrl) }
                                    .padding(bottom = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(if (isMe) Color.White.copy(alpha = 0.2f) else Color(0xFFFCE7F3)), // Pink-100
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Image,
                                        contentDescription = "Photo",
                                        tint = if (isMe) Color.White else pinkColor
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Photo",
                                        color = textColor,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                                    )
                                    Text(
                                        text = "Tap to view",
                                        color = textColor.copy(alpha = 0.7f),
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                            
                            if (message.text.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    verticalAlignment = Alignment.Bottom
                                ) {
                                    Text(
                                        text = message.text,
                                        color = textColor,
                                        modifier = Modifier.weight(1f, fill = false)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (message.isSending) "Sending..." else formatTime(message.createdAt),
                                        color = if (isMe) Color.White.copy(alpha = 0.7f) else Color(0xFF9CA3AF),
                                        fontSize = 10.sp
                                    )
                                }
                            } else {
                                // Timestamp for photo-only message
                                Text(
                                    text = if (message.isSending) "Sending..." else formatTime(message.createdAt),
                                    color = if (isMe) Color.White.copy(alpha = 0.7f) else Color(0xFF9CA3AF),
                                    fontSize = 10.sp,
                                    modifier = Modifier.align(Alignment.End)
                                )
                            }
                        } else if (message.media?.audioUrl != null) {
                            // Audio Message
                            val context = androidx.compose.ui.platform.LocalContext.current
                            var isPlaying by remember { mutableStateOf(false) }
                            var progress by remember { mutableStateOf(0f) }
                            val mediaPlayer = remember { android.media.MediaPlayer() }
                            
                            DisposableEffect(Unit) {
                                onDispose {
                                    if (mediaPlayer.isPlaying) {
                                        mediaPlayer.stop()
                                    }
                                    mediaPlayer.release()
                                }
                            }

                            // Progress updater
                            LaunchedEffect(isPlaying) {
                                if (isPlaying) {
                                    while (isPlaying) {
                                        if (mediaPlayer.duration > 0) {
                                            progress = mediaPlayer.currentPosition.toFloat() / mediaPlayer.duration.toFloat()
                                        }
                                        kotlinx.coroutines.delay(100)
                                    }
                                }
                            }

                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.width(200.dp)
                                ) {
                                    IconButton(
                                        onClick = {
                                            if (isPlaying) {
                                                mediaPlayer.pause()
                                                isPlaying = false
                                            } else {
                                                try {
                                                    if (mediaPlayer.duration == 0) { // Not prepared
                                                        mediaPlayer.setDataSource(message.media.audioUrl)
                                                        mediaPlayer.prepareAsync()
                                                        mediaPlayer.setOnPreparedListener { 
                                                            it.start() 
                                                            isPlaying = true
                                                        }
                                                        mediaPlayer.setOnCompletionListener { 
                                                            isPlaying = false 
                                                            progress = 0f
                                                        }
                                                    } else {
                                                        mediaPlayer.start()
                                                        isPlaying = true
                                                    }
                                                } catch (e: Exception) {
                                                    e.printStackTrace()
                                                }
                                            }
                                        },
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(CircleShape)
                                            .background(if (isMe) Color.White.copy(alpha = 0.2f) else Color(0xFFFCE7F3)) // Pink-100
                                    ) {
                                        Icon(
                                            imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                                            contentDescription = if (isPlaying) "Pause" else "Play",
                                            tint = if (isMe) Color.White else pinkColor,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                    
                                    Spacer(modifier = Modifier.width(8.dp))
                                    
                                    // Fake waveform with progress
                                    Row(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(24.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        val totalBars = 15
                                        repeat(totalBars) { index ->
                                            val isFilled = (index.toFloat() / totalBars.toFloat()) < progress
                                            Box(
                                                modifier = Modifier
                                                    .width(3.dp)
                                                    .height((10..24).random().dp)
                                                    .clip(CircleShape)
                                                    .background(
                                                        if (isMe) {
                                                            if (isFilled) Color.White else Color.White.copy(alpha = 0.4f)
                                                        } else {
                                                            if (isFilled) pinkColor else Color(0xFFF9A8D4) // Pink-300
                                                        }
                                                    )
                                            )
                                        }
                                    }
                                }
                                // Timestamp for audio
                                Text(
                                    text = if (message.isSending) "Sending..." else formatTime(message.createdAt),
                                    color = if (isMe) Color.White.copy(alpha = 0.7f) else Color(0xFF9CA3AF),
                                    fontSize = 10.sp,
                                    modifier = Modifier.align(Alignment.End)
                                )
                            }
                        } else {
                            // Plain text message - inline timestamp layout
                            val timestampText = if (message.isSending) "Sending..." else formatTime(message.createdAt)
                            
                            Row(
                                verticalAlignment = Alignment.Bottom
                            ) {
                                Text(
                                    text = message.text,
                                    color = textColor,
                                    modifier = Modifier.weight(1f, fill = false)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = timestampText,
                                    color = if (isMe) Color.White.copy(alpha = 0.7f) else Color(0xFF9CA3AF),
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                    modifier = Modifier.background(Color.White, RoundedCornerShape(16.dp))
                ) {
                    DropdownMenuItem(
                        text = { Text("Copy", color = Color.Black) },
                        onClick = { showMenu = false /* TODO: Implement Copy */ }
                    )
                    DropdownMenuItem(
                        text = { Text("Edit", color = Color.Black) },
                        onClick = { showMenu = false /* TODO: Implement Edit */ }
                    )
                    DropdownMenuItem(
                        text = { Text("Reply", color = Color.Black) },
                        onClick = { 
                            showMenu = false 
                            onReply(message)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Delete", color = Color.Red) },
                        onClick = { 
                            showMenu = false
                            onLongClick(message) 
                        }
                    )
                }
            }
    }
}

private fun formatTime(isoString: String): String {
    return try {
        // Simple parser, might need adjustment based on actual backend format
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        parser.timeZone = TimeZone.getTimeZone("UTC")
        val date = parser.parse(isoString) ?: return ""
        
        val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
        formatter.format(date)
    } catch (e: Exception) {
        ""
    }
}
