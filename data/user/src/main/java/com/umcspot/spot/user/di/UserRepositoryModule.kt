package com.umcspot.spot.user.di

import com.umcspot.spot.user.repository.UserRepository
import com.umcspot.spot.user.repositoryimpl.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UserRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindsDummyRepository(impl: UserRepositoryImpl): UserRepository
}