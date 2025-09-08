package com.example.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.core.data.global.WeatherType
import com.example.core.data.study.StudyItem
import java.time.LocalTime

/* ---------- Preview ---------- */
@Preview(showBackground = true)
@Composable
private fun HomeScreenContentPreview() {
    HomeScreenContent(
        temperature = 23,
        weatherType = WeatherType.SUNNY,
        currentTime = LocalTime.of(9, 41),
        popularStudies = List(3) { i ->
            StudyItem(
                id = "p$i",
                title = "Popular #$i",
                goal = "Goal $i",
                maxMember = 10,
                member = 7 + i,
                likes = 100 * (i + 1),
                views = 900 + (i * 200),
                studyImage = null
            )
        },
        recommendedStudies = emptyList(),
        onQuickMenuClick = {}
    )
}