package com.umcspot.spot.study.di


import com.umcspot.spot.study.repository.StudyRepository
import com.umcspot.spot.study.repositoryimpl.StudyRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class StudyRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindsStudyRepository(dummyRepositoryImpl: StudyRepositoryImpl): StudyRepository
}