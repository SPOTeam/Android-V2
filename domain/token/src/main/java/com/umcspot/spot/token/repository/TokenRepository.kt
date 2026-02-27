package com.umcspot.spot.token.repository

import com.umcspot.spot.model.SocialLoginType

interface TokenRepository {
    suspend fun finishSocialLogin(type : SocialLoginType, accessToken : String) : Result<Unit>

    suspend fun refreshTokenData() : Result<Unit>

    suspend fun spotLogout(): Result<Unit>

    suspend fun getUserId(): String
}