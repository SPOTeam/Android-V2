package com.umcspot.spot.study.di

import com.umcspot.spot.study.service.StudyService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StudyServiceModule {
    @Provides
    @Singleton
    fun providesDummyService(retrofit: Retrofit): StudyService = retrofit.create(
        StudyService::class.java
    )
}