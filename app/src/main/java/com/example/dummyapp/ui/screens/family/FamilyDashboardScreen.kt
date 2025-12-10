package com.example.dummyapp.ui.screens.family

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.dummyapp.data.models.*
import com.example.dummyapp.utils.NetworkResult
import com.example.dummyapp.viewmodel.AuthViewModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * Family Dashboard Screen
 * Shows child's data based on family member's permissions
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FamilyDashboardScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToChat: ((String) -> Unit)? = null,
    onNavigateToProfile: ((String) -> Unit)? = null,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val childDataState by viewModel.childDataState.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        viewModel.fetchChildData()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Family Dashboard",
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.performLogout()
                        onNavigateToLogin()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Logout",
                            tint = Color(0xFF6B7280)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color(0xFFF9FAFB)
    ) { paddingValues ->
        when (val state = childDataState) {
            is NetworkResult.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFFEC4899))
                }
            }

            is NetworkResult.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ErrorOutline,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = Color(0xFFEF4444)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = state.message ?: "Failed to load data",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFF6B7280),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.fetchChildData() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEC4899))
                    ) {
                        Text("Retry")
                    }
                }
            }

            is NetworkResult.Success -> {
                val data = state.data
                if (data == null) {
                    Box(
                        modifier = Modifier.fillMaxSize().padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No data available", color = Color(0xFF6B7280))
                    }
                } else {
                    val permissions = data.permissions
                    val hasViewMatches = permissions.contains("view_matches")

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        // Child Profile Header
                        ChildProfileHeader(childProfile = data.childProfile)

                        // Only show matches (chat feature disabled for family members)
                        if (hasViewMatches) {
                            MatchesList(
                                matches = data.matches ?: emptyList(),
                                onMatchClick = onNavigateToProfile
                            )
                        } else {
                            // No permissions
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = null,
                                        modifier = Modifier.size(64.dp),
                                        tint = Color(0xFF9CA3AF)
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = "No permissions granted",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Color(0xFF6B7280)
                                    )
                                    Text(
                                        text = "Ask the user to grant you access to view their data.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFF9CA3AF),
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(horizontal = 32.dp, vertical = 8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            null -> {
                // Initial state
            }
        }
    }
}

@Composable
fun ChildProfileHeader(childProfile: ChildProfile) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile Photo
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFFEC4899), Color(0xFFF43F5E))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (childProfile.photo != null) {
                    AsyncImage(
                        model = childProfile.photo,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(
                        text = childProfile.name.firstOrNull()?.uppercase() ?: "?",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = "Viewing",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF9CA3AF)
                )
                Text(
                    text = childProfile.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )
                if (childProfile.completeness != null) {
                    Text(
                        text = "Profile ${childProfile.completeness}% complete",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF6B7280)
                    )
                }
            }
        }
    }
}

@Composable
fun MatchesList(
    matches: List<FamilyMatch>,
    onMatchClick: ((String) -> Unit)? = null
) {
    if (matches.isEmpty()) {
        EmptyState(
            icon = Icons.Default.Favorite,
            title = "No Matches Yet",
            subtitle = "Matches will appear here when available."
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(matches) { match ->
                MatchCard(
                    match = match,
                    onClick = { match.user?.id?.let { onMatchClick?.invoke(it) } }
                )
            }
        }
    }
}

@Composable
fun MatchCard(
    match: FamilyMatch,
    onClick: () -> Unit = {}
) {
    val user = match.user ?: return

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Photo
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF3F4F6)),
                contentAlignment = Alignment.Center
            ) {
                if (user.photo != null) {
                    AsyncImage(
                        model = user.photo,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color(0xFF9CA3AF),
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF111827)
                )
                Row {
                    user.profession?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF6B7280)
                        )
                    }
                    if (user.profession != null && user.city != null) {
                        Text(" • ", color = Color(0xFF9CA3AF), fontSize = 12.sp)
                    }
                    user.city?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF6B7280)
                        )
                    }
                }
            }

            // Compatibility Score
            match.compatibilityScore?.let { score ->
                Box(
                    modifier = Modifier
                        .background(Color(0xFFECFDF5), RoundedCornerShape(20.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "$score%",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF059669)
                    )
                }
            }
        }
    }
}

@Composable
fun ConversationsList(
    conversations: List<FamilyConversation>,
    onConversationClick: ((String) -> Unit)?
) {
    if (conversations.isEmpty()) {
        EmptyState(
            icon = Icons.Default.Chat,
            title = "No Conversations",
            subtitle = "Conversations will appear here when available."
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(conversations) { conversation ->
                ConversationCard(
                    conversation = conversation,
                    onClick = { onConversationClick?.invoke(conversation.id) }
                )
            }
        }
    }
}

@Composable
fun ConversationCard(conversation: FamilyConversation, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Participant photos (stack or first one)
            val participant = conversation.participants?.firstOrNull()
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF3F4F6)),
                contentAlignment = Alignment.Center
            ) {
                if (participant?.photo != null) {
                    AsyncImage(
                        model = participant.photo,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color(0xFF9CA3AF),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = conversation.participants?.joinToString(", ") { it.name } ?: "Unknown",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF111827),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                conversation.lastMessage?.let { msg ->
                    Text(
                        text = when (msg.type) {
                            "image" -> "📷 Image"
                            "audio" -> "🎵 Audio"
                            else -> msg.content ?: ""
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF6B7280),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Time
            conversation.updatedAt?.let { time ->
                Text(
                    text = formatRelativeTime(time),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF9CA3AF)
                )
            }
        }
    }
}

@Composable
fun EmptyState(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color(0xFFF3F4F6), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = Color(0xFF9CA3AF)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF374151)
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF9CA3AF),
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun formatRelativeTime(isoDate: String): String {
    return try {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        format.timeZone = TimeZone.getTimeZone("UTC")
        val date = format.parse(isoDate) ?: return ""
        
        val now = System.currentTimeMillis()
        val diff = now - date.time
        
        val minutes = diff / (1000 * 60)
        val hours = diff / (1000 * 60 * 60)
        val days = diff / (1000 * 60 * 60 * 24)
        
        when {
            minutes < 1 -> "Just now"
            minutes < 60 -> "${minutes}m"
            hours < 24 -> "${hours}h"
            days < 7 -> "${days}d"
            else -> SimpleDateFormat("MMM d", Locale.getDefault()).format(date)
        }
    } catch (e: Exception) {
        ""
    }
}
