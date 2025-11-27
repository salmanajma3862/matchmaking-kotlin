package com.example.dummyapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dummyapp.data.models.User
import com.example.dummyapp.data.repository.AuthRepository
import com.example.dummyapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Auth ViewModel
 * Manages authentication UI state and business logic
 */
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    // Login State
    private val _loginState = MutableStateFlow<NetworkResult<User>?>(null)
    val loginState: StateFlow<NetworkResult<User>?> = _loginState.asStateFlow()
    
    // Signup State
    private val _signupState = MutableStateFlow<NetworkResult<User>?>(null)
    val signupState: StateFlow<NetworkResult<User>?> = _signupState.asStateFlow()
    
    // Email Verification State
    private val _verifyEmailState = MutableStateFlow<NetworkResult<Boolean>?>(null)
    val verifyEmailState: StateFlow<NetworkResult<Boolean>?> = _verifyEmailState.asStateFlow()
    
    // Forgot Password State
    private val _forgotPasswordState = MutableStateFlow<NetworkResult<Boolean>?>(null)
    val forgotPasswordState: StateFlow<NetworkResult<Boolean>?> = _forgotPasswordState.asStateFlow()

    // Current User State
    private val _currentUserState = MutableStateFlow<NetworkResult<User>?>(null)
    val currentUserState: StateFlow<NetworkResult<User>?> = _currentUserState.asStateFlow()
    
    init {
        fetchCurrentUser()
    }

    /**
     * Fetch current user
     */
    fun fetchCurrentUser() {
        viewModelScope.launch {
            authRepository.getCurrentUser().collect { result ->
                _currentUserState.value = result
            }
        }
    }
    
    /**
     * Login user
     */
    fun login(email: String, password: String) {
        viewModelScope.launch {
            authRepository.login(email, password).collect { result ->
                _loginState.value = result
            }
        }
    }
    
    /**
     * Sign up new user
     */
    fun signup(
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            authRepository.signup(email, password).collect { result ->
                _signupState.value = result
            }
        }
    }
    
    /**
     * Verify email with code
     */
    fun verifyEmail(email: String, code: String) {
        viewModelScope.launch {
            authRepository.verifyEmail(email, code).collect { result ->
                _verifyEmailState.value = result
            }
        }
    }
    
    /**
     * Resend verification code
     */
    fun resendVerificationCode(email: String) {
        viewModelScope.launch {
            authRepository.resendVerificationCode(email).collect { result ->
                _verifyEmailState.value = result
            }
        }
    }
    
    /**
     * Request password reset
     */
    fun forgotPassword(email: String) {
        viewModelScope.launch {
            authRepository.forgotPassword(email).collect { result ->
                _forgotPasswordState.value = result
            }
        }
    }
    
    /**
     * Clear login state
     */
    fun clearLoginState() {
        _loginState.value = null
    }
    
    /**
     * Clear signup state
     */
    fun clearSignupState() {
        _signupState.value = null
    }
    
    /**
     * Clear verify email state
     */
    fun clearVerifyEmailState() {
        _verifyEmailState.value = null
    }
    
    /**
     * Clear forgot password state
     */
    fun clearForgotPasswordState() {
        _forgotPasswordState.value = null
    }

    /**
     * Perform logout
     */
    fun performLogout() {
        viewModelScope.launch {
            authRepository.logout().collect {
                // Logout successful (or at least local data cleared)
                // State update in AuthContext (observing userPreferences) should trigger navigation
            }
        }
    }
}
