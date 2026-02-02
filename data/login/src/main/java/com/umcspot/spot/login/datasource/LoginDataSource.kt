package com.umcspot.spot.login.datasource

import com.umcspot.spot.login.dto.response.TokenResponseDto
import com.umcspot.spot.network.model.BaseResponse
import com.umcspot.spot.network.model.NullResultResponse

interface LoginDataSource {
    suspend fun getCallBackToken(type : String, accessToken : String): BaseResponse<TokenResponseDto>

    suspend fun refreshTokenData(refreshToken : String) : BaseResponse<TokenResponseDto>

    suspend fun spotLogout(): NullResultResponse
}