package com.umcspot.spot.buildconfig.di

import com.umcspot.spot.buildconfig.impl.WeatherConfigFieldsProviderImpl
import com.umcspot.spot.common.BuildConfigFieldProvider
import com.umcspot.spot.common.WeatherConfigFieldProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object WeatherConfigModule {
    @Provides
    @Singleton
    fun provideWeatherConfigFieldsProvider(
        weatherConfigFieldProvider: WeatherConfigFieldsProviderImpl
    ): WeatherConfigFieldProvider = weatherConfigFieldProvider
}