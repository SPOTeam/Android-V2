package com.umcspot.spot.post.di


import com.umcspot.spot.post.repository.PostRepository
import com.umcspot.spot.post.repositoryimpl.PostRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PostRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindsDummyRepository(dummyRepositoryImpl: PostRepositoryImpl): PostRepository
}