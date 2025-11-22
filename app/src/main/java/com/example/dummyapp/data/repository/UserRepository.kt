package com.example.dummyapp.data.repository

import com.example.dummyapp.data.api.UserApiService
import com.example.dummyapp.data.models.User
import com.example.dummyapp.data.models.request.*
import com.example.dummyapp.data.preferences.UserPreferences
import com.example.dummyapp.utils.Constants
import com.example.dummyapp.utils.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File
import java.io.IOException
import javax.inject.Inject

/**
 * User Repository
 * Handles user profile operations and API calls
 * Similar to userService in React Native
 */
class UserRepository @Inject constructor(
    private val userApiService: UserApiService,
    private val userPreferences: UserPreferences
) {
    
    /**
     * Get user profile by ID
     */
    fun getUserProfile(userId: String): Flow<NetworkResult<User>> = flow {
        emit(NetworkResult.Loading())
        
        try {
            val response = userApiService.getUserProfile(userId)
            
            if (response.isSuccessful && response.body() != null) {
                val userResponse = response.body()!!
                
                if (userResponse.success) {
                    emit(NetworkResult.Success(userResponse.user))
                } else {
                    emit(NetworkResult.Error(Constants.ErrorMessages.UNKNOWN_ERROR))
                }
            } else {
                emit(NetworkResult.Error(response.message() ?: Constants.ErrorMessages.UNKNOWN_ERROR))
            }
        } catch (e: HttpException) {
            emit(NetworkResult.Error(e.message() ?: Constants.ErrorMessages.SERVER_ERROR))
        } catch (e: IOException) {
            emit(NetworkResult.Error(Constants.ErrorMessages.NETWORK_ERROR))
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: Constants.ErrorMessages.UNKNOWN_ERROR))
        }
    }
    
    /**
     * Update user profile
     */
    fun updateProfile(request: UpdateProfileRequest): Flow<NetworkResult<User>> = flow {
        emit(NetworkResult.Loading())
        
        try {
            val response = userApiService.updateProfile(request)
            
            if (response.isSuccessful && response.body() != null) {
                val updateResponse = response.body()!!
                
                if (updateResponse.success) {
                    // Update local user name if changed
                    request.name?.let { userPreferences.saveUserName(it) }
                    
                    emit(NetworkResult.Success(updateResponse.user))
                } else {
                    emit(NetworkResult.Error(updateResponse.message))
                }
            } else {
                emit(NetworkResult.Error(response.message() ?: Constants.ErrorMessages.UNKNOWN_ERROR))
            }
        } catch (e: HttpException) {
            emit(NetworkResult.Error(e.message() ?: Constants.ErrorMessages.SERVER_ERROR))
        } catch (e: IOException) {
            emit(NetworkResult.Error(Constants.ErrorMessages.NETWORK_ERROR))
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: Constants.ErrorMessages.UNKNOWN_ERROR))
        }
    }
    
    /**
     * Complete profile with photos
     */
    fun completeProfile(
        photoFiles: List<File>,
        profileData: CompleteProfileRequest
    ): Flow<NetworkResult<User>> = flow {
        emit(NetworkResult.Loading())
        
        try {
            // Prepare photo parts
            val photoParts = photoFiles.mapIndexed { index, file ->
                // Determine mime type based on extension
                val extension = file.extension.lowercase()
                val mimeType = when {
                    extension == "png" -> "image/png"
                    extension == "webp" -> "image/webp"
                    extension == "jpg" || extension == "jpeg" -> "image/jpeg"
                    else -> "image/jpeg" // Default to jpeg if unknown
                }
                
                val requestFile = file.asRequestBody(mimeType.toMediaTypeOrNull())
                MultipartBody.Part.createFormData("photos", file.name, requestFile)
            }
            
            // Prepare other data parts
            fun createPart(value: Any?): okhttp3.RequestBody? {
                return value?.toString()?.toRequestBody("text/plain".toMediaTypeOrNull())
            }
            
            val response = userApiService.completeProfile(
                photos = photoParts,
                name = createPart(profileData.name),
                phone = createPart(profileData.phone),
                gender = createPart(profileData.gender),
                dob = createPart(profileData.dob),
                bio = createPart(profileData.bio),
                height = createPart(profileData.height),
                weight = createPart(profileData.weight),
                bodyType = createPart(profileData.bodyType),
                city = createPart(profileData.city),
                country = createPart(profileData.country),
                address = createPart(profileData.address),
                religion = createPart(profileData.religion),
                sect = createPart(profileData.sect),
                maritalStatus = createPart(profileData.maritalStatus),
                education = createPart(profileData.education),
                profession = createPart(profileData.profession),
                incomeRange = createPart(profileData.incomeRange),
                interests = createPart(profileData.interests?.joinToString(",")),
                hobbies = createPart(profileData.hobbies?.joinToString(",")),
                smoking = createPart(profileData.smoking),
                drinking = createPart(profileData.drinking),
                dietPreference = createPart(profileData.dietPreference),
                familyBackground = createPart(profileData.familyBackground),
                numberOfSiblings = createPart(profileData.numberOfSiblings),
                livingWithFamily = createPart(profileData.livingWithFamily)
            )
            
            if (response.isSuccessful && response.body() != null) {
                val completeResponse = response.body()!!
                
                if (completeResponse.success) {
                    // Mark profile as complete
                    userPreferences.setProfileComplete(true)
                    
                    // Update user name if returned
                    completeResponse.user?.name?.let { userPreferences.saveUserName(it) }
                    
                    emit(NetworkResult.Success(completeResponse.user))
                } else {
                    emit(NetworkResult.Error(completeResponse.message))
                }
            } else {
                emit(NetworkResult.Error(response.message() ?: Constants.ErrorMessages.UNKNOWN_ERROR))
            }
        } catch (e: HttpException) {
            emit(NetworkResult.Error(e.message() ?: Constants.ErrorMessages.SERVER_ERROR))
        } catch (e: IOException) {
            emit(NetworkResult.Error(Constants.ErrorMessages.NETWORK_ERROR))
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: Constants.ErrorMessages.UNKNOWN_ERROR))
        }
    }
    
    /**
     * Upload profile photos
     */
    fun uploadPhotos(photoFiles: List<File>): Flow<NetworkResult<List<String>>> = flow {
        emit(NetworkResult.Loading())
        
        try {
            val photoParts = photoFiles.map { file ->
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("photos", file.name, requestFile)
            }
            
            val response = userApiService.uploadPhotos(photoParts)
            
            if (response.isSuccessful && response.body() != null) {
                val uploadResponse = response.body()!!
                
                if (uploadResponse.success && uploadResponse.data != null) {
                    emit(NetworkResult.Success(uploadResponse.data))
                } else {
                    emit(NetworkResult.Error(uploadResponse.message ?: Constants.ErrorMessages.UNKNOWN_ERROR))
                }
            } else {
                emit(NetworkResult.Error(response.message() ?: Constants.ErrorMessages.UNKNOWN_ERROR))
            }
        } catch (e: HttpException) {
            emit(NetworkResult.Error(e.message() ?: Constants.ErrorMessages.SERVER_ERROR))
        } catch (e: IOException) {
            emit(NetworkResult.Error(Constants.ErrorMessages.NETWORK_ERROR))
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: Constants.ErrorMessages.UNKNOWN_ERROR))
        }
    }
    
    /**
     * Delete photo
     */
    fun deletePhoto(photoId: String): Flow<NetworkResult<Boolean>> = flow {
        emit(NetworkResult.Loading())
        
        try {
            val response = userApiService.deletePhoto(photoId)
            
            if (response.isSuccessful) {
                emit(NetworkResult.Success(true))
            } else {
                emit(NetworkResult.Error(response.message() ?: Constants.ErrorMessages.UNKNOWN_ERROR))
            }
        } catch (e: HttpException) {
            emit(NetworkResult.Error(e.message() ?: Constants.ErrorMessages.SERVER_ERROR))
        } catch (e: IOException) {
            emit(NetworkResult.Error(Constants.ErrorMessages.NETWORK_ERROR))
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: Constants.ErrorMessages.UNKNOWN_ERROR))
        }
    }
    
    /**
     * Set primary photo
     */
    fun setPrimaryPhoto(photoId: String): Flow<NetworkResult<Boolean>> = flow {
        emit(NetworkResult.Loading())
        
        try {
            val response = userApiService.setPrimaryPhoto(photoId)
            
            if (response.isSuccessful) {
                emit(NetworkResult.Success(true))
            } else {
                emit(NetworkResult.Error(response.message() ?: Constants.ErrorMessages.UNKNOWN_ERROR))
            }
        } catch (e: HttpException) {
            emit(NetworkResult.Error(e.message() ?: Constants.ErrorMessages.SERVER_ERROR))
        } catch (e: IOException) {
            emit(NetworkResult.Error(Constants.ErrorMessages.NETWORK_ERROR))
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: Constants.ErrorMessages.UNKNOWN_ERROR))
        }
    }
    
    /**
     * Update user preferences
     */
    fun updatePreferences(request: UserPreferencesRequest): Flow<NetworkResult<Boolean>> = flow {
        emit(NetworkResult.Loading())
        
        try {
            val response = userApiService.updatePreferences(request)
            
            if (response.isSuccessful && response.body() != null) {
                emit(NetworkResult.Success(true))
            } else {
                emit(NetworkResult.Error(response.message() ?: Constants.ErrorMessages.UNKNOWN_ERROR))
            }
        } catch (e: HttpException) {
            emit(NetworkResult.Error(e.message() ?: Constants.ErrorMessages.SERVER_ERROR))
        } catch (e: IOException) {
            emit(NetworkResult.Error(Constants.ErrorMessages.NETWORK_ERROR))
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: Constants.ErrorMessages.UNKNOWN_ERROR))
        }
    }
    
    /**
     * Delete account
     */
    fun deleteAccount(): Flow<NetworkResult<Boolean>> = flow {
        emit(NetworkResult.Loading())
        
        try {
            val response = userApiService.deleteAccount()
            
            if (response.isSuccessful) {
                // Clear local data
                userPreferences.clearAuthData()
                emit(NetworkResult.Success(true))
            } else {
                emit(NetworkResult.Error(response.message() ?: Constants.ErrorMessages.UNKNOWN_ERROR))
            }
        } catch (e: HttpException) {
            emit(NetworkResult.Error(e.message() ?: Constants.ErrorMessages.SERVER_ERROR))
        } catch (e: IOException) {
            emit(NetworkResult.Error(Constants.ErrorMessages.NETWORK_ERROR))
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: Constants.ErrorMessages.UNKNOWN_ERROR))
        }
    }
    
    /**
     * Block user
     */
    fun blockUser(userId: String, reason: String?): Flow<NetworkResult<Boolean>> = flow {
        emit(NetworkResult.Loading())
        
        try {
            val request = BlockUserRequest(userId = userId, reason = reason)
            val response = userApiService.blockUser(request)
            
            if (response.isSuccessful) {
                emit(NetworkResult.Success(true))
            } else {
                emit(NetworkResult.Error(response.message() ?: Constants.ErrorMessages.UNKNOWN_ERROR))
            }
        } catch (e: HttpException) {
            emit(NetworkResult.Error(e.message() ?: Constants.ErrorMessages.SERVER_ERROR))
        } catch (e: IOException) {
            emit(NetworkResult.Error(Constants.ErrorMessages.NETWORK_ERROR))
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: Constants.ErrorMessages.UNKNOWN_ERROR))
        }
    }
    
    /**
     * Unblock user
     */
    fun unblockUser(userId: String): Flow<NetworkResult<Boolean>> = flow {
        emit(NetworkResult.Loading())
        
        try {
            val response = userApiService.unblockUser(userId)
            
            if (response.isSuccessful) {
                emit(NetworkResult.Success(true))
            } else {
                emit(NetworkResult.Error(response.message() ?: Constants.ErrorMessages.UNKNOWN_ERROR))
            }
        } catch (e: HttpException) {
            emit(NetworkResult.Error(e.message() ?: Constants.ErrorMessages.SERVER_ERROR))
        } catch (e: IOException) {
            emit(NetworkResult.Error(Constants.ErrorMessages.NETWORK_ERROR))
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: Constants.ErrorMessages.UNKNOWN_ERROR))
        }
    }
    
    /**
     * Report user
     */
    fun reportUser(
        userId: String,
        reason: String,
        description: String?
    ): Flow<NetworkResult<Boolean>> = flow {
        emit(NetworkResult.Loading())
        
        try {
            val request = ReportUserRequest(
                userId = userId,
                reason = reason,
                description = description
            )
            val response = userApiService.reportUser(request)
            
            if (response.isSuccessful) {
                emit(NetworkResult.Success(true))
            } else {
                emit(NetworkResult.Error(response.message() ?: Constants.ErrorMessages.UNKNOWN_ERROR))
            }
        } catch (e: HttpException) {
            emit(NetworkResult.Error(e.message() ?: Constants.ErrorMessages.SERVER_ERROR))
        } catch (e: IOException) {
            emit(NetworkResult.Error(Constants.ErrorMessages.NETWORK_ERROR))
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: Constants.ErrorMessages.UNKNOWN_ERROR))
        }
    }
    
    /**
     * Update notification settings
     */
    fun updateNotificationSettings(
        request: NotificationSettingsRequest
    ): Flow<NetworkResult<Boolean>> = flow {
        emit(NetworkResult.Loading())
        
        try {
            val response = userApiService.updateNotificationSettings(request)
            
            if (response.isSuccessful) {
                emit(NetworkResult.Success(true))
            } else {
                emit(NetworkResult.Error(response.message() ?: Constants.ErrorMessages.UNKNOWN_ERROR))
            }
        } catch (e: HttpException) {
            emit(NetworkResult.Error(e.message() ?: Constants.ErrorMessages.SERVER_ERROR))
        } catch (e: IOException) {
            emit(NetworkResult.Error(Constants.ErrorMessages.NETWORK_ERROR))
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: Constants.ErrorMessages.UNKNOWN_ERROR))
        }
    }
    
    /**
     * Update privacy settings
     */
    fun updatePrivacySettings(
        request: PrivacySettingsRequest
    ): Flow<NetworkResult<Boolean>> = flow {
        emit(NetworkResult.Loading())
        
        try {
            val response = userApiService.updatePrivacySettings(request)
            
            if (response.isSuccessful) {
                emit(NetworkResult.Success(true))
            } else {
                emit(NetworkResult.Error(response.message() ?: Constants.ErrorMessages.UNKNOWN_ERROR))
            }
        } catch (e: HttpException) {
            emit(NetworkResult.Error(e.message() ?: Constants.ErrorMessages.SERVER_ERROR))
        } catch (e: IOException) {
            emit(NetworkResult.Error(Constants.ErrorMessages.NETWORK_ERROR))
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: Constants.ErrorMessages.UNKNOWN_ERROR))
        }
    }
}
