package com.salmanajmal.ziya.ui.components.chat

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun ChatInput(
    onSendMessage: (String) -> Unit,
    onTyping: (Boolean) -> Unit,
    onImageSelected: () -> Unit,
    onStartRecording: () -> Unit = {},
    onStopRecording: () -> Unit = {},
    onCancelRecording: () -> Unit = {},
    replyToMessage: com.salmanajmal.ziya.data.models.Message? = null,
    onCancelReply: () -> Unit = {}
) {
    var text by remember { mutableStateOf("") }
    var isRecording by remember { mutableStateOf(false) }
    var showEmojiPicker by remember { mutableStateOf(false) }
    
    val pinkColor = MaterialTheme.colorScheme.primary // Pink-500
    
    // Common emojis for quick access
    val commonEmojis = listOf(
        "😀", "😂", "🥰", "😍", "😘", "🥺", "😭", "😊",
        "❤️", "🔥", "✨", "👍", "🙏", "💕", "😎", "🎉",
        "👋", "🤗", "😅", "🥳", "💪", "🙌", "💯", "😇"
    )
    val roseColor = MaterialTheme.colorScheme.inversePrimary // Rose-500
    val gradientBrush = androidx.compose.ui.graphics.Brush.horizontalGradient(
        colors = listOf(pinkColor, roseColor)
    )
    
    // Recording timer
    var recordingSeconds by remember { mutableIntStateOf(0) }
    
    // Timer effect - runs independently
    LaunchedEffect(isRecording) {
        if (isRecording) {
            recordingSeconds = 0
            while (isRecording) {
                delay(1000)
                if (isRecording) recordingSeconds++
            }
        } else {
            recordingSeconds = 0
        }
    }
    
    // Format time as MM:SS
    val formattedTime = remember(recordingSeconds) {
        val minutes = recordingSeconds / 60
        val seconds = recordingSeconds % 60
        String.format("%02d:%02d", minutes, seconds)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // Reply Preview - only show when not recording
        if (replyToMessage != null && !isRecording) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(36.dp)
                        .background(pinkColor, RoundedCornerShape(2.dp))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Replying to ${replyToMessage.sender.name}",
                        style = MaterialTheme.typography.bodySmall,
                        color = pinkColor,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (replyToMessage.text.isNotEmpty()) replyToMessage.text else "Media",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                }
                IconButton(onClick = onCancelReply) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Cancel Reply",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Emoji Picker - show when emoji button is clicked
        if (showEmojiPicker && !isRecording) {
            androidx.compose.foundation.lazy.grid.LazyVerticalGrid(
                columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(8),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(commonEmojis.size) { index ->
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                text = text + commonEmojis[index]
                                onTyping(true)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = commonEmojis[index],
                            fontSize = 22.sp
                        )
                    }
                }
            }
        }

        // Main input row
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            if (isRecording) {
                // RECORDING STATE - Simple layout with delete, timer, and send
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Delete button to cancel recording
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFFFEE2E2),
                        modifier = Modifier
                            .size(44.dp)
                            .clickable {
                                isRecording = false
                                onCancelRecording()
                            }
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Cancel Recording",
                                tint = Color(0xFFEF4444),
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    // Recording indicator with timer and waveform
                    Surface(
                        shape = RoundedCornerShape(24.dp),
                        color = Color(0xFFFCE7F3),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            PulsingRecordingDot()
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = formattedTime,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            AudioWaveform(pinkColor = pinkColor)
                        }
                    }
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    // Send button (replaces mic when recording)
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(gradientBrush, CircleShape)
                            .clickable {
                                isRecording = false
                                onStopRecording()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send Audio",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            } else {
                // NORMAL INPUT STATE
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onImageSelected, modifier = Modifier.size(40.dp)) {
                        Icon(Icons.Default.Image, "Add Image", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    
                    IconButton(onClick = { showEmojiPicker = !showEmojiPicker }, modifier = Modifier.size(40.dp)) {
                        Icon(Icons.Default.Face, "Emoji", tint = if (showEmojiPicker) pinkColor else MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp)
                            .height(48.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(24.dp))
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (text.isEmpty()) {
                            Text("Type a message...", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
                        }
                        androidx.compose.foundation.text.BasicTextField(
                            value = text,
                            onValueChange = { text = it; onTyping(it.isNotEmpty()) },
                            textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface),
                            maxLines = 4,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    if (text.isNotBlank()) {
                        // Send text button
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(gradientBrush, CircleShape)
                                .clickable {
                                    onSendMessage(text)
                                    text = ""
                                    onTyping(false)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.AutoMirrored.Filled.Send, "Send", tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(20.dp))
                        }
                    } else {
                        // Mic button - tap to start recording
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(gradientBrush, CircleShape)
                                .clickable {
                                    isRecording = true
                                    onStartRecording()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.Mic, "Record Audio", tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PulsingRecordingDot() {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )
    
    Box(
        modifier = Modifier
            .size(10.dp)
            .scale(scale)
            .background(MaterialTheme.colorScheme.error, CircleShape)
    )
}

@Composable
private fun AudioWaveform(pinkColor: Color) {
    val infiniteTransition = rememberInfiniteTransition(label = "waveform")
    
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        repeat(5) { index ->
            val height by infiniteTransition.animateFloat(
                initialValue = 6f,
                targetValue = 16f + (index % 3) * 4f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 250 + index * 80,
                        easing = FastOutSlowInEasing
                    ),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "bar$index"
            )
            
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(height.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(pinkColor.copy(alpha = 0.6f + (index % 2) * 0.4f))
            )
        }
    }
}
