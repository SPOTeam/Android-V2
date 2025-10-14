package com.umcspot.spot.home.model

import com.umcspot.spot.model.QuickMenuType
import com.umcspot.spot.model.WeatherType
import com.umcspot.spot.study.model.ImageRef
import com.umcspot.spot.study.model.StudyResult
import com.umcspot.spot.weather.model.Weather
import com.umcspot.spot.weather.model.WeatherResult
import java.time.LocalTime

data class Home(
    val id: Int,
    val email: String,
    val weather: Weather
)

data class QuickMenuItem(
    val iconRes: Int,
    val label: String,
    val type: QuickMenuType
)

object HomeDummies {

    fun weather(now: LocalTime = LocalTime.now()) = WeatherResult(
        currentTime = now,
        weatherTemp = 23.4,
        weatherType = WeatherType.SUNNY
    )

    // ⚠️ StudyResult 필드에 맞춰 필요 시 수정
    private fun study(i: Int) = StudyResult(
        studyId = "dummy-$i",
        title = listOf("알고리즘 스터디", "모바일 앱 클론", "면접 대비 CS")[i % 3],
        goal = "주 2회 진행, 코드리뷰",
        maxMember = 10,
        member = 3 + (i % 4),
        likes = 12 + i,
        views = 200 + i * 15,
        studyImage = ImageRef.None // non-null이면 더미 이미지/리소스 채워 넣기
    )

    fun popular(count: Int = 3): List<StudyResult> = List(count) { study(it) }
    fun recommended(count: Int = 3): List<StudyResult> = List(count) { study(it + 3) }

    fun home(): HomeResult = HomeResult(
        weatherInfo = weather(),
        popularStudies = popular(),
        recommendedStudies = recommended()
    )
}