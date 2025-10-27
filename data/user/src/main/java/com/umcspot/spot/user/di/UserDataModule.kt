package com.umcspot.spot.user.di

import com.umcspot.spot.user.datasource.UserDataSource
import com.umcspot.spot.user.datasourceimpl.UserDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class UserDataModule {
    @Binds
    @Singleton
    abstract fun bindDummyRemoteDataSource(impl: UserDataSourceImpl): UserDataSource
}