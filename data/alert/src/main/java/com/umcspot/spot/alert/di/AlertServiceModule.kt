package com.umcspot.spot.alert.di

import com.umcspot.spot.alert.service.AlertService
import com.umcspot.spot.network.di.SpotApi
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
    fun providesDummyService(@SpotApi retrofit: Retrofit): AlertService = retrofit.create(
        AlertService::class.java
    )
}