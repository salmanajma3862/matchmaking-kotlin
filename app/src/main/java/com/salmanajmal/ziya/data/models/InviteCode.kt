package com.salmanajmal.ziya.data.models

import com.google.gson.annotations.SerializedName

/**
 * InviteCode - Represents a family invite code with usage tracking
 */
data class InviteCode(
    @SerializedName("_id")
    val id: String,
    val code: String,
    val scope: List<String>,
    val expiresAt: String,
    val status: String, // "active", "used", "expired"
    val usedBy: UsedByUser?,
    val usedAt: String?,
    val createdAt: String
) {
    /**
     * Check if the code is still active
     */
    fun isActive(): Boolean = status == "active"
    
    /**
     * Check if the code has been used
     */
    fun isUsed(): Boolean = status == "used"
    
    /**
     * Check if the code is expired
     */
    fun isExpired(): Boolean = status == "expired"
    
    /**
     * Get formatted scope as readable string
     */
    fun getScopeDescription(): String {
        return scope.joinToString(", ") { permission ->
            when (permission) {
                "view_matches" -> "View Matches"
                "chat" -> "Take Part in Chats"
                "view_profile" -> "View Profile"
                else -> permission.replace("_", " ").replaceFirstChar { it.uppercase() }
            }
        }
    }
}

/**
 * UsedByUser - Minimal user info for who used the invite code
 */
data class UsedByUser(
    @SerializedName("_id")
    val id: String,
    val name: String,
    val email: String?
)
