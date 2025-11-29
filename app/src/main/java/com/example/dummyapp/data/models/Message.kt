package com.example.dummyapp.data.models

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Message(
    @SerializedName("_id") val id: String,
    val conversationId: String,
    val sender: User, // Or Sender object if populated
    val text: String,
    val media: Media? = null,
    val messageType: String = "text", // text, image, audio, etc.
    val replyTo: Message? = null,
    val isEdited: Boolean = false,
    val isDeletedForEveryone: Boolean = false,
    val createdAt: String, // ISO Date string
    val updatedAt: String,
    @Transient val isSending: Boolean = false
)

data class Media(
    val imageUrl: String? = null,
    val audioUrl: String? = null,
    val videoUrl: String? = null,
    val fileUrl: String? = null
)
