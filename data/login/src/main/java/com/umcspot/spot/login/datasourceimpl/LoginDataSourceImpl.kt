package com.umcspot.spot.login.datasourceimpl

import com.umcspot.spot.login.datasource.LoginDataSource
import com.umcspot.spot.login.dto.response.TokenResponseDto
import com.umcspot.spot.login.service.LoginService
import com.umcspot.spot.network.model.BaseResponse
import javax.inject.Inject

class LoginDataSourceImpl @Inject constructor(
    private val loginService: LoginService
) : LoginDataSource {

    override suspend fun finishSocialLogin(
        type: String,
        accessToken: String
    ): BaseResponse<TokenResponseDto> =
        loginService.getCallBackToken(type, accessToken)
}