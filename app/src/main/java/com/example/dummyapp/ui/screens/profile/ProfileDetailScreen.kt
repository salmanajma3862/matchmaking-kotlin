package com.example.dummyapp.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.dummyapp.data.models.User
import com.example.dummyapp.utils.NetworkResult
import com.example.dummyapp.viewmodel.ProfileDetailViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ProfileDetailScreen(
    userId: String,
    matchStatus: String = "none", // sent, received, match, none
    onNavigateBack: () -> Unit,
    viewModel: ProfileDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(userId) {
        viewModel.fetchUserProfile(userId)
    }

    val userState by viewModel.userState.collectAsState()
    val actionState by viewModel.actionState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

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
                snackbarHostState = snackbarHostState
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ProfileDetailContent(
    user: User,
    matchStatus: String,
    onNavigateBack: () -> Unit,
    onUndoSwipe: () -> Unit,
    onAccept: () -> Unit,
    onReject: () -> Unit,
    onUnmatch: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(text = "") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    navigationIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            if (matchStatus != "none") {
                BottomActionBar(
                    matchStatus = matchStatus,
                    onUndoSwipe = onUndoSwipe,
                    onAccept = onAccept,
                    onReject = onReject,
                    onUnmatch = onUnmatch
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Header Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            ) {
                val photoUrl = user.photos?.firstOrNull { it.isPrimary }?.url ?: user.photos?.firstOrNull()?.url ?: "https://via.placeholder.com/400"
                AsyncImage(
                    model = photoUrl,
                    contentDescription = "Profile Picture",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                
                // Gradient Overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                                startY = 200f
                            )
                        )
                )

                // Name and Basic Info at bottom of image
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                        .padding(bottom = 24.dp) // Add some padding for the overlap
                ) {
                    Text(
                        text = "${user.name}, ${calculateAge(user.dob)}",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = user.city ?: "Unknown Location",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }

            // Content Body
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-24).dp) // Overlap slightly
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(24.dp)
                    .padding(bottom = paddingValues.calculateBottomPadding()) // Add padding for bottom bar
            ) {
                // Bio Section
                if (!user.bio.isNullOrBlank()) {
                    Text(
                        text = "About",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = user.bio,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // Basic Info Grid
                Text(
                    text = "Basics",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column(modifier = Modifier.weight(1f)) {
                        InfoItem(icon = Icons.Default.Accessibility, label = "${user.height ?: "-"} cm")
                        Spacer(modifier = Modifier.height(12.dp))
                        InfoItem(icon = Icons.Default.Work, label = user.profession ?: "-")
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        InfoItem(icon = Icons.Default.School, label = user.education ?: "-")
                        Spacer(modifier = Modifier.height(12.dp))
                        InfoItem(icon = Icons.Default.LocationOn, label = user.country ?: "-")
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))

                // Personal Details
                Text(
                    text = "Personal Details",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
                
                DetailRow("Religion", user.religion ?: "-")
                DetailRow("Sect", user.sect ?: "-")
                DetailRow("Marital Status", user.maritalStatus?.capitalize() ?: "-")
                DetailRow("Diet", user.dietPreference?.capitalize() ?: "-")
                DetailRow("Body Type", user.bodyType?.capitalize() ?: "-")
                if (user.weight != null) {
                    DetailRow("Weight", "${user.weight} kg")
                }
                DetailRow("Smoking", if (user.smoking == true) "Yes" else "No")
                DetailRow("Drinking", if (user.drinking == true) "Yes" else "No")

                Spacer(modifier = Modifier.height(24.dp))

                // Family Details
                Text(
                    text = "Family",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
                
                DetailRow("Family Background", user.familyBackground?.capitalize() ?: "-")
                if (user.numberOfSiblings != null) {
                    DetailRow("Siblings", "${user.numberOfSiblings}")
                }
                DetailRow("Living with Family", if (user.livingWithFamily == true) "Yes" else "No")

                Spacer(modifier = Modifier.height(24.dp))

                // Interests & Hobbies
                val allInterests = (user.interests ?: emptyList()) + (user.hobbies ?: emptyList())
                if (allInterests.isNotEmpty()) {
                    Text(
                        text = "Interests & Hobbies",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        allInterests.forEach { interest ->
                            SuggestionChip(
                                onClick = { },
                                label = { Text(interest) }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
                
                Spacer(modifier = Modifier.height(80.dp)) 
            }
        }
    }
}

@Composable
fun BottomActionBar(
    matchStatus: String,
    onUndoSwipe: () -> Unit,
    onAccept: () -> Unit,
    onReject: () -> Unit,
    onUnmatch: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (matchStatus) {
            "sent" -> {
                Button(
                    onClick = onUndoSwipe,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Undo Request")
                }
            }
            "received" -> {
                Button(
                    onClick = onReject,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Reject")
                }
                Button(
                    onClick = onAccept,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
                ) {
                    Text("Accept")
                }
            }
            "match" -> {
                Button(
                    onClick = onUnmatch,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Unmatch")
                }
                Button(
                    onClick = { /* Navigate to chat */ },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Message")
                }
            }
        }
    }
}

@Composable
fun InfoItem(icon: ImageVector, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.Gray)
        Text(text = value, fontWeight = FontWeight.Medium)
    }
}

fun calculateAge(birthDate: String?): String {
    if (birthDate.isNullOrEmpty()) return "?"
    return try {
        val year = birthDate.take(4).toInt()
        val currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
        (currentYear - year).toString()
    } catch (e: Exception) {
        "?"
    }
}

fun String.capitalize(): String {
    return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}



