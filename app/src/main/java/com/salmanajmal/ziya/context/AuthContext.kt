package com.salmanajmal.ziya.context

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import com.salmanajmal.ziya.data.models.User
import com.salmanajmal.ziya.data.preferences.UserPreferences
import com.salmanajmal.ziya.data.repository.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Auth State
 * Holds current authentication state
 */
data class AuthState(
    val isAuthenticated: Boolean = false,
    val isLoading: Boolean = true,
    val user: User? = null,
    val error: String? = null,
    val isEmailVerified: Boolean = false,
    val isProfileComplete: Boolean = false
)

/**
 * Auth Context Manager
 * Similar to AuthContext in React Native
 * Manages global authentication state using Compose State
 * 
 * Usage:
 * ```
 * val authContext = LocalAuthContext.current
 * val authState by authContext.authState.collectAsState()
 * 
 * if (authState.isAuthenticated) {
 *     // User is logged in
 * }
 * ```
 */
class AuthContextManager(
    private val authRepository: AuthRepository,
    private val userPreferences: UserPreferences,
    private val coroutineScope: CoroutineScope
) {
    // Auth state flow
    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()
    
    init {
        // Initialize auth state from preferences
        checkAuthStatus()
    }
    
    /**
     * Check authentication status on app start
     */
    fun checkAuthStatus() {
        coroutineScope.launch {
            _authState.value = _authState.value.copy(isLoading = true)
            
            try {
                val isLoggedIn = authRepository.isLoggedIn()
                val isEmailVerified = authRepository.isEmailVerified()
                val isProfileComplete = authRepository.isProfileComplete()
                
                if (isLoggedIn) {
                    // Try to fetch current user
                    authRepository.getCurrentUser().collect { result ->
                        when (result) {
                            is com.salmanajmal.ziya.utils.NetworkResult.Success -> {
                                _authState.value = AuthState(
                                    isAuthenticated = true,
                                    isLoading = false,
                                    user = result.data,
                                    isEmailVerified = isEmailVerified,
                                    isProfileComplete = isProfileComplete
                                )
                            }
                            is com.salmanajmal.ziya.utils.NetworkResult.Error -> {
                                // Token might be invalid, clear auth
                                clearAuth()
                            }
                            is com.salmanajmal.ziya.utils.NetworkResult.Loading -> {
                                _authState.value = _authState.value.copy(isLoading = true)
                            }
                        }
                    }
                } else {
                    _authState.value = AuthState(
                        isAuthenticated = false,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _authState.value = AuthState(
                    isAuthenticated = false,
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
    
    /**
     * Set user after successful login/signup
     */
    fun setUser(user: User) {
        coroutineScope.launch {
            _authState.value = _authState.value.copy(
                isAuthenticated = true,
                user = user,
                isLoading = false,
                error = null,
                isEmailVerified = user.isEmailVerified,
                isProfileComplete = user.photos?.isNotEmpty() ?: false
            )
        }
    }
    
    /**
     * Update user data
     */
    fun updateUser(user: User) {
        _authState.value = _authState.value.copy(
            user = user,
            isEmailVerified = user.isEmailVerified,
            isProfileComplete = user.photos?.isNotEmpty() ?: false
        )
    }
    
    /**
     * Set email verified
     */
    fun setEmailVerified(verified: Boolean) {
        _authState.value = _authState.value.copy(
            isEmailVerified = verified
        )
    }
    
    /**
     * Set profile complete
     */
    fun setProfileComplete(complete: Boolean) {
        _authState.value = _authState.value.copy(
            isProfileComplete = complete
        )
    }
    
    /**
     * Set loading state
     */
    fun setLoading(loading: Boolean) {
        _authState.value = _authState.value.copy(isLoading = loading)
    }
    
    /**
     * Set error
     */
    fun setError(error: String?) {
        _authState.value = _authState.value.copy(error = error)
    }
    
    /**
     * Clear authentication (logout)
     */
    fun clearAuth() {
        coroutineScope.launch {
            // Clear from repository/preferences
            authRepository.logout().collect { }
            
            // Reset state
            _authState.value = AuthState(
                isAuthenticated = false,
                isLoading = false,
                user = null,
                error = null
            )
        }
    }
    
    /**
     * Refresh user data
     */
    fun refreshUser() {
        coroutineScope.launch {
            authRepository.getCurrentUser().collect { result ->
                when (result) {
                    is com.salmanajmal.ziya.utils.NetworkResult.Success -> {
                        updateUser(result.data!!)
                    }
                    is com.salmanajmal.ziya.utils.NetworkResult.Error -> {
                        setError(result.message)
                    }
                    else -> {}
                }
            }
        }
    }
}

/**
 * CompositionLocal for AuthContext
 * Allows accessing auth state anywhere in the app
 */
val LocalAuthContext = staticCompositionLocalOf<AuthContextManager> {
    error("No AuthContext provided")
}

/**
 * Auth Context Provider
 * Wrap your app with this to provide auth context
 * 
 * Usage:
 * ```
 * AuthContextProvider(authRepository, userPreferences) {
 *     // Your app content
 * }
 * ```
 */
@Composable
fun AuthContextProvider(
    authRepository: AuthRepository,
    userPreferences: UserPreferences,
    content: @Composable () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    
    val authContextManager = remember {
        AuthContextManager(
            authRepository = authRepository,
            userPreferences = userPreferences,
            coroutineScope = coroutineScope
        )
    }
    
    CompositionLocalProvider(LocalAuthContext provides authContextManager) {
        content()
    }
}

/**
 * Helper composable to get auth state
 * Makes it easier to access auth state in composables
 */
@Composable
fun rememberAuthState(): AuthState {
    val authContext = LocalAuthContext.current
    return authContext.authState.collectAsState().value
}

/**
 * Helper composable to get current user
 */
@Composable
fun rememberCurrentUser(): User? {
    val authState = rememberAuthState()
    return authState.user
}

/**
 * Helper composable to check if authenticated
 */
@Composable
fun rememberIsAuthenticated(): Boolean {
    val authState = rememberAuthState()
    return authState.isAuthenticated
}
