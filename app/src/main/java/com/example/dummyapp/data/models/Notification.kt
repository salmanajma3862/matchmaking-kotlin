package com.example.dummyapp.data.models

import com.google.gson.annotations.SerializedName

/**
 * Notification Model
 */
data class Notification(
    @SerializedName("_id")
    val id: String,
    val userId: String,
    val type: String, // "match", "message", "like", "interest", "verification"
    val title: String,
    val body: String,
    val data: Map<String, String>? = null,
    val isRead: Boolean = false,
    val createdAt: String
)
