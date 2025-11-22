package com.example.dummyapp.data.repository

import com.example.dummyapp.data.api.AuthApiService
import com.example.dummyapp.data.models.User
import com.example.dummyapp.data.models.request.*
import com.example.dummyapp.data.preferences.UserPreferences
import com.example.dummyapp.utils.Constants
import com.example.dummyapp.utils.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Auth Repository
 * Handles authentication business logic and API calls
 * Similar to authService in React Native
 * 
 * Follows Repository Pattern:
 * - Single source of truth for auth operations
 * - Handles API calls and local storage
 * - Transforms responses into NetworkResult
 */
class AuthRepository @Inject constructor(
    private val authApiService: AuthApiService,
    private val userPreferences: UserPreferences
) {
    
    /**
     * Sign up a new user
     */
    fun signup(
        email: String,
        password: String
    ): Flow<NetworkResult<User>> = flow {
        emit(NetworkResult.Loading())
        
        try {
            val request = SignupRequest(
                email = email,
                password = password
            )
            
            val response = authApiService.signup(request)
            
            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!
                
                if (authResponse.success && authResponse.user != null) {
                    // Save auth data locally
                    authResponse.token?.let { token ->
                        userPreferences.saveAuthToken(token)
                    }
                    authResponse.refreshToken?.let { refreshToken ->
                        userPreferences.saveRefreshToken(refreshToken)
                    }
                    
                    val user = authResponse.user
                    userPreferences.saveAuthData(
                        token = authResponse.token ?: "",
                        refreshToken = authResponse.refreshToken,
                        userId = user.id,
                        email = user.email ?: "",
                        name = user.name,
                        phone = user.phone,
                        isEmailVerified = user.isEmailVerified,
                        isProfileComplete = user.photos?.isNotEmpty() ?: false
                    )
                    
                    emit(NetworkResult.Success(user))
                } else {
                    emit(NetworkResult.Error(authResponse.message))
                }
            } else {
                emit(NetworkResult.Error(response.message() ?: Constants.ErrorMessages.UNKNOWN_ERROR))
            }
        } catch (e: HttpException) {
            emit(NetworkResult.Error(e.message() ?: Constants.ErrorMessages.SERVER_ERROR))
        } catch (e: IOException) {
            emit(NetworkResult.Error(Constants.ErrorMessages.NETWORK_ERROR))
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: Constants.ErrorMessages.UNKNOWN_ERROR))
        }
    }
    
    /**
     * Login user
     */
    fun login(
        email: String,
        password: String
    ): Flow<NetworkResult<User>> = flow {
        emit(NetworkResult.Loading())
        
        try {
            val request = LoginRequest(email = email, password = password)
            val response = authApiService.login(request)
            
            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!
                
                if (authResponse.success && authResponse.user != null) {
                    val user = authResponse.user
                    
                    // Save auth data
                    userPreferences.saveAuthData(
                        token = authResponse.token ?: "",
                        refreshToken = authResponse.refreshToken,
                        userId = user.id,
                        email = user.email ?: "",
                        name = user.name,
                        phone = user.phone,
                        isEmailVerified = user.isEmailVerified,
                        isProfileComplete = user.photos?.isNotEmpty() ?: false
                    )
                    
                    emit(NetworkResult.Success(user))
                } else {
                    emit(NetworkResult.Error(authResponse.message))
                }
            } else {
                // Handle error response
                val errorBody = response.errorBody()?.string()
                if (errorBody != null) {
                    try {
                        val gson = com.google.gson.Gson()
                        val authResponse = gson.fromJson(errorBody, com.example.dummyapp.data.models.response.AuthResponse::class.java)
                        if (authResponse.requiresVerification == true) {
                            emit(NetworkResult.Error("VERIFICATION_REQUIRED"))
                        } else {
                            emit(NetworkResult.Error(authResponse.message))
                        }
                    } catch (e: Exception) {
                        emit(NetworkResult.Error(response.message() ?: Constants.ErrorMessages.INVALID_CREDENTIALS))
                    }
                } else {
                    emit(NetworkResult.Error(Constants.ErrorMessages.INVALID_CREDENTIALS))
                }
            }
        } catch (e: HttpException) {
            emit(NetworkResult.Error(Constants.ErrorMessages.INVALID_CREDENTIALS))
        } catch (e: IOException) {
            emit(NetworkResult.Error(Constants.ErrorMessages.NETWORK_ERROR))
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: Constants.ErrorMessages.UNKNOWN_ERROR))
        }
    }
    
    /**
     * Verify email with code
     */
    fun verifyEmail(
        email: String,
        code: String
    ): Flow<NetworkResult<Boolean>> = flow {
        emit(NetworkResult.Loading())
        
        try {
            val request = VerifyEmailRequest(email = email, code = code)
            val response = authApiService.verifyEmail(request)
            
            if (response.isSuccessful && response.body() != null) {
                val verifyResponse = response.body()!!
                
                if (verifyResponse.success && verifyResponse.isVerified) {
                    // Save auth data if provided
                    verifyResponse.token?.let { token ->
                        userPreferences.saveAuthToken(token)
                    }
                    
                    verifyResponse.user?.let { user ->
                        userPreferences.saveAuthData(
                            token = verifyResponse.token ?: "",
                            refreshToken = null, // Verify response might not have refresh token
                            userId = user.id,
                            email = user.email ?: "",
                            name = user.name,
                            phone = user.phone,
                            isEmailVerified = true,
                            isProfileComplete = user.photos?.isNotEmpty() ?: false
                        )
                    }

                    userPreferences.setEmailVerified(true)
                    emit(NetworkResult.Success(true))
                } else {
                    emit(NetworkResult.Error(verifyResponse.message))
                }
            } else {
                emit(NetworkResult.Error(response.message() ?: Constants.ErrorMessages.VALIDATION_ERROR))
            }
        } catch (e: HttpException) {
            emit(NetworkResult.Error(e.message() ?: Constants.ErrorMessages.SERVER_ERROR))
        } catch (e: IOException) {
            emit(NetworkResult.Error(Constants.ErrorMessages.NETWORK_ERROR))
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: Constants.ErrorMessages.UNKNOWN_ERROR))
        }
    }
    
    /**
     * Resend verification code
     */
    fun resendVerificationCode(email: String): Flow<NetworkResult<Boolean>> = flow {
        emit(NetworkResult.Loading())
        
        try {
            val request = ResendVerificationRequest(email = email)
            val response = authApiService.resendVerificationCode(request)
            
            if (response.isSuccessful && response.body() != null) {
                emit(NetworkResult.Success(true))
            } else {
                emit(NetworkResult.Error(response.message() ?: Constants.ErrorMessages.UNKNOWN_ERROR))
            }
        } catch (e: HttpException) {
            emit(NetworkResult.Error(e.message() ?: Constants.ErrorMessages.SERVER_ERROR))
        } catch (e: IOException) {
            emit(NetworkResult.Error(Constants.ErrorMessages.NETWORK_ERROR))
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: Constants.ErrorMessages.UNKNOWN_ERROR))
        }
    }
    
    /**
     * Request password reset
     */
    fun forgotPassword(email: String): Flow<NetworkResult<Boolean>> = flow {
        emit(NetworkResult.Loading())
        
        try {
            val request = ForgotPasswordRequest(email = email)
            val response = authApiService.forgotPassword(request)
            
            if (response.isSuccessful) {
                emit(NetworkResult.Success(true))
            } else {
                emit(NetworkResult.Error(response.message() ?: Constants.ErrorMessages.UNKNOWN_ERROR))
            }
        } catch (e: HttpException) {
            emit(NetworkResult.Error(e.message() ?: Constants.ErrorMessages.SERVER_ERROR))
        } catch (e: IOException) {
            emit(NetworkResult.Error(Constants.ErrorMessages.NETWORK_ERROR))
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: Constants.ErrorMessages.UNKNOWN_ERROR))
        }
    }
    
    /**
     * Reset password
     */
    fun resetPassword(
        email: String,
        token: String,
        newPassword: String
    ): Flow<NetworkResult<Boolean>> = flow {
        emit(NetworkResult.Loading())
        
        try {
            val request = ResetPasswordRequest(
                email = email,
                token = token,
                newPassword = newPassword
            )
            val response = authApiService.resetPassword(request)
            
            if (response.isSuccessful && response.body() != null) {
                emit(NetworkResult.Success(true))
            } else {
                emit(NetworkResult.Error(response.message() ?: Constants.ErrorMessages.UNKNOWN_ERROR))
            }
        } catch (e: HttpException) {
            emit(NetworkResult.Error(e.message() ?: Constants.ErrorMessages.SERVER_ERROR))
        } catch (e: IOException) {
            emit(NetworkResult.Error(Constants.ErrorMessages.NETWORK_ERROR))
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: Constants.ErrorMessages.UNKNOWN_ERROR))
        }
    }
    
    /**
     * Get current user
     */
    fun getCurrentUser(): Flow<NetworkResult<User>> = flow {
        emit(NetworkResult.Loading())
        
        try {
            val response = authApiService.getCurrentUser()
            
            if (response.isSuccessful && response.body() != null) {
                val userResponse = response.body()!!
                
                if (userResponse.success) {
                    emit(NetworkResult.Success(userResponse.user))
                } else {
                    emit(NetworkResult.Error(Constants.ErrorMessages.UNKNOWN_ERROR))
                }
            } else {
                emit(NetworkResult.Error(response.message() ?: Constants.ErrorMessages.INVALID_TOKEN))
            }
        } catch (e: HttpException) {
            if (e.code() == 401) {
                userPreferences.clearAuthData()
                emit(NetworkResult.Error(Constants.ErrorMessages.INVALID_TOKEN))
            } else {
                emit(NetworkResult.Error(e.message() ?: Constants.ErrorMessages.SERVER_ERROR))
            }
        } catch (e: IOException) {
            emit(NetworkResult.Error(Constants.ErrorMessages.NETWORK_ERROR))
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: Constants.ErrorMessages.UNKNOWN_ERROR))
        }
    }
    
    /**
     * Logout user
     */
    fun logout(): Flow<NetworkResult<Boolean>> = flow {
        emit(NetworkResult.Loading())
        
        try {
            val response = authApiService.logout()
            
            // Clear local data regardless of API response
            userPreferences.clearAuthData()
            
            if (response.isSuccessful) {
                emit(NetworkResult.Success(true))
            } else {
                // Still success locally even if API fails
                emit(NetworkResult.Success(true))
            }
        } catch (e: Exception) {
            // Clear local data even on error
            userPreferences.clearAuthData()
            emit(NetworkResult.Success(true))
        }
    }
    
    /**
     * Check if user is logged in
     */
    suspend fun isLoggedIn(): Boolean {
        return userPreferences.getIsLoggedIn()
    }
    
    /**
     * Get logged in state as Flow
     */
    fun isLoggedInFlow(): Flow<Boolean> {
        return userPreferences.isLoggedIn
    }
    
    /**
     * Check if email is verified
     */
    suspend fun isEmailVerified(): Boolean {
        return userPreferences.getIsEmailVerified()
    }
    
    /**
     * Check if profile is complete
     */
    suspend fun isProfileComplete(): Boolean {
        return userPreferences.getIsProfileComplete()
    }
}
