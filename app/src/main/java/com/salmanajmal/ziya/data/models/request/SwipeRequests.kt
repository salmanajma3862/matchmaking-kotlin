package com.salmanajmal.ziya.data.models.request

import com.google.gson.annotations.SerializedName

/**
 * Request body for recording a swipe
 */
data class SwipeRequest(
    @SerializedName("targetUserId")
    val targetUserId: String,
    
    @SerializedName("action")
    val action: String // "like", "dislike", "superlike"
)
