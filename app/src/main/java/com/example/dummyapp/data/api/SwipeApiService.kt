package com.example.dummyapp.data.api

import com.example.dummyapp.data.models.request.SwipeRequest
import com.example.dummyapp.data.models.response.ApiResponse
import com.example.dummyapp.data.models.response.MatchItem
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
}
