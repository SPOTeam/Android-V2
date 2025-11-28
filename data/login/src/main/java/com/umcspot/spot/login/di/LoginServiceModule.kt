package com.umcspot.spot.login.di

import com.umcspot.spot.login.service.LoginService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoginServiceModule {
    @Provides
    @Singleton
    fun provideOAuthApi(retrofit: Retrofit): LoginService =
        retrofit.create(LoginService::class.java)

}