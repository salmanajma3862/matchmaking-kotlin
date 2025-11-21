package com.example.dummyapp.data.api

import com.example.dummyapp.data.models.request.SwipeRequest
import com.example.dummyapp.data.models.request.SendInterestRequest
import com.example.dummyapp.data.models.response.*
import com.example.dummyapp.utils.Constants
import retrofit2.Response
import retrofit2.http.*

/**
 * Match API Service
 * Handles matching, swiping, and discovery functionality
 */
interface MatchApiService {
    
    /**
     * Get list of matches
     */
    @GET(Constants.Endpoints.GET_MATCHES)
    suspend fun getMatches(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = Constants.Pagination.MATCHES_PAGE_SIZE
    ): Response<MatchesResponse>
    
    /**
     * Get profiles to discover/swipe
     */
    @GET("${Constants.Endpoints.MATCH_BASE}/discover")
    suspend fun getDiscoverProfiles(
        @Query("limit") limit: Int = 10
    ): Response<DiscoverResponse>
    
    /**
     * Swipe on a profile (like, dislike, super_like)
     */
    @POST(Constants.Endpoints.SWIPE)
    suspend fun swipe(
        @Body request: SwipeRequest
    ): Response<SwipeResponse>
    
    /**
     * Send interest to a user
     */
    @POST(Constants.Endpoints.SEND_INTEREST)
    suspend fun sendInterest(
        @Body request: SendInterestRequest
    ): Response<ApiResponse<Any>>
    
    /**
     * Unmatch with a user
     */
    @DELETE("${Constants.Endpoints.MATCH_BASE}/{matchId}")
    suspend fun unmatch(
        @Path("matchId") matchId: String
    ): Response<ApiResponse<Any>>
    
    /**
     * Get match details
     */
    @GET("${Constants.Endpoints.MATCH_BASE}/{matchId}")
    suspend fun getMatchDetails(
        @Path("matchId") matchId: String
    ): Response<ApiResponse<com.example.dummyapp.data.models.Match>>
    
    /**
     * Get users who liked me
     */
    @GET("${Constants.Endpoints.MATCH_BASE}/likes/received")
    suspend fun getReceivedLikes(): Response<ApiResponse<List<com.example.dummyapp.data.models.User>>>
    
    /**
     * Get my swipe history
     */
    @GET("${Constants.Endpoints.MATCH_BASE}/swipes/history")
    suspend fun getSwipeHistory(
        @Query("page") page: Int = 1
    ): Response<ApiResponse<List<com.example.dummyapp.data.models.Swipe>>>
}
