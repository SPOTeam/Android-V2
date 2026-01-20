package com.umcspot.spot.user.repository

import com.umcspot.spot.model.StudyTheme
import com.umcspot.spot.user.model.UserPreferredRegionResult
import com.umcspot.spot.user.model.UserResult
import com.umcspot.spot.user.model.UserTheme

interface UserRepository {
    suspend fun getUserName() : Result<UserResult>
    suspend fun setUserName(name : String) : Result<Unit>
    suspend fun setUserTheme(theme : List<StudyTheme>) : Result<Unit>
    suspend fun setUserPreferredRegion(regions: List<String>) : Result<Unit>
    suspend fun getUserPreferredRegion() : Result<UserPreferredRegionResult>
}