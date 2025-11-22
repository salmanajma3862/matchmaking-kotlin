package com.example.dummyapp.ui.components.cards

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.dummyapp.data.models.User
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

enum class SwipeDirection {
    Left, Right, None
}

@Composable
fun SwipeableCard(
    user: User,
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val screenWidth = with(LocalDensity.current) { configuration.screenWidthDp.dp.toPx() }
    val swipeThreshold = screenWidth * 0.3f

    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }
    val rotation = remember { Animatable(0f) }
    
    val scope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .offset { IntOffset(offsetX.value.roundToInt(), offsetY.value.roundToInt()) }
            .rotate(rotation.value)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        scope.launch {
                            if (abs(offsetX.value) > swipeThreshold) {
                                val targetX = if (offsetX.value > 0) screenWidth * 1.5f else -screenWidth * 1.5f
                                
                                launch { offsetX.animateTo(targetX, tween(400)) }
                                launch { rotation.animateTo(if (offsetX.value > 0) 10f else -10f, tween(400)) }
                                
                                if (offsetX.value > 0) {
                                    onSwipeRight()
                                } else {
                                    onSwipeLeft()
                                }
                            } else {
                                launch { offsetX.animateTo(0f, tween(300)) }
                                launch { offsetY.animateTo(0f, tween(300)) }
                                launch { rotation.animateTo(0f, tween(300)) }
                            }
                        }
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        scope.launch {
                            offsetX.snapTo(offsetX.value + dragAmount.x)
                            offsetY.snapTo(offsetY.value + dragAmount.y)
                            rotation.snapTo((offsetX.value / screenWidth) * 20f)
                        }
                    }
                )
            }
    ) {
        ProfileCardContent(user)
        
        // Overlay for Like/Nope
        if (abs(offsetX.value) > 50) {
            val isLike = offsetX.value > 0
            val color = if (isLike) Color.Green else Color.Red
            val text = if (isLike) "LIKE" else "NOPE"
            val alignment = if (isLike) Alignment.TopStart else Alignment.TopEnd
            val rotationAngle = if (isLike) -15f else 15f
            
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                contentAlignment = alignment
            ) {
                Text(
                    text = text,
                    color = color,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .rotate(rotationAngle)
                        .border(4.dp, color, RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun ProfileCardContent(user: User) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(600.dp)
            .shadow(8.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // User Photo
            val photoUrl = user.photos?.firstOrNull()?.url ?: "https://via.placeholder.com/400x600"
            
            Image(
                painter = rememberAsyncImagePainter(photoUrl),
                contentDescription = "Profile Photo",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            
            // Gradient Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                            startY = 300f
                        )
                    )
            )
            
            // User Info
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    text = "${user.name}, ${calculateAge(user.dob)}",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "${user.profession ?: "Unknown"} • ${user.city ?: "Unknown"}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.9f)
                )
                
                if (!user.bio.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = user.bio,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f),
                        maxLines = 2
                    )
                }
            }
        }
    }
}

// Helper to calculate age (simplified)
fun calculateAge(dob: String?): String {
    if (dob == null) return "N/A"
    // In a real app, parse the date and calculate age
    // For now, just return a placeholder or try to parse year
    return try {
        val year = dob.take(4).toInt()
        val currentYear = 2025 // Using context date
        (currentYear - year).toString()
    } catch (e: Exception) {
        "25"
    }
}
