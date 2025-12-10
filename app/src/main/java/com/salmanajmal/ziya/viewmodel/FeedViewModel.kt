package com.salmanajmal.ziya.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salmanajmal.ziya.data.models.User
import com.salmanajmal.ziya.data.models.response.SwipeResponseData
import com.salmanajmal.ziya.data.repository.SwipeRepository
import com.salmanajmal.ziya.data.repository.UserRepository
import com.salmanajmal.ziya.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val swipeRepository: SwipeRepository
) : ViewModel() {

    // Feed State (List of users)
    private val _feedState = MutableStateFlow<NetworkResult<List<User>>?>(null)
    val feedState: StateFlow<NetworkResult<List<User>>?> = _feedState.asStateFlow()

    // Swipe State (Result of the last swipe action)
    private val _swipeState = MutableStateFlow<NetworkResult<SwipeResponseData>?>(null)
    val swipeState: StateFlow<NetworkResult<SwipeResponseData>?> = _swipeState.asStateFlow()

    // Current list of users to display
    private val _userList = MutableStateFlow<List<User>>(emptyList())
    val userList: StateFlow<List<User>> = _userList.asStateFlow()

    private var currentPage = 1
    private var isLastPage = false
    private var isLoading = false

    init {
        loadFeed()
    }

    fun loadFeed() {
        if (isLoading || isLastPage) return

        isLoading = true
        viewModelScope.launch {
            userRepository.getFeed(page = currentPage).collect { result ->
                _feedState.value = result
                
                if (result is NetworkResult.Success) {
                    val newUsers = result.data ?: emptyList()
                    if (newUsers.isEmpty()) {
                        isLastPage = true
                    } else {
                        val currentList = _userList.value.toMutableList()
                        currentList.addAll(newUsers)
                        _userList.value = currentList
                        currentPage++
                    }
                }
                isLoading = false
            }
        }
    }

    fun swipeUser(targetUserId: String, action: String) {
        viewModelScope.launch {
            swipeRepository.recordSwipe(targetUserId, action).collect { result ->
                _swipeState.value = result
                
                // If swipe was successful, remove user from the list locally if not already removed by UI
                if (result is NetworkResult.Success) {
                    removeUserFromList(targetUserId)
                }
            }
        }
    }

    fun removeUserFromList(userId: String) {
        val currentList = _userList.value.toMutableList()
        currentList.removeAll { it.id == userId }
        _userList.value = currentList

        // Load more if list is running low
        if (currentList.size < 3) {
            loadFeed()
        }
    }
    
    fun resetSwipeState() {
        _swipeState.value = null
    }

    fun refreshFeed() {
        currentPage = 1
        isLastPage = false
        _userList.value = emptyList()
        loadFeed()
    }
}
