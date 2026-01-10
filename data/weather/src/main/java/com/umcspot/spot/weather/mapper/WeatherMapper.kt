package com.umcspot.spot.weather.mapper

import com.umcspot.spot.weather.dto.request.WeatherRequestDto
import com.umcspot.spot.weather.dto.response.WeatherResponseDto
import com.umcspot.spot.weather.model.WeatherResult
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

// DTO -> Domain
fun WeatherResponseDto.toDomain(): WeatherResult =
    WeatherResult(
        currentTime = this.currentTime.toLocalTimeOrNull()!!,
        weatherTemp = this.weatherTemp,
        weatherType = this.weatherType
    )

fun String.toLocalTimeOrNull(locale: Locale = Locale.getDefault()): LocalTime? {
    val s = this.trim()
    // 1) ISO 기본 시도: HH:mm[:ss[.SSS]]
    runCatching { return LocalTime.parse(s, DateTimeFormatter.ISO_LOCAL_TIME) }

    // 2) 추가 포맷들 (필요에 따라 추가/삭제)
    val patterns = listOf(
        "H:m",            // 9:5
        "HH:mm",          // 09:05
        "H:m:s",          // 9:5:2
        "HH:mm:ss",       // 09:05:02
        "HHmm",           // 0905
        "Hmm",            // 905
        "h:m a",          // 9:05 AM / 9:05 오전 (ko locale)
        "hh:mm a",        // 09:05 AM
        "h:mma",          // 9:05AM
        "hh:mma"          // 09:05AM
    )

    for (p in patterns) {
        try {
            val fmt = DateTimeFormatter.ofPattern(p, locale)
            return LocalTime.parse(s, fmt)
        } catch (_: DateTimeParseException) { /* try next */ }
    }
    return null
}

/** 실패 시 예외를 던지는 버전이 필요하면 이걸 사용하세요. */
fun String.toLocalTimeOrThrow(locale: Locale = Locale.getDefault()): LocalTime =
    toLocalTimeOrNull(locale) ?: error("Invalid time format: '$this'")