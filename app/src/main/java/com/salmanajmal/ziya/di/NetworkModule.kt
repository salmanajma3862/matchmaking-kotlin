package com.salmanajmal.ziya.di

import android.content.Context
import com.salmanajmal.ziya.data.api.*
import com.salmanajmal.ziya.data.preferences.UserPreferences
import com.salmanajmal.ziya.utils.AuthInterceptor
import com.salmanajmal.ziya.utils.Constants
import com.salmanajmal.ziya.utils.ErrorInterceptor
import com.salmanajmal.ziya.utils.LoggingInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Network Module - Dependency Injection
 * Provides Retrofit, OkHttp, and API services
 * Similar to how you set up axios in React Native
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    /**
     * Provide UserPreferences instance
     */
    @Provides
    @Singleton
    fun provideUserPreferences(
        @ApplicationContext context: Context
    ): UserPreferences {
        return UserPreferences(context)
    }
    
    /**
     * Provide Gson instance
     */
    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .setDateFormat(Constants.DateFormat.API_FORMAT)
            .create()
    }
    
    /**
     * Provide OkHttpClient with interceptors
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(
        userPreferences: UserPreferences
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        return OkHttpClient.Builder()
            .connectTimeout(Constants.CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(Constants.READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(Constants.WRITE_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(AuthInterceptor(userPreferences))
            .addInterceptor(ErrorInterceptor(userPreferences))
            .addInterceptor(LoggingInterceptor())
            .addInterceptor(loggingInterceptor)
            .retryOnConnectionFailure(true)
            .build()
    }
    
    /**
     * Provide Retrofit instance
     */
    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
    
    /**
     * Provide AuthApiService
     */
    @Provides
    @Singleton
    fun provideAuthApiService(retrofit: Retrofit): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }
    
    /**
     * Provide UserApiService
     */
    @Provides
    @Singleton
    fun provideUserApiService(retrofit: Retrofit): UserApiService {
        return retrofit.create(UserApiService::class.java)
    }
    
    /**
     * Provide MessageApiService
     */
    @Provides
    @Singleton
    fun provideMessageApiService(retrofit: Retrofit): MessageApiService {
        return retrofit.create(MessageApiService::class.java)
    }
    
    /**
     * Provide NotificationApiService
     */
    @Provides
    @Singleton
    fun provideNotificationApiService(retrofit: Retrofit): NotificationApiService {
        return retrofit.create(NotificationApiService::class.java)
    }
    
    /**
     * Provide SwipeApiService
     */
    @Provides
    @Singleton
    fun provideSwipeApiService(retrofit: Retrofit): SwipeApiService {
        return retrofit.create(SwipeApiService::class.java)
    }
    
    /**
     * Provide NotificationHelper for push notification handling
     */
    @Provides
    @Singleton
    fun provideNotificationHelper(
        @ApplicationContext context: Context
    ): com.salmanajmal.ziya.utils.NotificationHelper {
        return com.salmanajmal.ziya.utils.NotificationHelper(context)
    }
}
