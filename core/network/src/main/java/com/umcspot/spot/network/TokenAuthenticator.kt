package com.umcspot.spot.network

import androidx.datastore.core.DataStore
import com.umcspot.spot.datastore.token.SpotTokenData
import com.umcspot.spot.datastore.userId.SpotUserIdData
import com.umcspot.spot.network.service.TokenRefreshService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val spotTokenDataStore: DataStore<SpotTokenData>,
    private val spotUserIdDataStore: DataStore<SpotUserIdData>,
    private val tokenRefreshService: TokenRefreshService
) : Authenticator {

    private val refreshLock = Any()
    private object RefreshAttempted

    override fun authenticate(route: Route?, response: Response): Request? {
        val refreshAttempted = response.request.tag(RefreshAttempted::class.java) != null
        if (refreshAttempted) {
            clearAuthData()
            return null
        }
        if (responseCount(response) >= 2) return null
        if (response.request.url.encodedPath.endsWith("/api/auth/reissue")) return null

        val requestAccessToken = response.request.header("Authorization")
            ?.removePrefix("Bearer ")
            .orEmpty()
        val latestAccessToken = runBlocking {
            spotTokenDataStore.data.first().accessToken
        }

        if (latestAccessToken.isNotBlank() && latestAccessToken != requestAccessToken) {
            return response.request.newBuilder()
                .header("Authorization", "Bearer $latestAccessToken")
                .tag(RefreshAttempted::class.java, RefreshAttempted)
                .build()
        }

        synchronized(refreshLock) {
            val tokenData = runBlocking { spotTokenDataStore.data.first() }
            if (tokenData.accessToken.isNotBlank() && tokenData.accessToken != requestAccessToken) {
                return response.request.newBuilder()
                    .header("Authorization", "Bearer ${tokenData.accessToken}")
                    .tag(RefreshAttempted::class.java, RefreshAttempted)
                    .build()
            }

            val refreshToken = tokenData.refreshToken
            if (refreshToken.isBlank()) {
                clearAuthData()
                return null
            }

            val refreshResponse = runBlocking {
                try {
                    tokenRefreshService.refreshTokenData(refreshToken)
                } catch (e: Exception) {
                    clearAuthData()
                    null
                }
            }
            if (refreshResponse == null) return null

            if (!refreshResponse.isSuccess) {
                clearAuthData()
                return null
            }

            val result = refreshResponse.result
            runBlocking {
                spotTokenDataStore.updateData { current ->
                    current.copy(
                        accessToken = result.accessToken,
                        refreshToken = result.refreshToken
                    )
                }
                spotUserIdDataStore.updateData { current ->
                    current.copy(userId = result.userId)
                }
            }

            return response.request.newBuilder()
                .header("Authorization", "Bearer ${result.accessToken}")
                .tag(RefreshAttempted::class.java, RefreshAttempted)
                .build()
        }
    }

    private fun clearAuthData() {
        runBlocking {
            spotTokenDataStore.updateData { current ->
                current.copy(accessToken = "", refreshToken = "")
            }
            spotUserIdDataStore.updateData { current ->
                current.copy(userId = "")
            }
        }
    }

    private fun responseCount(response: Response): Int {
        var count = 1
        var prior = response.priorResponse
        while (prior != null) {
            count++
            prior = prior.priorResponse
        }
        return count
    }
}
