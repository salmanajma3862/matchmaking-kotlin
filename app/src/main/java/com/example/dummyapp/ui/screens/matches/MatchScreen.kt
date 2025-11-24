package com.example.dummyapp.ui.screens.matches

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.dummyapp.data.models.User
import com.example.dummyapp.utils.NetworkResult
import com.example.dummyapp.viewmodel.MatchViewModel
import java.util.Calendar

@Composable
fun MatchScreen(
    viewModel: MatchViewModel = hiltViewModel(),
    onNavigateToProfileDetail: (String, String) -> Unit
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Sent", "Received", "Matches")

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(text = title) }
                )
            }
        }

        when (selectedTabIndex) {
            0 -> SentTab(viewModel, onNavigateToProfileDetail)
            1 -> ReceivedTab(viewModel, onNavigateToProfileDetail)
            2 -> MatchesTab(viewModel, onNavigateToProfileDetail)
        }
    }
}

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
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(swipes) { swipe ->
                        swipe.target?.let { user ->
                            UserCard(
                                user = user,
                                subtitle = "Sent: ${formatDate(swipe.createdAt)}",
                                onClick = { onNavigateToProfileDetail(user.id, "sent") }
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
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(swipes) { swipe ->
                        swipe.swiper?.let { user ->
                            UserCard(
                                user = user,
                                subtitle = "Received: ${formatDate(swipe.createdAt)}",
                                onClick = { onNavigateToProfileDetail(user.id, "received") }
                            )
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun MatchesTab(viewModel: MatchViewModel, onNavigateToProfileDetail: (String, String) -> Unit) {
    val matchesState by viewModel.matches.collectAsState()

    when (val state = matchesState) {
        is NetworkResult.Loading -> LoadingView()
        is NetworkResult.Error -> ErrorView(state.message)
        is NetworkResult.Success -> {
            val matches = state.data
            if (matches.isNullOrEmpty()) {
                EmptyView("No matches yet.")
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(matches) { match ->
                        match.user?.let { user ->
                            UserCard(
                                user = user,
                                subtitle = "Matched: ${formatDate(match.matchedAt)}",
                                onClick = { onNavigateToProfileDetail(user.id, "match") }
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
