package com.example.dummyapp.ui.screens.family

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dummyapp.ui.components.buttons.PrimaryButton
import com.example.dummyapp.ui.components.inputs.AppTextField
import com.example.dummyapp.ui.theme.*
import com.example.dummyapp.utils.NetworkResult
import com.example.dummyapp.viewmodel.AuthViewModel

/**
 * Family Link Screen
 * Step 2 of Family Onboarding: Enter Invite Code
 */
@Composable
fun FamilyLinkScreen(
    onNavigateToDashboard: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val colors = LocalExtendedColors.current
    val focusManager = LocalFocusManager.current
    
    // Form state
    var inviteCode by remember { mutableStateOf("") }
    var codeError by remember { mutableStateOf<String?>(null) }
    
    // State
    val linkState by viewModel.familyLinkState.collectAsState()
    
    // Handle link result
    LaunchedEffect(linkState) {
        when (linkState) {
            is NetworkResult.Success -> {
                viewModel.clearFamilyStates()
                onNavigateToDashboard()
            }
            else -> {}
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(AppSpacing.ScreenHorizontal),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))
            
            // Icon
            Icon(
                imageVector = Icons.Default.Link,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(64.dp)
            )
            
            Spacer(modifier = Modifier.height(AppSpacing.Large))
            
            // Header
            Text(
                text = "Enter Invite Code",
                style = MaterialTheme.typography.headlineLarge,
                color = colors.textPrimary,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(AppSpacing.Small))
            
            Text(
                text = "Enter the 6-character code shared by your family member",
                style = MaterialTheme.typography.bodyLarge,
                color = colors.textSecondary,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(AppSpacing.ExtraLarge2x))
            
            // Code Input
            AppTextField(
                value = inviteCode,
                onValueChange = {
                    inviteCode = it.uppercase()
                    codeError = null
                },
                label = "Invite Code",
                placeholder = "e.g. AB12CD",
                isError = codeError != null,
                errorMessage = codeError,
                leadingIcon = Icons.Default.VpnKey,
                keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                )
            )
            
            Spacer(modifier = Modifier.height(AppSpacing.Large2x))
            
            // Error Message
            if (linkState is NetworkResult.Error) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    shape = CustomShapes.CardMedium
                ) {
                    Row(
                        modifier = Modifier.padding(AppSpacing.CardPadding),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(AppSpacing.Small2x))
                        Text(
                            text = (linkState as NetworkResult.Error).message ?: "Linking failed",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                Spacer(modifier = Modifier.height(AppSpacing.Medium2x))
            }
            
            // Link Button
            PrimaryButton(
                text = "Connect",
                onClick = {
                    if (inviteCode.length < 6) {
                        codeError = "Code must be 6 characters"
                    } else {
                        viewModel.linkFamily(inviteCode)
                    }
                },
                loading = linkState is NetworkResult.Loading,
                icon = Icons.Default.Check
            )
        }
    }
}
