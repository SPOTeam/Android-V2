// HomeViewModel.kt (핵심만)
package com.umcspot.spot.mypage.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umcspot.spot.ui.state.UiState
import com.umcspot.spot.user.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(MyPageState())
    val uiState: StateFlow<MyPageState> = _uiState

    fun load() {
        loadPrefer()
        loadMemberInfo()
    }

    fun loadMemberInfo() {
        _uiState.update { it.copy(memberInfo = UiState.Loading) }

        viewModelScope.launch {
            userRepository.getMyPageInfo()
                .onSuccess { info ->
                    _uiState.update { it.copy(memberInfo = UiState.Success(info)) }
                }
                .onFailure { e ->
                    Log.e("HomeViewModel", "loadMyPageInfo error", e)
//                    _uiState.update { it.copy(weatherInfo = UiState.Failure(e.message ?: "날씨 불러오기 실패")) }
                }
        }
    }

    fun loadPrefer() {
        _uiState.update {
            it.copy(
                preferRegions = UiState.Loading,
                preferCategories = UiState.Loading
            )
        }

        viewModelScope.launch {
            userRepository.getUserPreferredRegionName()
                .onSuccess { info ->
                    _uiState.update { it.copy(preferRegions = UiState.Success(info.regionCodes)) }
                }
                .onFailure { e ->
                    Log.e("HomeViewModel", "loadPreferredRegion error", e)
//                    _uiState.update { it.copy(popularPostInfo = UiState.Failure(e.message ?: "인기 정보 실패")) }
                }
        }

        viewModelScope.launch {
            userRepository.getUserPreferredCategory()
                .onSuccess { info ->
                    _uiState.update { it.copy(preferCategories = UiState.Success(info.categories)) }
                }
                .onFailure { e ->
                    Log.e("HomeViewModel", "loadPreferCategories error", e)
//                    _uiState.update { it.copy(recommendStudies = UiState.Failure(e.message ?: "추천 스터디 실패")) }
                }
        }
    }
}

