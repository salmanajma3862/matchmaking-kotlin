package com.salmanajmal.ziya.ui.components.chat

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

/**
 * Animated typing indicator showing three bouncing dots
 * Similar to what you see in WhatsApp, iMessage, etc.
 */
@Composable
fun TypingIndicator(
    avatarUrl: String? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        // Avatar
        if (avatarUrl != null) {
            AsyncImage(
                model = avatarUrl,
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color.Gray),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        // Typing bubble with animated dots
        Box(
            modifier = Modifier
                .background(
                    color = Color(0xFFF0F0F0), // Light gray bubble
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomEnd = 16.dp,
                        bottomStart = 4.dp
                    )
                )
                .padding(horizontal = 16.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            AnimatedDots()
        }
    }
}

@Composable
private fun AnimatedDots() {
    val dotCount = 3
    val delayBetweenDots = 150 // ms

    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(dotCount) { index ->
            AnimatedDot(
                delay = index * delayBetweenDots
            )
        }
    }
}

@Composable
private fun AnimatedDot(
    delay: Int,
    dotSize: Float = 8f,
    dotColor: Color = Color(0xFF9E9E9E) // Gray color
) {
    val infiniteTransition = rememberInfiniteTransition(label = "dot")
    
    // Animate vertical offset for bouncing effect
    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -6f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 600
                0f at 0 with LinearEasing
                -6f at 150 with LinearEasing
                0f at 300 with LinearEasing
                0f at 600 with LinearEasing
            },
            repeatMode = RepeatMode.Restart,
            initialStartOffset = StartOffset(delay)
        ),
        label = "offsetY"
    )
    
    // Animate alpha for fading effect
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 600
                0.4f at 0 with LinearEasing
                1f at 150 with LinearEasing
                0.4f at 300 with LinearEasing
                0.4f at 600 with LinearEasing
            },
            repeatMode = RepeatMode.Restart,
            initialStartOffset = StartOffset(delay)
        ),
        label = "alpha"
    )

    Box(
        modifier = Modifier
            .offset(y = offsetY.dp)
            .size(dotSize.dp)
            .clip(CircleShape)
            .background(dotColor.copy(alpha = alpha))
    )
}
