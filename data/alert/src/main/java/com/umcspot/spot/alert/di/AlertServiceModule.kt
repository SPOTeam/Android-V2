package com.umcspot.spot.alert.di

import com.umcspot.spot.alert.service.AlertService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AlertServiceModule {
    @Provides
    @Singleton
    fun providesDummyService(retrofit: Retrofit): AlertService = retrofit.create(
        AlertService::class.java
    )
}