package com.umcspot.spot.weather.di

import com.umcspot.spot.weather.datasource.WeatherDataSource
import com.umcspot.spot.weather.datasourceimpl.WeatherDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class WeatherDataModule {
    @Binds
    @Singleton
    abstract fun bindDummyRemoteDataSource(impl: WeatherDataSourceImpl): WeatherDataSource
}