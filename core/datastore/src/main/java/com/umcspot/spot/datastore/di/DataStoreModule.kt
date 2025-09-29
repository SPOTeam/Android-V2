package com.umcspot.spot.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.umcspot.spot.datastore.SpotSecureDataStoreSerializer
import com.umcspot.spot.datastore.SpotTokenData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    fun providesDataStore(
        @ApplicationContext context: Context,
        spotSecureDataStoreSerializer: SpotSecureDataStoreSerializer
    ): DataStore<SpotTokenData> =
        DataStoreFactory.create(
            serializer = spotSecureDataStoreSerializer
        ) {
            context.dataStoreFile(DATASTORE_PREFERENCES)
        }

    private const val DATASTORE_PREFERENCES = "com.umcspot.spot.datastore"
}