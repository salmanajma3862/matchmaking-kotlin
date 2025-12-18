package com.salmanajmal.ziya.utils

/**
 * Application Constants
 * Central place for all app-wide constants
 */
object Constants {
    
    // ==================== API Configuration ====================
    
    /**
     * Base URL for the API
     * Using local IP address for reliable connection
     */
    const val BASE_URL = "http://192.168.69.2:5000/"
    // const val BASE_URL = "http://localhost:5000/" // For adb reverse
    // const val BASE_URL = "http://10.0.2.2:5000/" // Fallback for emulator
    // const val BASE_URL = "https://your-production-api.com/" // Production
    
    /**
     * API Endpoints
     */
    object Endpoints {
        // Auth endpoints
        const val AUTH_BASE = "api/auth"
        const val SIGNUP = "$AUTH_BASE/signup"
        const val LOGIN = "$AUTH_BASE/login"
        const val VERIFY_EMAIL = "$AUTH_BASE/verify-email"
        const val RESEND_VERIFICATION = "$AUTH_BASE/resend-verification"
        const val FORGOT_PASSWORD = "$AUTH_BASE/forgot-password"
        const val RESET_PASSWORD = "$AUTH_BASE/reset-password"
        const val LOGOUT = "$AUTH_BASE/logout"
        const val GET_CURRENT_USER = "$AUTH_BASE/me"
        
        // User endpoints
        const val USER_BASE = "api/user"
        const val COMPLETE_PROFILE = "$USER_BASE/complete-profile"
        const val GET_FEED = "$USER_BASE/feed"
        const val GET_PROFILE = "$USER_BASE/profile/{userId}"
        const val UPDATE_PROFILE = "$USER_BASE/profile"
        const val DELETE_ACCOUNT = "$USER_BASE/account"
        
        // Profile View Tracking (Premium Features)
        const val GET_PROFILE_VIEWERS = "$USER_BASE/profile/viewers"
        const val GET_VIEWED_PROFILES = "$USER_BASE/profile/viewed"
        
        // Swipe endpoints
        const val SWIPE_BASE = "api/swipe"
        const val SWIPE_ACTION = "$SWIPE_BASE/"
        const val GET_MATCHES = "$SWIPE_BASE/matches"
        const val GET_SENT_SWIPES = "$SWIPE_BASE/sent"
        const val GET_RECEIVED_SWIPES = "$SWIPE_BASE/received"
        const val UNDO_SWIPE = "$SWIPE_BASE/undo"
        const val UNMATCH = "$SWIPE_BASE/unmatch"
        
        // Message endpoints (to be added)
        const val MESSAGE_BASE = "api/messages"
        const val GET_CONVERSATIONS = "$MESSAGE_BASE/conversations"
        const val SEND_MESSAGE = "$MESSAGE_BASE/send"
        const val GET_MESSAGES = "$MESSAGE_BASE/{conversationId}"
        
        // Notification endpoints (to be added)
        const val NOTIFICATION_BASE = "api/notifications"
        const val GET_NOTIFICATIONS = "$NOTIFICATION_BASE"
        const val MARK_READ = "$NOTIFICATION_BASE/{id}/read"

        // Family endpoints
        const val FAMILY_BASE = "api/family"
        const val FAMILY_SIGNUP = "$FAMILY_BASE/signup"
        const val FAMILY_INVITE = "$FAMILY_BASE/invite"
        const val FAMILY_MY_CODES = "$FAMILY_BASE/my-codes"
        const val FAMILY_LINK = "$FAMILY_BASE/link"
        const val FAMILY_CHILD_DATA = "$FAMILY_BASE/child-data"
    }
    
    // ==================== Network Configuration ====================
    
    const val CONNECT_TIMEOUT = 30L // seconds
    const val READ_TIMEOUT = 30L // seconds
    const val WRITE_TIMEOUT = 30L // seconds
    
    // ==================== SharedPreferences Keys ====================
    
    object PrefsKeys {
        const val AUTH_TOKEN = "auth_token"
        const val REFRESH_TOKEN = "refresh_token"
        const val USER_ID = "user_id"
        const val USER_EMAIL = "user_email"
        const val USER_NAME = "user_name"
        const val USER_PHONE = "user_phone"
        const val IS_LOGGED_IN = "is_logged_in"
        const val IS_EMAIL_VERIFIED = "is_email_verified"
        const val IS_PROFILE_COMPLETE = "is_profile_complete"
        const val THEME_MODE = "theme_mode" // "light", "dark", "system"
        const val LANGUAGE = "language"
        const val NOTIFICATIONS_ENABLED = "notifications_enabled"
        const val FIRST_LAUNCH = "first_launch"
    }
    
    // ==================== Theme ====================
    
    object Theme {
        const val LIGHT = "light"
        const val DARK = "dark"
        const val SYSTEM = "system"
    }
    
    // ==================== User Profile ====================
    
    object Gender {
        const val MALE = "male"
        const val FEMALE = "female"
    }
    
    object MaritalStatus {
        const val SINGLE = "single"
        const val DIVORCED = "divorced"
        const val WIDOWED = "widowed"
    }
    
    // ==================== Validation ====================
    
    object Validation {
        const val MIN_PASSWORD_LENGTH = 8
        const val MIN_NAME_LENGTH = 2
        const val MAX_NAME_LENGTH = 50
        const val MIN_BIO_LENGTH = 10
        const val MAX_BIO_LENGTH = 500
        const val MIN_AGE = 18
        const val MAX_AGE = 100
        const val VERIFICATION_CODE_LENGTH = 6
    }
    
    // ==================== File Upload ====================
    
    object Upload {
        const val MAX_IMAGE_SIZE_MB = 5
        const val MAX_IMAGES_COUNT = 6
        const val ALLOWED_IMAGE_TYPES = "image/jpeg,image/png,image/jpg"
        const val MAX_VIDEO_SIZE_MB = 50
        const val MAX_AUDIO_SIZE_MB = 10
    }
    
    // ==================== Pagination ====================
    
    object Pagination {
        const val DEFAULT_PAGE_SIZE = 20
        const val MATCHES_PAGE_SIZE = 10
        const val MESSAGES_PAGE_SIZE = 50
        const val NOTIFICATIONS_PAGE_SIZE = 20
    }
    
    // ==================== Cache ====================
    
    object Cache {
        const val PROFILE_CACHE_DURATION = 5 * 60 * 1000L // 5 minutes
        const val MATCHES_CACHE_DURATION = 2 * 60 * 1000L // 2 minutes
        const val IMAGE_CACHE_SIZE = 50 * 1024 * 1024L // 50 MB
    }
    
    // ==================== Match & Swipe ====================
    
    object Swipe {
        const val LIKE = "like"
        const val DISLIKE = "dislike"
        const val SUPER_LIKE = "super_like"
        const val DAILY_SWIPE_LIMIT = 100 // Free users
        const val PREMIUM_SWIPE_LIMIT = -1 // Unlimited
    }
    
    // ==================== Subscription ====================
    
    object Subscription {
        const val FREE_TIER = "free"
        const val PREMIUM_TIER = "premium"
        const val GOLD_TIER = "gold"
    }
    
    // ==================== WebSocket (for real-time chat) ====================
    
    object WebSocket {
        const val WS_URL = "ws://10.0.2.2:3000/ws"
        const val RECONNECT_INTERVAL = 5000L // 5 seconds
        const val MAX_RECONNECT_ATTEMPTS = 5
        const val PING_INTERVAL = 30000L // 30 seconds
    }
    
    // ==================== Error Messages ====================
    
    object ErrorMessages {
        const val NETWORK_ERROR = "Network error. Please check your connection."
        const val SERVER_ERROR = "Server error. Please try again later."
        const val UNKNOWN_ERROR = "An unexpected error occurred."
        const val INVALID_CREDENTIALS = "Invalid email or password."
        const val INVALID_TOKEN = "Session expired. Please login again."
        const val VALIDATION_ERROR = "Please check your input and try again."
        const val NO_INTERNET = "No internet connection."
        const val TIMEOUT = "Request timed out. Please try again."
    }
    
    // ==================== Success Messages ====================
    
    object SuccessMessages {
        const val LOGIN_SUCCESS = "Login successful!"
        const val SIGNUP_SUCCESS = "Account created successfully!"
        const val EMAIL_VERIFIED = "Email verified successfully!"
        const val PROFILE_UPDATED = "Profile updated successfully!"
        const val PASSWORD_RESET = "Password reset successfully!"
        const val MESSAGE_SENT = "Message sent!"
    }
    
    // ==================== Date Formats ====================
    
    object DateFormat {
        const val API_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        const val DISPLAY_FORMAT = "MMM dd, yyyy"
        const val DISPLAY_FORMAT_WITH_TIME = "MMM dd, yyyy hh:mm a"
        const val TIME_ONLY = "hh:mm a"
        const val DATE_ONLY = "MMM dd, yyyy"
        const val BIRTH_DATE_FORMAT = "dd/MM/yyyy"
    }
    
    // ==================== Intent Extras ====================
    
    object IntentExtras {
        const val USER_ID = "user_id"
        const val MATCH_ID = "match_id"
        const val CONVERSATION_ID = "conversation_id"
        const val NOTIFICATION_ID = "notification_id"
        const val DEEP_LINK = "deep_link"
    }
    
    // ==================== Navigation Routes ====================
    
    object Routes {
        const val SPLASH = "splash"
        const val ONBOARDING = "onboarding"
        const val LOGIN = "login"
        const val SIGNUP = "signup"
        const val VERIFY_EMAIL = "verify_email"
        const val FORGOT_PASSWORD = "forgot_password"
        const val RESET_PASSWORD = "reset_password"
        const val COMPLETE_PROFILE = "complete_profile"
        const val HOME = "home"
        const val DISCOVER = "discover"
        const val MATCHES = "matches"
        const val MATCH_DETAILS = "match_details/{matchId}"
        const val MESSAGES = "messages"
        const val CHAT = "chat/{conversationId}"
        const val PROFILE = "profile"
        const val EDIT_PROFILE = "edit_profile"
        const val VIEW_PROFILE = "view_profile/{userId}"
        const val SETTINGS = "settings"
        const val NOTIFICATIONS = "notifications"
    }
    
    // ==================== Database ====================
    
    object Database {
        const val NAME = "matchmaking_database"
        const val VERSION = 1
    }
    
    // ==================== Feature Flags ====================
    
    object FeatureFlags {
        const val ENABLE_ANALYTICS = true
        const val ENABLE_CRASH_REPORTING = true
        const val ENABLE_PUSH_NOTIFICATIONS = true
        const val ENABLE_BIOMETRIC_AUTH = true
        const val ENABLE_VOICE_INTRO = true
        const val ENABLE_VIDEO_CALL = false // Coming soon
    }
}
