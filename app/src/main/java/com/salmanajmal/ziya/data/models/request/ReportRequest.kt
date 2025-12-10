package com.salmanajmal.ziya.data.models.request

data class ReportRequest(
    val reportedUserId: String,
    val reason: String,
    val description: String
)
