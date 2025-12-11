package com.salmanajmal.ziya.context

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import com.salmanajmal.ziya.data.models.Conversation
import com.salmanajmal.ziya.data.models.Message
import com.salmanajmal.ziya.data.models.request.SendMessageRequest
import com.salmanajmal.ziya.data.repository.ChatRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ChatState(
    val conversations: List<Conversation> = emptyList(),
    val currentConversation: Conversation? = null,
    val messages: List<Message> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val typingUsers: Map<String, String> = emptyMap() // conversationId -> userId
)

class ChatContextManager(
    private val chatRepository: ChatRepository,
    private val scope: CoroutineScope
) {
    private val _chatState = MutableStateFlow(ChatState())
    val chatState: StateFlow<ChatState> = _chatState.asStateFlow()

    init {
        // Observe socket events
        scope.launch {
            chatRepository.observeNewMessages().collect { message ->
                handleNewMessage(message)
            }
        }
        
        scope.launch {
            chatRepository.observeTyping().collect { (conversationId, userId) ->
                handleTyping(conversationId, userId)
            }
        }
        
        scope.launch {
            chatRepository.observeMessageUpdates().collect { message ->
                handleMessageUpdate(message)
            }
        }

        scope.launch {
            chatRepository.observeMessageDeletions().collect { event ->
                handleMessageDeletion(event)
            }
        }
    }

    fun connect(userId: String) {
        chatRepository.connectSocket(userId)
        fetchConversations()
    }

    fun disconnect() {
        chatRepository.disconnectSocket()
    }

    fun fetchConversations() {
        scope.launch {
            _chatState.value = _chatState.value.copy(isLoading = true)
            val result = chatRepository.getConversations()
            result.onSuccess { conversations ->
                _chatState.value = _chatState.value.copy(
                    conversations = conversations,
                    isLoading = false
                )
            }.onFailure { e ->
                _chatState.value = _chatState.value.copy(
                    error = e.message,
                    isLoading = false
                )
            }
        }
    }

    fun selectConversation(conversationId: String) {
        val conversation = _chatState.value.conversations.find { it.id == conversationId }
        
        if (conversation != null) {
            // Conversation found locally, use it directly
            _chatState.value = _chatState.value.copy(currentConversation = conversation, messages = emptyList())
            chatRepository.joinConversation(conversationId)
            fetchMessages(conversationId)
            markAsRead(conversationId)
        } else {
            // Conversation not in local list - fetch conversations first
            // This happens when navigating from notification with a fresh app start
            _chatState.value = _chatState.value.copy(currentConversation = null, messages = emptyList())
            chatRepository.joinConversation(conversationId)
            fetchMessages(conversationId)
            
            // Fetch conversations to get the full participant info
            scope.launch {
                val result = chatRepository.getConversations()
                result.onSuccess { conversations ->
                    _chatState.value = _chatState.value.copy(conversations = conversations)
                    
                    // Now find and set the current conversation
                    val foundConversation = conversations.find { it.id == conversationId }
                    if (foundConversation != null) {
                        _chatState.value = _chatState.value.copy(currentConversation = foundConversation)
                        markAsRead(conversationId)
                    }
                }
            }
        }
    }

    fun fetchMessages(conversationId: String) {
        scope.launch {
            _chatState.value = _chatState.value.copy(isLoading = true)
            val result = chatRepository.getMessages(conversationId)
            result.onSuccess { messages ->
                _chatState.value = _chatState.value.copy(
                    messages = messages.reversed(), // Assuming API returns newest first, we want oldest first for chat UI usually, or handle in UI
                    isLoading = false
                )
            }.onFailure {
                _chatState.value = _chatState.value.copy(isLoading = false)
            }
        }
    }

    fun sendMessage(conversationId: String, text: String, currentUser: com.salmanajmal.ziya.data.models.User, replyTo: String? = null) {
        scope.launch {
            // Optimistic Update
            val tempId = "temp_${System.currentTimeMillis()}"
            val tempMessage = Message(
                id = tempId,
                conversationId = conversationId,
                sender = currentUser,
                text = text,
                createdAt = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", java.util.Locale.US).apply { timeZone = java.util.TimeZone.getTimeZone("UTC") }.format(java.util.Date()),
                updatedAt = "",
                replyTo = _chatState.value.messages.find { it.id == replyTo }, // Try to find reply message locally
                isSending = true
            )
            handleNewMessage(tempMessage)

            val request = SendMessageRequest(
                conversationId = conversationId,
                text = text,
                replyTo = replyTo
            )
            
            val result = chatRepository.sendMessage(request)
            result.onSuccess { message ->
                // Remove temp message and add real message
                val currentMessages = _chatState.value.messages.filter { it.id != tempId }.toMutableList()
                // Check for duplicates (real message might have arrived via socket already)
                if (currentMessages.none { it.id == message.id }) {
                    currentMessages.add(0, message)
                }
                _chatState.value = _chatState.value.copy(messages = currentMessages)
                markAsRead(message.conversationId)
            }.onFailure {
                // Handle failure: remove temp message or mark as failed
                // For now, just remove it
                val currentMessages = _chatState.value.messages.filter { it.id != tempId }
                _chatState.value = _chatState.value.copy(messages = currentMessages)
            }
        }
    }

    fun sendPhotoMessage(conversationId: String, imageUri: android.net.Uri, context: android.content.Context) {
        scope.launch {
            try {
                val contentResolver = context.contentResolver
                val inputStream = contentResolver.openInputStream(imageUri)
                val type = contentResolver.getType(imageUri) ?: "image/jpeg"
                val file = java.io.File(context.cacheDir, "temp_image_${System.currentTimeMillis()}")
                
                inputStream?.use { input ->
                    file.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }

                val requestFile = okhttp3.RequestBody.create(type.toMediaTypeOrNull(), file)
                val body = okhttp3.MultipartBody.Part.createFormData("image", file.name, requestFile)
                val conversationIdBody = okhttp3.RequestBody.create("text/plain".toMediaTypeOrNull(), conversationId)
                val textBody = okhttp3.RequestBody.create("text/plain".toMediaTypeOrNull(), "")
                val messageTypeBody = okhttp3.RequestBody.create("text/plain".toMediaTypeOrNull(), "image")

                val result = chatRepository.sendMediaMessage(body, conversationIdBody, textBody, messageTypeBody)
                result.onSuccess { message ->
                    handleNewMessage(message)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle error
            }
        }
    }

    fun sendAudioMessage(conversationId: String, audioFile: java.io.File) {
        scope.launch {
            try {
                val type = "audio/mpeg" // Or determine from file extension
                val requestFile = okhttp3.RequestBody.create(type.toMediaTypeOrNull(), audioFile)
                // Backend expects 'image' field for file upload currently
                val body = okhttp3.MultipartBody.Part.createFormData("image", audioFile.name, requestFile)
                val conversationIdBody = okhttp3.RequestBody.create("text/plain".toMediaTypeOrNull(), conversationId)
                val textBody = okhttp3.RequestBody.create("text/plain".toMediaTypeOrNull(), "")
                val messageTypeBody = okhttp3.RequestBody.create("text/plain".toMediaTypeOrNull(), "audio")

                val result = chatRepository.sendMediaMessage(body, conversationIdBody, textBody, messageTypeBody)
                result.onSuccess { message ->
                    handleNewMessage(message)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle error
            }
        }
    }

    fun startTyping(conversationId: String) {
        chatRepository.startTyping(conversationId)
    }

    fun stopTyping(conversationId: String) {
        chatRepository.stopTyping(conversationId)
    }

    fun reportUser(reportedUserId: String, reason: String, description: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        scope.launch {
            val result = chatRepository.reportUser(reportedUserId, reason, description)
            result.onSuccess {
                onSuccess()
            }.onFailure { e ->
                onError(e.message ?: "Unknown error")
            }
        }
    }

    fun editMessage(messageId: String, newText: String) {
        scope.launch {
            chatRepository.editMessage(messageId, newText)
        }
    }

    fun deleteMessage(messageId: String, forEveryone: Boolean) {
        scope.launch {
            // Optimistic Update
            val originalMessages = _chatState.value.messages
            if (forEveryone) {
                val currentMessages = originalMessages.map { 
                    if (it.id == messageId) {
                        it.copy(
                            text = "message deleted for everyone",
                            isDeletedForEveryone = true,
                            media = null
                        )
                    } else it
                }
                _chatState.value = _chatState.value.copy(messages = currentMessages)
            } else {
                val currentMessages = originalMessages.filter { it.id != messageId }
                _chatState.value = _chatState.value.copy(messages = currentMessages)
            }

            val result = chatRepository.deleteMessage(messageId, forEveryone)
            result.onFailure {
                // Revert on failure
                _chatState.value = _chatState.value.copy(messages = originalMessages)
            }
        }
    }

    private fun markAsRead(conversationId: String) {
        scope.launch {
            chatRepository.markAsRead(conversationId)
            // Update local state unread count if needed
        }
    }

    private fun handleNewMessage(message: Message) {
        val currentMessages = _chatState.value.messages.toMutableList()
        
        // If message belongs to current conversation, add it
        if (message.conversationId == _chatState.value.currentConversation?.id) {
            // Check for duplicates
            if (currentMessages.none { it.id == message.id }) {
                // Prepend new messages (Newest -> Oldest)
                currentMessages.add(0, message)
                _chatState.value = _chatState.value.copy(messages = currentMessages)
                markAsRead(message.conversationId)
            }
        }

        // Update conversation list (last message)
        val updatedConversations = _chatState.value.conversations.map { conv ->
            if (conv.id == message.conversationId) {
                conv.copy(
                    lastMessage = message,
                    lastMessageText = message.text,
                    lastMessageAt = message.createdAt,
                    unreadCount = if (conv.id != _chatState.value.currentConversation?.id) {
                        // Increment unread count logic here if needed, or rely on fetchConversations refresh
                        conv.unreadCount // Simplified for now
                    } else conv.unreadCount
                )
            } else conv
        }.sortedByDescending { it.lastMessageAt } // Re-sort

        _chatState.value = _chatState.value.copy(conversations = updatedConversations)
    }
    
    private fun handleMessageUpdate(message: Message) {
        val currentMessages = _chatState.value.messages.map { 
            if (it.id == message.id) message else it 
        }
        _chatState.value = _chatState.value.copy(messages = currentMessages)
    }

    private fun handleMessageDeletion(event: com.salmanajmal.ziya.service.SocketManager.MessageDeletionEvent) {
        if (event.isDeletedForEveryone) {
            val currentMessages = _chatState.value.messages.map { 
                if (it.id == event.messageId) {
                    it.copy(
                        text = event.text ?: "message deleted for everyone",
                        isDeletedForEveryone = true,
                        media = null
                    )
                } else {
                    it
                }
            }
            _chatState.value = _chatState.value.copy(messages = currentMessages)
        } else {
            // Delete for me (or just removed from view)
            val currentMessages = _chatState.value.messages.filter { it.id != event.messageId }
            _chatState.value = _chatState.value.copy(messages = currentMessages)
        }
    }

    private fun handleTyping(conversationId: String, userId: String) {
        // Simple implementation: just show who is typing in current conversation
        // In a real app, you'd want to handle multiple typers and timeouts
        val currentTyping = _chatState.value.typingUsers.toMutableMap()
        currentTyping[conversationId] = userId
        _chatState.value = _chatState.value.copy(typingUsers = currentTyping)
        
        // Auto-clear typing status after a few seconds (since we don't have explicit stop event handling in this simple version)
        scope.launch {
            kotlinx.coroutines.delay(3000)
            val updatedTyping = _chatState.value.typingUsers.toMutableMap()
            updatedTyping.remove(conversationId)
            _chatState.value = _chatState.value.copy(typingUsers = updatedTyping)
        }
    }
}

val LocalChatContext = staticCompositionLocalOf<ChatContextManager> {
    error("No ChatContext provided")
}

@Composable
fun ChatProvider(
    chatRepository: ChatRepository,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    val chatContext = remember { ChatContextManager(chatRepository, scope) }

    CompositionLocalProvider(LocalChatContext provides chatContext) {
        content()
    }
}
