package com.salmanajmal.ziya.data.api

import com.salmanajmal.ziya.data.models.request.*
import com.salmanajmal.ziya.data.models.response.*
import com.salmanajmal.ziya.utils.Constants
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

/**
 * User API Service
 * Similar to userAPI.ts in React Native
 * Defines all user profile-related endpoints
 */
interface UserApiService {
    
    /**
     * Complete user profile after email verification
     * Includes photo upload
     */
    @Multipart
    @POST(Constants.Endpoints.COMPLETE_PROFILE)
    suspend fun completeProfile(
        @Part photos: List<MultipartBody.Part>,
        @Part("name") name: RequestBody? = null,
        @Part("phone") phone: RequestBody? = null,
        @Part("gender") gender: RequestBody? = null,
        @Part("dob") dob: RequestBody? = null,
        @Part("bio") bio: RequestBody? = null,
        @Part("height") height: RequestBody? = null,
        @Part("weight") weight: RequestBody? = null,
        @Part("bodyType") bodyType: RequestBody? = null,
        @Part("city") city: RequestBody? = null,
        @Part("country") country: RequestBody? = null,
        @Part("address") address: RequestBody? = null,
        @Part("religion") religion: RequestBody? = null,
        @Part("sect") sect: RequestBody? = null,
        @Part("maritalStatus") maritalStatus: RequestBody? = null,
        @Part("education") education: RequestBody? = null,
        @Part("profession") profession: RequestBody? = null,
        @Part("incomeRange") incomeRange: RequestBody? = null,
        @Part("interests") interests: RequestBody? = null,
        @Part("hobbies") hobbies: RequestBody? = null,
        @Part("smoking") smoking: RequestBody? = null,
        @Part("drinking") drinking: RequestBody? = null,
        @Part("dietPreference") dietPreference: RequestBody? = null,
        @Part("familyBackground") familyBackground: RequestBody? = null,
        @Part("numberOfSiblings") numberOfSiblings: RequestBody? = null,
        @Part("livingWithFamily") livingWithFamily: RequestBody? = null
    ): Response<AuthResponse>

    /**
     * Get recommended users feed with optional filters
     */
    @GET(Constants.Endpoints.GET_FEED)
    suspend fun getFeed(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10,
        @Query("minAge") minAge: Int? = null,
        @Query("maxAge") maxAge: Int? = null,
        @Query("city") city: String? = null,
        @Query("religion") religion: String? = null,
        @Query("maritalStatus") maritalStatus: String? = null,
        @Query("education") education: String? = null,
        @Query("minHeight") minHeight: Int? = null,
        @Query("maxHeight") maxHeight: Int? = null,
        @Query("smoking") smoking: Boolean? = null,
        @Query("drinking") drinking: Boolean? = null
    ): Response<ApiResponse<List<com.salmanajmal.ziya.data.models.User>>>

    /**
     * Get user profile by ID
     */
    @GET(Constants.Endpoints.GET_PROFILE)
    suspend fun getUserProfile(
        @Path("userId") userId: String
    ): Response<UserProfileResponse>
    
    /**
     * Get users who viewed my profile (Premium Feature)
     */
    @GET(Constants.Endpoints.GET_PROFILE_VIEWERS)
    suspend fun getProfileViewers(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): Response<com.salmanajmal.ziya.data.models.ProfileViewersResponse>
    
    /**
     * Get profiles I have viewed (Premium Feature)
     */
    @GET(Constants.Endpoints.GET_VIEWED_PROFILES)
    suspend fun getViewedProfiles(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): Response<com.salmanajmal.ziya.data.models.ProfileViewersResponse>
    
    /**
     * Update user profile
     */
    @PUT(Constants.Endpoints.UPDATE_PROFILE)
    suspend fun updateProfile(
        @Body request: UpdateProfileRequest
    ): Response<UpdateProfileResponse>
    
    /**
     * Upload profile photos
     */
    @Multipart
    @POST("${Constants.Endpoints.USER_BASE}/photos")
    suspend fun uploadPhotos(
        @Part photos: List<MultipartBody.Part>
    ): Response<ApiResponse<List<String>>>
    
    /**
     * Delete profile photo
     */
    @DELETE("${Constants.Endpoints.USER_BASE}/photos/{photoId}")
    suspend fun deletePhoto(
        @Path("photoId") photoId: String
    ): Response<ApiResponse<Any>>
    
    /**
     * Set primary photo
     */
    @PUT("${Constants.Endpoints.USER_BASE}/photos/{photoId}/primary")
    suspend fun setPrimaryPhoto(
        @Path("photoId") photoId: String
    ): Response<ApiResponse<Any>>
    
    /**
     * Update user preferences
     */
    @PUT("${Constants.Endpoints.USER_BASE}/preferences")
    suspend fun updatePreferences(
        @Body request: UserPreferencesRequest
    ): Response<ApiResponse<Any>>
    
    /**
     * Delete user account (soft delete)
     */
    @DELETE(Constants.Endpoints.DELETE_ACCOUNT)
    suspend fun deleteAccount(): Response<ApiResponse<Any>>
    
    /**
     * Block a user
     */
    @POST("${Constants.Endpoints.USER_BASE}/block")
    suspend fun blockUser(
        @Body request: BlockUserRequest
    ): Response<ApiResponse<Any>>
    
    /**
     * Unblock a user
     */
    @DELETE("${Constants.Endpoints.USER_BASE}/block/{userId}")
    suspend fun unblockUser(
        @Path("userId") userId: String
    ): Response<ApiResponse<Any>>
    
    /**
     * Report a user
     */
    @POST("${Constants.Endpoints.USER_BASE}/report")
    suspend fun reportUser(
        @Body request: ReportUserRequest
    ): Response<ApiResponse<Any>>
    
    /**
     * Get blocked users list
     */
    @GET("${Constants.Endpoints.USER_BASE}/blocked")
    suspend fun getBlockedUsers(): Response<ApiResponse<List<com.salmanajmal.ziya.data.models.User>>>
    
    /**
     * Update notification settings
     */
    @PUT("${Constants.Endpoints.USER_BASE}/settings/notifications")
    suspend fun updateNotificationSettings(
        @Body request: NotificationSettingsRequest
    ): Response<ApiResponse<Any>>
    
    /**
     * Update privacy settings
     */
    @PUT("${Constants.Endpoints.USER_BASE}/settings/privacy")
    suspend fun updatePrivacySettings(
        @Body request: PrivacySettingsRequest
    ): Response<ApiResponse<Any>>
}
