package com.umcspot.spot.home.di

import com.umcspot.spot.home.datasource.HomeDataSource
import com.umcspot.spot.home.datasourceimpl.HomeDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class HomeDataModule {
    @Binds
    @Singleton
    abstract fun bindHomeRemoteDataSource(impl: HomeDataSourceImpl): HomeDataSource
}