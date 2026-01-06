package com.umcspot.spot.user.repositoryimpl

import android.util.Log
import com.umcspot.spot.model.StudyTheme
import com.umcspot.spot.user.datasource.UserDataSource
import com.umcspot.spot.user.mapper.toDomain
import com.umcspot.spot.user.mapper.toRequestDto
import com.umcspot.spot.user.model.UserPreferredRegionResult
import com.umcspot.spot.user.model.UserResult
import com.umcspot.spot.user.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource
) : UserRepository {
    override suspend fun getUserName(): Result<UserResult> =
        runCatching {
            val userName = userDataSource.getUser()
            userName.result.toDomain()
        }.recoverCatching {
            UserResult(name = "123")
        }

    override suspend fun setUserName(name: String): Result<Unit> =
        runCatching {
            userDataSource.setUserName(name.toRequestDto())
        }


    override suspend fun setUserTheme(theme: List<StudyTheme>): Result<Unit> =
        runCatching {
            userDataSource.setUserTheme(theme.toRequestDto())
        }

    override suspend fun setUserPreferredRegion(regions: List<String>): Result<Unit> =
        runCatching {
            userDataSource.setUserPreferredRegion(regions)
        }


    override suspend fun getUserPreferredRegion(): Result<UserPreferredRegionResult> =
        runCatching {
            val userPreferredRegions = userDataSource.getUserPreferredRegion()
            userPreferredRegions.result.toDomain()
        }.onFailure { e ->
            Log.e("UserRepository", "getUserPreferredRegion failed", e)
        }
}