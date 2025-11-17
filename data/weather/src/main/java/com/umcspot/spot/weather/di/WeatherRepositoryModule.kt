package com.umcspot.spot.weather.di

import com.umcspot.spot.weather.repository.WeatherRepository
import com.umcspot.spot.weather.repositoryimpl.WeatherRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class WeatherRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindsDummyRepository(dummyRepositoryImpl: WeatherRepositoryImpl): WeatherRepository
}