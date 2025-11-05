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
            userName.data.toDomain()
        }.recoverCatching {
            UserResult(name = "추연우")
        }

    override suspend fun setUserTheme(theme: List<StudyTheme>): Result<UserTheme> =
        runCatching {
            val response = userService.setUserTheme(theme.toRequestDto())
            response.data.toDomain()
        }.recoverCatching {
            UserTheme(
                userThemes = listOf(StudyTheme.DISCUSSION, StudyTheme.SELFSTUDY)
            )
        }
}