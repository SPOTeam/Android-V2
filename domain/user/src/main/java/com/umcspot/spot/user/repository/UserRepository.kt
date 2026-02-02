package com.umcspot.spot.user.repository

import com.umcspot.spot.model.StudyTheme
import com.umcspot.spot.user.model.MyPageResult
import com.umcspot.spot.user.model.UserPreferredCategoryResult
import com.umcspot.spot.user.model.UserPreferredRegionResult
import com.umcspot.spot.user.model.UserResult

interface UserRepository {
    suspend fun getUserName() : Result<UserResult>
    suspend fun setUserName(name : String) : Result<Unit>
    suspend fun setUserTheme(theme : List<StudyTheme>) : Result<Unit>
    suspend fun setUserPreferredRegion(regions: List<String>) : Result<Unit>
    suspend fun getUserPreferredRegion() : Result<UserPreferredRegionResult>

    suspend fun getUserPreferredRegionName() : Result<UserPreferredRegionResult>

    suspend fun getMyPageInfo() : Result<MyPageResult>
    suspend fun getUserPreferredCategory() : Result<UserPreferredCategoryResult>

    suspend fun leaveSpot() : Result<String>
}