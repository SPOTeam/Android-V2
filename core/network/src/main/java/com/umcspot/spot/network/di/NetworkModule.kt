package com.umcspot.spot.network.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.umcspot.spot.common.BuildConfigFieldProvider
import com.umcspot.spot.common.WeatherConfigFieldProvider
import com.umcspot.spot.network.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun providesLoggingInterceptor() =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    // ---------- Spot API ----------

    @Provides
    @Singleton
    @SpotApi
    fun providesSpotOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()

    // ---------- Weather API ----------

    @Provides
    @Singleton
    @WeatherApi
    fun providesWeatherOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun providesConverterFactory(): Converter.Factory = Json.asConverterFactory(
        "application/json".toMediaType()
    )

    @Provides
    @Singleton
    @SpotApi
    fun providesSpotRetrofit(
        @SpotApi client: OkHttpClient,
        converterFactory: Converter.Factory,
        buildConfigProvider: BuildConfigFieldProvider
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(buildConfigProvider.get().baseUrl)
            .client(client)
            .addConverterFactory(converterFactory)
            .build()

    @Provides @Singleton @WeatherApi
    fun providesWeatherRetrofit(
        @WeatherApi weatherClient: OkHttpClient,
        converterFactory: Converter.Factory,
        weatherConfigProvider: WeatherConfigFieldProvider
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(weatherConfigProvider.getWeather().weatherUrl)
            .client(weatherClient)
            .addConverterFactory(converterFactory)
            .build()
}