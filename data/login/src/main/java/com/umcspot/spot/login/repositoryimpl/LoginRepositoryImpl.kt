package com.umcspot.spot.login.repositoryimpl

import androidx.datastore.core.DataStore
import com.umcspot.spot.datastore.token.SpotTokenData
import com.umcspot.spot.datastore.userId.SpotUserIdData
import com.umcspot.spot.login.datasource.LoginDataSource
import com.umcspot.spot.login.mapper.toDomain
import com.umcspot.spot.model.SocialLoginType
import com.umcspot.spot.token.model.TokenResult
import com.umcspot.spot.token.repository.TokenRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginDataStore: LoginDataSource,
    private val spotTokenDataStore: DataStore<SpotTokenData>,
    private val spotUserIdDataStore: DataStore<SpotUserIdData>
) : TokenRepository {

    override suspend fun finishSocialLogin(
        type: SocialLoginType,
        accessToken: String
    ): Result<Unit> =
        runCatching {
            val response = loginDataStore.getCallBackToken(type.title, accessToken)
            val tokenResult: TokenResult = response.result.toDomain()

            spotTokenDataStore.updateData { current ->
                current.copy(
                    accessToken = tokenResult.accessToken,
                    refreshToken = tokenResult.refreshToken
                )
            }

            spotUserIdDataStore.updateData { current ->
                current.copy(
                    userId = tokenResult.userId
                )
            }
        }

    override suspend fun refreshTokenData(
    ): Result<Unit> =
        runCatching {
            val tokenData = spotTokenDataStore.data.first()
            val refreshToken = tokenData.refreshToken


            require(refreshToken.isNotBlank()) { "Refresh token is empty" }

            val response = loginDataStore.refreshTokenData(refreshToken)
            val tokenResult: TokenResult = response.result.toDomain()

            spotTokenDataStore.updateData { current ->
                current.copy(
                    accessToken = tokenResult.accessToken,
                    refreshToken = tokenResult.refreshToken
                )
            }

            spotUserIdDataStore.updateData { current ->
                current.copy(userId = tokenResult.userId)
            }
        }

    override suspend fun spotLogout(): Result<Unit> =
        runCatching {
            loginDataStore.spotLogout().code

            spotTokenDataStore.updateData { current ->
                current.copy(
                    accessToken = "",
                    refreshToken = ""
                )
            }

            spotUserIdDataStore.updateData { current ->
                current.copy(userId = "")
            }
        }


    override suspend fun getUserId(): String {
        return spotUserIdDataStore.data.first().userId
    }
}
