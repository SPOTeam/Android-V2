package com.umcspot.spot.board.di

import com.umcspot.spot.board.datasource.BoardDataSource
import com.umcspot.spot.board.datasourceimpl.BoardDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BoardDataModule {
    @Binds
    @Singleton
    abstract fun bindBoardRemoteDataSource(impl: BoardDataSourceImpl): BoardDataSource
}