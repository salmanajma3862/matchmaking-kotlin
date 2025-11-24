package com.example.dummyapp.data.models

import com.google.gson.annotations.SerializedName

data class Conversation(
    @SerializedName("_id") val id: String,
    val participants: List<User>,
    val isOneToOne: Boolean,
    val lastMessage: Message? = null,
    val lastMessageText: String = "",
    val lastMessageAt: String? = null,
    val unreadCount: Map<String, Int> = emptyMap()
)
