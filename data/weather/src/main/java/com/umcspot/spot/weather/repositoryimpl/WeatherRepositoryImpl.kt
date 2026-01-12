package com.umcspot.spot.weather.repositoryimpl

import android.util.Log
import com.umcspot.spot.common.WeatherConfigFieldProvider
import com.umcspot.spot.weather.datasource.WeatherDataSource
import com.umcspot.spot.weather.mapper.toDomain
import com.umcspot.spot.weather.model.WeatherResult
import com.umcspot.spot.weather.repository.WeatherRepository
import com.umcspot.spot.weather.service.WeatherService
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.ln
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.tan

class WeatherRepositoryImpl @Inject constructor(
    private val weatherDataSource: WeatherDataSource,
    private val weatherConfigFieldProvider: WeatherConfigFieldProvider
) : WeatherRepository {
    override suspend fun getWeather(
        latitude : Double,
        longitude : Double
    ): Result<WeatherResult> =
        runCatching {
            val (nx, ny) = latLonToGrid(latitude, longitude)

            val serviceKey = weatherConfigFieldProvider.getWeather().token

            // 2) base_date / base_time 계산
            val now = LocalDateTime.now()
            val (baseDate, baseTime) = calculateBaseDateTime(now)

            // 1차 호출
            val first = weatherDataSource.getWeather(serviceKey, "JSON", baseDate, baseTime, nx, ny)

            // NO_DATA면 1시간 전으로 되돌려서 2차 호출
            val dto = if (first.response.header.resultCode == "03" &&
                first.response.header.resultMsg == "NO_DATA"
            ) {
                val (prevDate, prevTime) = calculateBaseDateTime(now.minusHours(1))
                weatherDataSource.getWeather(serviceKey, "JSON", prevDate, prevTime, nx, ny)
            } else {
                first
            }

            dto.toDomain()

        }.onFailure { e ->
            Log.e("WeatherRepository", "loadWeatherError", e)
        }.recoverCatching {
            WeatherResult.dummyFrom()
        }

    private fun calculateBaseDateTime(now: LocalDateTime): Pair<String, String> {
        val formatterDate = DateTimeFormatter.ofPattern("yyyyMMdd")
        val formatterTime = DateTimeFormatter.ofPattern("HHmm")

        val baseDate = now.format(formatterDate)
        val baseTime = now.withMinute(0).format(formatterTime) // HH00

        return baseDate to baseTime
    }

    private data class GridXY(val nx: Int, val ny: Int)

    private fun latLonToGrid(lat: Double, lon: Double): GridXY {
        // 기상청 고정 상수
        val RE = 6371.00877      // 지구 반경(km)
        val GRID = 5.0           // 격자 간격(km)
        val SLAT1 = 30.0         // 투영 위도1(degree)
        val SLAT2 = 60.0         // 투영 위도2(degree)
        val OLON = 126.0         // 기준 경도(degree)
        val OLAT = 38.0          // 기준 위도(degree)
        val XO = 43.0            // 기준 X(GRID)
        val YO = 136.0           // 기준 Y(GRID)

        val DEGRAD = Math.PI / 180.0

        val re = RE / GRID
        val slat1 = SLAT1 * DEGRAD
        val slat2 = SLAT2 * DEGRAD
        val olon = OLON * DEGRAD
        val olat = OLAT * DEGRAD

        var sn = tan(Math.PI * 0.25 + slat2 * 0.5) / tan(Math.PI * 0.25 + slat1 * 0.5)
        sn = ln(cos(slat1) / cos(slat2)) / ln(sn)

        var sf = tan(Math.PI * 0.25 + slat1 * 0.5)
        sf = sf.pow(sn) * cos(slat1) / sn

        var ro = tan(Math.PI * 0.25 + olat * 0.5)
        ro = re * sf / ro.pow(sn)

        var ra = tan(Math.PI * 0.25 + (lat * DEGRAD) * 0.5)
        ra = re * sf / ra.pow(sn)

        var theta = (lon * DEGRAD) - olon
        if (theta > Math.PI) theta -= 2.0 * Math.PI
        if (theta < -Math.PI) theta += 2.0 * Math.PI
        theta *= sn

        val x = floor(ra * sin(theta) + XO + 0.5).toInt()
        val y = floor(ro - ra * cos(theta) + YO + 0.5).toInt()

        return GridXY(nx = x, ny = y)
    }
}