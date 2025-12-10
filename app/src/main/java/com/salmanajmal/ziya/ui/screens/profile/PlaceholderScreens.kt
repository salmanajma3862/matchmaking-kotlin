package com.salmanajmal.ziya.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.salmanajmal.ziya.data.models.User
import com.salmanajmal.ziya.viewmodel.EditProfileViewModel

// Color constants matching app theme
private val PrimaryPink = Color(0xFFEC4899)
private val PinkGradientStart = Color(0xFFEC4899)
private val PinkGradientEnd = Color(0xFFF43F5E)
private val CardBackground = Color(0xFFFDF2F8)
private val SectionTitleColor = Color(0xFFBE185D)
private val TextPrimary = Color(0xFF111827)
private val TextSecondary = Color(0xFF6B7280)
private val InputBorder = Color(0xFFE5E7EB)
private val WarningColor = Color(0xFFF59E0B)

// Validation constants
private const val MAX_BIO_CHARS = 500
private const val MAX_INTERESTS = 5
private const val MAX_HOBBIES = 3

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    user: User?,
    onNavigateBack: () -> Unit,
    viewModel: EditProfileViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()
    
    // Initialize with user data
    LaunchedEffect(user) {
        user?.let { viewModel.initializeWithUser(it) }
    }
    
    // Handle save success
    LaunchedEffect(state.saveSuccess) {
        if (state.saveSuccess) {
            viewModel.clearSaveSuccess()
            onNavigateBack()
        }
    }
    
    // Name change confirmation dialog
    if (state.showNameChangeConfirmation) {
        AlertDialog(
            onDismissRequest = { viewModel.cancelNameChange() },
            containerColor = Color.White,
            shape = RoundedCornerShape(24.dp),
            title = { 
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(Color(0xFFFEF3C7), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Warning,
                            contentDescription = null,
                            tint = WarningColor,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Change Name?",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = TextPrimary
                    )
                }
            },
            text = { 
                Text(
                    "You will not be able to change your name for the next 30 days. Are you sure you want to proceed?",
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.confirmNameChange() },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryPink),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Yes, Change Name", fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { viewModel.cancelNameChange() },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF9FAFB))) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Premium Gradient Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(PinkGradientStart, PinkGradientEnd)
                        )
                    )
                    .padding(top = 48.dp, bottom = 24.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.White.copy(alpha = 0.2f), CircleShape)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    
                    Text(
                        "Edit Profile",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    
                    TextButton(
                        onClick = { viewModel.attemptSave() },
                        enabled = !state.isSaving,
                        colors = ButtonDefaults.textButtonColors(contentColor = Color.White)
                    ) {
                        if (state.isSaving) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp,
                                color = Color.White
                            )
                        } else {
                            Text("Save", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }
                }
            }

            // Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Error message
                state.errorMessage?.let { error ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEE2E2)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Error,
                                contentDescription = null,
                                tint = Color(0xFFDC2626),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                error,
                                color = Color(0xFFDC2626),
                                modifier = Modifier.weight(1f),
                                fontWeight = FontWeight.Medium
                            )
                            IconButton(onClick = { viewModel.clearError() }) {
                                Icon(Icons.Default.Close, contentDescription = "Dismiss", tint = Color(0xFFDC2626))
                            }
                        }
                    }
                }
                
                // ==================== Basic Info Section ====================
                ProfileSection(
                    title = "Basic Information",
                    icon = Icons.Default.Person,
                    iconColor = Color(0xFFDB2777)
                ) {
                    // Name field with warning
                    Column {
                        StyledTextField(
                            value = state.name,
                            onValueChange = { viewModel.updateName(it) },
                            label = "Full Name",
                            trailingIcon = if (state.name != state.originalName && state.originalName.isNotEmpty()) {
                                {
                                    Icon(
                                        Icons.Default.Info,
                                        contentDescription = null,
                                        tint = WarningColor,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            } else null
                        )
                        if (state.name != state.originalName && state.originalName.isNotEmpty()) {
                            Text(
                                "⚠️ Name can only be changed once per month",
                                color = WarningColor,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Bio with character count
                    Column {
                        StyledTextField(
                            value = state.bio,
                            onValueChange = { 
                                if (it.length <= MAX_BIO_CHARS) {
                                    viewModel.updateBio(it) 
                                }
                            },
                            label = "Bio",
                            minLines = 3,
                            maxLines = 5,
                            singleLine = false
                        )
                        Text(
                            "${state.bio.length}/$MAX_BIO_CHARS characters",
                            color = if (state.bio.length > MAX_BIO_CHARS - 50) WarningColor else TextSecondary,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp),
                            textAlign = TextAlign.End
                        )
                    }
                }
                
                // ==================== Physical Section ====================
                ProfileSection(
                    title = "Physical Details",
                    icon = Icons.Default.Accessibility,
                    iconColor = Color(0xFF9333EA)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StyledTextField(
                            value = state.height,
                            onValueChange = { viewModel.updateHeight(it) },
                            label = "Height (cm)",
                            modifier = Modifier.weight(1f),
                            keyboardType = KeyboardType.Number
                        )
                        
                        StyledTextField(
                            value = state.weight,
                            onValueChange = { viewModel.updateWeight(it) },
                            label = "Weight (kg)",
                            modifier = Modifier.weight(1f),
                            keyboardType = KeyboardType.Number
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    StyledDropdown(
                        label = "Body Type",
                        value = state.bodyType,
                        options = listOf("Slim", "Athletic", "Average", "Curvy", "Plus Size"),
                        onValueChange = { viewModel.updateBodyType(it) }
                    )
                }
                
                // ==================== Location Section ====================
                ProfileSection(
                    title = "Location",
                    icon = Icons.Default.LocationOn,
                    iconColor = Color(0xFF059669)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StyledTextField(
                            value = state.city,
                            onValueChange = { viewModel.updateCity(it) },
                            label = "City",
                            modifier = Modifier.weight(1f)
                        )
                        
                        StyledTextField(
                            value = state.country,
                            onValueChange = { viewModel.updateCountry(it) },
                            label = "Country",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                
                // ==================== Background Section ====================
                ProfileSection(
                    title = "Background",
                    icon = Icons.Default.School,
                    iconColor = Color(0xFF2563EB)
                ) {
                    StyledDropdown(
                        label = "Religion",
                        value = state.religion,
                        options = listOf("Islam", "Christianity", "Hinduism", "Buddhism", "Sikhism", "Judaism", "Other", "Prefer not to say"),
                        onValueChange = { viewModel.updateReligion(it) }
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    StyledTextField(
                        value = state.sect ?: "",
                        onValueChange = { viewModel.updateSect(it.takeIf { s -> s.isNotBlank() }) },
                        label = "Sect (optional)"
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    StyledDropdown(
                        label = "Marital Status",
                        value = state.maritalStatus,
                        options = listOf("Single", "Divorced", "Widowed", "Separated"),
                        onValueChange = { viewModel.updateMaritalStatus(it) }
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    StyledDropdown(
                        label = "Education",
                        value = state.education,
                        options = listOf("High School", "Bachelor's", "Master's", "PhD", "Other"),
                        onValueChange = { viewModel.updateEducation(it) }
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    StyledTextField(
                        value = state.profession,
                        onValueChange = { viewModel.updateProfession(it) },
                        label = "Profession"
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    StyledDropdown(
                        label = "Income Range",
                        value = state.incomeRange,
                        options = listOf("Below 50K", "50K-100K", "100K-200K", "200K-500K", "500K+", "Prefer not to say"),
                        onValueChange = { viewModel.updateIncomeRange(it) }
                    )
                }
                
                // ==================== Lifestyle Section ====================
                ProfileSection(
                    title = "Lifestyle",
                    icon = Icons.Default.Favorite,
                    iconColor = Color(0xFFEA580C)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        StyledSwitch(
                            label = "Smoking",
                            checked = state.smoking,
                            onCheckedChange = { viewModel.updateSmoking(it) },
                            modifier = Modifier.weight(1f)
                        )
                        
                        StyledSwitch(
                            label = "Drinking",
                            checked = state.drinking,
                            onCheckedChange = { viewModel.updateDrinking(it) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    StyledDropdown(
                        label = "Diet Preference",
                        value = state.dietPreference,
                        options = listOf("Halal", "Vegetarian", "Vegan", "Non-Vegetarian", "No Preference"),
                        onValueChange = { viewModel.updateDietPreference(it) }
                    )
                }
                
                // ==================== Family Section ====================
                ProfileSection(
                    title = "Family",
                    icon = Icons.Default.FamilyRestroom,
                    iconColor = Color(0xFF7C3AED)
                ) {
                    StyledTextField(
                        value = state.familyBackground,
                        onValueChange = { viewModel.updateFamilyBackground(it) },
                        label = "Family Background",
                        minLines = 2,
                        maxLines = 3,
                        singleLine = false
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    StyledTextField(
                        value = state.numberOfSiblings,
                        onValueChange = { viewModel.updateNumberOfSiblings(it) },
                        label = "Number of Siblings",
                        keyboardType = KeyboardType.Number
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    StyledSwitch(
                        label = "Living with Family",
                        checked = state.livingWithFamily,
                        onCheckedChange = { viewModel.updateLivingWithFamily(it) }
                    )
                }
                
                // ==================== Interests Section ====================
                ProfileSection(
                    title = "Interests & Hobbies",
                    icon = Icons.Default.Interests,
                    iconColor = Color(0xFF0891B2)
                ) {
                    ChipsSection(
                        label = "Interests",
                        items = state.interests,
                        maxItems = MAX_INTERESTS,
                        onAddItem = { viewModel.addInterest(it) },
                        onRemoveItem = { viewModel.removeInterest(it) },
                        chipColor = Color(0xFFDBEAFE),
                        chipTextColor = Color(0xFF1E40AF)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    ChipsSection(
                        label = "Hobbies",
                        items = state.hobbies,
                        maxItems = MAX_HOBBIES,
                        onAddItem = { viewModel.addHobby(it) },
                        onRemoveItem = { viewModel.removeHobby(it) },
                        chipColor = Color(0xFFDCFCE7),
                        chipTextColor = Color(0xFF166534)
                    )
                }
                
                // ==================== Looking For Section ====================
                ProfileSection(
                    title = "Looking For",
                    icon = Icons.Default.Search,
                    iconColor = Color(0xFFDB2777)
                ) {
                    StyledDropdown(
                        label = "Intention",
                        value = state.intention,
                        options = listOf("marriage", "friendship", "dating", "not_sure"),
                        displayMapper = { 
                            when (it) {
                                "marriage" -> "💍 Marriage"
                                "friendship" -> "🤝 Friendship"
                                "dating" -> "💕 Dating"
                                "not_sure" -> "🤔 Not Sure"
                                else -> it
                            }
                        },
                        onValueChange = { viewModel.updateIntention(it) }
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    StyledDropdown(
                        label = "Ready for Marriage",
                        value = state.readyForMarriageTimeframe,
                        options = listOf("soon", "6_months", "1_year", "not_sure"),
                        displayMapper = {
                            when (it) {
                                "soon" -> "As soon as possible"
                                "6_months" -> "Within 6 months"
                                "1_year" -> "Within 1 year"
                                "not_sure" -> "Not sure yet"
                                else -> it
                            }
                        },
                        onValueChange = { viewModel.updateReadyForMarriageTimeframe(it) }
                    )
                }
                
                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}

// ==================== Styled Components ====================

@Composable
private fun ProfileSection(
    title: String,
    icon: ImageVector,
    iconColor: Color,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(iconColor.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(18.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }
            content()
        }
    }
}

@Composable
private fun StyledTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = 1,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = TextSecondary) },
        modifier = modifier.fillMaxWidth(),
        singleLine = singleLine,
        minLines = if (singleLine) 1 else minLines,
        maxLines = if (singleLine) 1 else maxLines,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        trailingIcon = trailingIcon,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PrimaryPink,
            unfocusedBorderColor = InputBorder,
            focusedLabelColor = PrimaryPink,
            cursorColor = PrimaryPink
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StyledDropdown(
    label: String,
    value: String?,
    options: List<String>,
    onValueChange: (String) -> Unit,
    displayMapper: (String) -> String = { it }
) {
    var expanded by remember { mutableStateOf(false) }
    
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = value?.let { displayMapper(it) } ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text(label, color = TextSecondary) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryPink,
                unfocusedBorderColor = InputBorder,
                focusedLabelColor = PrimaryPink
            )
        )
        
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(displayMapper(option)) },
                    onClick = {
                        onValueChange(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun StyledSwitch(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(Color(0xFFF3F4F6), RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = TextPrimary, fontWeight = FontWeight.Medium)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = PrimaryPink,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFFD1D5DB)
            )
        )
    }
}

@Composable
private fun ChipsSection(
    label: String,
    items: List<String>,
    maxItems: Int,
    onAddItem: (String) -> Unit,
    onRemoveItem: (String) -> Unit,
    chipColor: Color,
    chipTextColor: Color
) {
    var newItemText by remember { mutableStateOf("") }
    
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                label,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = TextPrimary
            )
            Text(
                "${items.size}/$maxItems",
                style = MaterialTheme.typography.bodySmall,
                color = if (items.size >= maxItems) Color(0xFFDC2626) else TextSecondary
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Chips display
        if (items.isNotEmpty()) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                items(items) { item ->
                    Surface(
                        color = chipColor,
                        shape = RoundedCornerShape(20.dp),
                        onClick = { onRemoveItem(item) }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(item, color = chipTextColor, fontWeight = FontWeight.Medium, fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Remove",
                                modifier = Modifier.size(14.dp),
                                tint = chipTextColor
                            )
                        }
                    }
                }
            }
        }
        
        // Add new item
        if (items.size < maxItems) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = newItemText,
                    onValueChange = { newItemText = it },
                    placeholder = { Text("Add ${label.lowercase()}", color = TextSecondary) },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryPink,
                        unfocusedBorderColor = InputBorder
                    )
                )
                
                Surface(
                    onClick = {
                        if (newItemText.isNotBlank() && items.size < maxItems) {
                            onAddItem(newItemText.trim())
                            newItemText = ""
                        }
                    },
                    color = PrimaryPink,
                    shape = CircleShape,
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Add",
                            tint = Color.White
                        )
                    }
                }
            }
        } else {
            Text(
                "Maximum $maxItems ${label.lowercase()} reached",
                color = Color(0xFFDC2626),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// Keep other placeholder screens
@Composable
fun SettingsScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Settings Screen Placeholder")
    }
}

@Composable
fun NotificationsScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Notifications Screen Placeholder")
    }
}

@Composable
fun PrivacyScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Privacy Screen Placeholder")
    }
}

@Composable
fun SubscriptionScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Subscription Screen Placeholder")
    }
}

@Composable
fun HelpScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Help Screen Placeholder")
    }
}

@Composable
fun AboutScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("About Screen Placeholder")
    }
}
