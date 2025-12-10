package com.salmanajmal.ziya.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salmanajmal.ziya.data.models.response.MatchItem
import com.salmanajmal.ziya.data.models.response.ReceivedSwipeItem
import com.salmanajmal.ziya.data.models.response.SentSwipeItem
import com.salmanajmal.ziya.data.repository.SwipeRepository
import com.salmanajmal.ziya.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MatchViewModel @Inject constructor(
    private val swipeRepository: SwipeRepository
) : ViewModel() {

    private val _matches = MutableStateFlow<NetworkResult<List<MatchItem>>>(NetworkResult.Loading())
    val matches: StateFlow<NetworkResult<List<MatchItem>>> = _matches.asStateFlow()

    private val _sentSwipes = MutableStateFlow<NetworkResult<List<SentSwipeItem>>>(NetworkResult.Loading())
    val sentSwipes: StateFlow<NetworkResult<List<SentSwipeItem>>> = _sentSwipes.asStateFlow()

    private val _receivedSwipes = MutableStateFlow<NetworkResult<List<ReceivedSwipeItem>>>(NetworkResult.Loading())
    val receivedSwipes: StateFlow<NetworkResult<List<ReceivedSwipeItem>>> = _receivedSwipes.asStateFlow()

    init {
        fetchMatches()
        fetchSentSwipes()
        fetchReceivedSwipes()
    }

    fun fetchMatches() {
        viewModelScope.launch {
            swipeRepository.getMatches().collect {
                _matches.value = it
            }
        }
    }

    fun fetchSentSwipes() {
        viewModelScope.launch {
            swipeRepository.getSentSwipes().collect {
                _sentSwipes.value = it
            }
        }
    }

    fun fetchReceivedSwipes() {
        viewModelScope.launch {
            swipeRepository.getReceivedSwipes().collect {
                _receivedSwipes.value = it
            }
        }
    }
}
