package com.salmanajmal.ziya.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.LaunchedEffect
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
import com.salmanajmal.ziya.data.models.User
import com.salmanajmal.ziya.utils.NetworkResult
import com.salmanajmal.ziya.viewmodel.AuthViewModel
import com.salmanajmal.ziya.ui.theme.LocalThemeManager
import com.salmanajmal.ziya.ui.theme.ThemeMode
import com.salmanajmal.ziya.ui.theme.LightColors

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
    onLogout: () -> Unit,
    onInviteFamily: () -> Unit = {}
) {
    val currentUserState by viewModel.currentUserState.collectAsState()
    val scrollState = rememberScrollState()
    
    // Theme management
    val themeManager = LocalThemeManager.current
    var showThemeDialog by remember { mutableStateOf(false) }

    val user = when (val state = currentUserState) {
        is NetworkResult.Success -> state.data
        else -> null
    }
    
    // Theme Selection Dialog
    if (showThemeDialog) {
        androidx.compose.ui.window.Dialog(
            onDismissRequest = { showThemeDialog = false }
        ) {
            Surface(
                shape = RoundedCornerShape(28.dp),
                color = Color.White,
                tonalElevation = 0.dp,
                shadowElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Header Icon
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Palette,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Choose Theme",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937) // Gray-900
                    )
                    
                    Text(
                        text = "Customize your app appearance",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF6B7280), // Gray-500
                        modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ThemeOption(
                            title = "Light",
                            subtitle = "Always use light theme",
                            isSelected = themeManager.themeMode == ThemeMode.LIGHT,
                            onClick = {
                                themeManager.updateThemeMode(ThemeMode.LIGHT)
                                showThemeDialog = false
                            }
                        )
                        
                        ThemeOption(
                            title = "Dark",
                            subtitle = "Always use dark theme",
                            isSelected = themeManager.themeMode == ThemeMode.DARK,
                            onClick = {
                                themeManager.updateThemeMode(ThemeMode.DARK)
                                showThemeDialog = false
                            }
                        )
                        
                        ThemeOption(
                            title = "System",
                            subtitle = "Follow system settings",
                            isSelected = themeManager.themeMode == ThemeMode.SYSTEM,
                            onClick = {
                                themeManager.updateThemeMode(ThemeMode.SYSTEM)
                                showThemeDialog = false
                            }
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    TextButton(
                        onClick = { showThemeDialog = false },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color(0xFF6B7280) // Gray-500
                        )
                    ) {
                        Text(
                            text = "Cancel",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.tertiary) // Pink-500 to Rose-500
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
                            .border(4.dp, MaterialTheme.colorScheme.surface, CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
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
                        color = MaterialTheme.colorScheme.surface,
                        shadowElevation = 4.dp,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "Edit Profile",
                                tint = MaterialTheme.colorScheme.primary,
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
                    color = MaterialTheme.colorScheme.onPrimary
                )
                
                // City
                if (!user?.city.isNullOrEmpty()) {
                     Text(
                        text = "${user?.city}, ${user?.country ?: ""}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f),
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
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer), // Pink-50
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
                            color = MaterialTheme.colorScheme.onPrimaryContainer, // Pink-700
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "85%",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary, // Pink-600
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
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            
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
                    icon = Icons.Default.Palette,
                    label = "Theme",
                    subtitle = when (themeManager.themeMode) {
                        ThemeMode.LIGHT -> "Light mode"
                        ThemeMode.DARK -> "Dark mode"
                        ThemeMode.SYSTEM -> "System default"
                    },
                    onClick = { showThemeDialog = true },
                    iconTint = Color(0xFF7C3AED), // Violet-600
                    iconBgColor = Color(0xFFF5F3FF) // Violet-50
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
                MenuItem(
                    icon = Icons.Default.GroupAdd,
                    label = "Invite Family",
                    subtitle = "Create & manage family invite codes",
                    onClick = onInviteFamily,
                    iconTint = Color(0xFF059669), // Emerald-600
                    iconBgColor = Color(0xFFECFDF5) // Emerald-50
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
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.error // Red-500
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
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Made with ❤️",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
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
            color = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
        )
    }
}

@Composable
fun MenuSection(title: String, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(start = 20.dp, top = 16.dp, bottom = 12.dp)
        )
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
    Surface(
        onClick = onClick,
        color = Color.Transparent,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(iconTint.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(22.dp)
                )
                if (badgeCount > 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 4.dp, y = (-4).dp)
                            .size(18.dp)
                            .background(MaterialTheme.colorScheme.error, CircleShape)
                            .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = badgeCount.toString(),
                            color = MaterialTheme.colorScheme.onError,
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
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface, // Gray-900
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant // Gray-400
                )
            }
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f), // Gray-300
                modifier = Modifier.size(24.dp)
            )
        }
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

/**
 * Theme option item for the theme selection dialog
 */
@Composable
private fun ThemeOption(
    title: String,
    subtitle: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = if (isSelected) LightColors.TertiaryContainer else Color.Transparent,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = if (isSelected) LightColors.Tertiary else LightColors.OnSurface
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isSelected) LightColors.Tertiary.copy(alpha = 0.8f) else LightColors.OnSurfaceVariant
                )
            }
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = LightColors.Tertiary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}


