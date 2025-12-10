package com.salmanajmal.ziya.service

import android.util.Log
import com.salmanajmal.ziya.data.models.Message
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SocketManager @Inject constructor() {

    private var socket: Socket? = null
    private val gson = Gson()
    private val TAG = "SocketManager"

    // Replace with your actual backend URL
    private val BASE_URL = "http://10.0.2.2:5000" 

    fun connect(userId: String) {
        if (socket?.connected() == true) return

        try {
            val options = IO.Options().apply {
                query = "userId=$userId"
                transports = arrayOf("websocket")
            }
            socket = IO.socket(BASE_URL, options)
            
            socket?.on(Socket.EVENT_CONNECT) {
                Log.d(TAG, "Connected to socket")
            }
            
            socket?.on(Socket.EVENT_DISCONNECT) {
                Log.d(TAG, "Disconnected from socket")
            }
            
            socket?.on(Socket.EVENT_CONNECT_ERROR) { args ->
                Log.e(TAG, "Connection error: ${args[0]}")
            }

            socket?.connect()
        } catch (e: Exception) {
            Log.e(TAG, "Error connecting to socket", e)
        }
    }

    fun disconnect() {
        socket?.disconnect()
        socket?.off()
        socket = null
    }

    fun joinConversation(conversationId: String) {
        socket?.emit("join_conversation", JSONObject(mapOf("conversationId" to conversationId)))
    }

    fun leaveConversation(conversationId: String) {
        socket?.emit("leave_conversation", JSONObject(mapOf("conversationId" to conversationId)))
    }

    fun startTyping(conversationId: String) {
        socket?.emit("typing_start", JSONObject(mapOf("conversationId" to conversationId)))
    }

    fun stopTyping(conversationId: String) {
        socket?.emit("typing_stop", JSONObject(mapOf("conversationId" to conversationId)))
    }

    // Flow for new messages
    fun observeNewMessages(): Flow<Message> = callbackFlow {
        val listener = { args: Array<Any> ->
            try {
                val data = args[0] as JSONObject
                val message = gson.fromJson(data.toString(), Message::class.java)
                trySend(message)
            } catch (e: Exception) {
                Log.e(TAG, "Error parsing new message", e)
            }
            Unit
        }
        
        socket?.on("new_message", listener)
        
        awaitClose {
            socket?.off("new_message", listener)
        }
    }

    // Flow for typing status
    fun observeTyping(): Flow<Pair<String, String>> = callbackFlow {
        val startListener = { args: Array<Any> ->
            val data = args[0] as JSONObject
            val conversationId = data.getString("conversationId")
            val userId = data.getString("userId")
            trySend(conversationId to userId) // Typing started
            Unit
        }

        val stopListener = { args: Array<Any> ->
            // You might want a different event or data structure to signal stop
            // For now, let's just assume we handle it in UI logic or separate flow
            Unit
        }

        socket?.on("typing_start", startListener)
        
        awaitClose {
            socket?.off("typing_start", startListener)
        }
    }
    
    // Flow for message updates (edit/delete)
    fun observeMessageUpdates(): Flow<Message> = callbackFlow {
        val listener = { args: Array<Any> ->
            try {
                val data = args[0] as JSONObject
                val message = gson.fromJson(data.toString(), Message::class.java)
                trySend(message)
            } catch (e: Exception) {
                Log.e(TAG, "Error parsing updated message", e)
            }
            Unit
        }
        socket?.on("message_updated", listener)
        awaitClose { socket?.off("message_updated", listener) }
    }

    data class MessageDeletionEvent(
        val messageId: String,
        val isDeletedForEveryone: Boolean,
        val text: String? = null
    )

    fun observeMessageDeletions(): Flow<MessageDeletionEvent> = callbackFlow {
        val listener = { args: Array<Any> ->
            try {
                val data = args[0] as JSONObject
                val messageId = data.getString("messageId")
                val isDeletedForEveryone = data.optBoolean("deleteForEveryone", false)
                val text = data.optString("text", null)
                trySend(MessageDeletionEvent(messageId, isDeletedForEveryone, text))
            } catch (e: Exception) {
                Log.e(TAG, "Error parsing deleted message", e)
            }
            Unit
        }
        socket?.on("message_deleted", listener)
        awaitClose { socket?.off("message_deleted", listener) }
    }
}
