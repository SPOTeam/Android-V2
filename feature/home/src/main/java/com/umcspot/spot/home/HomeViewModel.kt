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
    private val studyRepository: StudyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState

    companion object {
        private const val FALLBACK_LONGITUDE = 126.9780
        private const val FALLBACK_LATITUDE = 37.5668
    }

    @SuppressLint("MissingPermission")
    fun loadWithLocation(fusedClient: FusedLocationProviderClient) {
        fusedClient.lastLocation
            .addOnSuccessListener { loc ->
                val latitude = loc?.latitude ?: FALLBACK_LATITUDE
                val longitude = loc?.longitude ?: FALLBACK_LONGITUDE
                load(latitude, longitude)
            }
            .addOnFailureListener { e ->
                Log.e("HomeViewModel", "loadWithLocation error", e)
                load(FALLBACK_LATITUDE, FALLBACK_LONGITUDE)
            }
    }

    fun load(latitude: Double = FALLBACK_LATITUDE, longitude: Double = FALLBACK_LONGITUDE) {
        loadWeather(latitude, longitude)
        loadPosts()
    }

    private fun loadWeather(latitude: Double, longitude: Double) {
        _uiState.update { it.copy(weatherInfo = UiState.Loading) }
        viewModelScope.launch {
            weatherRepository.getWeather(latitude, longitude)
                .onSuccess { weather ->
                    _uiState.update { it.copy(weatherInfo = UiState.Success(weather)) }
                }
                .onFailure { e ->
                    Log.e("HomeViewModel", "loadWeather error", e)
                }
        }
    }

    private fun loadPosts() {
        _uiState.update {
            it.copy(
                popularPostInfo = UiState.Loading,
                popularStudies = UiState.Loading,
                recommendStudies = UiState.Loading
            )
        }

        viewModelScope.launch {
            boardRepository.getBestSinglePost()
                .onSuccess { info ->
                    _uiState.update { it.copy(popularPostInfo = UiState.Success(info)) }
                }
                .onFailure { e ->
                    Log.e("HomeViewModel", "loadBestSinglePost error", e)
                }
        }

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
            }.onFailure { e ->
                Log.e("HomeViewModel", "loadRecruitingStudies error", e)
            }
        }

        viewModelScope.launch {
            studyRepository.getRecommendedStudies()
                .onSuccess { list ->
                    _uiState.update { it.copy(recommendStudies = UiState.Success(list)) }
                }
                .onFailure { e ->
                    Log.e("HomeViewModel", "loadRecommendedStudies error", e)
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
                }
        }
    }
}