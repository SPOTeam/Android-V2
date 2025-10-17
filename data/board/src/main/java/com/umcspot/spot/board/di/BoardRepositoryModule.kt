package com.umcspot.spot.board.di


import com.umcspot.spot.board.repositoryimpl.BoardRepositoryImpl
import com.umcspot.spot.domain.board.repository.BoardRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BoardRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindsDummyRepository(dummyRepositoryImpl: BoardRepositoryImpl): BoardRepository
}