package com.salmanajmal.ziya.service

import android.util.Log
import com.salmanajmal.ziya.data.models.Message
import com.salmanajmal.ziya.utils.Constants
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SocketManager @Inject constructor() {

    private var socket: Socket? = null
    private val gson = Gson()
    private val TAG = "SocketManager"

    // Use the same base URL as the API (remove trailing slash for socket.io)
    private val BASE_URL = Constants.BASE_URL.trimEnd('/')

    // SharedFlows for emitting events - these work even before socket connects
    private val _newMessages = MutableSharedFlow<Message>(extraBufferCapacity = 10)
    private val _typingStart = MutableSharedFlow<Pair<String, String>>(extraBufferCapacity = 10)
    private val _typingStop = MutableSharedFlow<Pair<String, String>>(extraBufferCapacity = 10)
    private val _messageUpdates = MutableSharedFlow<Message>(extraBufferCapacity = 10)
    private val _messageDeletions = MutableSharedFlow<MessageDeletionEvent>(extraBufferCapacity = 10)
    private val _userStatusChanges = MutableSharedFlow<UserStatusEvent>(extraBufferCapacity = 10)

    fun connect(userId: String) {
        Log.d(TAG, "🔌 connect() called for userId: $userId")
        
        if (socket?.connected() == true) {
            Log.d(TAG, "⚠️ Socket already connected, skipping connection")
            return
        }

        try {
            val options = IO.Options().apply {
                query = "userId=$userId"
                transports = arrayOf("websocket")
            }
            Log.d(TAG, "🔧 Creating socket with options: query=${"userId=$userId"}, transports=[websocket]")
            socket = IO.socket(BASE_URL, options)
            
            // Register all event listeners BEFORE connecting
            setupEventListeners()
            
            socket?.on(Socket.EVENT_CONNECT) {
                Log.d(TAG, "✅ Socket connected successfully! Socket ID: ${socket?.id()}")
            }
            
            socket?.on(Socket.EVENT_DISCONNECT) { args ->
                val reason = if (args.isNotEmpty()) args[0].toString() else "unknown"
                Log.d(TAG, "🔴 Socket disconnected. Reason: $reason")
            }
            
            socket?.on(Socket.EVENT_CONNECT_ERROR) { args ->
                val error = if (args.isNotEmpty()) args[0].toString() else "unknown error"
                Log.e(TAG, "❌ Socket connection error: $error")
            }

            socket?.connect()
            Log.d(TAG, "📡 Socket.connect() called, waiting for connection...")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Exception while connecting to socket", e)
        }
    }

    private fun setupEventListeners() {
        Log.d(TAG, "📝 Setting up socket event listeners")
        
        // New message listener
        socket?.on("new_message") { args ->
            try {
                val data = args[0] as JSONObject
                Log.d(TAG, "📥 Received new_message event: ${data.toString().take(200)}...")
                val message = gson.fromJson(data.toString(), Message::class.java)
                Log.d(TAG, "✅ Parsed message - ID: ${message.id}, Sender: ${message.sender.id}, Text: ${message.text?.take(50)}")
                _newMessages.tryEmit(message)
                Log.d(TAG, "📨 Message emitted to SharedFlow")
            } catch (e: Exception) {
                Log.e(TAG, "❌ Error parsing new message", e)
            }
        }
        Log.d(TAG, "✅ new_message listener registered")

        // Typing start listener
        socket?.on("typing_start") { args ->
            try {
                val data = args[0] as JSONObject
                val conversationId = data.getString("conversationId")
                val typingUserId = data.getString("userId")
                Log.d(TAG, "⌨️ Received typing_start - Conversation: $conversationId, User: $typingUserId")
                _typingStart.tryEmit(conversationId to typingUserId)
            } catch (e: Exception) {
                Log.e(TAG, "❌ Error parsing typing_start event", e)
            }
        }
        Log.d(TAG, "✅ typing_start listener registered")

        // Typing stop listener  
        socket?.on("typing_stop") { args ->
            try {
                val data = args[0] as JSONObject
                val conversationId = data.getString("conversationId")
                val typingUserId = data.getString("userId")
                Log.d(TAG, "⌨️ Received typing_stop - Conversation: $conversationId, User: $typingUserId")
                _typingStop.tryEmit(conversationId to typingUserId)
            } catch (e: Exception) {
                Log.e(TAG, "❌ Error parsing typing_stop event", e)
            }
        }
        Log.d(TAG, "✅ typing_stop listener registered")

        // Message updated listener
        socket?.on("message_updated") { args ->
            try {
                val data = args[0] as JSONObject
                Log.d(TAG, "📥 Received message_updated event: ${data.toString().take(200)}...")
                val message = gson.fromJson(data.toString(), Message::class.java)
                Log.d(TAG, "✅ Parsed updated message - ID: ${message.id}")
                _messageUpdates.tryEmit(message)
            } catch (e: Exception) {
                Log.e(TAG, "❌ Error parsing updated message", e)
            }
        }
        Log.d(TAG, "✅ message_updated listener registered")

        // Message deleted listener
        socket?.on("message_deleted") { args ->
            try {
                val data = args[0] as JSONObject
                val messageId = data.getString("messageId")
                val isDeletedForEveryone = data.optBoolean("deleteForEveryone", false)
                val text = data.optString("text", null)
                Log.d(TAG, "📥 Received message_deleted event - ID: $messageId, ForEveryone: $isDeletedForEveryone")
                _messageDeletions.tryEmit(MessageDeletionEvent(messageId, isDeletedForEveryone, text))
            } catch (e: Exception) {
                Log.e(TAG, "❌ Error parsing deleted message", e)
            }
        }
        Log.d(TAG, "✅ message_deleted listener registered")

        // User status change listener - for real-time online/offline updates
        socket?.on("user_status_change") { args ->
            try {
                val data = args[0] as JSONObject
                val userId = data.getString("userId")
                val isOnline = data.getBoolean("isOnline")
                val lastSeen = data.optString("lastSeen", null)
                Log.d(TAG, "👤 Received user_status_change - User: $userId, Online: $isOnline, LastSeen: $lastSeen")
                _userStatusChanges.tryEmit(UserStatusEvent(userId, isOnline, lastSeen))
            } catch (e: Exception) {
                Log.e(TAG, "❌ Error parsing user_status_change event", e)
            }
        }
        Log.d(TAG, "✅ user_status_change listener registered")

        Log.d(TAG, "✅ All event listeners set up successfully")
    }

    fun disconnect() {
        Log.d(TAG, "🔌 disconnect() called")
        socket?.disconnect()
        socket?.off()
        socket = null
        Log.d(TAG, "✅ Socket disconnected and cleaned up")
    }

    fun joinConversation(conversationId: String) {
        Log.d(TAG, "💬 joinConversation() - Joining room: $conversationId")
        socket?.emit("join_conversation", JSONObject(mapOf("conversationId" to conversationId)))
        Log.d(TAG, "📤 Emitted join_conversation event for: $conversationId")
    }

    fun leaveConversation(conversationId: String) {
        Log.d(TAG, "🚪 leaveConversation() - Leaving room: $conversationId")
        socket?.emit("leave_conversation", JSONObject(mapOf("conversationId" to conversationId)))
        Log.d(TAG, "📤 Emitted leave_conversation event for: $conversationId")
    }

    fun startTyping(conversationId: String) {
        Log.d(TAG, "⌨️ startTyping() - Emitting typing_start for: $conversationId")
        socket?.emit("typing_start", JSONObject(mapOf("conversationId" to conversationId)))
    }

    fun stopTyping(conversationId: String) {
        Log.d(TAG, "⌨️ stopTyping() - Emitting typing_stop for: $conversationId")
        socket?.emit("typing_stop", JSONObject(mapOf("conversationId" to conversationId)))
    }

    // Expose flows for observing events
    fun observeNewMessages(): Flow<Message> = _newMessages.asSharedFlow()
    
    fun observeTyping(): Flow<Pair<String, String>> = _typingStart.asSharedFlow()
    
    fun observeTypingStop(): Flow<Pair<String, String>> = _typingStop.asSharedFlow()
    
    fun observeMessageUpdates(): Flow<Message> = _messageUpdates.asSharedFlow()
    
    fun observeMessageDeletions(): Flow<MessageDeletionEvent> = _messageDeletions.asSharedFlow()
    
    fun observeUserStatusChanges(): Flow<UserStatusEvent> = _userStatusChanges.asSharedFlow()

    data class MessageDeletionEvent(
        val messageId: String,
        val isDeletedForEveryone: Boolean,
        val text: String? = null
    )

    data class UserStatusEvent(
        val userId: String,
        val isOnline: Boolean,
        val lastSeen: String? = null
    )
}


