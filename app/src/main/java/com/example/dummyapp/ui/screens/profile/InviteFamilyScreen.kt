package com.example.dummyapp.ui.screens.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.ui.draw.rotate
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
fun InviteFamilyScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val createInviteState by viewModel.createInviteState.collectAsState()
    val inviteCodesState by viewModel.myInviteCodesState.collectAsState()
    val clipboardManager = LocalClipboardManager.current
    
    var copiedCode by remember { mutableStateOf<String?>(null) }
    var selectedDuration by remember { mutableIntStateOf(24) } // Default: 24 hours
    var isCreateSectionExpanded by remember { mutableStateOf(false) } // Collapsed by default
    var codeToDelete by remember { mutableStateOf<String?>(null) }
    var codeToDeactivate by remember { mutableStateOf<String?>(null) }
    
    // Fetch invite codes on launch
    LaunchedEffect(Unit) {
        viewModel.fetchMyInviteCodes()
    }
    
    // Refresh codes after successful creation
    LaunchedEffect(createInviteState) {
        if (createInviteState is NetworkResult.Success) {
            viewModel.fetchMyInviteCodes()
            // Clear create state after a delay
            kotlinx.coroutines.delay(2000)
            viewModel.clearCreateInviteState()
        }
    }
    
    // Reset copied indicator after delay
    LaunchedEffect(copiedCode) {
        if (copiedCode != null) {
            kotlinx.coroutines.delay(2000)
            copiedCode = null
        }
    }

    // Delete confirmation dialog
    if (codeToDelete != null) {
        AlertDialog(
            onDismissRequest = { codeToDelete = null },
            title = { Text("Delete Invite Code?", fontWeight = FontWeight.Bold) },
            text = { Text("This will permanently remove the invite code. This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        codeToDelete?.let { id ->
                            viewModel.deleteInviteCode(id) { /* success handled via refresh */ }
                        }
                        codeToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { codeToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Deactivate confirmation dialog
    if (codeToDeactivate != null) {
        AlertDialog(
            onDismissRequest = { codeToDeactivate = null },
            title = { Text("Deactivate Invite Code?", fontWeight = FontWeight.Bold) },
            text = { Text("This will expire the code immediately. Family members won't be able to use it anymore.") },
            confirmButton = {
                Button(
                    onClick = {
                        codeToDeactivate?.let { id ->
                            viewModel.deactivateInviteCode(id) { /* success handled via refresh */ }
                        }
                        codeToDeactivate = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEA580C))
                ) {
                    Text("Deactivate")
                }
            },
            dismissButton = {
                TextButton(onClick = { codeToDeactivate = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Invite Family",
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
        containerColor = Color(0xFFF9FAFB)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Create New Invite Section
            item {
                CreateInviteCard(
                    isExpanded = isCreateSectionExpanded,
                    onToggleExpand = { isCreateSectionExpanded = !isCreateSectionExpanded },
                    selectedDuration = selectedDuration,
                    onDurationChange = { selectedDuration = it },
                    createState = createInviteState,
                    onCreateInvite = {
                        // Only view_matches scope as per requirement
                        viewModel.createInvite(listOf("view_matches"), selectedDuration)
                    }
                )
            }
            
            // Codes List Header
            item {
                val codesCount = when (val state = inviteCodesState) {
                    is NetworkResult.Success -> state.data?.size ?: 0
                    else -> 0
                }
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Your Invite Codes",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827)
                    )
                    if (codesCount > 0) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFECFDF5)
                        ) {
                            Text(
                                text = codesCount.toString(),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF059669)
                            )
                        }
                    }
                }
            }
            
            // Codes List Content
            when (val state = inviteCodesState) {
                is NetworkResult.Loading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = Color(0xFFEC4899),
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
                is NetworkResult.Error -> {
                    item {
                        ErrorCard(
                            message = state.message ?: "Failed to load codes",
                            onRetry = { viewModel.fetchMyInviteCodes() }
                        )
                    }
                }
                is NetworkResult.Success -> {
                    val codes = state.data
                    if (codes.isNullOrEmpty()) {
                        item { EmptyCodesCard() }
                    } else {
                        items(codes, key = { it.id }) { invite ->
                            InviteCodeCard(
                                invite = invite,
                                isCopied = copiedCode == invite.code,
                                onCopy = {
                                    clipboardManager.setText(AnnotatedString(invite.code))
                                    copiedCode = invite.code
                                },
                                onDelete = { codeToDelete = invite.id },
                                onDeactivate = { codeToDeactivate = invite.id }
                            )
                        }
                    }
                }
                null -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = Color(0xFFEC4899),
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
            }
            
            // Bottom spacing
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun CreateInviteCard(
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    selectedDuration: Int,
    onDurationChange: (Int) -> Unit,
    createState: NetworkResult<Any?>?,
    onCreateInvite: () -> Unit
) {
    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        label = "chevronRotation"
    )
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header with gradient
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xFFEC4899), Color(0xFFF43F5E))
                        )
                    )
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(Color.White.copy(alpha = 0.2f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PersonAdd,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Create New Invite",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Generate a code for family access",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }
                    }
                    
                    IconButton(onClick = onToggleExpand) {
                        Icon(
                            imageVector = Icons.Default.ExpandMore,
                            contentDescription = if (isExpanded) "Collapse" else "Expand",
                            tint = Color.White,
                            modifier = Modifier
                                .size(28.dp)
                                .rotate(rotationAngle)
                        )
                    }
                }
            }
            
            // Expandable content
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // Permission info
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFFDF2F8), RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = null,
                            tint = Color(0xFFEC4899),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Permission",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFBE185D)
                            )
                            Text(
                                text = "View Matches",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF9D174D)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Duration selection
                    Text(
                        text = "Code Expires In",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF374151)
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Duration chips in a flow/wrap layout
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        DurationChip("1 Hr", 1, selectedDuration, onDurationChange, Modifier.weight(1f))
                        DurationChip("24 Hrs", 24, selectedDuration, onDurationChange, Modifier.weight(1f))
                        DurationChip("1 Week", 168, selectedDuration, onDurationChange, Modifier.weight(1f))
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        DurationChip("1 Month", 720, selectedDuration, onDurationChange, Modifier.weight(1f))
                        DurationChip("Lifetime", -1, selectedDuration, onDurationChange, Modifier.weight(1f))
                    }
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    // Success message
                    AnimatedVisibility(visible = createState is NetworkResult.Success) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFECFDF5), RoundedCornerShape(12.dp))
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFF059669),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Invite code created successfully!",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF065F46)
                            )
                        }
                    }
                    
                    // Error message
                    if (createState is NetworkResult.Error) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFFEF2F2), RoundedCornerShape(12.dp))
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Error,
                                contentDescription = null,
                                tint = Color(0xFFDC2626),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = createState.message ?: "Failed to create code",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF991B1B)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(if (createState is NetworkResult.Success || createState is NetworkResult.Error) 12.dp else 0.dp))
                    
                    // Generate button
                    Button(
                        onClick = onCreateInvite,
                        enabled = createState !is NetworkResult.Loading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFEC4899),
                            disabledContainerColor = Color(0xFFFBCFE8)
                        )
                    ) {
                        if (createState is NetworkResult.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(22.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Generate Invite Code",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DurationChip(
    label: String,
    value: Int,
    selectedValue: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val isSelected = value == selectedValue
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) Color(0xFFEC4899) else Color(0xFFF3F4F6),
        label = "chipBg"
    )
    val textColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else Color(0xFF374151),
        label = "chipText"
    )
    
    Surface(
        onClick = { onSelect(value) },
        modifier = modifier.height(40.dp),
        shape = RoundedCornerShape(10.dp),
        color = backgroundColor
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                color = textColor
            )
        }
    }
}

@Composable
private fun InviteCodeCard(
    invite: InviteCode,
    isCopied: Boolean,
    onCopy: () -> Unit,
    onDelete: () -> Unit,
    onDeactivate: () -> Unit
) {
    val statusColor = when {
        invite.isUsed() -> Color(0xFF059669)
        invite.isExpired() -> Color(0xFFDC2626)
        else -> Color(0xFF2563EB)
    }
    
    val statusBgColor = when {
        invite.isUsed() -> Color(0xFFECFDF5)
        invite.isExpired() -> Color(0xFFFEF2F2)
        else -> Color(0xFFEFF6FF)
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
                                Color(0xFFF3F4F6),
                                RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = invite.code,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp,
                            color = Color(0xFF111827)
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color(0xFF9CA3AF)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = invite.getScopeDescription().ifEmpty { "No permissions" },
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF6B7280),
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
                            Color(0xFFECFDF5),
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
                            color = Color(0xFF065F46)
                        )
                        invite.usedAt?.let { usedAt ->
                            Text(
                                text = formatDate(usedAt),
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF059669)
                            )
                        }
                    }
                }
            }

            // Action buttons for managing the code
            if (!invite.isUsed()) {
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = Color(0xFFF3F4F6))
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (invite.isActive()) {
                        OutlinedButton(
                            onClick = onDeactivate,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xFFEA580C)
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Block,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Deactivate", fontSize = 13.sp)
                        }
                    }
                    
                    OutlinedButton(
                        onClick = onDelete,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFFDC2626)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Delete", fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyCodesCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .background(Color(0xFFFDF2F8), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.QrCode2,
                    contentDescription = null,
                    modifier = Modifier.size(36.dp),
                    tint = Color(0xFFEC4899)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No Invite Codes Yet",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Create your first invite code above to give family members access to your matches.",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF6B7280),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ErrorCard(
    message: String,
    onRetry: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.ErrorOutline,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = Color(0xFFEF4444)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF991B1B),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = onRetry,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFFDC2626)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Retry")
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
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")
            val date = inputFormat.parse(isoDate)
            
            val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            outputFormat.format(date!!)
        } catch (e2: Exception) {
            isoDate.take(10)
        }
    }
}
