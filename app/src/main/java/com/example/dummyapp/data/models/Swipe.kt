package com.example.dummyapp.data.models

import com.google.gson.annotations.SerializedName

/**
 * Swipe Model
 */
data class Swipe(
    @SerializedName("_id")
    val id: String,
    val userId: String,
    val targetUserId: String,
    val action: String, // "like", "dislike", "super_like"
    val timestamp: String,
    val isMatch: Boolean = false
)
