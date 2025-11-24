package com.example.dummyapp.data.models

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
    val gender: String, // "male" or "female"
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
    
    // Auth & Account
    val phone: String,
    val email: String? = null,
    val isEmailVerified: Boolean = false,
    val lastLoginAt: String? = null,
    
    // Presence
    val isOnline: Boolean = false,
    val lastSeen: String? = null,
    
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
    val distance: Double? = null
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

