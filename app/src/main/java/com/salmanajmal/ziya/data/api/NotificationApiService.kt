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
     * Register device for push notifications
     */
    @POST("${Constants.Endpoints.NOTIFICATION_BASE}/device")
    suspend fun registerDevice(
        @Body deviceInfo: Map<String, String>
    ): Response<ApiResponse<Any>>
    
    /**
     * Unregister device
     */
    @DELETE("${Constants.Endpoints.NOTIFICATION_BASE}/device/{token}")
    suspend fun unregisterDevice(
        @Path("token") deviceToken: String
    ): Response<ApiResponse<Any>>
}
