package com.salmanajmal.ziya.data.api

import com.salmanajmal.ziya.data.models.FamilyChildData
import com.salmanajmal.ziya.data.models.InviteCode
import com.salmanajmal.ziya.data.models.request.*
import com.salmanajmal.ziya.data.models.response.*
import com.salmanajmal.ziya.utils.Constants
import retrofit2.Response
import retrofit2.http.*

/**
 * Auth API Service
 * Similar to authAPI.ts in React Native
 * Defines all authentication-related endpoints
 */
interface AuthApiService {
    
    /**
     * Sign up a new user
     */
    @POST(Constants.Endpoints.SIGNUP)
    suspend fun signup(
        @Body request: SignupRequest
    ): Response<AuthResponse>
    
    /**
     * Login with phone and password
     */
    @POST(Constants.Endpoints.LOGIN)
    suspend fun login(
        @Body request: LoginRequest
    ): Response<AuthResponse>
    
    /**
     * Verify email with code
     */
    @POST(Constants.Endpoints.VERIFY_EMAIL)
    suspend fun verifyEmail(
        @Body request: VerifyEmailRequest
    ): Response<VerifyEmailResponse>
    
    /**
     * Resend verification code
     */
    @POST(Constants.Endpoints.RESEND_VERIFICATION)
    suspend fun resendVerificationCode(
        @Body request: ResendVerificationRequest
    ): Response<ApiResponse<Any>>
    
    /**
     * Request password reset
     */
    @POST(Constants.Endpoints.FORGOT_PASSWORD)
    suspend fun forgotPassword(
        @Body request: ForgotPasswordRequest
    ): Response<ApiResponse<Any>>
    
    /**
     * Reset password with token
     */
    @POST(Constants.Endpoints.RESET_PASSWORD)
    suspend fun resetPassword(
        @Body request: ResetPasswordRequest
    ): Response<PasswordResetResponse>
    
    /**
     * Logout (requires authentication)
     */
    @POST(Constants.Endpoints.LOGOUT)
    suspend fun logout(): Response<ApiResponse<Any>>
    
    /**
     * Get current user info (requires authentication)
     */
    @GET(Constants.Endpoints.GET_CURRENT_USER)
    suspend fun getCurrentUser(): Response<UserProfileResponse>
    
    /**
     * Refresh access token
     */
    @POST("${Constants.Endpoints.AUTH_BASE}/refresh")
    suspend fun refreshToken(
        @Body refreshToken: Map<String, String>
    ): Response<AuthResponse>

    // ==================== Family Endpoints ====================

    @POST(Constants.Endpoints.FAMILY_SIGNUP)
    suspend fun signupFamily(
        @Body request: SignupFamilyRequest
    ): Response<AuthResponse>

    @POST(Constants.Endpoints.FAMILY_LINK)
    suspend fun linkFamily(
        @Body request: LinkFamilyRequest
    ): Response<ApiResponse<Any>>

    @POST(Constants.Endpoints.FAMILY_INVITE)
    suspend fun createInvite(
        @Body request: CreateInviteRequest
    ): Response<ApiResponse<Any>>

    @GET(Constants.Endpoints.FAMILY_MY_CODES)
    suspend fun getMyInviteCodes(): Response<ApiResponse<List<InviteCode>>>

    @GET(Constants.Endpoints.FAMILY_CHILD_DATA)
    suspend fun getChildData(): Response<ApiResponse<FamilyChildData>>

    @DELETE("${Constants.Endpoints.FAMILY_BASE}/invite/{codeId}")
    suspend fun deleteInviteCode(@Path("codeId") codeId: String): Response<ApiResponse<Any>>

    @PATCH("${Constants.Endpoints.FAMILY_BASE}/invite/{codeId}/deactivate")
    suspend fun deactivateInviteCode(@Path("codeId") codeId: String): Response<ApiResponse<Any>>
}
