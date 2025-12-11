package com.salmanajmal.ziya.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.salmanajmal.ziya.MainActivity
import com.salmanajmal.ziya.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationHelper @Inject constructor(
    private val context: Context
) {
    companion object {
        const val CHANNEL_ID_MESSAGES = "messages_channel"
        const val CHANNEL_ID_MATCHES = "matches_channel"
        const val CHANNEL_NAME_MESSAGES = "Messages"
        const val CHANNEL_NAME_MATCHES = "Matches"
        
        private var notificationId = 0
        fun getNextNotificationId(): Int = notificationId++
    }
    
    /**
     * Create notification channels for Android 8.0+
     * Call this during app initialization
     */
    fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            
            // Messages channel - high importance for chat notifications
            val messagesChannel = NotificationChannel(
                CHANNEL_ID_MESSAGES,
                CHANNEL_NAME_MESSAGES,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for new messages"
                enableVibration(true)
                setShowBadge(true)
            }
            
            // Matches channel - high importance for new match notifications
            val matchesChannel = NotificationChannel(
                CHANNEL_ID_MATCHES,
                CHANNEL_NAME_MATCHES,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for new matches"
                enableVibration(true)
                setShowBadge(true)
            }
            
            notificationManager.createNotificationChannels(listOf(messagesChannel, matchesChannel))
        }
    }
    
    /**
     * Show a notification for a new message
     */
    fun showMessageNotification(
        title: String,
        body: String,
        conversationId: String? = null
    ) {
        val notificationId = getNextNotificationId()
        
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            conversationId?.let { putExtra("conversationId", it) }
            putExtra("notificationType", "new_message")
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context,
            notificationId, // Use unique request code to ensure new PendingIntent is created
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(context, CHANNEL_ID_MESSAGES)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()
        
        showNotification(notificationId, notification)
    }
    
    /**
     * Show a notification for a new match
     */
    fun showMatchNotification(
        title: String,
        body: String,
        matchId: String? = null
    ) {
        val notificationId = getNextNotificationId()
        
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            matchId?.let { putExtra("matchId", it) }
            putExtra("notificationType", "new_match")
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context,
            notificationId, // Use unique request code to ensure new PendingIntent is created
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(context, CHANNEL_ID_MATCHES)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()
        
        showNotification(notificationId, notification)
    }
    
    private fun showNotification(notificationId: Int, notification: android.app.Notification) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
        }
        
        NotificationManagerCompat.from(context).notify(notificationId, notification)
    }
}
