package com.umcspot.spot.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
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
    fun provideSpotTokenDataStore(
        @ApplicationContext context: Context,
        serializer: SpotSecureDataStoreSerializer
    ): DataStore<SpotTokenData> {
        return DataStoreFactory.create(
            serializer = serializer,
            produceFile = { context.dataStoreFile("spot_tokens.secure") }
        )
    }
}
