package com.example.dummyapp.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.dummyapp.data.models.User
import com.example.dummyapp.utils.NetworkResult
import com.example.dummyapp.viewmodel.AuthViewModel

@Composable
fun ProfileScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onViewProfile: () -> Unit,
    onEditProfile: () -> Unit,
    onSettings: () -> Unit,
    onNotifications: () -> Unit,
    onPrivacy: () -> Unit,
    onSubscription: () -> Unit,
    onHelp: () -> Unit,
    onAbout: () -> Unit,
    onLogout: () -> Unit
) {
    val currentUserState by viewModel.currentUserState.collectAsState()
    val scrollState = rememberScrollState()

    val user = when (val state = currentUserState) {
        is NetworkResult.Success -> state.data
        else -> null
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFFEC4899), Color(0xFFF43F5E)) // Pink-500 to Rose-500
                    )
                )
                .padding(top = 48.dp, bottom = 32.dp, start = 24.dp, end = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Profile Picture
                Box(modifier = Modifier.padding(bottom = 16.dp)) {
                    Box(
                        modifier = Modifier
                            .size(112.dp)
                            .clip(CircleShape)
                            .border(4.dp, Color.White, CircleShape)
                            .background(Color.LightGray)
                    ) {
                        if (user?.photos?.isNotEmpty() == true) {
                            AsyncImage(
                                model = user.photos.firstOrNull { it.isPrimary }?.url ?: user.photos.first().url,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(64.dp)
                                    .align(Alignment.Center),
                                tint = Color.White
                            )
                        }
                    }
                    
                    // Edit Button
                    Surface(
                        onClick = onEditProfile,
                        shape = CircleShape,
                        color = Color.White,
                        shadowElevation = 4.dp,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "Edit Profile",
                                tint = Color(0xFFEC4899),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                // Name & Age
                Text(
                    text = if (user != null) "${user.name}, ${calculateUserAge(user.dob)}" else "Loading...",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                
                // City
                if (!user?.city.isNullOrEmpty()) {
                     Text(
                        text = "${user?.city}, ${user?.country ?: ""}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                // Stats
                Row(
                    horizontalArrangement = Arrangement.spacedBy(32.dp),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    StatItem(count = "${user?.totalMatches ?: 0}", label = "Matches")
                    StatItem(count = "${user?.profileViews ?: 0}", label = "Profile Views")
                    StatItem(count = "85%", label = "Complete")
                }
            }
        }

        // Profile Completion Card (Conditional)
        // Hardcoded condition for demo purposes as we don't have completion logic yet
        if (true) { 
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .offset(y = (-24).dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF1F2)), // Pink-50
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Complete your profile",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFFBE185D), // Pink-700
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "85%",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFFDB2777), // Pink-600
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { 0.85f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = Color(0xFFEC4899), // Pink-500
                        trackColor = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "A complete profile gets more matches!",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFDB2777) // Pink-600
                    )
                }
            }
        }

        // Menu Sections
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            
            // Account Section
            MenuSection(title = "ACCOUNT") {
                MenuItem(
                    icon = Icons.Default.Person,
                    label = "View My Public Profile",
                    subtitle = "See how others view your profile",
                    onClick = onViewProfile,
                    iconTint = Color(0xFFDB2777), // Pink-600
                    iconBgColor = Color(0xFFFDF2F8) // Pink-50
                )
                MenuItem(
                    icon = Icons.Default.Edit,
                    label = "Edit Profile",
                    subtitle = "Update your information and photos",
                    onClick = onEditProfile,
                    iconTint = Color(0xFF9333EA), // Purple-600
                    iconBgColor = Color(0xFFFAF5FF) // Purple-50
                )
                MenuItem(
                    icon = Icons.Default.Settings,
                    label = "Settings",
                    subtitle = "App preferences and privacy",
                    onClick = onSettings,
                    iconTint = Color(0xFF374151), // Gray-700
                    iconBgColor = Color(0xFFF3F4F6) // Gray-100
                )
                MenuItem(
                    icon = Icons.Default.Notifications,
                    label = "Notifications",
                    subtitle = "Manage notification preferences",
                    onClick = onNotifications,
                    iconTint = Color(0xFFEA580C), // Orange-600
                    iconBgColor = Color(0xFFFFF7ED), // Orange-50
                    badgeCount = 3
                )
                MenuItem(
                    icon = Icons.Default.Security,
                    label = "Privacy & Safety",
                    subtitle = "Control your visibility and safety",
                    onClick = onPrivacy,
                    iconTint = Color(0xFF2563EB), // Blue-600
                    iconBgColor = Color(0xFFEFF6FF) // Blue-50
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Premium Section
            MenuSection(title = "PREMIUM") {
                MenuItem(
                    icon = Icons.Default.Favorite,
                    label = "Upgrade to Premium",
                    subtitle = "Get unlimited likes and more features",
                    onClick = onSubscription,
                    iconTint = Color(0xFFDB2777), // Pink-600
                    iconBgColor = Color(0xFFFDF2F8) // Pink-50
                )
                MenuItem(
                    icon = Icons.Default.CreditCard,
                    label = "Payment & Billing",
                    subtitle = "Manage your subscription",
                    onClick = onSubscription,
                    iconTint = Color(0xFF9333EA), // Purple-600
                    iconBgColor = Color(0xFFFAF5FF) // Purple-50
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Support Section
            MenuSection(title = "SUPPORT") {
                MenuItem(
                    icon = Icons.Default.Help,
                    label = "Help & Support",
                    subtitle = "Get help and contact us",
                    onClick = onHelp,
                    iconTint = Color(0xFF16A34A), // Green-600
                    iconBgColor = Color(0xFFF0FDF4) // Green-50
                )
                MenuItem(
                    icon = Icons.Default.Description,
                    label = "Terms & About",
                    subtitle = "Legal information and app details",
                    onClick = onAbout,
                    iconTint = Color(0xFF4F46E5), // Indigo-600
                    iconBgColor = Color(0xFFEEF2FF) // Indigo-50
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Logout
            Button(
                onClick = {
                    viewModel.performLogout()
                    onLogout()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFFEF4444) // Red-500
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Logout", fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Version
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Version 1.0.0",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Text(
                    text = "Made with ❤️",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun StatItem(count: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = count,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.8f)
        )
    }
}

@Composable
fun MenuSection(title: String, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray,
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
        )
        HorizontalDivider(color = Color(0xFFF3F4F6))
        content()
    }
}

@Composable
fun MenuItem(
    icon: ImageVector,
    label: String,
    subtitle: String,
    onClick: () -> Unit,
    iconTint: Color,
    iconBgColor: Color,
    badgeCount: Int = 0
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(iconBgColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )
            if (badgeCount > 0) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 4.dp, y = (-4).dp)
                        .size(16.dp)
                        .background(Color.Red, CircleShape)
                        .border(2.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = badgeCount.toString(),
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF111827), // Gray-900
                fontWeight = FontWeight.Medium
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF6B7280) // Gray-500
            )
        }
        
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = Color(0xFF9CA3AF), // Gray-400
            modifier = Modifier.size(20.dp)
        )
    }
}

// Helper to calculate age from Date string (simplified)
private fun calculateUserAge(dobString: String?): Int {
    if (dobString == null) return 25 // Default
    try {
        // Handle ISO 8601 format (e.g., 2000-01-01T00:00:00.000Z)
        val format = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", java.util.Locale.US)
        format.timeZone = java.util.TimeZone.getTimeZone("UTC")
        val dob = format.parse(dobString) ?: return 25
        
        val today = java.util.Calendar.getInstance()
        val birthDate = java.util.Calendar.getInstance()
        birthDate.time = dob
        var age = today.get(java.util.Calendar.YEAR) - birthDate.get(java.util.Calendar.YEAR)
        if (today.get(java.util.Calendar.DAY_OF_YEAR) < birthDate.get(java.util.Calendar.DAY_OF_YEAR)) {
            age--
        }
        return age
    } catch (e: Exception) {
        // Try simpler format if ISO fails (e.g. yyyy-MM-dd)
        try {
            val format = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
            val dob = format.parse(dobString) ?: return 25
            val today = java.util.Calendar.getInstance()
            val birthDate = java.util.Calendar.getInstance()
            birthDate.time = dob
            var age = today.get(java.util.Calendar.YEAR) - birthDate.get(java.util.Calendar.YEAR)
            if (today.get(java.util.Calendar.DAY_OF_YEAR) < birthDate.get(java.util.Calendar.DAY_OF_YEAR)) {
                age--
            }
            return age
        } catch (e2: Exception) {
            return 25
        }
    }
}
