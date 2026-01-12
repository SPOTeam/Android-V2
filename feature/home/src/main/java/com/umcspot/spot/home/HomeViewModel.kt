// HomeViewModel.kt (핵심만)
package com.umcspot.spot.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.umcspot.spot.domain.board.repository.BoardRepository
import com.umcspot.spot.model.RecruitingStudySort
import com.umcspot.spot.study.repository.StudyRepository
import com.umcspot.spot.ui.state.UiState
import com.umcspot.spot.weather.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val boardRepository: BoardRepository,
    private val studyRepository : StudyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState

    companion object {
        // 서울시청 fallback 좌표
        private const val FALLBACK_LONGITUDE = 126.9780
        private const val FALLBACK_LATITUDE = 37.5668
    }

    @SuppressLint("MissingPermission")
    fun loadWithLocation(fusedClient: FusedLocationProviderClient) {
        fusedClient.lastLocation
            .addOnSuccessListener { loc ->
                val longitude = loc?.longitude ?: FALLBACK_LONGITUDE
                val latitude = loc?.latitude ?: FALLBACK_LATITUDE

                load(longitude, latitude)
            }
            .addOnFailureListener { e ->
                Log.e("HomeViewModel", "loadWithLocation error", e)
                load(FALLBACK_LONGITUDE, FALLBACK_LATITUDE)
            }
    }

    fun load(longitude : Double = FALLBACK_LONGITUDE, latitude : Double = FALLBACK_LATITUDE) {
        loadWeather(longitude, latitude)
        loadPosts()
    }

    fun loadWeather(longitude : Double, latitude : Double) {
        _uiState.update { it.copy(weatherInfo = UiState.Loading) }

        viewModelScope.launch {
            weatherRepository.getWeather(longitude, latitude)
                .onSuccess { weather ->
                    _uiState.update { it.copy(weatherInfo = UiState.Success(weather)) }
                }
                .onFailure { e ->
                    Log.e("HomeViewModel", "loadWeather error", e)
//                    _uiState.update { it.copy(weatherInfo = UiState.Failure(e.message ?: "날씨 불러오기 실패")) }
                }
        }
    }

    fun loadPosts() {
        _uiState.update {
            it.copy(
                popularPostInfo = UiState.Loading,
                popularStudies = UiState.Loading,
                recommendStudies = UiState.Loading
            )
        }

        // 실시간 인기글
        viewModelScope.launch {
            boardRepository.getBestSinglePost()
                .onSuccess { info ->
                    _uiState.update { it.copy(popularPostInfo = UiState.Success(info)) }
                }
                .onFailure { e ->
                    Log.e("HomeViewModel", "loadBestSinglePost error", e)
//                    _uiState.update { it.copy(popularPostInfo = UiState.Failure(e.message ?: "인기 정보 실패")) }
                }
        }

        // 지금 가장 인기있는 스터디
        viewModelScope.launch {
            studyRepository.getRecruitingStudies(
                feeCategory = null,
                categories = null,
                isOnline = null,
                sortBy = RecruitingStudySort.HITS,
                cursor = null,
                size = 3
            ).onSuccess { list ->
                _uiState.update { it.copy(popularStudies = UiState.Success(list)) }
            }
                .onFailure { e ->
                    Log.e("HomeViewModel", "loadgetPopularStudies error", e)

//                    _uiState.update { it.copy(popularStudies = UiState.Failure(e.message ?: "인기 스터디 실패")) }
                }
        }

        // 전공/진로학습 스터디 이건 어때요
        viewModelScope.launch {
            studyRepository.getRecommendedStudies()
                .onSuccess { list ->
                    _uiState.update { it.copy(recommendStudies = UiState.Success(list)) }
                }
                .onFailure { e ->
                    Log.e("HomeViewModel", "loadgetPopularStudies error", e)

//                    _uiState.update { it.copy(recommendStudies = UiState.Failure(e.message ?: "추천 스터디 실패")) }
                }
        }
    }

    fun reLoadRecommendedStudies() {
        _uiState.update { it.copy(recommendStudies = UiState.Loading) }

        viewModelScope.launch {
            studyRepository.getRecommendedStudies()
                .onSuccess { list ->
                    _uiState.update { it.copy(recommendStudies = UiState.Success(list)) }
                }
                .onFailure { e ->
                    Log.e("HomeViewModel", "reLoadRecommendedStudies error", e)
                    // 필요하면 Failure로:
                    // _uiState.update { it.copy(recommendStudies = UiState.Failure(e.message ?: "추천 스터디 실패")) }
                }
        }
    }
}

