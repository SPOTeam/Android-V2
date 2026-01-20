package com.umcspot.spot.user.mapper

import com.umcspot.spot.model.SocialLoginType
import com.umcspot.spot.model.StudyTheme
import com.umcspot.spot.model.toImageRef
import com.umcspot.spot.user.dto.request.UserNameRequestDto
import com.umcspot.spot.user.dto.request.UserPreferredRegionRequestDto
import com.umcspot.spot.user.dto.request.UserThemeRequestDto
import com.umcspot.spot.user.dto.response.MyPageResponseDto
import com.umcspot.spot.user.dto.response.StudyParticipationInfo
import com.umcspot.spot.user.dto.response.UserPreferredCategoryResponseDto
import com.umcspot.spot.user.dto.response.UserPreferredRegionResponseDto
import com.umcspot.spot.user.model.UserResult
import com.umcspot.spot.user.dto.response.UserResponseDto
import com.umcspot.spot.user.model.MyPageResult
import com.umcspot.spot.user.model.UserPreferredCategoryResult
import com.umcspot.spot.user.model.UserPreferredRegionResult

fun List<StudyTheme>.toRequestDto(): UserThemeRequestDto =
    UserThemeRequestDto(userThemes = this)

fun String.toRequestDto(): UserNameRequestDto =
    UserNameRequestDto(name = this)

// DTO -> Domain
fun UserResponseDto.toDomain(): UserResult =
    UserResult(
        name = this.name
    )

fun List<String>.toPreferredRegionRequestDto() : UserPreferredRegionRequestDto =
    UserPreferredRegionRequestDto(
        regionCodes = this
    )

fun UserPreferredRegionResponseDto.toDomain(): UserPreferredRegionResult =
    UserPreferredRegionResult(
        regionCodes = regionCodes,
        totalCount = totalCount
    )

fun MyPageResponseDto.toDomain(): MyPageResult =
    MyPageResult(
        memberId = memberId,
        nickname = nickname,
        profileImageUrl = profileImageUrl.toImageRef(),
        loginType = SocialLoginType.from(loginType),
        email = email,
        participateCount = studyParticipationInfo.participatingStudyCount,
        recruitingCount = studyParticipationInfo.recruitingStudyCount,
        appliedCount = studyParticipationInfo.appliedStudyCount
    )

fun UserPreferredCategoryResponseDto.toDomain(): UserPreferredCategoryResult =
    UserPreferredCategoryResult(
        categories = categories.map { StudyTheme.from(it)?.title },
        totalCount = totalCount
    )
