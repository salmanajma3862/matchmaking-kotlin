package com.example.dummyapp.data.models.response

import com.example.dummyapp.data.models.User
import com.google.gson.annotations.SerializedName

/**
 * API Response Models
 * Data classes for receiving responses from the backend
 */

// ==================== Base Response ====================

/**
 * Generic API Response wrapper
 */
data class ApiResponse<T>(
    val success: Boolean,
    val message: String? = null,
    val data: T? = null,
    val error: String? = null
)

// ==================== Auth Responses ====================

/**
 * Auth Response (Login & Signup)
 */
data class AuthResponse(
    val success: Boolean,
    val message: String,
    val token: String? = null,
    val refreshToken: String? = null,
    val user: User? = null,
    val requiresVerification: Boolean? = null
)

/**
 * Email Verification Response
 */
data class VerifyEmailResponse(
    val success: Boolean,
    val message: String,
    val isVerified: Boolean,
    val token: String? = null,
    val user: User? = null
)

/**
 * Password Reset Response
 */
data class PasswordResetResponse(
    val success: Boolean,
    val message: String
)

// ==================== User Responses ====================

/**
 * User Profile Response
 */
data class UserProfileResponse(
    val success: Boolean,
    val data: User
)

/**
 * Update Profile Response
 */
data class UpdateProfileResponse(
    val success: Boolean,
    val message: String,
    val user: User
)

/**
 * Complete Profile Response
 */
data class CompleteProfileResponse(
    val success: Boolean,
    val message: String,
    val user: User
)

// ==================== Match Responses ====================

/**
 * Matches List Response
 */
data class MatchesResponse(
    val success: Boolean,
    val matches: List<com.example.dummyapp.data.models.Match>,
    val total: Int,
    val page: Int,
    val totalPages: Int
)

/**
 * Swipe Response
 */
data class SwipeResponse(
    val success: Boolean,
    val message: String,
    val isMatch: Boolean,
    val match: com.example.dummyapp.data.models.Match? = null
)

/**
 * Discover Profiles Response
 */
data class DiscoverResponse(
    val success: Boolean,
    val users: List<User>,
    val hasMore: Boolean
)

// ==================== Message Responses ====================

/**
 * Conversations Response
 */
data class ConversationsResponse(
    val success: Boolean,
    val conversations: List<com.example.dummyapp.data.models.Conversation>,
    val total: Int
)

/**
 * Messages Response
 */
data class MessagesResponse(
    val success: Boolean,
    val messages: List<com.example.dummyapp.data.models.Message>,
    val total: Int,
    val page: Int,
    val totalPages: Int
)

/**
 * Send Message Response
 */
data class SendMessageResponse(
    val success: Boolean,
    val message: com.example.dummyapp.data.models.Message
)

// ==================== Notification Responses ====================

/**
 * Notifications Response
 */
data class NotificationsResponse(
    val success: Boolean,
    val notifications: List<com.example.dummyapp.data.models.Notification>,
    val unreadCount: Int
)

// ==================== Error Response ====================

/**
 * Error Response from API
 */
data class ErrorResponse(
    val success: Boolean = false,
    val message: String,
    val error: String? = null,
    val errors: List<ValidationError>? = null
)

/**
 * Validation Error
 */
data class ValidationError(
    val field: String,
    val message: String
)

// ==================== Subscription Responses ====================

/**
 * Subscription Response
 */
data class SubscriptionResponse(
    val success: Boolean,
    val message: String,
    val subscription: com.example.dummyapp.data.models.Subscription
)

// ==================== Analytics Responses ====================

/**
 * Profile Views Response
 */
data class ProfileViewsResponse(
    val success: Boolean,
    val views: Int,
    val recentViewers: List<User>
)

/**
 * Match Stats Response
 */
data class MatchStatsResponse(
    val success: Boolean,
    val totalMatches: Int,
    val totalLikes: Int,
    val totalSuperLikes: Int,
    val matchRate: Double
)
