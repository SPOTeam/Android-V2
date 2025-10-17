package com.umcspot.spot.board.di

import com.umcspot.spot.board.service.BoardService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BoardServiceModule {
    @Provides
    @Singleton
    fun providesBoardService(retrofit: Retrofit): BoardService = retrofit.create(
        BoardService::class.java
    )
}