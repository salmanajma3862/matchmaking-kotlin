package com.salmanajmal.ziya.ui.screens.profile

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.salmanajmal.ziya.data.models.User
import com.salmanajmal.ziya.utils.NetworkResult
import com.salmanajmal.ziya.viewmodel.ProfileDetailViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDetailScreen(
    userId: String,
    matchStatus: String = "none", // sent, received, match, none
    onNavigateBack: () -> Unit,
    onNavigateToChat: (String) -> Unit,
    viewModel: ProfileDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(userId) {
        viewModel.fetchUserProfile(userId)
    }

    val userState by viewModel.userState.collectAsState()
    val actionState by viewModel.actionState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle navigation events
    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { conversationId ->
            onNavigateToChat(conversationId)
        }
    }

    // Handle action results
    LaunchedEffect(actionState) {
        actionState?.let { result ->
            when (result) {
                is NetworkResult.Success -> {
                    onNavigateBack() // Go back on success
                    viewModel.resetActionState()
                }
                is NetworkResult.Error -> {
                    snackbarHostState.showSnackbar(result.message ?: "Action failed")
                    viewModel.resetActionState()
                }
                else -> {}
            }
        }
    }

    when (val result = userState) {
        is NetworkResult.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is NetworkResult.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = result.message ?: "An error occurred")
                Button(onClick = { viewModel.fetchUserProfile(userId) }) {
                    Text("Retry")
                }
            }
        }
        is NetworkResult.Success -> {
            val user = result.data
            if (user == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("User not found")
                }
                return
            }
            
            ProfileDetailContent(
                user = user, 
                matchStatus = matchStatus,
                onNavigateBack = onNavigateBack,
                onUndoSwipe = { viewModel.undoSwipe(userId) },
                onAccept = { viewModel.acceptMatch(userId) },
                onReject = { viewModel.rejectMatch(userId) },
                onUnmatch = { viewModel.unmatchUser(userId) },
                onMessage = { viewModel.initiateMessage(userId) },
                onLike = { viewModel.acceptMatch(userId) },
                onNope = { viewModel.rejectMatch(userId) },
                snackbarHostState = snackbarHostState
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProfileDetailContent(
    user: User,
    matchStatus: String,
    onNavigateBack: () -> Unit,
    onUndoSwipe: () -> Unit,
    onAccept: () -> Unit,
    onReject: () -> Unit,
    onUnmatch: () -> Unit,
    onMessage: () -> Unit,
    onLike: () -> Unit,
    onNope: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val photos = user.photos ?: emptyList()
    val pagerState = rememberPagerState(pageCount = { if (photos.isNotEmpty()) photos.size else 1 })

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        // Photo Gallery (Background)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight * 0.6f)
                .background(Color.Black)
        ) {
            HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
                val photoUrl = if (photos.isNotEmpty()) photos[page].url else "https://via.placeholder.com/400"
                AsyncImage(
                    model = photoUrl,
                    contentDescription = "Profile Photo",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // Gradient Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.4f),
                                Color.Transparent,
                                Color.Transparent
                            )
                        )
                    )
            )

            // Photo Indicators
            if (photos.size > 1) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 80.dp) // Below header
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    repeat(photos.size) { index ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(
                                    if (pagerState.currentPage == index) Color.White
                                    else Color.White.copy(alpha = 0.4f)
                                )
                        )
                    }
                }
            }
        }

        // Header Controls (Absolute top)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier
                    .background(Color.White.copy(alpha = 0.2f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            IconButton(
                onClick = { /* More options */ },
                modifier = Modifier
                    .background(Color.White.copy(alpha = 0.2f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "More",
                    tint = Color.White
                )
            }
        }

        // Content Body (Scrollable)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Spacer to push content down (overlap image)
            Spacer(modifier = Modifier.height(screenHeight * 0.55f))

            // White Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .padding(bottom = 150.dp) // Extra space for bottom action bar
                ) {
                    // Basic Info
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 4.dp)
                    ) {
                        Text(
                            text = "${user.name}, ${calculateAge(user.dob)}",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        if (user.isVerified) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = "Verified",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    if (!user.city.isNullOrEmpty() && !user.country.isNullOrEmpty()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.LocationOn,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${user.city}, ${user.country}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Quick Stats
                    Row(
                        modifier = Modifier.padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        if (!user.profession.isNullOrEmpty()) {
                            QuickStat(icon = Icons.Outlined.WorkOutline, text = user.profession)
                        }
                        if (!user.education.isNullOrEmpty()) {
                            QuickStat(icon = Icons.Outlined.School, text = user.education)
                        }
                    }

                    Divider(color = MaterialTheme.colorScheme.outlineVariant)
                    Spacer(modifier = Modifier.height(24.dp))

                    // Bio
                    if (!user.bio.isNullOrBlank()) {
                        Text(
                            text = user.bio,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 24.sp
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Divider(color = MaterialTheme.colorScheme.outlineVariant)
                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    // Basic Details Grid
                    SectionTitle("Basic Details")
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            val col1 = mutableListOf<Pair<String, String>>()
                            val col2 = mutableListOf<Pair<String, String>>()

                            user.height?.let { col1.add("Height" to formatHeight(it)) }
                            user.bodyType?.let { col2.add("Body Type" to it.capitalize()) }
                            user.maritalStatus?.let { col1.add("Marital Status" to it.capitalize()) }
                            user.religion?.let { col2.add("Religion" to it) }
                            user.sect?.let { col1.add("Sect" to it) }
                            user.incomeRange?.let { col2.add("Income Range" to it) }

                            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                                col1.forEach { DetailItem(it.first, it.second) }
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                                col2.forEach { DetailItem(it.first, it.second) }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    Divider(color = MaterialTheme.colorScheme.outlineVariant)
                    Spacer(modifier = Modifier.height(24.dp))

                    // Lifestyle
                    SectionTitle("Lifestyle")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        LifestyleChip(
                            icon = Icons.Outlined.SmokingRooms,
                            text = if (user.smoking == true) "Smoker" else "Non-smoker"
                        )
                        LifestyleChip(
                            icon = Icons.Outlined.LocalBar,
                            text = if (user.drinking == true) "Drinks" else "Doesn't drink"
                        )
                        user.dietPreference?.let {
                            LifestyleChip(icon = Icons.Outlined.Restaurant, text = it)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    Divider(color = MaterialTheme.colorScheme.outlineVariant)
                    Spacer(modifier = Modifier.height(24.dp))

                    // Looking For
                    if (!user.intention.isNullOrEmpty() || !user.readyForMarriageTimeframe.isNullOrEmpty()) {
                        SectionTitle("Looking For")
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            user.intention?.let {
                                LookingForItem(icon = Icons.Outlined.TrackChanges, text = it.replace("_", " ").capitalize())
                            }
                            user.readyForMarriageTimeframe?.let {
                                LookingForItem(icon = Icons.Outlined.AccessTime, text = "Ready ${it.replace("_", " ")}")
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Divider(color = MaterialTheme.colorScheme.outlineVariant)
                        Spacer(modifier = Modifier.height(24.dp))
                    }


                    // Interests & Hobbies
                    val allInterests = (user.interests ?: emptyList()) + (user.hobbies ?: emptyList())
                    if (allInterests.isNotEmpty()) {
                        SectionTitle("Interests & Hobbies")
                        @OptIn(ExperimentalLayoutApi::class)
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            user.interests?.forEach { interest ->
                                InterestChip(text = interest, color = MaterialTheme.colorScheme.primary, bgColor = MaterialTheme.colorScheme.primaryContainer)
                            }
                            user.hobbies?.forEach { hobby ->
                                InterestChip(text = hobby, color = MaterialTheme.colorScheme.tertiary, bgColor = MaterialTheme.colorScheme.tertiaryContainer)
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Divider(color = Color(0xFFF3F4F6))
                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    // Family Background
                    if (!user.familyBackground.isNullOrEmpty() || user.numberOfSiblings != null || user.livingWithFamily != null) {
                        SectionTitle("Family Background")
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            user.familyBackground?.let {
                                LookingForItem(icon = Icons.Outlined.MenuBook, text = it)
                            }
                            user.numberOfSiblings?.let {
                                LookingForItem(icon = Icons.Outlined.Group, text = "$it sibling${if (it != 1) "s" else ""}")
                            }
                            user.livingWithFamily?.let {
                                LookingForItem(icon = Icons.Outlined.Home, text = if (it) "Living with family" else "Living independently")
                            }
                        }
                    }
                }
            }
        }

        // Bottom Action Bar (Fixed at bottom)
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .navigationBarsPadding()
                .padding(16.dp)
        ) {
            BottomActionBar(
                matchStatus = matchStatus,
                onUndoSwipe = onUndoSwipe,
                onAccept = onAccept,
                onReject = onReject,
                onUnmatch = onUnmatch,
                onMessage = onMessage,
                onLike = onLike,
                onNope = onNope
            )
        }
        
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun QuickStat(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant, // gray-600
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant, // gray-500
        modifier = Modifier.padding(bottom = 12.dp)
    )
}

@Composable
fun DetailItem(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant, // gray-500
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface // gray-900
        )
    }
}

@Composable
fun LifestyleChip(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun LookingForItem(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun InterestChip(text: String, color: Color, bgColor: Color) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = color,
        modifier = Modifier
            .background(bgColor, CircleShape)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    )
}

@Composable
fun BottomActionBar(
    matchStatus: String,
    onUndoSwipe: () -> Unit,
    onAccept: () -> Unit,
    onReject: () -> Unit,
    onUnmatch: () -> Unit,
    onMessage: () -> Unit,
    onLike: () -> Unit,
    onNope: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Like/Heart Button (Only if needed, but logic says matchStatus)
        // Figma has a Heart button and a Message button.
        // Existing logic has different buttons based on status.
        // I will keep existing logic but style them to look better if possible, 
        // or just keep them as is since the user said "dont change the button and their logic".
        // I will just wrap them to fit the layout.
        
        when (matchStatus) {
            "sent" -> {
                Button(
                    onClick = onUndoSwipe,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Undo Request")
                }
            }
            "received" -> {
                Button(
                    onClick = onReject,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Reject")
                }
                Button(
                    onClick = onAccept,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary), // Green
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Accept")
                }
            }
            "match" -> {
                Button(
                    onClick = onUnmatch,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.weight(0.3f)
                ) {
                    Text("Unmatch")
                }
                Button(
                    onClick = onMessage,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .weight(0.7f)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primaryContainer)
                            ),
                            CircleShape
                        )
                ) {
                    Icon(Icons.Outlined.Message, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Message")
                }
            }
            "none" -> {
                // Nope button
                Button(
                    onClick = onNope,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE5E7EB)),
                    modifier = Modifier
                        .weight(0.4f)
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Pass",
                        tint = Color(0xFF6B7280),
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                // Like button
                Button(
                    onClick = onLike,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                    modifier = Modifier
                        .weight(0.6f)
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Favorite,
                        contentDescription = "Like",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Like", color = Color.White, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

fun calculateAge(birthDate: String?): String {
    if (birthDate.isNullOrEmpty()) return "?"
    return try {
        val year = birthDate.take(4).toInt()
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        (currentYear - year).toString()
    } catch (e: Exception) {
        "?"
    }
}

fun formatHeight(heightCm: Int): String {
    val feet = heightCm / 30.48
    val inches = (heightCm % 30.48) / 2.54
    return "${feet.toInt()}'${Math.round(inches)}\""
}

fun String.capitalize(): String {
    return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}



