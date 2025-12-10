package com.salmanajmal.ziya.data.repository

import com.salmanajmal.ziya.data.api.SwipeApiService
import com.salmanajmal.ziya.data.models.request.SwipeRequest
import com.salmanajmal.ziya.data.models.response.MatchItem
import com.salmanajmal.ziya.data.models.response.ReceivedSwipeItem
import com.salmanajmal.ziya.data.models.response.SentSwipeItem
import com.salmanajmal.ziya.data.models.response.SwipeResponseData
import com.salmanajmal.ziya.utils.Constants
import com.salmanajmal.ziya.utils.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class SwipeRepository @Inject constructor(
    private val swipeApiService: SwipeApiService
) {

    /**
     * Record a swipe (like, dislike, superlike)
     */
    fun recordSwipe(targetUserId: String, action: String): Flow<NetworkResult<SwipeResponseData>> = flow {
        emit(NetworkResult.Loading())

        try {
            val request = SwipeRequest(targetUserId, action)
            val response = swipeApiService.recordSwipe(request)

            if (response.isSuccessful && response.body() != null) {
                val apiResponse = response.body()!!
                if (apiResponse.success && apiResponse.data != null) {
                    emit(NetworkResult.Success(apiResponse.data))
                } else {
                    emit(NetworkResult.Error(apiResponse.message ?: Constants.ErrorMessages.UNKNOWN_ERROR))
                }
            } else {
                // Parse error body if possible
                emit(NetworkResult.Error(response.message() ?: Constants.ErrorMessages.UNKNOWN_ERROR))
            }
        } catch (e: HttpException) {
            emit(NetworkResult.Error(e.message ?: Constants.ErrorMessages.NETWORK_ERROR))
        } catch (e: IOException) {
            emit(NetworkResult.Error(Constants.ErrorMessages.NETWORK_ERROR))
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: Constants.ErrorMessages.UNKNOWN_ERROR))
        }
    }

    /**
     * Get matches list
     */
    fun getMatches(page: Int = 1, limit: Int = 20): Flow<NetworkResult<List<MatchItem>>> = flow {
        emit(NetworkResult.Loading())

        try {
            val response = swipeApiService.getMatches(page, limit)

            if (response.isSuccessful && response.body() != null) {
                val apiResponse = response.body()!!
                if (apiResponse.success && apiResponse.data != null) {
                    emit(NetworkResult.Success(apiResponse.data))
                } else {
                    emit(NetworkResult.Error(apiResponse.message ?: Constants.ErrorMessages.UNKNOWN_ERROR))
                }
            } else {
                emit(NetworkResult.Error(response.message() ?: Constants.ErrorMessages.UNKNOWN_ERROR))
            }
        } catch (e: HttpException) {
            emit(NetworkResult.Error(e.message ?: Constants.ErrorMessages.NETWORK_ERROR))
        } catch (e: IOException) {
            emit(NetworkResult.Error(Constants.ErrorMessages.NETWORK_ERROR))
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: Constants.ErrorMessages.UNKNOWN_ERROR))
        }
    }

    /**
     * Get sent swipes
     */
    fun getSentSwipes(page: Int = 1, limit: Int = 20): Flow<NetworkResult<List<SentSwipeItem>>> = flow {
        emit(NetworkResult.Loading())
        try {
            val response = swipeApiService.getSentSwipes(page, limit)
            if (response.isSuccessful && response.body() != null) {
                val apiResponse = response.body()!!
                if (apiResponse.success && apiResponse.data != null) {
                    emit(NetworkResult.Success(apiResponse.data))
                } else {
                    emit(NetworkResult.Error(apiResponse.message ?: Constants.ErrorMessages.UNKNOWN_ERROR))
                }
            } else {
                emit(NetworkResult.Error(response.message() ?: Constants.ErrorMessages.UNKNOWN_ERROR))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: Constants.ErrorMessages.UNKNOWN_ERROR))
        }
    }

    /**
     * Get received swipes
     */
    fun getReceivedSwipes(page: Int = 1, limit: Int = 20): Flow<NetworkResult<List<ReceivedSwipeItem>>> = flow {
        emit(NetworkResult.Loading())
        try {
            val response = swipeApiService.getReceivedSwipes(page, limit)
            if (response.isSuccessful && response.body() != null) {
                val apiResponse = response.body()!!
                if (apiResponse.success && apiResponse.data != null) {
                    emit(NetworkResult.Success(apiResponse.data))
                } else {
                    emit(NetworkResult.Error(apiResponse.message ?: Constants.ErrorMessages.UNKNOWN_ERROR))
                }
            } else {
                emit(NetworkResult.Error(response.message() ?: Constants.ErrorMessages.UNKNOWN_ERROR))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: Constants.ErrorMessages.UNKNOWN_ERROR))
        }
    }

    /**
     * Undo a swipe
     */
    fun undoSwipe(targetUserId: String): Flow<NetworkResult<Unit>> = flow {
        emit(NetworkResult.Loading())
        try {
            val request = mapOf("targetUserId" to targetUserId)
            val response = swipeApiService.undoSwipe(request)
            if (response.isSuccessful && response.body() != null) {
                val apiResponse = response.body()!!
                if (apiResponse.success) {
                    emit(NetworkResult.Success(Unit))
                } else {
                    emit(NetworkResult.Error(apiResponse.message ?: Constants.ErrorMessages.UNKNOWN_ERROR))
                }
            } else {
                emit(NetworkResult.Error(response.message() ?: Constants.ErrorMessages.UNKNOWN_ERROR))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: Constants.ErrorMessages.UNKNOWN_ERROR))
        }
    }

    /**
     * Unmatch a user
     */
    fun unmatchUser(targetUserId: String): Flow<NetworkResult<Unit>> = flow {
        emit(NetworkResult.Loading())
        try {
            val request = mapOf("targetUserId" to targetUserId)
            val response = swipeApiService.unmatchUser(request)
            if (response.isSuccessful && response.body() != null) {
                val apiResponse = response.body()!!
                if (apiResponse.success) {
                    emit(NetworkResult.Success(Unit))
                } else {
                    emit(NetworkResult.Error(apiResponse.message ?: Constants.ErrorMessages.UNKNOWN_ERROR))
                }
            } else {
                emit(NetworkResult.Error(response.message() ?: Constants.ErrorMessages.UNKNOWN_ERROR))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: Constants.ErrorMessages.UNKNOWN_ERROR))
        }
    }
}
