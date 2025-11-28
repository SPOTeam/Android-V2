package com.umcspot.spot.login.service

import com.umcspot.spot.login.dto.response.TokenResponseDto
import com.umcspot.spot.network.model.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LoginService {

    @GET("/api/oauth/client/{type}")
    suspend fun getCallBackToken(
        @Path("type") type : String,
        @Query("accessToken") accessToken : String
    ) : BaseResponse<TokenResponseDto>

}
