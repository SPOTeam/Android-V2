package com.umcspot.spot.weather.di

import com.umcspot.spot.network.di.WeatherApi
import com.umcspot.spot.weather.service.WeatherService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WeatherServiceModule {
    @Provides
    @Singleton
    fun providesWeatherService(@WeatherApi retrofit: Retrofit): WeatherService = retrofit.create(
        WeatherService::class.java
    )
}