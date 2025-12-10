package com.salmanajmal.ziya.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salmanajmal.ziya.data.models.User
import com.salmanajmal.ziya.data.repository.ChatRepository
import com.salmanajmal.ziya.data.repository.SwipeRepository
import com.salmanajmal.ziya.data.repository.UserRepository
import com.salmanajmal.ziya.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileDetailViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val swipeRepository: SwipeRepository,
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _userState = MutableStateFlow<NetworkResult<User>>(NetworkResult.Loading())
    val userState: StateFlow<NetworkResult<User>> = _userState.asStateFlow()

    private val _actionState = MutableStateFlow<NetworkResult<Unit>?>(null)
    val actionState: StateFlow<NetworkResult<Unit>?> = _actionState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<String>()
    val navigationEvent: SharedFlow<String> = _navigationEvent.asSharedFlow()

    fun fetchUserProfile(userId: String) {
        viewModelScope.launch {
            userRepository.getUserProfile(userId).collect { result ->
                _userState.value = result
            }
        }
    }

    fun undoSwipe(targetUserId: String) {
        viewModelScope.launch {
            swipeRepository.undoSwipe(targetUserId).collect { result ->
                _actionState.value = result
            }
        }
    }

    fun unmatchUser(targetUserId: String) {
        viewModelScope.launch {
            swipeRepository.unmatchUser(targetUserId).collect { result ->
                _actionState.value = result
            }
        }
    }

    fun acceptMatch(targetUserId: String) {
        viewModelScope.launch {
            // Accepting is essentially swiping "like"
            swipeRepository.recordSwipe(targetUserId, "like").collect { result ->
                if (result is NetworkResult.Success) {
                    _actionState.value = NetworkResult.Success(Unit)
                } else if (result is NetworkResult.Error) {
                    _actionState.value = NetworkResult.Error(result.message ?: "Unknown error")
                } else {
                    _actionState.value = NetworkResult.Loading()
                }
            }
        }
    }

    fun rejectMatch(targetUserId: String) {
        viewModelScope.launch {
            // Rejecting is swiping "dislike"
            swipeRepository.recordSwipe(targetUserId, "dislike").collect { result ->
                if (result is NetworkResult.Success) {
                    _actionState.value = NetworkResult.Success(Unit)
                } else if (result is NetworkResult.Error) {
                    _actionState.value = NetworkResult.Error(result.message ?: "Unknown error")
                } else {
                    _actionState.value = NetworkResult.Loading()
                }
            }
        }
    }

    fun resetActionState() {
        _actionState.value = null
    }

    fun initiateMessage(targetUserId: String) {
        viewModelScope.launch {
            val result = chatRepository.createConversation(targetUserId)
            result.onSuccess { conversation ->
                _navigationEvent.emit(conversation.id)
            }.onFailure { error ->
                _actionState.value = NetworkResult.Error(error.message ?: "Failed to start chat")
            }
        }
    }
}
