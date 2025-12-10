package com.salmanajmal.ziya.data.repository

import com.salmanajmal.ziya.data.api.MessageApiService
import com.salmanajmal.ziya.data.models.Conversation
import com.salmanajmal.ziya.data.models.Message
import com.salmanajmal.ziya.data.models.request.CreateConversationRequest
import com.salmanajmal.ziya.data.models.request.EditMessageRequest
import com.salmanajmal.ziya.data.models.request.MarkReadRequest
import com.salmanajmal.ziya.data.models.request.SendMessageRequest
import com.salmanajmal.ziya.service.SocketManager
import okhttp3.MultipartBody
import okhttp3.RequestBody
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor(
    private val apiService: MessageApiService,
    private val socketManager: SocketManager
) {

    // Socket Management
    fun connectSocket(userId: String) = socketManager.connect(userId)
    fun disconnectSocket() = socketManager.disconnect()
    fun joinConversation(conversationId: String) = socketManager.joinConversation(conversationId)
    fun leaveConversation(conversationId: String) = socketManager.leaveConversation(conversationId)
    fun startTyping(conversationId: String) = socketManager.startTyping(conversationId)
    fun stopTyping(conversationId: String) = socketManager.stopTyping(conversationId)

    // Socket Observations
    fun observeNewMessages(): Flow<Message> = socketManager.observeNewMessages()
    fun observeTyping(): Flow<Pair<String, String>> = socketManager.observeTyping()
    fun observeMessageUpdates(): Flow<Message> = socketManager.observeMessageUpdates()
    fun observeMessageDeletions(): Flow<SocketManager.MessageDeletionEvent> = socketManager.observeMessageDeletions()

    // API Calls
    suspend fun getConversations(): Result<List<Conversation>> {
        return try {
            val response = apiService.getConversations()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error fetching conversations: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMessages(conversationId: String, page: Int = 1): Result<List<Message>> {
        return try {
            val response = apiService.getMessages(conversationId, page)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error fetching messages: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun sendMessage(request: SendMessageRequest): Result<Message> {
        return try {
            val response = apiService.sendMessage(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error sending message: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun sendMediaMessage(
        mediaPart: MultipartBody.Part,
        conversationId: RequestBody,
        text: RequestBody,
        messageType: RequestBody
    ): Result<Message> {
        return try {
            // Reusing sendPhotoMessage API endpoint as it expects 'image' part which we construct manually
            val response = apiService.sendPhotoMessage(mediaPart, conversationId, text, messageType)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error sending media message: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createConversation(participantId: String): Result<Conversation> {
        return try {
            val response = apiService.createConversation(CreateConversationRequest(participantId))
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error creating conversation: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun editMessage(messageId: String, newText: String): Result<Message> {
        return try {
            val response = apiService.editMessage(messageId, EditMessageRequest(newText))
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error editing message: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteMessage(messageId: String, deleteForEveryone: Boolean): Result<Boolean> {
        return try {
            val response = apiService.deleteMessageWithBody(messageId, mapOf("deleteForEveryone" to deleteForEveryone))
            if (response.isSuccessful) {
                Result.success(true)
            } else {
                Result.failure(Exception("Error deleting message: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun markAsRead(conversationId: String): Result<Boolean> {
        return try {
            val response = apiService.markAsRead(MarkReadRequest(conversationId))
            if (response.isSuccessful) {
                Result.success(true)
            } else {
                Result.failure(Exception("Error marking as read: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun reportUser(reportedUserId: String, reason: String, description: String): Result<Boolean> {
        return try {
            val response = apiService.reportUser(
                com.salmanajmal.ziya.data.models.request.ReportRequest(
                    reportedUserId = reportedUserId,
                    reason = reason,
                    description = description
                )
            )
            if (response.isSuccessful) {
                Result.success(true)
            } else {
                Result.failure(Exception("Error reporting user: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
