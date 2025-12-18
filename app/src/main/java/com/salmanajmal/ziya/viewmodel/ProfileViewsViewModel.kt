package com.salmanajmal.ziya.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salmanajmal.ziya.data.models.ProfileViewerEntry
import com.salmanajmal.ziya.data.models.ProfileViewersResponse
import com.salmanajmal.ziya.data.repository.UserRepository
import com.salmanajmal.ziya.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewsViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    // Who viewed my profile
    private val _profileViewers = MutableStateFlow<NetworkResult<ProfileViewersResponse>>(NetworkResult.Loading())
    val profileViewers: StateFlow<NetworkResult<ProfileViewersResponse>> = _profileViewers.asStateFlow()

    // Profiles I have viewed
    private val _viewedProfiles = MutableStateFlow<NetworkResult<ProfileViewersResponse>>(NetworkResult.Loading())
    val viewedProfiles: StateFlow<NetworkResult<ProfileViewersResponse>> = _viewedProfiles.asStateFlow()

    // Pagination state for viewers
    private var viewersPage = 1
    private var viewersHasMore = true
    private val viewersList = mutableListOf<ProfileViewerEntry>()

    // Pagination state for viewed
    private var viewedPage = 1
    private var viewedHasMore = true
    private val viewedList = mutableListOf<ProfileViewerEntry>()

    private val pageLimit = 10

    init {
        fetchProfileViewers()
        fetchViewedProfiles()
    }

    fun fetchProfileViewers(loadMore: Boolean = false) {
        if (loadMore && !viewersHasMore) return
        
        viewModelScope.launch {
            if (!loadMore) {
                viewersPage = 1
                viewersList.clear()
            }
            
            userRepository.getProfileViewers(viewersPage, pageLimit).collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        val response = result.data
                        if (response != null) {
                            response.data?.let { newItems ->
                                viewersList.addAll(newItems)
                            }
                            viewersHasMore = response.hasMore == true
                            viewersPage++
                            
                            // Create a new response with accumulated data
                            _profileViewers.value = NetworkResult.Success(
                                response.copy(data = viewersList.toList())
                            )
                        }
                    }
                    is NetworkResult.Error -> {
                        _profileViewers.value = result
                    }
                    is NetworkResult.Loading -> {
                        if (!loadMore) {
                            _profileViewers.value = result
                        }
                    }
                }
            }
        }
    }

    fun fetchViewedProfiles(loadMore: Boolean = false) {
        if (loadMore && !viewedHasMore) return
        
        viewModelScope.launch {
            if (!loadMore) {
                viewedPage = 1
                viewedList.clear()
            }
            
            userRepository.getViewedProfiles(viewedPage, pageLimit).collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        val response = result.data
                        if (response != null) {
                            response.data?.let { newItems ->
                                viewedList.addAll(newItems)
                            }
                            viewedHasMore = response.hasMore == true
                            viewedPage++
                            
                            // Create a new response with accumulated data
                            _viewedProfiles.value = NetworkResult.Success(
                                response.copy(data = viewedList.toList())
                            )
                        }
                    }
                    is NetworkResult.Error -> {
                        _viewedProfiles.value = result
                    }
                    is NetworkResult.Loading -> {
                        if (!loadMore) {
                            _viewedProfiles.value = result
                        }
                    }
                }
            }
        }
    }

    fun loadMoreViewers() = fetchProfileViewers(loadMore = true)
    fun loadMoreViewed() = fetchViewedProfiles(loadMore = true)
    
    fun canLoadMoreViewers() = viewersHasMore
    fun canLoadMoreViewed() = viewedHasMore
}
