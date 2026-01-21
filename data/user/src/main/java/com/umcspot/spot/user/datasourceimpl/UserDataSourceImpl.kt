package com.umcspot.spot.user.datasourceimpl

import android.util.Log
import com.umcspot.spot.model.StudyTheme
import com.umcspot.spot.network.model.BaseResponse
import com.umcspot.spot.network.model.NullResultResponse
import com.umcspot.spot.user.datasource.UserDataSource
import com.umcspot.spot.user.dto.request.UserNameRequestDto
import com.umcspot.spot.user.dto.request.UserThemeRequestDto
import com.umcspot.spot.user.dto.response.MyPageResponseDto
import com.umcspot.spot.user.dto.response.UserPreferredCategoryResponseDto
import com.umcspot.spot.user.dto.response.UserPreferredRegionResponseDto
import com.umcspot.spot.user.dto.response.UserResponseDto
import com.umcspot.spot.user.dto.response.UserThemeResponseDto
import com.umcspot.spot.user.mapper.toPreferredRegionRequestDto
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

    override suspend fun setUserPreferredRegion(regions: List<String>): NullResultResponse {
        Log.d("UserDataSource", "setUserPreferredRegion: ${regions.toPreferredRegionRequestDto()}")

        val res = userService.setUserPreferredRegion(regions.toPreferredRegionRequestDto())
        return res
    }

    override suspend fun getUserPreferredRegion(): BaseResponse<UserPreferredRegionResponseDto> =
        userService.getUserPreferredRegion()

    override suspend fun getMyPageInfo(): BaseResponse<MyPageResponseDto> =
        userService.getMyPageInfo()

    override suspend fun getUserPreferredCategory(): BaseResponse<UserPreferredCategoryResponseDto> =
        userService.getUserPreferredCategory()
}