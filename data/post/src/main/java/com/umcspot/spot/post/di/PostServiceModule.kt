package com.umcspot.spot.post.di

import com.umcspot.spot.post.service.PostService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PostServiceModule {
    @Provides
    @Singleton
    fun providesPostService(retrofit: Retrofit): PostService = retrofit.create(
        PostService::class.java
    )
}