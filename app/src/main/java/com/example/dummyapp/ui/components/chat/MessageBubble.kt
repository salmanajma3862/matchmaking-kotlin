package com.example.dummyapp.ui.components.chat

import androidx.compose.foundation.background
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.dummyapp.data.models.Message
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectTapGestures

@Composable
fun MessageBubble(
    message: Message,
    isMe: Boolean,
    avatarUrl: String? = null,
    onImageClick: (String) -> Unit = {},
    onLongClick: (Message) -> Unit = {}
) {
    val pinkColor = Color(0xFFEC4899) // Pink-500
    val roseColor = Color(0xFFF43F5E) // Rose-500
    
    val bubbleBrush = if (isMe) {
        androidx.compose.ui.graphics.Brush.horizontalGradient(
            colors = listOf(pinkColor, roseColor)
        )
    } else {
        androidx.compose.ui.graphics.SolidColor(Color(0xFFF3F4F6)) // Gray-100
    }
    
    val textColor = if (isMe) Color.White else Color(0xFF111827) // Gray-900
    
    val shape = if (isMe) {
        RoundedCornerShape(topStart = 16.dp, topEnd = 4.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
    } else {
        RoundedCornerShape(topStart = 4.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 0.dp, vertical = 4.dp),
        horizontalAlignment = if (isMe) Alignment.End else Alignment.Start
    ) {
        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            if (!isMe) {
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
            }

            Box(
                modifier = Modifier
                    .background(bubbleBrush, shape)
                    .widthIn(max = 280.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onLongPress = { onLongClick(message) }
                        )
                    }
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Column {
                    if (message.isDeletedForEveryone) {
                        Text(
                            text = "This message was deleted",
                            color = textColor.copy(alpha = 0.6f),
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
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
                                Text(
                                    text = message.text,
                                    color = textColor
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
                        } else {
                            Text(
                                text = message.text,
                                color = textColor
                            )
                        }
                    }
                }
            }
        }
        
        Text(
            text = formatTime(message.createdAt),
            color = Color(0xFF9CA3AF), // Gray-400
            fontSize = 10.sp,
            modifier = Modifier.padding(
                top = 4.dp,
                start = if (!isMe) 36.dp else 0.dp
            )
        )
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
