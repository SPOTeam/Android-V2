package com.umcspot.spot.login.datasourceimpl

import com.umcspot.spot.login.datasource.LoginDataSource
import com.umcspot.spot.login.dto.response.TokenResponseDto
import com.umcspot.spot.login.service.LoginService
import com.umcspot.spot.network.model.BaseResponse
import javax.inject.Inject

class LoginDataSourceImpl @Inject constructor(
    private val loginService: LoginService
) : LoginDataSource {
    override suspend fun getRedirectUrl(
        type : String
    ): BaseResponse<String> =
        loginService.getRedirectUrl(type)

    override suspend fun getCallBackToken(
        type : String,
        code : String
    ): BaseResponse<TokenResponseDto> =
        loginService.getCallBackToken(type, code)

}