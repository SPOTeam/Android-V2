package com.umcspot.spot.user.datasource

import com.umcspot.spot.model.StudyTheme
import com.umcspot.spot.network.model.BaseResponse
import com.umcspot.spot.user.dto.request.UserThemeRequestDto
import com.umcspot.spot.user.dto.response.UserResponseDto
import com.umcspot.spot.user.dto.response.UserThemeResponseDto

interface UserDataSource {
    suspend fun getUser(): BaseResponse<UserResponseDto>

    suspend fun setUserTheme(themes : UserThemeRequestDto): BaseResponse<UserThemeResponseDto>

}