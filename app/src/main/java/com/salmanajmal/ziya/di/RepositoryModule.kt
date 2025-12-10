package com.salmanajmal.ziya.di

import com.salmanajmal.ziya.data.preferences.UserPreferences
import com.salmanajmal.ziya.data.repository.AuthRepository
import com.salmanajmal.ziya.data.repository.UserRepository
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
        authApiService: com.salmanajmal.ziya.data.api.AuthApiService,
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
        userApiService: com.salmanajmal.ziya.data.api.UserApiService,
        userPreferences: UserPreferences
    ): UserRepository {
        return UserRepository(userApiService, userPreferences)
    }

    /**
     * Provide SwipeRepository
     */
    @Provides
    @Singleton
    fun provideSwipeRepository(
        swipeApiService: com.salmanajmal.ziya.data.api.SwipeApiService
    ): com.salmanajmal.ziya.data.repository.SwipeRepository {
        return com.salmanajmal.ziya.data.repository.SwipeRepository(swipeApiService)
    }
}
