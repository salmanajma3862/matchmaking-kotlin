package com.example.dummyapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dummyapp.data.models.User
import com.example.dummyapp.data.repository.UserRepository
import com.example.dummyapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileDetailViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _userState = MutableStateFlow<NetworkResult<User>>(NetworkResult.Loading())
    val userState: StateFlow<NetworkResult<User>> = _userState.asStateFlow()

    fun fetchUserProfile(userId: String) {
        viewModelScope.launch {
            userRepository.getUserProfile(userId).collect { result ->
                _userState.value = result
            }
        }
    }
}
