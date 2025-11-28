package com.umcspot.spot.login.di

import com.umcspot.spot.login.datasource.LoginDataSource
import com.umcspot.spot.login.datasourceimpl.LoginDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LoginDataModule {
    @Binds
    @Singleton
    abstract fun bindDummyRemoteDataSource(impl: LoginDataSourceImpl): LoginDataSource
}