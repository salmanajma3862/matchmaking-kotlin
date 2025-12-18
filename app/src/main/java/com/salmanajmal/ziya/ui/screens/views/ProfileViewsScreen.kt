package com.salmanajmal.ziya.ui.screens.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.salmanajmal.ziya.data.models.ProfileViewerEntry
import com.salmanajmal.ziya.data.models.ProfileViewersResponse
import com.salmanajmal.ziya.data.models.User
import com.salmanajmal.ziya.ui.theme.LocalExtendedColors
import com.salmanajmal.ziya.utils.NetworkResult
import com.salmanajmal.ziya.viewmodel.ProfileViewsViewModel

@Composable
fun ProfileViewsScreen(
    isPremium: Boolean,
    onNavigateToProfileDetail: (String, String) -> Unit,
    onNavigateToSubscription: () -> Unit,
    viewModel: ProfileViewsViewModel = hiltViewModel()
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("My Visitors", "Recently Viewed")

    val profileViewers by viewModel.profileViewers.collectAsState()
    val viewedProfiles by viewModel.viewedProfiles.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        // Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Visitors",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Tab Row
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { 
                        Text(
                            text = title,
                            fontWeight = if (selectedTabIndex == index) FontWeight.SemiBold else FontWeight.Normal
                        ) 
                    }
                )
            }
        }

        // Content
        when (selectedTabIndex) {
            0 -> ProfileViewersList(
                state = profileViewers,
                isPremium = isPremium,
                onLoadMore = { viewModel.loadMoreViewers() },
                canLoadMore = viewModel.canLoadMoreViewers(),
                onNavigateToProfileDetail = onNavigateToProfileDetail,
                onNavigateToSubscription = onNavigateToSubscription,
                emptyMessage = "No one has viewed your profile yet"
            )
            1 -> ProfileViewersList(
                state = viewedProfiles,
                isPremium = isPremium,
                onLoadMore = { viewModel.loadMoreViewed() },
                canLoadMore = viewModel.canLoadMoreViewed(),
                onNavigateToProfileDetail = onNavigateToProfileDetail,
                onNavigateToSubscription = onNavigateToSubscription,
                emptyMessage = "You haven't viewed any profiles yet"
            )
        }
    }
}

@Composable
private fun ProfileViewersList(
    state: NetworkResult<ProfileViewersResponse>,
    isPremium: Boolean,
    onLoadMore: () -> Unit,
    canLoadMore: Boolean,
    onNavigateToProfileDetail: (String, String) -> Unit,
    onNavigateToSubscription: () -> Unit,
    emptyMessage: String
) {
    val gridState = rememberLazyGridState()
    val extendedColors = LocalExtendedColors.current

    // Detect when to load more
    LaunchedEffect(gridState) {
        snapshotFlow { 
            val layoutInfo = gridState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisibleItem >= totalItems - 3 && canLoadMore && isPremium
        }.collect { shouldLoad ->
            if (shouldLoad) {
                onLoadMore()
            }
        }
    }

    when (state) {
        is NetworkResult.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is NetworkResult.Error -> {
            // Show friendly premium upgrade message for any error (including forbidden)
            PremiumUpgradeCard(
                onUpgrade = onNavigateToSubscription
            )
        }
        is NetworkResult.Success -> {
            val items = state.data?.data ?: emptyList()
            
            if (items.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Star,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = emptyMessage,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    state = gridState,
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(items) { entry ->
                        ProfileViewerCard(
                            entry = entry,
                            isPremium = isPremium,
                            onClick = { 
                                if (isPremium) {
                                    entry.user?.id?.let { userId ->
                                        onNavigateToProfileDetail(userId, "none")
                                    }
                                } else {
                                    onNavigateToSubscription()
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileViewerCard(
    entry: ProfileViewerEntry,
    isPremium: Boolean,
    onClick: () -> Unit
) {
    val user = entry.user ?: return
    val extendedColors = LocalExtendedColors.current
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.75f) // 3:4 aspect ratio like MatchProfileCard
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // Profile Photo (blurred for non-premium)
        AsyncImage(
            model = user.photos?.firstOrNull()?.url ?: "",
            contentDescription = "Profile photo",
            modifier = Modifier
                .fillMaxSize()
                .then(if (!isPremium) Modifier.blur(20.dp) else Modifier),
            contentScale = ContentScale.Crop
        )
        
        // Gradient Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.7f)
                        )
                    )
                )
        )
        
        // Lock Icon for non-premium
        if (!isPremium) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(48.dp)
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Lock,
                    contentDescription = "Premium required",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        
        // Time badge at top right
        entry.viewedAt?.let { timestamp ->
            Surface(
                color = Color.Black.copy(alpha = 0.6f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.TopEnd)
            ) {
                Text(
                    text = formatTimeAgo(timestamp),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
        
        // User Info at bottom
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(12.dp)
        ) {
            Text(
                text = if (isPremium) {
                    "${user.name}, ${user.age ?: calculateAge(user.dob)}"
                } else {
                    "• • • • •"
                },
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (isPremium) {
                Text(
                    text = user.city ?: "Unknown",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.9f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            } else {
                Text(
                    text = "Upgrade to see",
                    style = MaterialTheme.typography.bodySmall,
                    color = extendedColors.premium
                )
            }
        }
        
        // Premium star badge for non-premium
        if (!isPremium) {
            Surface(
                color = extendedColors.premium,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.TopStart)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Star,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = "Premium",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// Helper function to calculate age from DOB
private fun calculateAge(dob: String?): String {
    if (dob.isNullOrEmpty()) return "?"
    return try {
        val year = dob.substring(0, 4).toInt()
        val currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
        (currentYear - year).toString()
    } catch (e: Exception) {
        "?"
    }
}

@Composable
private fun PremiumBanner(
    onUpgrade: () -> Unit
) {
    val extendedColors = LocalExtendedColors.current
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onUpgrade),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            extendedColors.premiumGradientStart,
                            extendedColors.premiumGradientEnd
                        )
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.Star,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Unlock Full Access",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "See who viewed your profile with Premium",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
                Button(
                    onClick = onUpgrade,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = extendedColors.premium
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text("Upgrade", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun PremiumUpgradeCard(
    onUpgrade: () -> Unit
) {
    val extendedColors = LocalExtendedColors.current
    
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Premium icon with gradient background
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                extendedColors.premiumGradientStart,
                                extendedColors.premiumGradientEnd
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Star,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Premium Feature",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "See who's interested in you and the profiles you've viewed with Premium",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = onUpgrade,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = extendedColors.premium
                )
            ) {
                Icon(
                    imageVector = Icons.Rounded.Star,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Upgrade to Premium",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

private fun formatTimeAgo(timestamp: String): String {
    return try {
        val formatter = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", java.util.Locale.US)
        formatter.timeZone = java.util.TimeZone.getTimeZone("UTC")
        val date = formatter.parse(timestamp)
        val now = System.currentTimeMillis()
        val diff = now - (date?.time ?: 0)
        
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        
        when {
            days > 0 -> "${days}d ago"
            hours > 0 -> "${hours}h ago"
            minutes > 0 -> "${minutes}m ago"
            else -> "Just now"
        }
    } catch (e: Exception) {
        ""
    }
}
