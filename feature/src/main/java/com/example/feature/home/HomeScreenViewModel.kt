package com.example.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.ui.component.weather.WeatherType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

// 화면 전체 상태
data class HomeUiState(
    val isLoading: Boolean = false,
    val weatherTemp: Int? = null,
    val weatherType: WeatherType? = null,
    val currentTime: LocalTime? = null,
    val popularStudies: List<StudyItem> = emptyList(),
    val recommendedStudies: List<StudyItem> = emptyList(),
    val error: String? = null
)


data class StudyItem(
    val id: String,
    val title: String,
    val goal: String,
    val members: String = "10/10",
    val likes: String = "100",
    val views: String = "999+",
    val thumbnailRes: Int? = null // 실제 리소스 연결은 뷰에서
)

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    // 나중에 Repository 주입
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        load()
    }

    fun load() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, error = null) }
        try {
            // TODO: 실제 API 호출로 교체
            delay(400)

            val now = LocalTime.now()
            val pop = List(3) { i -> StudyItem("p$i", "Popular #$i", "Goal $i") }
            val rec = List(3) { i -> StudyItem("r$i", "Recommend #$i", "Goal $i") }

            _uiState.update {
                it.copy(
                    isLoading = false,
                    weatherTemp = 15,
                    weatherType = WeatherType.SUNNY,
                    currentTime = now,
                    popularStudies = pop,
                    recommendedStudies = rec,
                    error = null
                )
            }
        } catch (t: Throwable) {
            _uiState.update {
                it.copy(isLoading = false, error = (t.message ?: "알 수 없는 오류"))
            }
        }
    }

    fun refreshRecommend() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, error = null) }
        try {
            // TODO: 추천만 갱신하는 실제 로직으로 교체
            delay(250)
            val rec = List(3) { i -> StudyItem("r${System.nanoTime()}-$i", "Refreshed #$i", "New Goal $i") }
            _uiState.update {
                it.copy(
                    isLoading = false,
                    recommendedStudies = rec
                )
            }
        } catch (t: Throwable) {
            _uiState.update { it.copy(isLoading = false, error = (t.message ?: "추천 갱신 실패")) }
        }
    }
}
