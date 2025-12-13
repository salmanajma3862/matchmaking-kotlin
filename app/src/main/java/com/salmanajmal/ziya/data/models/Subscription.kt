package com.salmanajmal.ziya.data.models

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Subscription(
    @SerializedName("_id")
    val id: String,
    val userId: String,
    val planId: String,
    val platform: String,
    val purchaseToken: String,
    val orderId: String?,
    val startDate: Date,
    val expiryDate: Date,
    val isActive: Boolean,
    val autoRenew: Boolean,
    val paymentState: Int?
)
