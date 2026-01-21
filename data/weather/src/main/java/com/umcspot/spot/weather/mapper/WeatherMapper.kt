package com.umcspot.spot.weather.mapper

import com.umcspot.spot.model.WeatherType
import com.umcspot.spot.weather.dto.response.WeatherResponseDto
import com.umcspot.spot.weather.model.WeatherResult
import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun WeatherResponseDto.toDomain(): WeatherResult {
    val items = response.body!!.items.item

    // 기준 시간 (HHmm)
    val time = items.firstOrNull()?.baseTime
        ?.let { LocalTime.parse(it, DateTimeFormatter.ofPattern("HHmm")) }
        ?: LocalTime.MIDNIGHT

    val temp = items.find { it.category == "T1H" }
        ?.obsrValue?.toDoubleOrNull()

    val pty = items.find { it.category == "PTY" }
        ?.obsrValue

    val rain1h = items.find { it.category == "RN1" }
        ?.obsrValue?.toDoubleOrNull()

    val wind = items.find { it.category == "WSD" }
        ?.obsrValue?.toDoubleOrNull()

    val weatherType = resolveWeatherType(
        pty = pty,         // PTY: 강수 형태 코드 (0: 없음, 1: 비, 2: 비/눈, 3: 눈, 4: 소나기)
        rain1h = rain1h,   // RN1: 1시간 강수량(mm)
        wind = wind,       // WSD: 풍속 (m/s, 체감 날씨·강풍 판단 기준)
        temp = temp        // T1H: 기온 (섭씨 °C, 폭염/한파 판단용)
    )

    return WeatherResult(
        currentTime = time,
        weatherTemp = temp,
        weatherType = weatherType
    )
}
private fun resolveWeatherType(
    pty: String?,      // PTY: 강수 형태
    rain1h: Double?,   // RN1: 1시간 강수량(mm)
    wind: Double?,     // WSD: 풍속(m/s)
    temp: Double?      // T1H: 기온(°C)
): WeatherType {

    val r = (rain1h ?: 0.0).coerceAtLeast(0.0)
    val w = (wind ?: 0.0).coerceAtLeast(0.0)
    val t = temp ?: 0.0

    // 1️⃣ 실내 추천 (표: pty=1,2,4 AND rain1h > 0)
    if ((pty == "1" || pty == "2" || pty == "4") && r > 0.0) {
        return WeatherType.RAIN
    }

    // 2️⃣ 눈 (표: 눈)
    if (pty == "3") {
        return WeatherType.SNOW
    }

    // 3️⃣ 강풍 (표: wind >= 8)
    if (w >= 8.0) {
        return WeatherType.WIND
    }

    // 4️⃣ 추위 주의 (표: temp <= 9)
    if (t <= 9.0) {
        return WeatherType.COLD
    }

    // 5️⃣ 더위 주의 (표: temp >= 26)
    if (t >= 26.0) {
        return WeatherType.HOT
    }

    // 6️⃣ 좋은날 (표 조건 만족 시)
    // 좋은날 : pty=0, rain1h=0, wind<7, temp 10~25
    if (pty == "0" && r == 0.0 && w < 7.0 && t in 10.0..25.0) {
        return WeatherType.SUNNY
    }

    // 7️⃣ 그 외 기본값
    return WeatherType.SUNNY
}

