package com.example.dummyapp.data.models.request

import com.google.gson.annotations.SerializedName

/**
 * API Request Models
 * Data classes for sending requests to the backend
 */

// ==================== Auth Requests ====================

/**
 * Signup Request
 */
data class SignupRequest(
    val email: String,
    val password: String
)

/**
 * Login Request
 */
data class LoginRequest(
    val email: String,
    val password: String
)

/**
 * Verify Email Request
 */
data class VerifyEmailRequest(
    val email: String,
    val code: String
)

/**
 * Resend Verification Code Request
 */
data class ResendVerificationRequest(
    val email: String
)

/**
 * Forgot Password Request
 */
data class ForgotPasswordRequest(
    val email: String
)

/**
 * Reset Password Request
 */
data class ResetPasswordRequest(
    val email: String,
    val token: String,
    val newPassword: String
)

// ==================== Profile Requests ====================

/**
 * Complete Profile Request
 * Sent after email verification with photos and additional details
 */
data class CompleteProfileRequest(
    val name: String? = null,
    val phone: String? = null,
    val gender: String? = null,
    val dob: String? = null,
    val bio: String? = null,
    val height: Int? = null,
    val weight: Int? = null,
    val bodyType: String? = null,
    val city: String? = null,
    val country: String? = null,
    val address: String? = null,
    val religion: String? = null,
    val sect: String? = null,
    val maritalStatus: String? = null,
    val education: String? = null,
    val profession: String? = null,
    val incomeRange: String? = null,
    val interests: List<String>? = null,
    val hobbies: List<String>? = null,
    val smoking: Boolean? = null,
    val drinking: Boolean? = null,
    val dietPreference: String? = null,
    val familyBackground: String? = null,
    val numberOfSiblings: Int? = null,
    val livingWithFamily: Boolean? = null,
    val preferences: UserPreferencesRequest? = null
)

/**
 * User Preferences Request
 */
data class UserPreferencesRequest(
    val ageRange: AgeRangeRequest? = null,
    val distanceRange: Int? = null,
    val genderPreference: String? = null,
    val religionPreference: String? = null,
    val educationPreference: String? = null,
    val maritalStatusPreference: List<String>? = null
)

/**
 * Age Range Request
 */
data class AgeRangeRequest(
    val min: Int,
    val max: Int
)

/**
 * Update Profile Request
 */
data class UpdateProfileRequest(
    val name: String? = null,
    val bio: String? = null,
    val height: Int? = null,
    val weight: Int? = null,
    val bodyType: String? = null,
    val city: String? = null,
    val country: String? = null,
    val address: String? = null,
    val religion: String? = null,
    val sect: String? = null,
    val maritalStatus: String? = null,
    val education: String? = null,
    val profession: String? = null,
    val incomeRange: String? = null,
    val interests: List<String>? = null,
    val hobbies: List<String>? = null,
    val smoking: Boolean? = null,
    val drinking: Boolean? = null,
    val dietPreference: String? = null,
    val familyBackground: String? = null,
    val numberOfSiblings: Int? = null,
    val livingWithFamily: Boolean? = null,
    val intention: String? = null,
    val readyForMarriageTimeframe: String? = null
)

// ==================== Match & Swipe Requests ====================

/**
 * Send Interest Request
 */
data class SendInterestRequest(
    val targetUserId: String,
    val message: String? = null
)

// ==================== Message Requests ====================


/**
 * Mark Message as Read Request
 */
data class MarkMessageReadRequest(
    val messageId: String
)

// ==================== Settings Requests ====================

/**
 * Update Notification Settings Request
 */
data class NotificationSettingsRequest(
    val notificationsEnabled: Boolean,
    val messageNotifications: Boolean,
    val matchNotifications: Boolean,
    val familyApprovalNotifications: Boolean
)

/**
 * Update Privacy Settings Request
 */
data class PrivacySettingsRequest(
    val profileVisibility: String, // "public", "members_only", "private"
    val showOnlineStatus: Boolean,
    val showLastSeen: Boolean,
    val showDistance: Boolean
)

/**
 * Block User Request
 */
data class BlockUserRequest(
    val userId: String,
    val reason: String? = null
)

/**
 * Report User Request
 */
data class ReportUserRequest(
    val userId: String,
    val reason: String,
    val description: String? = null
)

// ==================== Subscription Requests ====================

/**
 * Subscribe Request
 */
data class SubscribeRequest(
    val tier: String, // "premium", "gold"
    val duration: String, // "monthly", "yearly"
    val paymentMethod: String
)

// ==================== Family Requests ====================

/**
 * Signup Family Request
 */
data class SignupFamilyRequest(
    val email: String,
    val password: String,
    val name: String,
    val relation: String,
    val relationDetail: String?
)

/**
 * Link Family Request
 */
data class LinkFamilyRequest(
    val inviteCode: String
)

/**
 * Create Invite Request
 */
data class CreateInviteRequest(
    val scope: List<String>,
    val durationInHours: Int
)
