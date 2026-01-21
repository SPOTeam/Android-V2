package com.umcspot.spot.mypage.participating

import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umcspot.spot.study.repository.StudyRepository
import com.umcspot.spot.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParticipatingStudyViewModel @Inject constructor(
    private val studyRepository: StudyRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ParticipatingStudyState())
    val uiState: StateFlow<ParticipatingStudyState> = _uiState

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()

    fun loadParticipatingStudy() {
        _uiState.update { it.copy(participatingStudy = UiState.Loading) }

        viewModelScope.launch {
            studyRepository.getParticipatingStudy(
                cursor = null,
                size = 10
            )
            .onSuccess { info ->
                _uiState.update { it.copy(participatingStudy = UiState.Success(info)) }
            }
            .onFailure { e ->
                Log.e("HomeViewModel", "loadParticipatingStudy error", e)
//                    _uiState.update { it.copy(weatherInfo = UiState.Failure(e.message ?: "날씨 불러오기 실패")) }
            }
        }
    }

    fun loadNextPage() {
        val currentUi = _uiState.value.participatingStudy
        val success = currentUi as? UiState.Success ?: return
        val currentList = success.data

        if (!currentList.hasNext) return
        if (_isLoadingMore.value) return

        viewModelScope.launch {
            _isLoadingMore.value = true
            runCatching {
                studyRepository.getParticipatingStudy(
                    cursor = currentList.nextCursor,
                    size = 10
                ).getOrThrow()
            }.onSuccess { newPage ->
                val merged = currentList.copy(
                    studyList = currentList.studyList + newPage.studyList,
                )
                _uiState.update { it.copy(participatingStudy = UiState.Success(merged)) }
            }.onFailure { e ->
                Log.e("HomeViewModel", "loadNextpageError", e)
            }
            _isLoadingMore.value = false
        }
    }
}

