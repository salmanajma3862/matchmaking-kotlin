package com.example.dummyapp.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.MarkEmailRead
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dummyapp.context.LocalAuthContext
import com.example.dummyapp.ui.components.buttons.AppTextButton
import com.example.dummyapp.ui.components.buttons.PrimaryButton
import com.example.dummyapp.ui.theme.*
import com.example.dummyapp.utils.NetworkResult
import com.example.dummyapp.viewmodel.AuthViewModel
import kotlinx.coroutines.delay

/**
 * Verify Email Screen
 * Professional OTP verification interface
 */
@Composable
fun VerifyEmailScreen(
    email: String,
    onNavigateBack: () -> Unit,
    onNavigateToProfileSetup: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val colors = LocalExtendedColors.current
    val authContext = LocalAuthContext.current
    
    // State
    var code by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isResendEnabled by remember { mutableStateOf(false) }
    var timerSeconds by remember { mutableStateOf(60) }
    
    // Verification state
    val verifyState by viewModel.verifyEmailState.collectAsState()
    
    // Timer for resend
    LaunchedEffect(key1 = timerSeconds) {
        if (timerSeconds > 0) {
            delay(1000L)
            timerSeconds--
        } else {
            isResendEnabled = true
        }
    }
    
    // Handle verification result
    LaunchedEffect(verifyState) {
        when (verifyState) {
            is NetworkResult.Success -> {
                // Update auth context
                authContext.setEmailVerified(true)
                viewModel.clearVerifyEmailState()
                onNavigateToProfileSetup()
            }
            is NetworkResult.Error -> {
                errorMessage = (verifyState as NetworkResult.Error).message ?: "Verification failed"
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
                .padding(AppSpacing.ScreenHorizontal),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            
            // Back button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = colors.textPrimary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(AppSpacing.Large))
            
            // Icon
            Surface(
                modifier = Modifier.size(80.dp),
                shape = CustomShapes.Circle,
                color = MaterialTheme.colorScheme.primaryContainer,
                tonalElevation = AppElevation.Level2
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.MarkEmailRead,
                        contentDescription = "Email Verification",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(AppSpacing.Large2x))
            
            // Header
            Text(
                text = "Verify Your Email",
                style = MaterialTheme.typography.headlineLarge,
                color = colors.textPrimary,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(AppSpacing.Small))
            
            Text(
                text = "We've sent a verification code to",
                style = MaterialTheme.typography.bodyLarge,
                color = colors.textSecondary,
                textAlign = TextAlign.Center
            )
            
            Text(
                text = email,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(AppSpacing.ExtraLarge2x))
            
            // OTP Input
            OtpInput(
                value = code,
                onValueChange = { 
                    if (it.length <= 6) {
                        code = it
                        errorMessage = null
                    }
                },
                length = 6
            )
            
            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(AppSpacing.Medium))
                Text(
                    text = errorMessage!!,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
            
            Spacer(modifier = Modifier.height(AppSpacing.Large2x))
            
            // Verify Button
            PrimaryButton(
                text = "Verify Email",
                onClick = {
                    if (code.length == 6) {
                        viewModel.verifyEmail(email, code)
                    } else {
                        errorMessage = "Please enter a valid 6-digit code"
                    }
                },
                loading = verifyState is NetworkResult.Loading,
                enabled = code.length == 6
            )
            
            Spacer(modifier = Modifier.height(AppSpacing.ExtraLarge))
            
            // Resend Code
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Didn't receive the code? ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.textSecondary
                )
                
                if (isResendEnabled) {
                    AppTextButton(
                        text = "Resend",
                        onClick = {
                            viewModel.resendVerificationCode(email)
                            isResendEnabled = false
                            timerSeconds = 60
                        }
                    )
                } else {
                    Text(
                        text = "Resend in ${timerSeconds}s",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.textTertiary
                    )
                }
            }
        }
    }
}

@Composable
fun OtpInput(
    value: String,
    onValueChange: (String) -> Unit,
    length: Int = 6
) {
    val colors = LocalExtendedColors.current
    
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        decorationBox = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                repeat(length) { index ->
                    val char = when {
                        index >= value.length -> ""
                        else -> value[index].toString()
                    }
                    val isFocused = value.length == index
                    
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(0.8f)
                            .border(
                                width = if (isFocused) 2.dp else 1.dp,
                                color = if (isFocused) MaterialTheme.colorScheme.primary else colors.inputBorder,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .background(
                                color = MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = char,
                            style = MaterialTheme.typography.headlineMedium,
                            color = colors.textPrimary,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        },
        cursorBrush = SolidColor(Color.Transparent) // Hide cursor
    )
}
