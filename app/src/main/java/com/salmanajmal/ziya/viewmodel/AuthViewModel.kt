package com.salmanajmal.ziya.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salmanajmal.ziya.data.models.FamilyChildData
import com.salmanajmal.ziya.data.models.InviteCode
import com.salmanajmal.ziya.data.models.User
import com.salmanajmal.ziya.data.repository.AuthRepository
import com.salmanajmal.ziya.utils.NetworkResult
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
    private val authRepository: AuthRepository,
    private val notificationRepository: com.salmanajmal.ziya.data.repository.NotificationRepository
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

    // Family State
    private val _familySignupState = MutableStateFlow<NetworkResult<User>?>(null)
    val familySignupState: StateFlow<NetworkResult<User>?> = _familySignupState.asStateFlow()

    private val _familyLinkState = MutableStateFlow<NetworkResult<Boolean>?>(null)
    val familyLinkState: StateFlow<NetworkResult<Boolean>?> = _familyLinkState.asStateFlow()

    private val _createInviteState = MutableStateFlow<NetworkResult<Any?>?>(null)
    val createInviteState: StateFlow<NetworkResult<Any?>?> = _createInviteState.asStateFlow()

    private val _childDataState = MutableStateFlow<NetworkResult<FamilyChildData>?>(null)
    val childDataState: StateFlow<NetworkResult<FamilyChildData>?> = _childDataState.asStateFlow()

    private val _myInviteCodesState = MutableStateFlow<NetworkResult<List<InviteCode>>?>(null)
    val myInviteCodesState: StateFlow<NetworkResult<List<InviteCode>>?> = _myInviteCodesState.asStateFlow()


    
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
                
                // Register FCM token after successful login
                if (result is NetworkResult.Success) {
                    registerPushToken()
                }
            }
        }
    }
    
    /**
     * Register FCM push token with backend
     * Called automatically after successful login
     */
    private fun registerPushToken() {
        viewModelScope.launch {
            try {
                notificationRepository.registerFcmToken()
            } catch (e: Exception) {
                // Log but don't fail login if push registration fails
                android.util.Log.e("AuthViewModel", "Failed to register push token", e)
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
            // Unregister push token before logout
            try {
                notificationRepository.unregisterFcmToken()
            } catch (e: Exception) {
                android.util.Log.e("AuthViewModel", "Failed to unregister push token", e)
            }
            
            authRepository.logout().collect {
                // Logout successful (or at least local data cleared)
                // State update in AuthContext (observing userPreferences) should trigger navigation
            }
        }
    }

    // ==================== Family Methods ====================

    fun signupFamily(email: String, password: String, name: String, relation: String, relationDetail: String?) {
        viewModelScope.launch {
            authRepository.signupFamily(email, password, name, relation, relationDetail).collect { result ->
                _familySignupState.value = result
            }
        }
    }

    fun linkFamily(inviteCode: String) {
        viewModelScope.launch {
            authRepository.linkFamily(inviteCode).collect { result ->
                _familyLinkState.value = result
            }
        }
    }

    fun createInvite(scope: List<String>, durationInHours: Int) {
        viewModelScope.launch {
            authRepository.createInvite(scope, durationInHours).collect { result ->
                _createInviteState.value = result
            }
        }
    }

    fun fetchChildData() {
        viewModelScope.launch {
            authRepository.getChildData().collect { result ->
                _childDataState.value = result
            }
        }
    }

    fun fetchMyInviteCodes() {
        viewModelScope.launch {
            authRepository.getMyInviteCodes().collect { result ->
                _myInviteCodesState.value = result
            }
        }
    }

    fun deleteInviteCode(codeId: String, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            authRepository.deleteInviteCode(codeId).collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        fetchMyInviteCodes() // Refresh the list
                        onComplete(true)
                    }
                    is NetworkResult.Error -> onComplete(false)
                    is NetworkResult.Loading -> { /* Ignore */ }
                }
            }
        }
    }

    fun deactivateInviteCode(codeId: String, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            authRepository.deactivateInviteCode(codeId).collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        fetchMyInviteCodes() // Refresh the list
                        onComplete(true)
                    }
                    is NetworkResult.Error -> onComplete(false)
                    is NetworkResult.Loading -> { /* Ignore */ }
                }
            }
        }
    }

    fun clearFamilyStates() {
        _familySignupState.value = null
        _familyLinkState.value = null
        _createInviteState.value = null
        _childDataState.value = null
        // Note: Do NOT clear _myInviteCodesState here - it breaks the UI refresh
    }

    fun clearCreateInviteState() {
        _createInviteState.value = null
    }
}
