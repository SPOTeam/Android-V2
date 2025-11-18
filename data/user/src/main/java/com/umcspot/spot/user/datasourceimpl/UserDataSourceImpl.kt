package com.umcspot.spot.user.datasourceimpl

import com.umcspot.spot.model.StudyTheme
import com.umcspot.spot.network.model.BaseResponse
import com.umcspot.spot.network.model.NullResultResponse
import com.umcspot.spot.user.datasource.UserDataSource
import com.umcspot.spot.user.dto.request.UserNameRequestDto
import com.umcspot.spot.user.dto.request.UserThemeRequestDto
import com.umcspot.spot.user.dto.response.UserResponseDto
import com.umcspot.spot.user.dto.response.UserThemeResponseDto
import com.umcspot.spot.user.service.UserService
import javax.inject.Inject


class UserDataSourceImpl @Inject constructor(
    private val userService: UserService
) : UserDataSource {

    override suspend fun getUser(

    ): BaseResponse<UserResponseDto> =
        userService.getUser()

    override suspend fun setUserName(
        name : UserNameRequestDto
    ): NullResultResponse =
        userService.setUserName(name)

    override suspend fun setUserTheme(
        themes: UserThemeRequestDto
    ): NullResultResponse =
        userService.setUserTheme(themes)

}