package com.salmanajmal.ziya.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color(0xFFF9FAFB) // gray-50
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Account Settings
            SectionHeader("ACCOUNT")
            Column(modifier = Modifier.background(Color.White)) {
                SettingItem(
                    label = "Phone Number",
                    value = "+1 234 567 8900",
                    onClick = {}
                )
                HorizontalDivider(color = Color(0xFFF3F4F6))
                SettingItem(
                    label = "Email",
                    value = "john@example.com",
                    onClick = {}
                )
                HorizontalDivider(color = Color(0xFFF3F4F6))
                SettingItem(
                    label = "Change Password",
                    value = "",
                    onClick = {}
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Discovery Settings
            SectionHeader("DISCOVERY")
            Column(modifier = Modifier.background(Color.White)) {
                SettingItem(
                    label = "Location",
                    value = "New York, NY",
                    onClick = {}
                )
                HorizontalDivider(color = Color(0xFFF3F4F6))
                SettingItem(
                    label = "Maximum Distance",
                    value = "50 km",
                    onClick = {}
                )
                HorizontalDivider(color = Color(0xFFF3F4F6))
                SettingItem(
                    label = "Age Range",
                    value = "25-35",
                    onClick = {}
                )
                HorizontalDivider(color = Color(0xFFF3F4F6))
                
                var showOnApp by remember { mutableStateOf(true) }
                SettingToggle(
                    label = "Show me on the app",
                    subtitle = "Be visible to other users",
                    enabled = showOnApp,
                    onCheckedChange = { showOnApp = it }
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Text(
            text = title,
            fontSize = 12.sp,
            color = Color(0xFF6B7280), // gray-500
            fontWeight = FontWeight.Medium
        )
    }
    HorizontalDivider(color = Color(0xFFF3F4F6))
}

@Composable
fun SettingItem(
    label: String,
    value: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = Color(0xFF111827), // gray-900
            fontSize = 16.sp
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (value.isNotEmpty()) {
                Text(
                    text = value,
                    color = Color(0xFF6B7280), // gray-500
                    fontSize = 14.sp
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color(0xFF9CA3AF) // gray-400
            )
        }
    }
}

@Composable
fun SettingToggle(
    label: String,
    subtitle: String? = null,
    enabled: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!enabled) }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                color = Color(0xFF111827), // gray-900
                fontSize = 16.sp
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    color = Color(0xFF6B7280), // gray-500
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
        Switch(
            checked = enabled,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFFEC4899), // pink-500
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFFD1D5DB) // gray-300
            )
        )
    }
}
