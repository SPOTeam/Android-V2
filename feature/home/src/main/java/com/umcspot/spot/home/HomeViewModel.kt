// HomeViewModel.kt (핵심만)
package com.umcspot.spot.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umcspot.spot.home.model.Home
import com.umcspot.spot.home.model.HomeResult
import com.umcspot.spot.home.repository.HomeRepository
import com.umcspot.spot.ui.state.UiState
import com.umcspot.spot.weather.model.Weather
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {
    data class HomeUiState(
        val user: UiState<HomeResult> = UiState.Empty,
    )
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    fun load() {
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

        // 3) 실제 호출 (지금은 API가 안 떠 있어도 OK)
        viewModelScope.launch {
            homeRepository.getHomeData(request)
                .onSuccess { result ->
                    _uiState.update { it.copy(user = UiState.Success(result)) }
                }
                .onFailure {
//                    _uiState.update { it.copy(user = UiState.Success(HomeDummies.home())) }
                }
        }
    }
}
