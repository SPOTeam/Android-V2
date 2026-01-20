package com.umcspot.spot.user.mapper

import com.umcspot.spot.model.StudyTheme
import com.umcspot.spot.user.dto.request.UserNameRequestDto
import com.umcspot.spot.user.dto.request.UserPreferredRegionRequestDto
import com.umcspot.spot.user.dto.request.UserThemeRequestDto
import com.umcspot.spot.user.dto.response.UserPreferredRegionResponseDto
import com.umcspot.spot.user.model.UserResult
import com.umcspot.spot.user.dto.response.UserResponseDto
import com.umcspot.spot.user.dto.response.UserThemeResponseDto
import com.umcspot.spot.user.model.UserPreferredRegionResult
import com.umcspot.spot.user.model.UserTheme
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

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

