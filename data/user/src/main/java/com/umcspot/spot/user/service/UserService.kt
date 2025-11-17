package com.umcspot.spot.user.service

import com.umcspot.spot.network.model.BaseResponse
import com.umcspot.spot.user.dto.request.UserThemeRequestDto
import com.umcspot.spot.user.dto.response.UserResponseDto
import com.umcspot.spot.user.dto.response.UserThemeResponseDto
import com.umcspot.spot.user.model.UserTheme
import retrofit2.http.Body
import retrofit2.http.GET

interface UserService {
    @GET("/api/members/name")
    suspend fun getUser(
    ): BaseResponse<UserResponseDto>

    @GET("/api/v1/service")
    suspend fun setUserTheme(
        @Body request : UserThemeRequestDto
    ): BaseResponse<UserThemeResponseDto>
}