package com.example.dummyapp.ui.components.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicNone
import androidx.compose.material.icons.filled.Send
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
    onStopRecording: () -> Unit = {}
) {
    var text by remember { mutableStateOf("") }
    var isRecording by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onImageSelected) {
            Icon(Icons.Default.Add, contentDescription = "Add Image", tint = MaterialTheme.colorScheme.primary)
        }

        TextField(
            value = if (isRecording) "Recording..." else text,
            onValueChange = { 
                if (!isRecording) {
                    text = it
                    onTyping(it.isNotEmpty())
                }
            },
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            placeholder = { Text("Type a message...") },
            shape = RoundedCornerShape(24.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            maxLines = 4,
            enabled = !isRecording
        )

        if (text.isNotBlank()) {
            IconButton(
                onClick = {
                    onSendMessage(text)
                    text = ""
                    onTyping(false)
                },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Icon(Icons.Default.Send, contentDescription = "Send")
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

            IconButton(
                onClick = { /* Handled by interaction source */ },
                interactionSource = interactionSource,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = if (isRecording) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = if (isRecording) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.onSecondaryContainer
                )
            ) {
                Icon(
                    imageVector = if (isRecording) Icons.Filled.Mic else Icons.Filled.MicNone,
                    contentDescription = "Record Audio"
                )
            }
        }
    }
}
