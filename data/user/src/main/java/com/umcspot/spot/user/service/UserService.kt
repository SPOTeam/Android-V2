package com.umcspot.spot.user.service

import com.umcspot.spot.network.model.BaseResponse
import com.umcspot.spot.network.model.NullResultResponse
import com.umcspot.spot.user.dto.request.UserNameRequestDto
import com.umcspot.spot.user.dto.request.UserThemeRequestDto
import com.umcspot.spot.user.dto.response.UserResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserService {
    @GET("/api/members/name")
    suspend fun getUser(
    ): BaseResponse<UserResponseDto>

    @POST("/api/members/name")
    suspend fun setUserName(
        @Body request : UserNameRequestDto
    ): NullResultResponse

    @POST("/api/members/preferred-categories")
    suspend fun setUserTheme(
        @Body request : UserThemeRequestDto
    ): NullResultResponse
}