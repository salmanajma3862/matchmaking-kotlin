package com.example.dummyapp.utils

/**
 * Sealed class for handling network responses
 * Similar to Redux/React Native request states (loading, success, error)
 * 
 * Usage:
 * ```
 * when (result) {
 *     is NetworkResult.Loading -> showLoading()
 *     is NetworkResult.Success -> handleSuccess(result.data)
 *     is NetworkResult.Error -> showError(result.message)
 * }
 * ```
 */
sealed class NetworkResult<T>(
    val data: T? = null,
    val message: String? = null
) {
    /**
     * Loading state - request in progress
     */
    class Loading<T> : NetworkResult<T>()
    
    /**
     * Success state - request completed successfully
     * @param data The response data
     */
    class Success<T>(data: T) : NetworkResult<T>(data)
    
    /**
     * Error state - request failed
     * @param message Error message to display
     * @param data Optional partial data (for cases like cached data)
     */
    class Error<T>(message: String, data: T? = null) : NetworkResult<T>(data, message)
}

/**
 * Resource wrapper for UI state management
 * Includes loading state for better UX
 */
sealed class Resource<T> {
    class Idle<T> : Resource<T>()
    class Loading<T> : Resource<T>()
    class Success<T>(val data: T) : Resource<T>()
    class Error<T>(val message: String, val data: T? = null) : Resource<T>()
}

/**
 * Simple Result wrapper for operations without network
 */
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}
