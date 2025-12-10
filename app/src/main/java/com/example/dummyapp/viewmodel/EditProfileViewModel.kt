package com.example.dummyapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dummyapp.data.models.User
import com.example.dummyapp.data.models.request.UpdateProfileRequest
import com.example.dummyapp.data.repository.UserRepository
import com.example.dummyapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Edit Profile State
 * Manages all form fields and UI state for the Edit Profile screen
 */
data class EditProfileState(
    // Basic Info
    val name: String = "",
    val bio: String = "",
    
    // Physical
    val height: String = "",
    val weight: String = "",
    val bodyType: String? = null,
    
    // Location
    val city: String = "",
    val country: String = "",
    
    // Background
    val religion: String? = null,
    val sect: String? = null,
    val maritalStatus: String? = null,
    val education: String? = null,
    val profession: String = "",
    val incomeRange: String? = null,
    
    // Lifestyle
    val smoking: Boolean = false,
    val drinking: Boolean = false,
    val dietPreference: String? = null,
    
    // Family
    val familyBackground: String = "",
    val numberOfSiblings: String = "",
    val livingWithFamily: Boolean = false,
    
    // Interests
    val interests: List<String> = emptyList(),
    val hobbies: List<String> = emptyList(),
    
    // Looking For
    val intention: String? = null,
    val readyForMarriageTimeframe: String? = null,
    
    // UI State
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val saveSuccess: Boolean = false,
    val showNameChangeConfirmation: Boolean = false,
    val originalName: String = "",
    val pendingName: String = "" // Name waiting for confirmation
)

/**
 * Edit Profile ViewModel
 * Handles profile editing form state and API calls
 */
@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    
    private val _state = MutableStateFlow(EditProfileState())
    val state: StateFlow<EditProfileState> = _state.asStateFlow()
    
    /**
     * Initialize form with user data
     */
    fun initializeWithUser(user: User) {
        _state.update { currentState ->
            currentState.copy(
                name = user.name,
                bio = user.bio ?: "",
                height = user.height?.toString() ?: "",
                weight = user.weight?.toString() ?: "",
                bodyType = user.bodyType,
                city = user.city ?: "",
                country = user.country ?: "",
                religion = user.religion,
                sect = user.sect,
                maritalStatus = user.maritalStatus,
                education = user.education,
                profession = user.profession ?: "",
                incomeRange = user.incomeRange,
                smoking = user.smoking ?: false,
                drinking = user.drinking ?: false,
                dietPreference = user.dietPreference,
                familyBackground = user.familyBackground ?: "",
                numberOfSiblings = user.numberOfSiblings?.toString() ?: "",
                livingWithFamily = user.livingWithFamily ?: false,
                interests = user.interests ?: emptyList(),
                hobbies = user.hobbies ?: emptyList(),
                intention = user.intention,
                readyForMarriageTimeframe = user.readyForMarriageTimeframe,
                originalName = user.name,
                isLoading = false
            )
        }
    }
    
    // ==================== Field Update Methods ====================
    
    fun updateName(name: String) {
        _state.update { it.copy(name = name) }
    }
    
    fun updateBio(bio: String) {
        _state.update { it.copy(bio = bio) }
    }
    
    fun updateHeight(height: String) {
        _state.update { it.copy(height = height) }
    }
    
    fun updateWeight(weight: String) {
        _state.update { it.copy(weight = weight) }
    }
    
    fun updateBodyType(bodyType: String?) {
        _state.update { it.copy(bodyType = bodyType) }
    }
    
    fun updateCity(city: String) {
        _state.update { it.copy(city = city) }
    }
    
    fun updateCountry(country: String) {
        _state.update { it.copy(country = country) }
    }
    
    fun updateReligion(religion: String?) {
        _state.update { it.copy(religion = religion) }
    }
    
    fun updateSect(sect: String?) {
        _state.update { it.copy(sect = sect) }
    }
    
    fun updateMaritalStatus(maritalStatus: String?) {
        _state.update { it.copy(maritalStatus = maritalStatus) }
    }
    
    fun updateEducation(education: String?) {
        _state.update { it.copy(education = education) }
    }
    
    fun updateProfession(profession: String) {
        _state.update { it.copy(profession = profession) }
    }
    
    fun updateIncomeRange(incomeRange: String?) {
        _state.update { it.copy(incomeRange = incomeRange) }
    }
    
    fun updateSmoking(smoking: Boolean) {
        _state.update { it.copy(smoking = smoking) }
    }
    
    fun updateDrinking(drinking: Boolean) {
        _state.update { it.copy(drinking = drinking) }
    }
    
    fun updateDietPreference(dietPreference: String?) {
        _state.update { it.copy(dietPreference = dietPreference) }
    }
    
    fun updateFamilyBackground(familyBackground: String) {
        _state.update { it.copy(familyBackground = familyBackground) }
    }
    
    fun updateNumberOfSiblings(numberOfSiblings: String) {
        _state.update { it.copy(numberOfSiblings = numberOfSiblings) }
    }
    
    fun updateLivingWithFamily(livingWithFamily: Boolean) {
        _state.update { it.copy(livingWithFamily = livingWithFamily) }
    }
    
    fun updateIntention(intention: String?) {
        _state.update { it.copy(intention = intention) }
    }
    
    fun updateReadyForMarriageTimeframe(timeframe: String?) {
        _state.update { it.copy(readyForMarriageTimeframe = timeframe) }
    }
    
    // ==================== Interests & Hobbies ====================
    
    fun addInterest(interest: String) {
        if (interest.isNotBlank()) {
            _state.update { currentState ->
                currentState.copy(interests = currentState.interests + interest.trim())
            }
        }
    }
    
    fun removeInterest(interest: String) {
        _state.update { currentState ->
            currentState.copy(interests = currentState.interests.filter { it != interest })
        }
    }
    
    fun addHobby(hobby: String) {
        if (hobby.isNotBlank()) {
            _state.update { currentState ->
                currentState.copy(hobbies = currentState.hobbies + hobby.trim())
            }
        }
    }
    
    fun removeHobby(hobby: String) {
        _state.update { currentState ->
            currentState.copy(hobbies = currentState.hobbies.filter { it != hobby })
        }
    }
    
    // ==================== Name Change Confirmation ====================
    
    fun attemptSave() {
        val currentState = _state.value
        
        // Check if name is being changed
        if (currentState.name != currentState.originalName && currentState.name.isNotBlank()) {
            // Show confirmation dialog
            _state.update { it.copy(
                showNameChangeConfirmation = true,
                pendingName = currentState.name
            ) }
        } else {
            // No name change, save directly
            saveProfile()
        }
    }
    
    fun confirmNameChange() {
        _state.update { it.copy(showNameChangeConfirmation = false) }
        saveProfile()
    }
    
    fun cancelNameChange() {
        // Revert to original name and close dialog
        _state.update { it.copy(
            name = it.originalName,
            showNameChangeConfirmation = false,
            pendingName = ""
        ) }
    }
    
    // ==================== Save Profile ====================
    
    private fun saveProfile() {
        val currentState = _state.value
        
        viewModelScope.launch {
            val request = UpdateProfileRequest(
                name = currentState.name.takeIf { it.isNotBlank() },
                bio = currentState.bio.takeIf { it.isNotBlank() },
                height = currentState.height.toIntOrNull(),
                weight = currentState.weight.toIntOrNull(),
                bodyType = currentState.bodyType,
                city = currentState.city.takeIf { it.isNotBlank() },
                country = currentState.country.takeIf { it.isNotBlank() },
                religion = currentState.religion,
                sect = currentState.sect,
                maritalStatus = currentState.maritalStatus,
                education = currentState.education,
                profession = currentState.profession.takeIf { it.isNotBlank() },
                incomeRange = currentState.incomeRange,
                smoking = currentState.smoking,
                drinking = currentState.drinking,
                dietPreference = currentState.dietPreference,
                familyBackground = currentState.familyBackground.takeIf { it.isNotBlank() },
                numberOfSiblings = currentState.numberOfSiblings.toIntOrNull(),
                livingWithFamily = currentState.livingWithFamily,
                interests = currentState.interests.takeIf { it.isNotEmpty() },
                hobbies = currentState.hobbies.takeIf { it.isNotEmpty() },
                intention = currentState.intention,
                readyForMarriageTimeframe = currentState.readyForMarriageTimeframe
            )
            
            userRepository.updateProfile(request).collect { result ->
                when (result) {
                    is NetworkResult.Loading -> {
                        _state.update { it.copy(isSaving = true, errorMessage = null) }
                    }
                    is NetworkResult.Success -> {
                        _state.update { it.copy(
                            isSaving = false,
                            saveSuccess = true,
                            errorMessage = null,
                            originalName = currentState.name // Update original name on success
                        ) }
                    }
                    is NetworkResult.Error -> {
                        _state.update { it.copy(
                            isSaving = false,
                            errorMessage = result.message,
                            // Revert name if rate limited
                            name = if (result.message?.contains("change your name") == true) {
                                it.originalName
                            } else {
                                it.name
                            }
                        ) }
                    }
                }
            }
        }
    }
    
    // ==================== Clear States ====================
    
    fun clearError() {
        _state.update { it.copy(errorMessage = null) }
    }
    
    fun clearSaveSuccess() {
        _state.update { it.copy(saveSuccess = false) }
    }
}
