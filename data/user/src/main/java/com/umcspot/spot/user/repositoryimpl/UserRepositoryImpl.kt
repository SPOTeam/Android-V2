package com.umcspot.spot.user.repositoryimpl

import com.umcspot.spot.model.StudyTheme
import com.umcspot.spot.user.mapper.toDomain
import com.umcspot.spot.user.mapper.toRequestDto
import com.umcspot.spot.user.model.UserResult
import com.umcspot.spot.user.model.UserTheme
import com.umcspot.spot.user.repository.UserRepository
import com.umcspot.spot.user.service.UserService
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userService: UserService
) : UserRepository {
    override suspend fun getUserName(): Result<UserResult> =
        runCatching {
            val userName = userService.getUser()
            userName.result.toDomain()
        }.recoverCatching {
            UserResult(name = "123")
        }

    override suspend fun setUserName(name: String): Result<Unit> =
        runCatching {
            userService.setUserName(name.toRequestDto())
        }


    override suspend fun setUserTheme(theme: List<StudyTheme>): Result<Unit> =
        runCatching {
            userService.setUserTheme(theme.toRequestDto())
        }
}