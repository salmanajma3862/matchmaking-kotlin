package com.example.dummyapp.ui.screens.profile

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dummyapp.data.models.InviteCode
import com.example.dummyapp.utils.NetworkResult
import com.example.dummyapp.viewmodel.AuthViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InviteCodesScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val inviteCodesState by viewModel.myInviteCodesState.collectAsState()
    val clipboardManager = LocalClipboardManager.current
    var copiedCode by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.fetchMyInviteCodes()
    }

    // Reset copied indicator after a delay
    LaunchedEffect(copiedCode) {
        if (copiedCode != null) {
            kotlinx.coroutines.delay(2000)
            copiedCode = null
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "My Invite Codes",
                        fontWeight = FontWeight.Bold
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color(0xFF111827)
                )
            )
        },
        containerColor = Color(0xFFF9FAFB) // Gray-50
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = inviteCodesState) {
                is NetworkResult.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Color(0xFFEC4899) // Pink-500
                        )
                    }
                }
                is NetworkResult.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ErrorOutline,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = Color(0xFFEF4444) // Red-500
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = state.message ?: "Failed to load invite codes",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color(0xFF6B7280), // Gray-500
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.fetchMyInviteCodes() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFEC4899) // Pink-500
                            )
                        ) {
                            Text("Retry")
                        }
                    }
                }
                is NetworkResult.Success -> {
                    val codes = state.data
                    if (codes.isNullOrEmpty()) {
                        // Empty State
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .background(
                                        Color(0xFFFDF2F8), // Pink-50
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.QrCode2,
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp),
                                    tint = Color(0xFFEC4899) // Pink-500
                                )
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                text = "No Invite Codes Yet",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF111827) // Gray-900
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Generate invite codes from your profile to let family members access your profile.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF6B7280), // Gray-500
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(codes) { invite ->
                                InviteCodeCard(
                                    invite = invite,
                                    isCopied = copiedCode == invite.code,
                                    onCopy = {
                                        clipboardManager.setText(AnnotatedString(invite.code))
                                        copiedCode = invite.code
                                    }
                                )
                            }
                        }
                    }
                }
                null -> {
                    // Initial state, loading will be triggered
                }
            }
        }
    }
}

@Composable
fun InviteCodeCard(
    invite: InviteCode,
    isCopied: Boolean,
    onCopy: () -> Unit
) {
    val statusColor = when {
        invite.isUsed() -> Color(0xFF059669) // Emerald-600
        invite.isExpired() -> Color(0xFFDC2626) // Red-600
        else -> Color(0xFF2563EB) // Blue-600
    }
    
    val statusBgColor = when {
        invite.isUsed() -> Color(0xFFECFDF5) // Emerald-50
        invite.isExpired() -> Color(0xFFFEF2F2) // Red-50
        else -> Color(0xFFEFF6FF) // Blue-50
    }
    
    val statusLabel = when {
        invite.isUsed() -> "Used"
        invite.isExpired() -> "Expired"
        else -> "Active"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header Row: Code + Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Code with copy button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                Color(0xFFF3F4F6), // Gray-100
                                RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = invite.code,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp,
                            color = Color(0xFF111827) // Gray-900
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    val copyIconColor by animateColorAsState(
                        targetValue = if (isCopied) Color(0xFF059669) else Color(0xFF6B7280),
                        label = "copyIconColor"
                    )
                    
                    IconButton(
                        onClick = onCopy,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = if (isCopied) Icons.Default.Check else Icons.Default.ContentCopy,
                            contentDescription = "Copy code",
                            tint = copyIconColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                
                // Status Badge
                Box(
                    modifier = Modifier
                        .background(statusBgColor, RoundedCornerShape(20.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = statusLabel,
                        style = MaterialTheme.typography.labelMedium,
                        color = statusColor,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Scope/Permissions
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color(0xFF9CA3AF) // Gray-400
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = invite.getScopeDescription().ifEmpty { "No permissions" },
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF6B7280), // Gray-500
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Created and Expires
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color(0xFF9CA3AF)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Created: ${formatDate(invite.createdAt)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF6B7280)
                    )
                }
                
                Text(
                    text = "Expires: ${formatDate(invite.expiresAt)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (invite.isExpired()) Color(0xFFDC2626) else Color(0xFF6B7280)
                )
            }
            
            // Used By section (if used)
            if (invite.usedBy != null) {
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = Color(0xFFF3F4F6))
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color(0xFFECFDF5), // Emerald-50
                            RoundedCornerShape(12.dp)
                        )
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFF059669), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = invite.usedBy.name.firstOrNull()?.uppercase() ?: "?",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Column {
                        Text(
                            text = "Used by ${invite.usedBy.name}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF065F46) // Emerald-800
                        )
                        invite.usedAt?.let { usedAt ->
                            Text(
                                text = formatDate(usedAt),
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF059669) // Emerald-600
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Format ISO date string to readable format
 */
private fun formatDate(isoDate: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date = inputFormat.parse(isoDate)
        
        val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        outputFormat.format(date!!)
    } catch (e: Exception) {
        try {
            // Try alternative format without milliseconds
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")
            val date = inputFormat.parse(isoDate)
            
            val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            outputFormat.format(date!!)
        } catch (e2: Exception) {
            isoDate.take(10) // Fallback: just show first 10 chars (date part)
        }
    }
}
