package com.umcspot.spot.post.di

import com.umcspot.spot.post.datasource.PostDataSource
import com.umcspot.spot.post.datasourceimpl.PostDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PostDataModule {
    @Binds
    @Singleton
    abstract fun bindPostRemoteDataSource(impl: PostDataSourceImpl): PostDataSource
}