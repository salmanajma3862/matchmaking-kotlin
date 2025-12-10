package com.salmanajmal.ziya.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salmanajmal.ziya.data.models.request.AgeRangeRequest
import com.salmanajmal.ziya.data.models.request.CompleteProfileRequest
import com.salmanajmal.ziya.data.models.request.UserPreferencesRequest
import com.salmanajmal.ziya.data.repository.UserRepository
import com.salmanajmal.ziya.utils.FileUtils
import com.salmanajmal.ziya.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

data class ProfileSetupState(
    // Step 1: Basic Info
    val name: String = "",
    val gender: String = "", // "male", "female"
    val dob: Long? = null, // Timestamp

    // Step 2: Location
    val city: String = "",
    val country: String = "",
    val address: String = "",

    // Step 3: Photos
    val photos: List<Uri> = emptyList(),

    // Step 4: Physical & Personal
    val height: String = "", // stored as string for input, convert to number later
    val weight: String = "",
    val bodyType: String = "",
    val bio: String = "",

    // Future Steps (Placeholders)
    val religion: String = "",
    val sect: String = "",
    val maritalStatus: String = "",
    val education: String = "",
    val profession: String = "",
    val incomeRange: String = "",
    
    val smoking: Boolean = false,
    val drinking: Boolean = false,
    val dietPreference: String = "",
    val livingWithFamily: Boolean = false,

    val interests: List<String> = emptyList(),
    val hobbies: List<String> = emptyList(),

    val intention: String = "",
    val readyForMarriageTimeframe: String = "",

    // Step 8: Partner Preferences
    val partnerMinAge: Float = 18f,
    val partnerMaxAge: Float = 60f,
    val partnerCity: String = "",
    val partnerGender: String = "",
    val partnerMaritalStatus: List<String> = emptyList(),
    val partnerReligion: List<String> = emptyList(),
    
    // UI State
    val currentStep: Int = 1,
    val totalSteps: Int = 8,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ProfileSetupViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val application: Application
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileSetupState())
    val state: StateFlow<ProfileSetupState> = _state.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<ProfileSetupNavigationEvent>()
    val navigationEvent: SharedFlow<ProfileSetupNavigationEvent> = _navigationEvent.asSharedFlow()

    fun onEvent(event: ProfileSetupEvent) {
        when (event) {
            is ProfileSetupEvent.UpdateName -> _state.update { it.copy(name = event.name) }
            is ProfileSetupEvent.UpdateGender -> _state.update { it.copy(gender = event.gender) }
            is ProfileSetupEvent.UpdateDob -> _state.update { it.copy(dob = event.dob) }
            
            is ProfileSetupEvent.UpdateCity -> _state.update { it.copy(city = event.city) }
            is ProfileSetupEvent.UpdateCountry -> _state.update { it.copy(country = event.country) }
            is ProfileSetupEvent.UpdateAddress -> _state.update { it.copy(address = event.address) }
            
            is ProfileSetupEvent.AddPhoto -> {
                val currentPhotos = _state.value.photos.toMutableList()
                if (currentPhotos.size < 6) {
                    currentPhotos.add(event.uri)
                    _state.update { it.copy(photos = currentPhotos) }
                }
            }
            is ProfileSetupEvent.RemovePhoto -> {
                val currentPhotos = _state.value.photos.toMutableList()
                currentPhotos.remove(event.uri)
                _state.update { it.copy(photos = currentPhotos) }
            }
            
            is ProfileSetupEvent.UpdateHeight -> _state.update { it.copy(height = event.height) }
            is ProfileSetupEvent.UpdateWeight -> _state.update { it.copy(weight = event.weight) }
            is ProfileSetupEvent.UpdateBodyType -> _state.update { it.copy(bodyType = event.bodyType) }
            is ProfileSetupEvent.UpdateBio -> _state.update { it.copy(bio = event.bio) }

            // Step 5
            is ProfileSetupEvent.UpdateReligion -> _state.update { it.copy(religion = event.religion) }
            is ProfileSetupEvent.UpdateSect -> _state.update { it.copy(sect = event.sect) }
            is ProfileSetupEvent.UpdateMaritalStatus -> _state.update { it.copy(maritalStatus = event.maritalStatus) }
            is ProfileSetupEvent.UpdateEducation -> _state.update { it.copy(education = event.education) }
            is ProfileSetupEvent.UpdateProfession -> _state.update { it.copy(profession = event.profession) }
            is ProfileSetupEvent.UpdateIncomeRange -> _state.update { it.copy(incomeRange = event.incomeRange) }

            // Step 6
            is ProfileSetupEvent.UpdateSmoking -> _state.update { it.copy(smoking = event.smoking) }
            is ProfileSetupEvent.UpdateDrinking -> _state.update { it.copy(drinking = event.drinking) }
            is ProfileSetupEvent.UpdateDietPreference -> _state.update { it.copy(dietPreference = event.dietPreference) }
            is ProfileSetupEvent.UpdateLivingWithFamily -> _state.update { it.copy(livingWithFamily = event.livingWithFamily) }

            // Step 7
            is ProfileSetupEvent.ToggleInterest -> {
                val current = _state.value.interests.toMutableList()
                if (current.contains(event.interest)) current.remove(event.interest) else current.add(event.interest)
                _state.update { it.copy(interests = current) }
            }
            is ProfileSetupEvent.ToggleHobby -> {
                val current = _state.value.hobbies.toMutableList()
                if (current.contains(event.hobby)) current.remove(event.hobby) else current.add(event.hobby)
                _state.update { it.copy(hobbies = current) }
            }

            // Step 8
            is ProfileSetupEvent.UpdateIntention -> _state.update { it.copy(intention = event.intention) }
            is ProfileSetupEvent.UpdateReadyForMarriageTimeframe -> _state.update { it.copy(readyForMarriageTimeframe = event.timeframe) }
            is ProfileSetupEvent.UpdatePartnerAgeRange -> _state.update { it.copy(partnerMinAge = event.min, partnerMaxAge = event.max) }
            is ProfileSetupEvent.UpdatePartnerCity -> _state.update { it.copy(partnerCity = event.city) }
            is ProfileSetupEvent.UpdatePartnerGender -> _state.update { it.copy(partnerGender = event.gender) }
            
            is ProfileSetupEvent.NextStep -> {
                if (_state.value.currentStep < _state.value.totalSteps) {
                    _state.update { it.copy(currentStep = it.currentStep + 1) }
                } else {
                    submitProfile()
                }
            }
            is ProfileSetupEvent.PreviousStep -> {
                if (_state.value.currentStep > 1) {
                    _state.update { it.copy(currentStep = it.currentStep - 1) }
                }
            }
        }
    }

    private fun submitProfile() {
        viewModelScope.launch {
            val currentState = _state.value
            
            // Convert Uris to Files
            val photoFiles = currentState.photos.mapNotNull { uri ->
                FileUtils.getFileFromUri(application, uri)
            }

            if (photoFiles.isEmpty()) {
                _state.update { it.copy(error = "Please upload at least one photo") }
                return@launch
            }

            val dobString = currentState.dob?.let { 
                SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date(it)) 
            }

            val request = CompleteProfileRequest(
                name = currentState.name,
                gender = currentState.gender,
                dob = dobString,
                city = currentState.city,
                country = currentState.country,
                address = currentState.address,
                height = currentState.height.toIntOrNull(),
                weight = currentState.weight.toIntOrNull(),
                bodyType = currentState.bodyType,
                bio = currentState.bio,
                religion = currentState.religion,
                sect = currentState.sect,
                maritalStatus = currentState.maritalStatus,
                education = currentState.education,
                profession = currentState.profession,
                incomeRange = currentState.incomeRange,
                smoking = currentState.smoking,
                drinking = currentState.drinking,
                dietPreference = currentState.dietPreference,
                livingWithFamily = currentState.livingWithFamily,
                interests = currentState.interests,
                hobbies = currentState.hobbies,
                preferences = UserPreferencesRequest(
                    ageRange = AgeRangeRequest(
                        min = currentState.partnerMinAge.toInt(),
                        max = currentState.partnerMaxAge.toInt()
                    ),
                    genderPreference = currentState.partnerGender,
                    // Add other preferences as needed
                )
            )

            userRepository.completeProfile(photoFiles, request).collect { result ->
                when (result) {
                    is NetworkResult.Loading -> {
                        _state.update { it.copy(isLoading = true, error = null) }
                    }
                    is NetworkResult.Success -> {
                        _state.update { it.copy(isLoading = false) }
                        _navigationEvent.emit(ProfileSetupNavigationEvent.NavigateToHome)
                    }
                    is NetworkResult.Error -> {
                        _state.update { it.copy(isLoading = false, error = result.message) }
                    }
                }
            }
        }
    }
}

sealed class ProfileSetupEvent {
    data class UpdateName(val name: String) : ProfileSetupEvent()
    data class UpdateGender(val gender: String) : ProfileSetupEvent()
    data class UpdateDob(val dob: Long?) : ProfileSetupEvent()
    
    data class UpdateCity(val city: String) : ProfileSetupEvent()
    data class UpdateCountry(val country: String) : ProfileSetupEvent()
    data class UpdateAddress(val address: String) : ProfileSetupEvent()
    
    data class AddPhoto(val uri: Uri) : ProfileSetupEvent()
    data class RemovePhoto(val uri: Uri) : ProfileSetupEvent()
    
    data class UpdateHeight(val height: String) : ProfileSetupEvent()
    data class UpdateWeight(val weight: String) : ProfileSetupEvent()
    data class UpdateBodyType(val bodyType: String) : ProfileSetupEvent()
    data class UpdateBio(val bio: String) : ProfileSetupEvent()

    // Step 5
    data class UpdateReligion(val religion: String) : ProfileSetupEvent()
    data class UpdateSect(val sect: String) : ProfileSetupEvent()
    data class UpdateMaritalStatus(val maritalStatus: String) : ProfileSetupEvent()
    data class UpdateEducation(val education: String) : ProfileSetupEvent()
    data class UpdateProfession(val profession: String) : ProfileSetupEvent()
    data class UpdateIncomeRange(val incomeRange: String) : ProfileSetupEvent()

    // Step 6
    data class UpdateSmoking(val smoking: Boolean) : ProfileSetupEvent()
    data class UpdateDrinking(val drinking: Boolean) : ProfileSetupEvent()
    data class UpdateDietPreference(val dietPreference: String) : ProfileSetupEvent()
    data class UpdateLivingWithFamily(val livingWithFamily: Boolean) : ProfileSetupEvent()

    // Step 7
    data class ToggleInterest(val interest: String) : ProfileSetupEvent()
    data class ToggleHobby(val hobby: String) : ProfileSetupEvent()

    // Step 8
    data class UpdateIntention(val intention: String) : ProfileSetupEvent()
    data class UpdateReadyForMarriageTimeframe(val timeframe: String) : ProfileSetupEvent()
    data class UpdatePartnerAgeRange(val min: Float, val max: Float) : ProfileSetupEvent()
    data class UpdatePartnerCity(val city: String) : ProfileSetupEvent()
    data class UpdatePartnerGender(val gender: String) : ProfileSetupEvent()
    
    object NextStep : ProfileSetupEvent()
    object PreviousStep : ProfileSetupEvent()
}

sealed class ProfileSetupNavigationEvent {
    object NavigateToHome : ProfileSetupNavigationEvent()
}
