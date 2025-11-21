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
    
    // Step state
    var currentStep by remember { mutableStateOf(1) }
    val totalSteps = 3
    
    // Form state - Step 1 (Contact Info)
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    
    // Form state - Step 2 (Security)
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }
    
    // Form state - Step 3 (Personal Info)
    var fullName by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    var acceptTerms by remember { mutableStateOf(false) }
    var fullNameError by remember { mutableStateOf<String?>(null) }
    var genderError by remember { mutableStateOf<String?>(null) }
    var dateOfBirthError by remember { mutableStateOf<String?>(null) }
    var termsError by remember { mutableStateOf<String?>(null) }
    
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
                IconButton(onClick = {
                    if (currentStep > 1) {
                        currentStep--
                    } else {
                        onNavigateToLogin()
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = colors.textPrimary
                    )
                }
            }
            
            // Progress Indicator
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = AppSpacing.Medium2x),
                horizontalArrangement = Arrangement.spacedBy(AppSpacing.Small2x)
            ) {
                repeat(totalSteps) { step ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp)
                            .background(
                                color = if (step < currentStep) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                shape = CustomShapes.ButtonSmall
                            )
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(AppSpacing.Large))
            
            // Header
            Text(
                text = when (currentStep) {
                    1 -> "Create Account"
                    2 -> "Secure Your Account"
                    else -> "Tell Us About You"
                },
                style = MaterialTheme.typography.headlineLarge,
                color = colors.textPrimary,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(AppSpacing.Small))
            
            Text(
                text = when (currentStep) {
                    1 -> "Enter your contact information"
                    2 -> "Choose a strong password"
                    else -> "Complete your profile"
                },
                style = MaterialTheme.typography.bodyLarge,
                color = colors.textSecondary,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(AppSpacing.ExtraLarge2x))
            
            // Step Content
            when (currentStep) {
                1 -> Step1ContactInfo(
                    phone = phone,
                    onPhoneChange = { 
                        phone = it
                        phoneError = null
                    },
                    phoneError = phoneError,
                    email = email,
                    onEmailChange = { 
                        email = it
                        emailError = null
                    },
                    emailError = emailError
                )
                2 -> Step2Security(
                    password = password,
                    onPasswordChange = { 
                        password = it
                        passwordError = null
                    },
                    passwordError = passwordError,
                    confirmPassword = confirmPassword,
                    onConfirmPasswordChange = { 
                        confirmPassword = it
                        confirmPasswordError = null
                    },
                    confirmPasswordError = confirmPasswordError
                )
                3 -> Step3PersonalInfo(
                    fullName = fullName,
                    onFullNameChange = { 
                        fullName = it
                        fullNameError = null
                    },
                    fullNameError = fullNameError,
                    gender = gender,
                    onGenderChange = { 
                        gender = it
                        genderError = null
                    },
                    genderError = genderError,
                    dateOfBirth = dateOfBirth,
                    onDateOfBirthChange = { 
                        dateOfBirth = it
                        dateOfBirthError = null
                    },
                    dateOfBirthError = dateOfBirthError,
                    showDatePicker = showDatePicker,
                    onShowDatePickerChange = { showDatePicker = it },
                    acceptTerms = acceptTerms,
                    onAcceptTermsChange = { 
                        acceptTerms = it
                        termsError = null
                    },
                    termsError = termsError
                )
            }
            
            Spacer(modifier = Modifier.height(AppSpacing.Large2x))
            
            // Error Message
            if (signupState is NetworkResult.Error && currentStep == 3) {
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
            
            // Action Buttons
            PrimaryButton(
                text = if (currentStep == totalSteps) "Create Account" else "Continue",
                onClick = {
                    when (currentStep) {
                        1 -> {
                            // Validate Step 1
                            var isValid = true
                            
                            if (phone.isEmpty()) {
                                phoneError = "Phone number is required"
                                isValid = false
                            } else if (!ValidationUtils.isValidPhone(phone)) {
                                phoneError = "Invalid phone number"
                                isValid = false
                            }
                            
                            if (email.isEmpty()) {
                                emailError = "Email is required"
                                isValid = false
                            } else if (!ValidationUtils.isValidEmail(email)) {
                                emailError = "Invalid email address"
                                isValid = false
                            }
                            
                            if (isValid) currentStep++
                        }
                        2 -> {
                            // Validate Step 2
                            var isValid = true
                            
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
                            
                            if (isValid) currentStep++
                        }
                        3 -> {
                            // Validate Step 3 and submit
                            var isValid = true
                            
                            val nameValidation = ValidationUtils.validateName(fullName)
                            if (!nameValidation.isValid) {
                                fullNameError = nameValidation.errorMessage
                                isValid = false
                            }
                            
                            if (gender.isEmpty()) {
                                genderError = "Please select your gender"
                                isValid = false
                            }
                            
                            if (dateOfBirth.isEmpty()) {
                                dateOfBirthError = "Date of birth is required"
                                isValid = false
                            } else {
                                val ageValidation = ValidationUtils.validateAge(dateOfBirth)
                                if (!ageValidation.isValid) {
                                    dateOfBirthError = ageValidation.errorMessage
                                    isValid = false
                                }
                            }
                            
                            if (!acceptTerms) {
                                termsError = "You must accept the terms and conditions"
                                isValid = false
                            }
                            
                            if (isValid) {
                                val normalizedPhone = ValidationUtils.normalizePhoneNumber(phone)
                                viewModel.signup(
                                    phone = normalizedPhone,
                                    email = email,
                                    password = password,
                                    name = fullName,
                                    gender = gender.lowercase(),
                                    dob = dateOfBirth
                                )
                            }
                        }
                    }
                },
                loading = signupState is NetworkResult.Loading,
                icon = if (currentStep == totalSteps) Icons.Default.Check else Icons.Default.ArrowForward
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

@Composable
private fun Step1ContactInfo(
    phone: String,
    onPhoneChange: (String) -> Unit,
    phoneError: String?,
    email: String,
    onEmailChange: (String) -> Unit,
    emailError: String?
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.Medium2x)
    ) {
        PhoneTextField(
            value = phone,
            onValueChange = onPhoneChange,
            isError = phoneError != null,
            errorMessage = phoneError,
            imeAction = ImeAction.Next
        )
        
        EmailTextField(
            value = email,
            onValueChange = onEmailChange,
            isError = emailError != null,
            errorMessage = emailError,
            imeAction = ImeAction.Done
        )
    }
}

@Composable
private fun Step2Security(
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordError: String?,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
    confirmPasswordError: String?
) {
    val colors = LocalExtendedColors.current
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.Medium2x)
    ) {
        PasswordTextField(
            value = password,
            onValueChange = onPasswordChange,
            isError = passwordError != null,
            errorMessage = passwordError,
            imeAction = ImeAction.Next
        )
        
        PasswordTextField(
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            isError = confirmPasswordError != null,
            errorMessage = confirmPasswordError,
            label = "Confirm Password",
            placeholder = "Re-enter your password",
            imeAction = ImeAction.Done
        )
        
        // Password Requirements
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = colors.infoContainer
            ),
            shape = CustomShapes.CardSmall
        ) {
            Column(
                modifier = Modifier.padding(AppSpacing.CardPadding),
                verticalArrangement = Arrangement.spacedBy(AppSpacing.Small)
            ) {
                Text(
                    text = "Password must contain:",
                    style = MaterialTheme.typography.labelMedium,
                    color = colors.textPrimary,
                    fontWeight = FontWeight.SemiBold
                )
                
                PasswordRequirement("At least 8 characters", password.length >= 8)
                PasswordRequirement("One uppercase letter", password.any { it.isUpperCase() })
                PasswordRequirement("One lowercase letter", password.any { it.isLowerCase() })
                PasswordRequirement("One number", password.any { it.isDigit() })
                PasswordRequirement("One special character", password.any { !it.isLetterOrDigit() })
            }
        }
    }
}

@Composable
private fun PasswordRequirement(text: String, satisfied: Boolean) {
    val colors = LocalExtendedColors.current
    
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.Small)
    ) {
        Icon(
            imageVector = if (satisfied) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
            contentDescription = null,
            tint = if (satisfied) colors.success else colors.textTertiary,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = if (satisfied) colors.success else colors.textSecondary
        )
    }
}

@Composable
private fun Step3PersonalInfo(
    fullName: String,
    onFullNameChange: (String) -> Unit,
    fullNameError: String?,
    gender: String,
    onGenderChange: (String) -> Unit,
    genderError: String?,
    dateOfBirth: String,
    onDateOfBirthChange: (String) -> Unit,
    dateOfBirthError: String?,
    showDatePicker: Boolean,
    onShowDatePickerChange: (Boolean) -> Unit,
    acceptTerms: Boolean,
    onAcceptTermsChange: (Boolean) -> Unit,
    termsError: String?
) {
    val colors = LocalExtendedColors.current
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.Medium2x)
    ) {
        // Full Name
        AppTextField(
            value = fullName,
            onValueChange = onFullNameChange,
            label = "Full Name",
            placeholder = "Enter your full name",
            isError = fullNameError != null,
            errorMessage = fullNameError,
            leadingIcon = Icons.Default.Person,
            imeAction = ImeAction.Next
        )
        
        // Gender Selection
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.Small)
        ) {
            Text(
                text = "Gender",
                style = MaterialTheme.typography.labelLarge,
                color = colors.textPrimary
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(AppSpacing.Medium)
            ) {
                GenderOption(
                    text = "Male",
                    selected = gender == "Male",
                    onClick = { onGenderChange("Male") },
                    modifier = Modifier.weight(1f)
                )
                GenderOption(
                    text = "Female",
                    selected = gender == "Female",
                    onClick = { onGenderChange("Female") },
                    modifier = Modifier.weight(1f)
                )
                GenderOption(
                    text = "Other",
                    selected = gender == "Other",
                    onClick = { onGenderChange("Other") },
                    modifier = Modifier.weight(1f)
                )
            }
            
            if (genderError != null) {
                Text(
                    text = genderError,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        
        // Date of Birth
        AppTextField(
            value = dateOfBirth,
            onValueChange = onDateOfBirthChange,
            label = "Date of Birth",
            placeholder = "DD/MM/YYYY",
            isError = dateOfBirthError != null,
            errorMessage = dateOfBirthError,
            leadingIcon = Icons.Default.CalendarToday,
            trailingIcon = Icons.Default.ArrowDropDown,
            readOnly = true,
            onClick = { onShowDatePickerChange(true) },
            imeAction = ImeAction.Done
        )
        
        // Terms & Conditions
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.Small2x)
        ) {
            Checkbox(
                checked = acceptTerms,
                onCheckedChange = onAcceptTermsChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = colors.textSecondary
                )
            )
            Text(
                text = "I accept the ",
                style = MaterialTheme.typography.bodyMedium,
                color = colors.textSecondary
            )
            Text(
                text = "Terms & Conditions",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { /* Open T&C */ }
            )
        }
        
        if (termsError != null) {
            Text(
                text = termsError,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
private fun GenderOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalExtendedColors.current
    
    Card(
        onClick = onClick,
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = CustomShapes.ButtonMedium,
        border = if (selected) null else CardDefaults.outlinedCardBorder()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppSpacing.Medium2x),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = if (selected) MaterialTheme.colorScheme.onPrimary else colors.textPrimary,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
            )
        }
    }
}
