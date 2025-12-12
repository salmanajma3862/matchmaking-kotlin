package com.salmanajmal.ziya.data.models

import com.google.gson.annotations.SerializedName

/**
 * User Model - Main domain model
 * Matches the backend User schema
 */
data class User(
    @SerializedName("_id")
    val id: String,
    
    // Basic Profile
    val name: String,
    val gender: String? = null, // "male" or "female"
    val dob: String? = null, // ISO date string
    val bio: String? = null,
    val height: Int? = null,
    val weight: Int? = null,
    val bodyType: String? = null,
    val city: String? = null,
    val country: String? = null,
    val address: String? = null,
    val religion: String? = null,
    val sect: String? = null,
    val maritalStatus: String? = "single",
    val education: String? = null,
    val profession: String? = null,
    val incomeRange: String? = null,
    
    // Photos & Media
    val photos: List<Photo>? = null,
    val voiceIntroUrl: String? = null,
    
    // Interests & Hobbies
    val interests: List<String>? = null,
    val hobbies: List<String>? = null,
    
    // Life Habits
    val smoking: Boolean? = false,
    val drinking: Boolean? = false,
    val dietPreference: String? = null,
    
    // Family
    val familyBackground: String? = null,
    val numberOfSiblings: Int? = null,
    val livingWithFamily: Boolean? = false,

    // Looking For
    val intention: String? = null,
    val readyForMarriageTimeframe: String? = null,

    
    // Auth & Account
    val phone: String? = null,
    val email: String? = null,
    val role: String? = "user", // "user", "family_unlinked", "family", "admin"
    val linkedChild: String? = null, // ID of linked child
    val familyMembers: List<String>? = null, // IDs of family members
    val familyMode: FamilyMode? = null, // Family mode settings including permissions
    val isEmailVerified: Boolean = false,
    val lastLoginAt: String? = null,
    
    // Presence
    val isOnline: Boolean = false,
    val lastSeen: String? = null,
    
    // Privacy Settings
    val privacySettings: PrivacySettings? = null,
    
    // Subscription
    val subscription: Subscription? = null,
    
    // Preferences
    val preferences: UserPreferences? = null,
    
    // Verification
    val isVerified: Boolean = false,
    val verificationBadge: String? = null,
    
    // Onboarding
    val onboardingCompleted: Boolean = false,

    // Computed fields
    val age: Int? = null,
    val matchPercentage: Int? = null,
    val distance: Double? = null,
    
    // Stats
    val totalMatches: Int? = 0,
    val profileViews: Int? = 0
)

/**
 * Photo Model
 */
data class Photo(
    val url: String,
    val isPrimary: Boolean = false,
    @SerializedName("_id")
    val id: String? = null
)

/**
 * Privacy Settings Model
 */
data class PrivacySettings(
    val hideOnlineStatus: Boolean = false,
    val hideLastSeen: Boolean = false,
    val hideProfilePhoto: Boolean = false,
    val blockStrangersFromMessaging: Boolean = false
)

/**
 * Subscription Model
 */
data class Subscription(
    val tier: String = "free", // "free", "premium", "gold"
    val isActive: Boolean = false,
    val startDate: String? = null,
    val endDate: String? = null,
    val autoRenew: Boolean = false
)

/**
 * User Preferences Model
 */
data class UserPreferences(
    val ageRange: AgeRange? = null,
    val distanceRange: Int? = null,
    val genderPreference: String? = null,
    val religionPreference: String? = null,
    val educationPreference: String? = null,
    val maritalStatusPreference: List<String>? = null
)

/**
 * Age Range Model
 */
data class AgeRange(
    val min: Int = 18,
    val max: Int = 100
)

/**
 * Match Model
 */
data class Match(
    @SerializedName("_id")
    val id: String,
    val user: User,
    val matchedAt: String,
    val matchScore: Int? = null,
    val compatibility: Compatibility? = null,
    val status: String = "active", // "active", "blocked", "unmatched"
    val lastMessage: Message? = null,
    val unreadCount: Int = 0
)

/**
 * Compatibility Model
 */
data class Compatibility(
    val score: Int,
    val commonInterests: List<String>? = null,
    val strengths: List<String>? = null,
    val considerations: List<String>? = null
)

/**
 * Family Mode Model - Contains permissions for family members
 */
data class FamilyMode(
    val enabled: Boolean = false,
    val familyRelation: String? = null,
    val familyRelationDetail: String? = null,
    val permissions: List<String>? = null // e.g., ["view_matches", "chat"]
)

/**
 * Family Child Data Response - Data returned to family members
 */
data class FamilyChildData(
    val permissions: List<String>,
    val childProfile: ChildProfile,
    val matches: List<FamilyMatch>?,
    val conversations: List<FamilyConversation>?
)

data class ChildProfile(
    val id: String,
    val name: String,
    val photo: String?,
    val completeness: Int?
)

data class FamilyMatch(
    val matchedAt: String?,
    val compatibilityScore: Int?,
    val user: FamilyMatchUser?
)

data class FamilyMatchUser(
    val id: String,
    val name: String,
    val photo: String?,
    val city: String?,
    val profession: String?
)

data class FamilyConversation(
    val id: String,
    val participants: List<FamilyParticipant>?,
    val lastMessage: FamilyLastMessage?,
    val updatedAt: String?
)

data class FamilyParticipant(
    val id: String,
    val name: String,
    val photo: String?
)

data class FamilyLastMessage(
    val content: String?,
    val type: String?,
    val createdAt: String?
)
