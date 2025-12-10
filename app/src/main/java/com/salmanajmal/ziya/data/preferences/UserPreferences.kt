package com.salmanajmal.ziya.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.salmanajmal.ziya.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * DataStore-based preferences manager
 * Modern replacement for SharedPreferences with coroutines support
 * Similar to AsyncStorage in React Native but type-safe and reactive
 * 
 * Usage:
 * ```
 * // Save
 * userPreferences.saveAuthToken("your_token")
 * 
 * // Read as Flow (reactive)
 * userPreferences.authToken.collect { token ->
 *     // Handle token changes
 * }
 * 
 * // Read as suspend function (one-time)
 * val token = userPreferences.getAuthToken()
 * ```
 */

// DataStore instance
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferences(private val context: Context) {
    
    private val dataStore = context.dataStore
    
    // ==================== Keys ====================
    
    companion object {
        private val AUTH_TOKEN = stringPreferencesKey(Constants.PrefsKeys.AUTH_TOKEN)
        private val REFRESH_TOKEN = stringPreferencesKey(Constants.PrefsKeys.REFRESH_TOKEN)
        private val USER_ID = stringPreferencesKey(Constants.PrefsKeys.USER_ID)
        private val USER_EMAIL = stringPreferencesKey(Constants.PrefsKeys.USER_EMAIL)
        private val USER_NAME = stringPreferencesKey(Constants.PrefsKeys.USER_NAME)
        private val USER_PHONE = stringPreferencesKey(Constants.PrefsKeys.USER_PHONE)
        private val IS_LOGGED_IN = booleanPreferencesKey(Constants.PrefsKeys.IS_LOGGED_IN)
        private val IS_EMAIL_VERIFIED = booleanPreferencesKey(Constants.PrefsKeys.IS_EMAIL_VERIFIED)
        private val IS_PROFILE_COMPLETE = booleanPreferencesKey(Constants.PrefsKeys.IS_PROFILE_COMPLETE)
        private val THEME_MODE = stringPreferencesKey(Constants.PrefsKeys.THEME_MODE)
        private val LANGUAGE = stringPreferencesKey(Constants.PrefsKeys.LANGUAGE)
        private val NOTIFICATIONS_ENABLED = booleanPreferencesKey(Constants.PrefsKeys.NOTIFICATIONS_ENABLED)
        private val FIRST_LAUNCH = booleanPreferencesKey(Constants.PrefsKeys.FIRST_LAUNCH)
    }
    
    // ==================== Auth Token ====================
    
    /**
     * Save auth token
     */
    suspend fun saveAuthToken(token: String) {
        dataStore.edit { preferences ->
            preferences[AUTH_TOKEN] = token
        }
    }
    
    /**
     * Get auth token as Flow (reactive)
     */
    val authToken: Flow<String?> = dataStore.data.map { preferences ->
        preferences[AUTH_TOKEN]
    }
    
    /**
     * Get auth token once (suspend)
     */
    suspend fun getAuthToken(): String? {
        return dataStore.data.first()[AUTH_TOKEN]
    }
    
    /**
     * Clear auth token
     */
    suspend fun clearAuthToken() {
        dataStore.edit { preferences ->
            preferences.remove(AUTH_TOKEN)
        }
    }
    
    // ==================== Refresh Token ====================
    
    suspend fun saveRefreshToken(token: String) {
        dataStore.edit { preferences ->
            preferences[REFRESH_TOKEN] = token
        }
    }
    
    val refreshToken: Flow<String?> = dataStore.data.map { preferences ->
        preferences[REFRESH_TOKEN]
    }
    
    suspend fun getRefreshToken(): String? {
        return dataStore.data.first()[REFRESH_TOKEN]
    }
    
    suspend fun clearRefreshToken() {
        dataStore.edit { preferences ->
            preferences.remove(REFRESH_TOKEN)
        }
    }
    
    // ==================== User Info ====================
    
    suspend fun saveUserId(userId: String) {
        dataStore.edit { preferences ->
            preferences[USER_ID] = userId
        }
    }
    
    val userId: Flow<String?> = dataStore.data.map { preferences ->
        preferences[USER_ID]
    }
    
    suspend fun getUserId(): String? {
        return dataStore.data.first()[USER_ID]
    }
    
    suspend fun saveUserEmail(email: String) {
        dataStore.edit { preferences ->
            preferences[USER_EMAIL] = email
        }
    }
    
    val userEmail: Flow<String?> = dataStore.data.map { preferences ->
        preferences[USER_EMAIL]
    }
    
    suspend fun getUserEmail(): String? {
        return dataStore.data.first()[USER_EMAIL]
    }
    
    suspend fun saveUserName(name: String) {
        dataStore.edit { preferences ->
            preferences[USER_NAME] = name
        }
    }
    
    val userName: Flow<String?> = dataStore.data.map { preferences ->
        preferences[USER_NAME]
    }
    
    suspend fun getUserName(): String? {
        return dataStore.data.first()[USER_NAME]
    }
    
    suspend fun saveUserPhone(phone: String) {
        dataStore.edit { preferences ->
            preferences[USER_PHONE] = phone
        }
    }
    
    val userPhone: Flow<String?> = dataStore.data.map { preferences ->
        preferences[USER_PHONE]
    }
    
    suspend fun getUserPhone(): String? {
        return dataStore.data.first()[USER_PHONE]
    }
    
    // ==================== Login State ====================
    
    suspend fun setLoggedIn(isLoggedIn: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = isLoggedIn
        }
    }
    
    val isLoggedIn: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[IS_LOGGED_IN] ?: false
    }
    
    suspend fun getIsLoggedIn(): Boolean {
        return dataStore.data.first()[IS_LOGGED_IN] ?: false
    }
    
    // ==================== Email Verification ====================
    
    suspend fun setEmailVerified(isVerified: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_EMAIL_VERIFIED] = isVerified
        }
    }
    
    val isEmailVerified: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[IS_EMAIL_VERIFIED] ?: false
    }
    
    suspend fun getIsEmailVerified(): Boolean {
        return dataStore.data.first()[IS_EMAIL_VERIFIED] ?: false
    }
    
    // ==================== Profile Completion ====================
    
    suspend fun setProfileComplete(isComplete: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_PROFILE_COMPLETE] = isComplete
        }
    }
    
    val isProfileComplete: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[IS_PROFILE_COMPLETE] ?: false
    }
    
    suspend fun getIsProfileComplete(): Boolean {
        return dataStore.data.first()[IS_PROFILE_COMPLETE] ?: false
    }
    
    // ==================== Theme ====================
    
    suspend fun saveThemeMode(mode: String) {
        dataStore.edit { preferences ->
            preferences[THEME_MODE] = mode
        }
    }
    
    val themeMode: Flow<String> = dataStore.data.map { preferences ->
        preferences[THEME_MODE] ?: Constants.Theme.SYSTEM
    }
    
    suspend fun getThemeMode(): String {
        return dataStore.data.first()[THEME_MODE] ?: Constants.Theme.SYSTEM
    }
    
    // ==================== Language ====================
    
    suspend fun saveLanguage(language: String) {
        dataStore.edit { preferences ->
            preferences[LANGUAGE] = language
        }
    }
    
    val language: Flow<String> = dataStore.data.map { preferences ->
        preferences[LANGUAGE] ?: "en"
    }
    
    suspend fun getLanguage(): String {
        return dataStore.data.first()[LANGUAGE] ?: "en"
    }
    
    // ==================== Notifications ====================
    
    suspend fun setNotificationsEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[NOTIFICATIONS_ENABLED] = enabled
        }
    }
    
    val notificationsEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[NOTIFICATIONS_ENABLED] ?: true
    }
    
    suspend fun getNotificationsEnabled(): Boolean {
        return dataStore.data.first()[NOTIFICATIONS_ENABLED] ?: true
    }
    
    // ==================== First Launch ====================
    
    suspend fun setFirstLaunch(isFirst: Boolean) {
        dataStore.edit { preferences ->
            preferences[FIRST_LAUNCH] = isFirst
        }
    }
    
    val isFirstLaunch: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[FIRST_LAUNCH] ?: true
    }
    
    suspend fun getIsFirstLaunch(): Boolean {
        return dataStore.data.first()[FIRST_LAUNCH] ?: true
    }
    
    // ==================== Batch Save (for login) ====================
    
    /**
     * Save all auth data in one transaction
     * Use this after successful login
     */
    suspend fun saveAuthData(
        token: String,
        refreshToken: String?,
        userId: String,
        email: String,
        name: String,
        phone: String,
        isEmailVerified: Boolean,
        isProfileComplete: Boolean
    ) {
        dataStore.edit { preferences ->
            preferences[AUTH_TOKEN] = token
            refreshToken?.let { preferences[REFRESH_TOKEN] = it }
            preferences[USER_ID] = userId
            preferences[USER_EMAIL] = email
            preferences[USER_NAME] = name
            preferences[USER_PHONE] = phone
            preferences[IS_LOGGED_IN] = true
            preferences[IS_EMAIL_VERIFIED] = isEmailVerified
            preferences[IS_PROFILE_COMPLETE] = isProfileComplete
        }
    }
    
    // ==================== Clear All (Logout) ====================
    
    /**
     * Clear all auth-related data
     * Use this on logout
     */
    suspend fun clearAuthData() {
        dataStore.edit { preferences ->
            preferences.remove(AUTH_TOKEN)
            preferences.remove(REFRESH_TOKEN)
            preferences.remove(USER_ID)
            preferences.remove(USER_EMAIL)
            preferences.remove(USER_NAME)
            preferences.remove(USER_PHONE)
            preferences[IS_LOGGED_IN] = false
            preferences[IS_EMAIL_VERIFIED] = false
            preferences[IS_PROFILE_COMPLETE] = false
        }
    }
    
    /**
     * Clear all data (reset app)
     */
    suspend fun clearAll() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
