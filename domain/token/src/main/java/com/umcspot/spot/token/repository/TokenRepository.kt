package com.umcspot.spot.token.repository

import com.umcspot.spot.model.SocialLoginType
import com.umcspot.spot.token.model.TokenResult

interface TokenRepository {

    suspend fun getRedirectUrl(type : SocialLoginType) : Result<String>

    suspend fun getCallBackToken(type : SocialLoginType, code : String) : Result<TokenResult>
}