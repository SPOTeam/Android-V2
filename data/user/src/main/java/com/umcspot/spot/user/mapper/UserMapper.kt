package com.umcspot.spot.user.mapper

import com.umcspot.spot.model.StudyTheme
import com.umcspot.spot.user.dto.request.UserThemeRequestDto
import com.umcspot.spot.user.model.UserResult
import com.umcspot.spot.user.dto.response.UserResponseDto
import com.umcspot.spot.user.dto.response.UserThemeResponseDto
import com.umcspot.spot.user.model.UserTheme
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

fun List<StudyTheme>.toRequestDto(): UserThemeRequestDto =
    UserThemeRequestDto(userThemes = this)

// DTO -> Domain
fun UserResponseDto.toDomain(): UserResult =
    UserResult(
        name = this.name
    )

fun UserThemeResponseDto.toDomain(): UserTheme =
    UserTheme(
        userThemes = userThemes
    )

