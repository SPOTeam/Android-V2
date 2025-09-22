package com.umcspot.spot.buildconfig.di

import com.umcspot.spot.buildconfig.impl.BuildConfigFieldsProviderImpl
import com.umcspot.spot.common.BuildConfigFieldProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object BuildConfigModule {
    @Provides
    @Singleton
    fun provideBuildConfigFieldsProvider(
        buildConfigFieldProvider: BuildConfigFieldsProviderImpl
    ): BuildConfigFieldProvider = buildConfigFieldProvider
}