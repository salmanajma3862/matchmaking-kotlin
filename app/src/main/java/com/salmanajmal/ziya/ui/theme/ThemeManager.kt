package com.salmanajmal.ziya.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.salmanajmal.ziya.data.preferences.UserPreferences
import com.salmanajmal.ziya.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Theme mode options
 */
enum class ThemeMode {
    LIGHT,
    DARK,
    SYSTEM;
    
    companion object {
        fun fromString(value: String): ThemeMode {
            return when (value) {
                Constants.Theme.LIGHT -> LIGHT
                Constants.Theme.DARK -> DARK
                else -> SYSTEM
            }
        }
        
        fun toString(mode: ThemeMode): String {
            return when (mode) {
                LIGHT -> Constants.Theme.LIGHT
                DARK -> Constants.Theme.DARK
                SYSTEM -> Constants.Theme.SYSTEM
            }
        }
    }
}

/**
 * ThemeManager - Manages app-wide theme state
 * Provides reactive theme updates and persists user preference
 */
class ThemeManager(private val userPreferences: UserPreferences) {
    
    var themeMode by mutableStateOf(ThemeMode.SYSTEM)
        private set
    
    private val scope = CoroutineScope(Dispatchers.IO)
    
    init {
        // Load saved theme preference on initialization
        runBlocking {
            val savedTheme = userPreferences.getThemeMode()
            themeMode = ThemeMode.fromString(savedTheme)
        }
    }
    
    /**
     * Update the theme mode and persist to preferences
     */
    fun updateThemeMode(mode: ThemeMode) {
        themeMode = mode
        scope.launch {
            userPreferences.saveThemeMode(ThemeMode.toString(mode))
        }
    }
    
    /**
     * Check if dark theme should be used based on current mode
     */
    @Composable
    fun shouldUseDarkTheme(): Boolean {
        return when (themeMode) {
            ThemeMode.LIGHT -> false
            ThemeMode.DARK -> true
            ThemeMode.SYSTEM -> androidx.compose.foundation.isSystemInDarkTheme()
        }
    }
}

/**
 * CompositionLocal for accessing ThemeManager
 */
val LocalThemeManager = compositionLocalOf<ThemeManager> { 
    error("ThemeManager not provided")
}
