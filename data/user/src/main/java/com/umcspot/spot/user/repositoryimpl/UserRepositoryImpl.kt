package com.umcspot.spot.user.repositoryimpl

import android.content.Context
import android.util.Log
import com.umcspot.spot.common.location.LocationStore
import com.umcspot.spot.model.StudyTheme
import com.umcspot.spot.user.datasource.UserDataSource
import com.umcspot.spot.user.mapper.toDomain
import com.umcspot.spot.user.mapper.toRequestDto
import com.umcspot.spot.user.model.MyPageResult
import com.umcspot.spot.user.model.UserPreferredCategoryResult
import com.umcspot.spot.user.model.UserPreferredRegionResult
import com.umcspot.spot.user.model.UserResult
import com.umcspot.spot.user.repository.UserRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource,
    @ApplicationContext private val appContext: Context,
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

    override suspend fun getUserPreferredRegionName(): Result<UserPreferredRegionResult> =
        runCatching {
            val dto = userDataSource.getUserPreferredRegion()

            // assets 기반 지역 테이블 로드
            val locations = LocationStore.load(appContext)

            val codeMap = locations.associateBy { it.code }

            // 4) code 리스트를 fullName(지명) 리스트로 변환
            val regionNames = dto.result.regionCodes.mapNotNull { code ->
                codeMap[code]?.neighborhood
            }

            UserPreferredRegionResult(
                regionCodes = regionNames,
                totalCount = dto.result.totalCount
            )
        }.onFailure { e ->
            Log.e("UserRepository", "getUserPreferredRegion failed", e)
        }

    override suspend fun getMyPageInfo(): Result<MyPageResult> =
        runCatching {
            userDataSource.getMyPageInfo().result.toDomain()
        }.onFailure {
            Log.e("UserRepository", "getMyPageInfo failed", it)
        }

    override suspend fun getUserPreferredCategory(): Result<UserPreferredCategoryResult> =
        runCatching {
            userDataSource.getUserPreferredCategory().result.toDomain()
        }.onFailure {
            Log.e("UserRepository", "getUserPreferredCategory failed", it)
        }

    override suspend fun leaveSpot() : Result<String> =
        runCatching {
            userDataSource.leaveSpot().code
        }.onFailure {
            Log.e("UserRepository", "leaveSpot failed", it)
        }
}