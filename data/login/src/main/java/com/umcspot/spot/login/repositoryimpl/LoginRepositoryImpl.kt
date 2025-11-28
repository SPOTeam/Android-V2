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
            // 1) 소셜 로그인 콜백 토큰 서버로부터 받기
            val response = studyService.getCallBackToken(type.title, accessToken)
            val tokenResult: TokenResult = response.result.toDomain()

            // 2) DataStore에 access / refresh 저장
            spotTokenDataStore.updateData { current ->
                current.copy(
                    accessToken = tokenResult.accessToken,
                    refreshToken = tokenResult.refreshToken
                )
            }

            // 3) ViewModel 에서 추가 처리 필요하면 쓰라고 그대로 반환
            tokenResult
        }
}