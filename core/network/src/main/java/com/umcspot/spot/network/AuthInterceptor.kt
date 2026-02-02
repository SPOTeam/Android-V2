package com.umcspot.spot.network

import androidx.datastore.core.DataStore
import com.umcspot.spot.datastore.token.SpotTokenData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val spotTokenDataStore: DataStore<SpotTokenData>
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val accessToken = runBlocking {
            spotTokenDataStore.data.first().accessToken
        }

        // 토큰 없으면 그냥 원래 요청 진행
        if (accessToken.isBlank()) {
            return chain.proceed(original)
        }

        // 토큰 있으면 Authorization 헤더 추가
        val newRequest = original.newBuilder()
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        return chain.proceed(newRequest)
    }
}
