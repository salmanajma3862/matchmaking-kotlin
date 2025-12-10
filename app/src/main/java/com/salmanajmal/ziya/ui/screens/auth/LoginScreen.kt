package com.salmanajmal.ziya.ui.screens.auth

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
import com.salmanajmal.ziya.context.LocalAuthContext
import com.salmanajmal.ziya.ui.components.buttons.PrimaryButton
import com.salmanajmal.ziya.ui.components.buttons.SecondaryButton
import com.salmanajmal.ziya.ui.components.buttons.AppTextButton
import com.salmanajmal.ziya.ui.components.inputs.EmailTextField
import com.salmanajmal.ziya.ui.components.inputs.PasswordTextField
import com.salmanajmal.ziya.ui.components.inputs.PhoneTextField
import com.salmanajmal.ziya.ui.theme.*
import com.salmanajmal.ziya.utils.NetworkResult
import com.salmanajmal.ziya.utils.ValidationUtils
import com.salmanajmal.ziya.viewmodel.AuthViewModel

/**
 * Login Screen
 * Elegant login interface with phone/email and password
 */
@Composable
fun LoginScreen(
    onNavigateToSignup: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToVerifyEmail: (String) -> Unit,
    onNavigateToProfileSetup: () -> Unit,
    onNavigateToFamilySignup: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val colors = LocalExtendedColors.current
    val authContext = LocalAuthContext.current
    val focusManager = LocalFocusManager.current
    
    // Form state
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    
    // Login state
    val loginState by viewModel.loginState.collectAsState()
    
    // Handle login result
    LaunchedEffect(loginState) {
        when (loginState) {
            is NetworkResult.Success -> {
                val user = (loginState as NetworkResult.Success).data
                authContext.setUser(user!!)
                viewModel.clearLoginState()
                
                if (user.onboardingCompleted) {
                    onNavigateToHome()
                } else {
                    onNavigateToProfileSetup()
                }
            }
            is NetworkResult.Error -> {
                val error = loginState as NetworkResult.Error
                if (error.message == "VERIFICATION_REQUIRED") {
                    viewModel.clearLoginState()
                    onNavigateToVerifyEmail(email)
                }
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
            Spacer(modifier = Modifier.height(60.dp))
            
            // Logo/Icon
            Surface(
                modifier = Modifier.size(80.dp),
                shape = CustomShapes.Circle,
                color = MaterialTheme.colorScheme.primaryContainer,
                tonalElevation = AppElevation.Level2
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Logo",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(AppSpacing.Large2x))
            
            // Welcome Text
            Text(
                text = "Welcome Back",
                style = MaterialTheme.typography.headlineLarge,
                color = colors.textPrimary,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(AppSpacing.Small))
            
            Text(
                text = "Sign in to continue your journey",
                style = MaterialTheme.typography.bodyLarge,
                color = colors.textSecondary,
                textAlign = TextAlign.Center
            )
            
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
                    onDone = { focusManager.clearFocus() }
                )
            )
            
            Spacer(modifier = Modifier.height(AppSpacing.Small2x))
            
            // Forgot Password
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                AppTextButton(
                    text = "Forgot Password?",
                    onClick = onNavigateToForgotPassword
                )
            }
            
            Spacer(modifier = Modifier.height(AppSpacing.Large2x))
            
            // Error Message
            if (loginState is NetworkResult.Error) {
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
                            text = (loginState as NetworkResult.Error).message ?: "Login failed",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                Spacer(modifier = Modifier.height(AppSpacing.Medium2x))
            }
            
            // Login Button
            PrimaryButton(
                text = "Login",
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
                    
                    if (password.isEmpty()) {
                        passwordError = "Password is required"
                        isValid = false
                    }
                    
                    if (isValid) {
                        viewModel.login(email, password)
                    }
                },
                loading = loginState is NetworkResult.Loading,
                icon = Icons.Default.Login
            )
            
            Spacer(modifier = Modifier.height(AppSpacing.ExtraLarge))
            
            // Divider with "OR"
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = colors.divider
                )
                Text(
                    text = "OR",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.textTertiary,
                    modifier = Modifier.padding(horizontal = AppSpacing.Medium2x)
                )
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = colors.divider
                )
            }
            
            Spacer(modifier = Modifier.height(AppSpacing.ExtraLarge))
            
            // Family Signup Link
            TextButton(
                onClick = onNavigateToFamilySignup,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Join as Family Member",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(AppSpacing.Large))

            // Sign Up Link
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don't have an account? ",
                    style = MaterialTheme.typography.bodyLarge,
                    color = colors.textSecondary
                )
                Text(
                    text = "Sign Up",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { onNavigateToSignup() }
                )
            }
            
            Spacer(modifier = Modifier.height(AppSpacing.Large2x))
        }
    }
}
