package com.salmanajmal.ziya.data.repository

import android.content.Context
import com.google.firebase.messaging.FirebaseMessaging
import com.salmanajmal.ziya.data.api.NotificationApiService
import com.salmanajmal.ziya.data.api.RegisterPushTokenRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Notification Repository
 * Handles FCM token registration with the backend
 */
@Singleton
class NotificationRepository @Inject constructor(
    private val notificationApiService: NotificationApiService,
    @ApplicationContext private val context: Context
) {
    
    companion object {
        private const val TAG = "NotificationRepository"
        private const val PREFS_NAME = "fcm_prefs"
        private const val KEY_FCM_TOKEN = "fcm_token"
        private const val KEY_TOKEN_REGISTERED = "token_registered"
    }
    
    /**
     * Get the current FCM token and register it with the backend
     * This should be called after successful login
     */
    suspend fun registerFcmToken(): Result<Unit> {
        return try {
            // Get the current FCM token
            val token = FirebaseMessaging.getInstance().token.await()
            android.util.Log.d(TAG, "FCM Token obtained: ${token.take(20)}...")
            
            // Save token locally
            saveTokenLocally(token)
            
            // Register with backend
            val response = notificationApiService.registerPushToken(
                RegisterPushTokenRequest(fcmToken = token)
            )
            
            if (response.isSuccessful) {
                markTokenAsRegistered(true)
                android.util.Log.d(TAG, "FCM token registered with backend successfully")
                Result.success(Unit)
            } else {
                android.util.Log.e(TAG, "Failed to register FCM token: ${response.message()}")
                Result.failure(Exception("Failed to register push token"))
            }
        } catch (e: Exception) {
            android.util.Log.e(TAG, "Error registering FCM token", e)
            Result.failure(e)
        }
    }
    
    /**
     * Unregister FCM token from backend (call on logout)
     */
    suspend fun unregisterFcmToken(): Result<Unit> {
        return try {
            val response = notificationApiService.unregisterPushToken()
            
            if (response.isSuccessful) {
                markTokenAsRegistered(false)
                android.util.Log.d(TAG, "FCM token unregistered from backend")
            }
            
            Result.success(Unit)
        } catch (e: Exception) {
            android.util.Log.e(TAG, "Error unregistering FCM token", e)
            Result.failure(e)
        }
    }
    
    /**
     * Check if token needs to be registered (e.g., after app restart)
     */
    fun isTokenRegistered(): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_TOKEN_REGISTERED, false)
    }
    
    /**
     * Get locally stored FCM token
     */
    fun getStoredToken(): String? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_FCM_TOKEN, null)
    }
    
    private fun saveTokenLocally(token: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_FCM_TOKEN, token).apply()
    }
    
    private fun markTokenAsRegistered(registered: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_TOKEN_REGISTERED, registered).apply()
    }
}
