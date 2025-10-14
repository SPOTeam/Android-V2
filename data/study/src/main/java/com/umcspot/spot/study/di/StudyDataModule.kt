package com.umcspot.spot.study.di

import com.umcspot.spot.study.datasource.StudyDataSource
import com.umcspot.spot.study.datasourceimpl.StudyDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class StudyDataModule {
    @Binds
    @Singleton
    abstract fun bindDummyRemoteDataSource(impl: StudyDataSourceImpl): StudyDataSource
}