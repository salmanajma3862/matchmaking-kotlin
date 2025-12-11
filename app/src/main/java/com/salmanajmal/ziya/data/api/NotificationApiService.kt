package com.salmanajmal.ziya.data.api

import com.salmanajmal.ziya.data.models.response.*
import com.salmanajmal.ziya.utils.Constants
import retrofit2.Response
import retrofit2.http.*

/**
 * Notification API Service
 * Handles push notifications and in-app notifications
 */
interface NotificationApiService {
    
    /**
     * Get all notifications
     */
    @GET(Constants.Endpoints.GET_NOTIFICATIONS)
    suspend fun getNotifications(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = Constants.Pagination.NOTIFICATIONS_PAGE_SIZE
    ): Response<NotificationsResponse>
    
    /**
     * Mark notification as read
     */
    @PUT(Constants.Endpoints.MARK_READ)
    suspend fun markAsRead(
        @Path("id") notificationId: String
    ): Response<ApiResponse<Any>>
    
    /**
     * Mark all notifications as read
     */
    @PUT("${Constants.Endpoints.NOTIFICATION_BASE}/read-all")
    suspend fun markAllAsRead(): Response<ApiResponse<Any>>
    
    /**
     * Delete a notification
     */
    @DELETE("${Constants.Endpoints.NOTIFICATION_BASE}/{id}")
    suspend fun deleteNotification(
        @Path("id") notificationId: String
    ): Response<ApiResponse<Any>>
    
    /**
     * Delete all notifications
     */
    @DELETE("${Constants.Endpoints.NOTIFICATION_BASE}/all")
    suspend fun deleteAllNotifications(): Response<ApiResponse<Any>>
    
    /**
     * Register device for push notifications (FCM token)
     */
    @POST("/api/notifications/register")
    suspend fun registerPushToken(
        @Body body: RegisterPushTokenRequest
    ): Response<ApiResponse<Any>>
    
    /**
     * Unregister device from push notifications
     */
    @DELETE("/api/notifications/unregister")
    suspend fun unregisterPushToken(): Response<ApiResponse<Any>>
}

/**
 * Request body for registering FCM push token
 */
data class RegisterPushTokenRequest(
    val fcmToken: String
)

