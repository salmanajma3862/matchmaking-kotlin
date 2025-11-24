package com.example.dummyapp.data.api

import com.example.dummyapp.data.models.request.SwipeRequest
import com.example.dummyapp.data.models.response.ApiResponse
import com.example.dummyapp.data.models.response.MatchItem
import com.example.dummyapp.data.models.response.ReceivedSwipeItem
import com.example.dummyapp.data.models.response.SentSwipeItem
import com.example.dummyapp.data.models.response.SwipeResponseData
import com.example.dummyapp.utils.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SwipeApiService {

    @POST(Constants.Endpoints.SWIPE_ACTION)
    suspend fun recordSwipe(
        @Body request: SwipeRequest
    ): Response<ApiResponse<SwipeResponseData>>

    @GET(Constants.Endpoints.GET_MATCHES)
    suspend fun getMatches(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): Response<ApiResponse<List<MatchItem>>>

    @GET(Constants.Endpoints.GET_SENT_SWIPES)
    suspend fun getSentSwipes(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): Response<ApiResponse<List<SentSwipeItem>>>

    @GET(Constants.Endpoints.GET_RECEIVED_SWIPES)
    suspend fun getReceivedSwipes(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): Response<ApiResponse<List<ReceivedSwipeItem>>>

    @POST(Constants.Endpoints.UNDO_SWIPE)
    suspend fun undoSwipe(
        @Body request: Map<String, String>
    ): Response<ApiResponse<Unit>>

    @POST(Constants.Endpoints.UNMATCH)
    suspend fun unmatchUser(
        @Body request: Map<String, String>
    ): Response<ApiResponse<Unit>>
}
