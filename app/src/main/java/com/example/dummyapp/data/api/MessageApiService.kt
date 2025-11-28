package com.example.dummyapp.data.api

import com.example.dummyapp.data.models.Conversation
import com.example.dummyapp.data.models.Message
import com.example.dummyapp.data.models.request.CreateConversationRequest
import com.example.dummyapp.data.models.request.EditMessageRequest
import com.example.dummyapp.data.models.request.MarkReadRequest
import com.example.dummyapp.data.models.request.SendMessageRequest
import com.example.dummyapp.data.models.response.ApiResponse
import com.example.dummyapp.utils.Constants
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface MessageApiService {

    @GET("api/chat/conversations")
    suspend fun getConversations(): Response<List<Conversation>>

    @GET("api/chat/messages/{conversationId}")
    suspend fun getMessages(
        @Path("conversationId") conversationId: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): Response<List<Message>>

    @POST("api/chat/send")
    suspend fun sendMessage(
        @Body request: SendMessageRequest
    ): Response<Message>

    @Multipart
    @POST("api/chat/send")
    suspend fun sendPhotoMessage(
        @Part image: MultipartBody.Part,
        @Part("conversationId") conversationId: RequestBody,
        @Part("text") text: RequestBody,
        @Part("messageType") messageType: RequestBody
    ): Response<Message>

    @POST("api/chat/conversation")
    suspend fun createConversation(
        @Body request: CreateConversationRequest
    ): Response<Conversation>

    @PUT("api/chat/edit/{messageId}")
    suspend fun editMessage(
        @Path("messageId") messageId: String,
        @Body request: EditMessageRequest
    ): Response<Message>

    @DELETE("api/chat/delete/{messageId}")
    suspend fun deleteMessage(
        @Path("messageId") messageId: String,
        @Body deleteForEveryone: Boolean // You might need a wrapper object if body is JSON
    ): Response<Any>
    
    // For delete with body, use HTTP generic or custom annotation if needed. 
    // Retrofit @DELETE doesn't support body by default. 
    // Use @HTTP(method = "DELETE", path = "...", hasBody = true)
    @HTTP(method = "DELETE", path = "api/chat/delete/{messageId}", hasBody = true)
    suspend fun deleteMessageWithBody(
        @Path("messageId") messageId: String,
        @Body body: Map<String, Boolean> // { "deleteForEveryone": true }
    ): Response<Any>

    @POST("api/chat/read")
    suspend fun markAsRead(
        @Body request: MarkReadRequest
    ): Response<Any>

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
