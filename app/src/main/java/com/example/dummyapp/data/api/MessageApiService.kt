package com.example.dummyapp.data.api

import com.example.dummyapp.data.models.request.SendMessageRequest
import com.example.dummyapp.data.models.request.MarkMessageReadRequest
import com.example.dummyapp.data.models.response.*
import com.example.dummyapp.utils.Constants
import retrofit2.Response
import retrofit2.http.*

/**
 * Message API Service
 * Handles messaging and conversation functionality
 */
interface MessageApiService {
    
    /**
     * Get all conversations
     */
    @GET(Constants.Endpoints.GET_CONVERSATIONS)
    suspend fun getConversations(): Response<ConversationsResponse>
    
    /**
     * Get messages in a conversation
     */
    @GET(Constants.Endpoints.GET_MESSAGES)
    suspend fun getMessages(
        @Path("conversationId") conversationId: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = Constants.Pagination.MESSAGES_PAGE_SIZE
    ): Response<MessagesResponse>
    
    /**
     * Send a message
     */
    @POST(Constants.Endpoints.SEND_MESSAGE)
    suspend fun sendMessage(
        @Body request: SendMessageRequest
    ): Response<SendMessageResponse>
    
    /**
     * Mark message as read
     */
    @PUT("${Constants.Endpoints.MESSAGE_BASE}/{messageId}/read")
    suspend fun markMessageAsRead(
        @Path("messageId") messageId: String
    ): Response<ApiResponse<Any>>
    
    /**
     * Mark all messages in conversation as read
     */
    @PUT("${Constants.Endpoints.MESSAGE_BASE}/conversation/{conversationId}/read")
    suspend fun markConversationAsRead(
        @Path("conversationId") conversationId: String
    ): Response<ApiResponse<Any>>
    
    /**
     * Delete a message
     */
    @DELETE("${Constants.Endpoints.MESSAGE_BASE}/{messageId}")
    suspend fun deleteMessage(
        @Path("messageId") messageId: String
    ): Response<ApiResponse<Any>>
    
    /**
     * Delete entire conversation
     */
    @DELETE("${Constants.Endpoints.MESSAGE_BASE}/conversation/{conversationId}")
    suspend fun deleteConversation(
        @Path("conversationId") conversationId: String
    ): Response<ApiResponse<Any>>
    
    /**
     * Send typing indicator
     */
    @POST("${Constants.Endpoints.MESSAGE_BASE}/typing")
    suspend fun sendTypingIndicator(
        @Body conversationId: Map<String, String>
    ): Response<ApiResponse<Any>>
}
