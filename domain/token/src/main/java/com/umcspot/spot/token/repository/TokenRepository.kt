package com.umcspot.spot.token.repository

import com.umcspot.spot.model.SocialLoginType
import com.umcspot.spot.token.model.TokenResult

interface TokenRepository {
    suspend fun finishSocialLogin(type : SocialLoginType, accessToken : String) : Result<TokenResult>
}