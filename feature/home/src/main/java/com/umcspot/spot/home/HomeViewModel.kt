// HomeViewModel.kt (핵심만)
package com.umcspot.spot.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umcspot.spot.home.model.Home
import com.umcspot.spot.home.model.HomeDummies
import com.umcspot.spot.home.model.HomeResult
import com.umcspot.spot.home.repository.HomeRepository
import com.umcspot.spot.ui.state.UiState
import com.umcspot.spot.weather.model.Weather
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {
    data class HomeUiState(val user: UiState<HomeResult> = UiState.Empty)
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var fallbackJob: Job? = null

    fun getDummies() {
        val request = Home(
            id = 1,
            email = "123123123",
            weather = Weather(
                longitude = 126.9780,
                latitude = 37.5665
            )
        )

        // 1) 먼저 로딩으로 전환
        _uiState.update { it.copy(user = UiState.Loading) }

        // 2) 로딩이 timeout 이상 지속되면 더미로 대체
        startDummyFallback(timeoutMs = 1_500L)

        // 3) 실제 호출 (지금은 API가 안 떠 있어도 OK)
        viewModelScope.launch {
            homeRepository.getDummies(request)
                .onSuccess { result ->
                    cancelFallback()
                    _uiState.update { it.copy(user = UiState.Success(result)) }
                }
                .onFailure {
                    cancelFallback()
                    // 실패 시에도 더미로 대체
                    _uiState.update { it.copy(user = UiState.Success(HomeDummies.home())) }
                }
        }
    }

    private fun startDummyFallback(timeoutMs: Long) {
        cancelFallback()
        fallbackJob = viewModelScope.launch {
            delay(timeoutMs)
            // 아직도 로딩이면 더미 주입
            if (_uiState.value.user is UiState.Loading) {
                _uiState.update { it.copy(user = UiState.Success(HomeDummies.home())) }
            }
        }
    }

    private fun cancelFallback() {
        fallbackJob?.cancel()
        fallbackJob = null
    }
}
