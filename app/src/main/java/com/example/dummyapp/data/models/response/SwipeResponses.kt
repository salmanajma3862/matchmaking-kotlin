package com.example.dummyapp.data.models.response

import com.example.dummyapp.data.models.User
import com.google.gson.annotations.SerializedName

/**
 * Data returned when a swipe is recorded
 */
data class SwipeResponseData(
    val swipe: SwipeData,
    val isMatch: Boolean,
    val match: MatchData?
)

data class SwipeData(
    val _id: String,
    val swiper: String,
    val target: String,
    val action: String,
    val createdAt: String
)

data class MatchData(
    val _id: String,
    val users: List<String>,
    val matchType: String,
    val createdAt: String
)

/**
 * Data item for the matches list
 */
data class MatchItem(
    val matchId: String,
    val matchedAt: String,
    val user: User?,
    val lastMessage: String?,
    val unreadCount: Int
)

data class SentSwipeItem(
    val _id: String,
    val target: User?,
    val action: String,
    val createdAt: String
)

data class ReceivedSwipeItem(
    val _id: String,
    val swiper: User?,
    val action: String,
    val createdAt: String
)

