package com.example.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.global.WeatherType
import com.example.core.data.home.HomeUiState
import com.example.core.data.study.StudyItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    // TODO: Repository 주입 예정
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        load()
    }

    /** 초기 데이터 로드 (더미) */
    fun load() = viewModelScope.launch {
        try {
            // 실제 API 대체
            delay(300)

            val now = LocalTime.now()

            val popular = listOf(
                StudyItem(id = "p1", title = "Sample Study", goal = "Sample Goal", maxMember = 10, member = 10, likes = 100, views = 3400),
                StudyItem(id = "p2", title = "Android Compose", goal = "UI 클린업",    maxMember = 8,  member = 6,  likes = 250,  views = 999),
                StudyItem(id = "p3", title = "CS 기초",         goal = "알고리즘",     maxMember = 12, member = 9,  likes = 1200, views = 1200),
            )

            val recommended = listOf(
                StudyItem(id = "r1", title = "코틀린 협업",  goal = "코루틴/Flow", maxMember = 6, member = 3, likes = 87,   views = 540),
                StudyItem(id = "r2", title = "iOS 입문",     goal = "SwiftUI",    maxMember = 5, member = 1, likes = 12,   views = 88),
                StudyItem(id = "r3", title = "백엔드 스터디", goal = "Spring",     maxMember = 10, member = 4, likes = 430,  views = 2000),
            )

            _uiState.update {
                it.copy(
                    weatherTemp = 23,
                    weatherType = WeatherType.SUNNY,
                    currentTime = now,
                    popularStudies = popular,
                    recommendedStudies = recommended,
                    error = null
                )
            }
        } catch (t: Throwable) {
            _uiState.update { it.copy(error = t.message ?: "알 수 없는 오류") }
        }
    }

    /** 추천 스터디만 새로고침 (더미) */
    fun refreshRecommend() = viewModelScope.launch {
        try {
            delay(200)
            val refreshed = listOf(
                StudyItem(id = "r${System.currentTimeMillis()}", title = "Refreshed A", goal = "New Goal A", maxMember = 8, member = 5, likes = 90,  views = 1300),
                StudyItem(id = "r${System.nanoTime()}",          title = "Refreshed B", goal = "New Goal B", maxMember = 10, member = 7, likes = 12, views = 70),
                StudyItem(id = "r${System.nanoTime()+1}",        title = "Refreshed C", goal = "New Goal C", maxMember = 12, member = 6, likes = 1005, views = 999),
            )
            _uiState.update { it.copy(recommendedStudies = refreshed) }
        } catch (t: Throwable) {
            _uiState.update { it.copy(error = t.message ?: "추천 갱신 실패") }
        }
    }
}
