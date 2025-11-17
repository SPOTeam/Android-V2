package com.umcspot.spot.login.service

import com.umcspot.spot.login.dto.response.TokenResponseDto
import com.umcspot.spot.network.model.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LoginService {
    @GET("/api/oauth/redirect-url/{type}")
    suspend fun getRedirectUrl(@Path("type") type: String): BaseResponse<String>

    @GET("/api/oauth/callback/{type}")
    suspend fun getCallBackToken(
        @Path("type") type : String,
        @Query("code") code : String
    ) : BaseResponse<TokenResponseDto>
}
