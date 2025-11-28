package com.umcspot.spot.login.datasource

import com.umcspot.spot.login.dto.response.TokenResponseDto
import com.umcspot.spot.model.SocialLoginType
import com.umcspot.spot.network.model.BaseResponse

interface LoginDataSource {
    suspend fun finishSocialLogin(type : String, accessToken : String): BaseResponse<TokenResponseDto>

}