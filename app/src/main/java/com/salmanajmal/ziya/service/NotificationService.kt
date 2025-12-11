package com.salmanajmal.ziya.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.salmanajmal.ziya.utils.NotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Firebase Cloud Messaging service to handle push notifications
 */
@AndroidEntryPoint
class NotificationService : FirebaseMessagingService() {
    
    companion object {
        private const val TAG = "NotificationService"
    }
    
    @Inject
    lateinit var notificationHelper: NotificationHelper
    
    /**
     * Called when a new FCM token is generated
     * This can happen when:
     * - App is installed fresh
     * - App data is cleared
     * - Token is refreshed by Firebase
     */
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "New FCM token: $token")
        
        // Store token locally - will be registered with backend when user logs in
        getSharedPreferences("fcm_prefs", MODE_PRIVATE)
            .edit()
            .putString("fcm_token", token)
            .putBoolean("token_needs_registration", true)
            .apply()
    }
    
    /**
     * Called when a message is received from FCM
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "Message received from: ${remoteMessage.from}")
        
        // Check if message contains a notification payload
        remoteMessage.notification?.let { notification ->
            Log.d(TAG, "Notification - Title: ${notification.title}, Body: ${notification.body}")
            handleNotification(notification.title, notification.body, remoteMessage.data)
        }
        
        // Check if message contains a data payload
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Data payload: ${remoteMessage.data}")
            handleDataMessage(remoteMessage.data)
        }
    }
    
    private fun handleNotification(title: String?, body: String?, data: Map<String, String>) {
        val notificationType = data["type"] ?: "general"
        
        when (notificationType) {
            "new_message" -> {
                notificationHelper.showMessageNotification(
                    title = title ?: "New Message",
                    body = body ?: "You have a new message",
                    conversationId = data["conversationId"]
                )
            }
            "new_match" -> {
                notificationHelper.showMatchNotification(
                    title = title ?: "New Match!",
                    body = body ?: "You have a new match",
                    matchId = data["matchId"]
                )
            }
            else -> {
                // Generic notification
                notificationHelper.showMessageNotification(
                    title = title ?: "Ziya",
                    body = body ?: "You have a new notification"
                )
            }
        }
    }
    
    private fun handleDataMessage(data: Map<String, String>) {
        val type = data["type"] ?: return
        val title = data["title"]
        val body = data["body"]
        
        when (type) {
            "new_message" -> {
                notificationHelper.showMessageNotification(
                    title = title ?: "New Message",
                    body = body ?: "You have a new message",
                    conversationId = data["conversationId"]
                )
            }
            "new_match" -> {
                notificationHelper.showMatchNotification(
                    title = title ?: "New Match!",
                    body = body ?: "You have a new match",
                    matchId = data["matchId"]
                )
            }
        }
    }
}
