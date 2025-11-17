package com.umcspot.spot.login.repositoryimpl

import com.umcspot.spot.login.mapper.toDomain
import com.umcspot.spot.login.service.LoginService
import com.umcspot.spot.model.SocialLoginType
import com.umcspot.spot.token.model.TokenResult
import com.umcspot.spot.token.repository.TokenRepository
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val studyService: LoginService
) : TokenRepository {

    override suspend fun finishSocialLogin(
        type: SocialLoginType,
        accessToken: String
    ): Result<TokenResult> =
        runCatching {
            val response = studyService.getCallBackToken(type.title, accessToken)
            response.result.toDomain()
        }
}