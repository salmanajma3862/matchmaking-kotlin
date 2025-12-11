package com.salmanajmal.ziya

import android.app.Application
import com.salmanajmal.ziya.utils.NotificationHelper
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/**
 * Application Class
 * Entry point for the app
 * Annotated with @HiltAndroidApp to enable Hilt dependency injection
 */
@HiltAndroidApp
class MatchmakingApplication : Application() {
    
    @Inject
    lateinit var notificationHelper: NotificationHelper
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize app-wide components here
        println("🚀 Matchmaking App Started")
        
        // Create notification channels for Android 8.0+
        notificationHelper.createNotificationChannels()
    }
}

