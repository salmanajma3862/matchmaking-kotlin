package com.example.dummyapp.data.models.request

data class ReportRequest(
    val reportedUserId: String,
    val reason: String,
    val description: String
)
