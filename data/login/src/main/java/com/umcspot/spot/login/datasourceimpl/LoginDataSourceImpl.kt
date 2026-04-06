package com.umcspot.spot.login.datasourceimpl

import com.umcspot.spot.login.datasource.LoginDataSource
import com.umcspot.spot.login.dto.response.TokenResponseDto
import com.umcspot.spot.login.service.LoginService
import com.umcspot.spot.network.model.BaseResponse
import com.umcspot.spot.network.model.NullResultResponse
import javax.inject.Inject

class LoginDataSourceImpl @Inject constructor(
    private val loginService: LoginService
) : LoginDataSource {

    override suspend fun getCallBackToken(
        type: String,
        accessToken: String
    ): BaseResponse<TokenResponseDto> =
        loginService.getCallBackToken(type, accessToken)

    override suspend fun refreshTokenData(refreshToken: String): BaseResponse<TokenResponseDto> =
        loginService.refreshTokenData(refreshToken)

    override suspend fun spotLogout() : NullResultResponse =
        loginService.spotLogout()
}