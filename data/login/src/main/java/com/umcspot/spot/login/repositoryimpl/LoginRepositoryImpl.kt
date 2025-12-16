package com.umcspot.spot.login.repositoryimpl

import androidx.datastore.core.DataStore
import com.umcspot.spot.datastore.SpotTokenData
import com.umcspot.spot.login.mapper.toDomain
import com.umcspot.spot.login.service.LoginService
import com.umcspot.spot.model.SocialLoginType
import com.umcspot.spot.token.model.TokenResult
import com.umcspot.spot.token.repository.TokenRepository
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val studyService: LoginService,
    private val spotTokenDataStore: DataStore<SpotTokenData>
) : TokenRepository {

    override suspend fun finishSocialLogin(
        type: SocialLoginType,
        accessToken: String
    ): Result<TokenResult> =
        runCatching {
            val response = studyService.getCallBackToken(type.title, accessToken)
            val tokenResult: TokenResult = response.result.toDomain()

            spotTokenDataStore.updateData { current ->
                current.copy(
                    accessToken = tokenResult.accessToken,
                    refreshToken = tokenResult.refreshToken
                )
            }

            tokenResult
        }
}