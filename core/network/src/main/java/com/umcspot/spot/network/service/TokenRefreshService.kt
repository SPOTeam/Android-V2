package com.umcspot.spot.network.service


import com.umcspot.spot.network.model.BaseResponse
import com.umcspot.spot.network.model.TokenRefreshResponse
import retrofit2.http.Header
import retrofit2.http.POST

interface TokenRefreshService {

    @POST("/api/auth/reissue")
    suspend fun refreshTokenData(
        @Header("refreshToken") refreshToken: String,
    ): BaseResponse<TokenRefreshResponse>
}