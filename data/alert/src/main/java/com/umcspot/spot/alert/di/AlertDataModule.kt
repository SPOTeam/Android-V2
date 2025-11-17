package com.umcspot.spot.alert.di

import com.umcspot.spot.alert.datasource.AlertDataSource
import com.umcspot.spot.alert.datasourceimpl.AlertDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AlertDataModule {
    @Binds
    @Singleton
    abstract fun bindDummyRemoteDataSource(impl: AlertDataSourceImpl): AlertDataSource
}