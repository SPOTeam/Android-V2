package com.umcspot.spot.network.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.umcspot.spot.common.BuildConfigFieldProvider
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

    @Provides
    @Singleton
    fun providesOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun providesConverterFactory(): Converter.Factory = Json.asConverterFactory(
        "application/json".toMediaType()
    )

    @Provides
    @Singleton
    fun providesRetrofit(
        client: OkHttpClient,
        converterFactory: Converter.Factory,
        buildConfigProvider: BuildConfigFieldProvider
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(/*buildConfigProvider.get().baseUrl*/"https://api-spot.site/")
            .client(client)
            .addConverterFactory(converterFactory)
            .build()
}