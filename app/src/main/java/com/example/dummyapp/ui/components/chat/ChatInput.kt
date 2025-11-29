package com.example.dummyapp.ui.components.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Face
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ChatInput(
    onSendMessage: (String) -> Unit,
    onTyping: (Boolean) -> Unit,
    onImageSelected: () -> Unit,
    onStartRecording: () -> Unit = {},
    onStopRecording: () -> Unit = {},
    replyToMessage: com.example.dummyapp.data.models.Message? = null,
    onCancelReply: () -> Unit = {}
) {
    var text by remember { mutableStateOf("") }
    var isRecording by remember { mutableStateOf(false) }
    
    val pinkColor = Color(0xFFEC4899) // Pink-500
    val roseColor = Color(0xFFF43F5E) // Rose-500
    val gradientBrush = androidx.compose.ui.graphics.Brush.horizontalGradient(
        colors = listOf(pinkColor, roseColor)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp)
    ) {
        // Reply Preview
        if (replyToMessage != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .background(Color(0xFFF3F4F6), RoundedCornerShape(12.dp))
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
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                    Text(
                        text = if (replyToMessage.text.isNotEmpty()) replyToMessage.text else "Media",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                }
                IconButton(onClick = onCancelReply) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Cancel Reply",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image Button
            IconButton(
                onClick = onImageSelected,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = "Add Image",
                    tint = Color(0xFF6B7280) // Gray-500
                )
            }
            
            // Smile Button (Placeholder)
            IconButton(
                onClick = { /* TODO */ },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Face, // Using Face as Smile
                    contentDescription = "Emoji",
                    tint = Color(0xFF6B7280) // Gray-500
                )
            }

            // Input Field
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
                    .height(48.dp)
                    .background(Color(0xFFF3F4F6), RoundedCornerShape(24.dp)) // Gray-100
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (text.isEmpty() && !isRecording) {
                    Text(
                        text = "Type a message...",
                        color = Color(0xFF9CA3AF), // Gray-400
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                
                androidx.compose.foundation.text.BasicTextField(
                    value = if (isRecording) "Recording..." else text,
                    onValueChange = { 
                        if (!isRecording) {
                            text = it
                            onTyping(it.isNotEmpty())
                        }
                    },
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = if (isRecording) Color(0xFFEF4444) else Color.Black // Red-500 if recording
                    ),
                    maxLines = 4,
                    enabled = !isRecording,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Send/Mic Button
            if (text.isNotBlank()) {
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
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            } else {
                 // Mic Button
                 val interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
                 val isPressed by interactionSource.collectIsPressedAsState()
                 
                 LaunchedEffect(isPressed) {
                     if (isPressed) {
                         isRecording = true
                         onStartRecording()
                     } else {
                         if (isRecording) {
                             isRecording = false
                             onStopRecording()
                         }
                     }
                 }

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            if (isRecording) androidx.compose.ui.graphics.SolidColor(Color(0xFFEF4444)) else gradientBrush, 
                            CircleShape
                        )
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = {}
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isRecording) Icons.Filled.Mic else Icons.Filled.Mic,
                        contentDescription = "Record Audio",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
        
        if (isRecording) {
            Text(
                text = "Recording...",
                color = Color(0xFFEF4444),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 4.dp)
            )
        }
    }
}
