package com.example.dummyapp.data.models.request

import com.example.dummyapp.data.models.Media

data class SendMessageRequest(
    val conversationId: String,
    val text: String? = null,
    val media: Media? = null,
    val messageType: String = "text",
    val replyTo: String? = null
)

data class CreateConversationRequest(
    val participantId: String
)

data class EditMessageRequest(
    val text: String
)

data class MarkReadRequest(
    val conversationId: String
)
