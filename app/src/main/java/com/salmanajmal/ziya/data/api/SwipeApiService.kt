package com.salmanajmal.ziya.data.api

import com.salmanajmal.ziya.data.models.request.SwipeRequest
import com.salmanajmal.ziya.data.models.response.ApiResponse
import com.salmanajmal.ziya.data.models.response.MatchItem
import com.salmanajmal.ziya.data.models.response.ReceivedSwipeItem
import com.salmanajmal.ziya.data.models.response.SentSwipeItem
import com.salmanajmal.ziya.data.models.response.SwipeResponseData
import com.salmanajmal.ziya.utils.Constants
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
