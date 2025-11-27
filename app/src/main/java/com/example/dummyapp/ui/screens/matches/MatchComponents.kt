package com.example.dummyapp.ui.screens.matches

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Message
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.dummyapp.data.models.User
import com.example.dummyapp.utils.NetworkResult
import com.example.dummyapp.viewmodel.MatchViewModel
import java.util.Calendar

@Composable
fun SentTab(viewModel: MatchViewModel, onNavigateToProfileDetail: (String, String) -> Unit) {
    val sentSwipesState by viewModel.sentSwipes.collectAsState()

    when (val state = sentSwipesState) {
        is NetworkResult.Loading -> LoadingView()
        is NetworkResult.Error -> ErrorView(state.message)
        is NetworkResult.Success -> {
            val swipes = state.data
            if (swipes.isNullOrEmpty()) {
                EmptyView("No sent likes yet.")
            } else {
                androidx.compose.foundation.lazy.grid.LazyVerticalGrid(
                    columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(swipes) { swipe ->
                        swipe.target?.let { user ->
                            MatchProfileCard(
                                user = user,
                                isNew = false,
                                onMessage = { onNavigateToProfileDetail(user.id, "sent") },
                                onViewProfile = { onNavigateToProfileDetail(user.id, "sent") }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReceivedTab(viewModel: MatchViewModel, onNavigateToProfileDetail: (String, String) -> Unit) {
    val receivedSwipesState by viewModel.receivedSwipes.collectAsState()

    when (val state = receivedSwipesState) {
        is NetworkResult.Loading -> LoadingView()
        is NetworkResult.Error -> ErrorView(state.message)
        is NetworkResult.Success -> {
            val swipes = state.data
            if (swipes.isNullOrEmpty()) {
                EmptyView("No received likes yet.")
            } else {
                androidx.compose.foundation.lazy.grid.LazyVerticalGrid(
                    columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(swipes) { swipe ->
                        swipe.swiper?.let { user ->
                            MatchProfileCard(
                                user = user,
                                isNew = false,
                                onMessage = { onNavigateToProfileDetail(user.id, "received") },
                                onViewProfile = { onNavigateToProfileDetail(user.id, "received") }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UserCard(user: User, subtitle: String, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile Image
            AsyncImage(
                model = user.photos?.firstOrNull()?.url ?: "https://via.placeholder.com/150",
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color.Gray),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = "${user.name}, ${calculateAge(user.dob)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${user.city ?: "Unknown"}, ${user.country ?: ""}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun LoadingView() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorView(message: String?) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = message ?: "An error occurred", color = Color.Red)
    }
}

@Composable
fun EmptyView(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = message, style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
    }
}

// Helper functions
fun formatDate(dateString: String): String {
    return try {
        dateString.substring(0, 10)
    } catch (e: Exception) {
        dateString
    }
}

fun calculateAge(dob: String?): String {
    if (dob.isNullOrEmpty()) return "?"
    return try {
        // Simple approximation using Calendar to avoid java.time crash on older Android versions
        val year = dob.substring(0, 4).toInt()
        val currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
        (currentYear - year).toString()
    } catch (e: Exception) {
        "?"
    }
}

@Composable
fun MatchProfileCard(
    user: User,
    isNew: Boolean = false,
    onMessage: () -> Unit,
    onViewProfile: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.75f) // 3:4 aspect ratio
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onViewProfile)
            .background(Color.White) // Fallback background
    ) {
        // Image
        AsyncImage(
            model = user.photos?.firstOrNull()?.url ?: "https://via.placeholder.com/150",
            contentDescription = "Profile Picture",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Gradient Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.6f)
                        )
                    )
                )
        )

        // New Badge
        if (isNew) {
            Surface(
                color = androidx.compose.ui.graphics.Color(0xFFEC4899), // Pink-500
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.TopEnd)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Filled.Favorite,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = "New",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Info
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
                .padding(end = 48.dp) // Make space for FAB
        ) {
            Text(
                text = "${user.name}, ${calculateAge(user.dob)}",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${user.city ?: "Unknown"}, ${user.country ?: ""}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.9f)
            )
        }

        // Message FAB
        FloatingActionButton(
            onClick = onMessage,
            containerColor = Color.White,
            contentColor = androidx.compose.ui.graphics.Color(0xFFEC4899), // Pink-500
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .size(48.dp),
            shape = CircleShape
        ) {
            Icon(
                imageVector = androidx.compose.material.icons.Icons.Filled.Message,
                contentDescription = "Message",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
