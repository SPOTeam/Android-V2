package com.umcspot.spot.alert.di


import com.umcspot.spot.alert.repository.AlertRepository
import com.umcspot.spot.alert.repositoryimpl.AlertRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AlertRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindsDummyRepository(dummyRepositoryImpl: AlertRepositoryImpl): AlertRepository
}