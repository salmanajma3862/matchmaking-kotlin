package com.example.dummyapp.di

import com.example.dummyapp.data.preferences.UserPreferences
import com.example.dummyapp.data.repository.AuthRepository
import com.example.dummyapp.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Repository Module
 * Provides repository instances for dependency injection
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    
    /**
     * Provide AuthRepository
     */
    @Provides
    @Singleton
    fun provideAuthRepository(
        authApiService: com.example.dummyapp.data.api.AuthApiService,
        userPreferences: UserPreferences
    ): AuthRepository {
        return AuthRepository(authApiService, userPreferences)
    }
    
    /**
     * Provide UserRepository
     */
    @Provides
    @Singleton
    fun provideUserRepository(
        userApiService: com.example.dummyapp.data.api.UserApiService,
        userPreferences: UserPreferences
    ): UserRepository {
        return UserRepository(userApiService, userPreferences)
    }
}
