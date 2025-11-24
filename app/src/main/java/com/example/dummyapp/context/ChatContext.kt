package com.example.dummyapp.context

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import com.example.dummyapp.data.models.Conversation
import com.example.dummyapp.data.models.Message
import com.example.dummyapp.data.models.request.SendMessageRequest
import com.example.dummyapp.data.repository.ChatRepository
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
            chatRepository.observeMessageDeletions().collect { messageId ->
                handleMessageDeletion(messageId)
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
        _chatState.value = _chatState.value.copy(currentConversation = conversation, messages = emptyList())
        
        if (conversation != null) {
            chatRepository.joinConversation(conversationId)
            fetchMessages(conversationId)
            markAsRead(conversationId)
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

    fun sendMessage(conversationId: String, text: String, replyTo: String? = null) {
        scope.launch {
            val request = SendMessageRequest(
                conversationId = conversationId,
                text = text,
                replyTo = replyTo
            )
            // Optimistic update could be done here
            chatRepository.sendMessage(request)
        }
    }

    fun startTyping(conversationId: String) {
        chatRepository.startTyping(conversationId)
    }

    fun stopTyping(conversationId: String) {
        chatRepository.stopTyping(conversationId)
    }

    fun editMessage(messageId: String, newText: String) {
        scope.launch {
            chatRepository.editMessage(messageId, newText)
        }
    }

    fun deleteMessage(messageId: String, forEveryone: Boolean) {
        scope.launch {
            chatRepository.deleteMessage(messageId, forEveryone)
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
            currentMessages.add(message)
            _chatState.value = _chatState.value.copy(messages = currentMessages)
            markAsRead(message.conversationId)
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

    private fun handleMessageDeletion(messageId: String) {
        val currentMessages = _chatState.value.messages.filter { it.id != messageId }
        _chatState.value = _chatState.value.copy(messages = currentMessages)
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
