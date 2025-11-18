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
            val response = userService.setUserName(name.toRequestDto())

            if (!response.isSuccess) {
                throw IllegalStateException("API 실패: code=${response.code}, msg=${response.message}")
            }
        }


    override suspend fun setUserTheme(theme: List<StudyTheme>): Result<Unit> =
        runCatching {
            val response = userService.setUserTheme(theme.toRequestDto())
//            response.result.toDomain()
        }.recoverCatching {
            UserTheme(
                userThemes = listOf(StudyTheme.MAJOR_CAREER, StudyTheme.SELF_STUDY)
            )
        }
}