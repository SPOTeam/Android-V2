package com.umcspot.spot.home.di

import com.umcspot.spot.home.service.HomeService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomeServiceModule {
    @Provides
    @Singleton
    fun providesDummyService(retrofit: Retrofit): HomeService = retrofit.create(
        HomeService::class.java
    )
}