package com.example.dummyapp.utils

import okhttp3.Interceptor
import okhttp3.Response
import com.example.dummyapp.data.preferences.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

/**
 * Auth Interceptor
 * Automatically adds Authorization header to all API requests
 * Similar to axios interceptors in React Native
 */
class AuthInterceptor(
    private val userPreferences: UserPreferences
) : Interceptor {
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        
        // Skip auth for certain endpoints
        val url = originalRequest.url.toString()
        if (shouldSkipAuth(url)) {
            return chain.proceed(originalRequest)
        }
        
        // Get token from preferences (blocking call within interceptor)
        val token = runBlocking {
            userPreferences.getAuthToken()
        }
        
        // Add Authorization header if token exists
        val newRequest = if (token != null) {
            originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            originalRequest
        }
        
        return chain.proceed(newRequest)
    }
    
    /**
     * Check if endpoint should skip authentication
     */
    private fun shouldSkipAuth(url: String): Boolean {
        val publicEndpoints = listOf(
            "/auth/signup",
            "/auth/login",
            "/auth/verify-email",
            "/auth/resend-verification",
            "/auth/forgot-password",
            "/auth/reset-password",
            "/auth/refresh"
        )
        
        return publicEndpoints.any { url.contains(it) }
    }
}

/**
 * Logging Interceptor
 * Logs HTTP requests and responses for debugging
 */
class LoggingInterceptor : Interceptor {
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        
        // Log request
        println("🌐 REQUEST: ${request.method} ${request.url}")
        request.headers.forEach { (name, value) ->
            if (name != "Authorization") { // Don't log sensitive headers
                println("  Header: $name = $value")
            }
        }
        
        val startTime = System.currentTimeMillis()
        val response = chain.proceed(request)
        val duration = System.currentTimeMillis() - startTime
        
        // Log response
        println("✅ RESPONSE: ${response.code} ${request.url} (${duration}ms)")
        
        return response
    }
}

/**
 * Error Interceptor
 * Handles common HTTP errors globally
 */
class ErrorInterceptor(
    private val userPreferences: UserPreferences
) : Interceptor {
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        
        when (response.code) {
            401 -> {
                // Unauthorized - clear auth data and force re-login
                println("❌ 401 Unauthorized - Clearing auth data")
                runBlocking {
                    userPreferences.clearAuthData()
                }
            }
            403 -> {
                println("❌ 403 Forbidden - Access denied")
            }
            404 -> {
                println("⚠️ 404 Not Found - Endpoint not found")
            }
            500, 502, 503 -> {
                println("❌ ${response.code} Server Error")
            }
        }
        
        return response
    }
}
