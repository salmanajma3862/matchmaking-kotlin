package com.example.dummyapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application Class
 * Entry point for the app
 * Annotated with @HiltAndroidApp to enable Hilt dependency injection
 */
@HiltAndroidApp
class MatchmakingApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize app-wide components here
        println("🚀 Matchmaking App Started")
    }
}
