package com.umcspot.spot.login.di

import com.umcspot.spot.login.repositoryimpl.LoginRepositoryImpl
import com.umcspot.spot.token.repository.TokenRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LoginRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindsDummyRepository(dummyRepositoryImpl: LoginRepositoryImpl): TokenRepository
}