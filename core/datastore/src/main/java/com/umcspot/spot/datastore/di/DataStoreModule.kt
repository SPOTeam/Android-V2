package com.umcspot.spot.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.umcspot.spot.datastore.token.SpotSecureDataStoreSerializer
import com.umcspot.spot.datastore.token.SpotTokenData
import com.umcspot.spot.datastore.userId.SpotUserIdData
import com.umcspot.spot.datastore.userId.SpotUserIdDataStoreSerializer
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

    @Provides
    @Singleton
    fun provideSpotUserIdDataStore(
        @ApplicationContext context: Context,
        serializer: SpotUserIdDataStoreSerializer
    ): DataStore<SpotUserIdData> {
        return DataStoreFactory.create(
            serializer = serializer,
            produceFile = { context.dataStoreFile("spot_userId.secure") }
        )
    }
}
