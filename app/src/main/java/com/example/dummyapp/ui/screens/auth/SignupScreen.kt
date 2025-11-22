package com.example.dummyapp.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dummyapp.ui.components.buttons.PrimaryButton
import com.example.dummyapp.ui.components.buttons.AppTextButton
import com.example.dummyapp.ui.components.inputs.EmailTextField
import com.example.dummyapp.ui.components.inputs.PasswordTextField
import com.example.dummyapp.ui.components.inputs.PhoneTextField
import com.example.dummyapp.ui.components.inputs.AppTextField
import com.example.dummyapp.ui.theme.*
import com.example.dummyapp.utils.NetworkResult
import com.example.dummyapp.utils.ValidationUtils
import com.example.dummyapp.viewmodel.AuthViewModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * Signup Screen
 * Multi-step elegant registration interface
 */
@Composable
fun SignupScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToVerifyEmail: (email: String) -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val colors = LocalExtendedColors.current
    val focusManager = LocalFocusManager.current
    
    // Form state
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }
    
    // Signup state
    val signupState by viewModel.signupState.collectAsState()
    
    // Handle signup result
    LaunchedEffect(signupState) {
        when (signupState) {
            is NetworkResult.Success -> {
                viewModel.clearSignupState()
                onNavigateToVerifyEmail(email)
            }
            is NetworkResult.Error -> {
                // Error is shown in UI
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
            Spacer(modifier = Modifier.height(40.dp))
            
            // Back button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(onClick = onNavigateToLogin) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = colors.textPrimary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(AppSpacing.Large))
            
            // Header
            Text(
                text = "Create Account",
                style = MaterialTheme.typography.headlineLarge,
                color = colors.textPrimary,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(AppSpacing.Small))
            
            Text(
                text = "Sign up to get started",
                style = MaterialTheme.typography.bodyLarge,
                color = colors.textSecondary,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(AppSpacing.ExtraLarge2x))
            
            // Step Content
            Spacer(modifier = Modifier.height(AppSpacing.ExtraLarge2x))
            
            // Email Input
            EmailTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = null
                },
                isError = emailError != null,
                errorMessage = emailError,
                keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                    onNext = { focusManager.moveFocus(androidx.compose.ui.focus.FocusDirection.Down) }
                )
            )
            
            Spacer(modifier = Modifier.height(AppSpacing.Medium2x))
            
            // Password Input
            PasswordTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = null
                },
                isError = passwordError != null,
                errorMessage = passwordError,
                keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                    onNext = { focusManager.moveFocus(androidx.compose.ui.focus.FocusDirection.Down) }
                )
            )
            
            Spacer(modifier = Modifier.height(AppSpacing.Medium2x))
            
            // Confirm Password Input
            PasswordTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    confirmPasswordError = null
                },
                isError = confirmPasswordError != null,
                errorMessage = confirmPasswordError,
                label = "Confirm Password",
                keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                )
            )
            
            Spacer(modifier = Modifier.height(AppSpacing.Large2x))
            
            // Error Message
            if (signupState is NetworkResult.Error) {
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
                            text = (signupState as NetworkResult.Error).message ?: "Signup failed",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                Spacer(modifier = Modifier.height(AppSpacing.Medium2x))
            }
            
            // Signup Button
            PrimaryButton(
                text = "Create Account",
                onClick = {
                    // Validate inputs
                    var isValid = true
                    
                    if (email.isEmpty()) {
                        emailError = "Email is required"
                        isValid = false
                    } else if (!ValidationUtils.isValidEmail(email)) {
                        emailError = "Invalid email address"
                        isValid = false
                    }
                    
                    val passwordValidation = ValidationUtils.validatePassword(password)
                    if (!passwordValidation.isValid) {
                        passwordError = passwordValidation.errorMessage
                        isValid = false
                    }
                    
                    if (confirmPassword.isEmpty()) {
                        confirmPasswordError = "Please confirm your password"
                        isValid = false
                    } else if (password != confirmPassword) {
                        confirmPasswordError = "Passwords do not match"
                        isValid = false
                    }
                    
                    if (isValid) {
                        viewModel.signup(email, password)
                    }
                },
                loading = signupState is NetworkResult.Loading,
                icon = Icons.Default.Check
            )
            
            Spacer(modifier = Modifier.height(AppSpacing.ExtraLarge))
            
            // Login Link
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Already have an account? ",
                    style = MaterialTheme.typography.bodyLarge,
                    color = colors.textSecondary
                )
                Text(
                    text = "Login",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { onNavigateToLogin() }
                )
            }
            
            Spacer(modifier = Modifier.height(AppSpacing.Large2x))
        }
    }
}










