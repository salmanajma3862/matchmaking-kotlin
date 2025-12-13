package com.salmanajmal.ziya.data.api

import com.salmanajmal.ziya.data.models.response.ApiResponse
import com.salmanajmal.ziya.data.models.Subscription
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SubscriptionApiService {
    @POST("api/subscription/verify")
    suspend fun verifyPurchase(@Body request: VerifyPurchaseRequest): Response<ApiResponse<Subscription>>
}

data class VerifyPurchaseRequest(
    val purchaseToken: String,
    val productId: String,
    val orderId: String
)
