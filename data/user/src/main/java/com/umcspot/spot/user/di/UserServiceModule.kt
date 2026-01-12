package com.umcspot.spot.user.di

import com.umcspot.spot.network.di.SpotApi
import com.umcspot.spot.user.service.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserServiceModule {
    @Provides
    @Singleton
    fun providesUserService(@SpotApi retrofit: Retrofit): UserService = retrofit.create(
        UserService::class.java
    )
}