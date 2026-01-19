package com.umcspot.spot.jjim

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umcspot.spot.model.ActivityType
import com.umcspot.spot.model.FeeRange
import com.umcspot.spot.model.RecruitingStudySort
import com.umcspot.spot.model.StudyTheme
import com.umcspot.spot.study.model.StudyResultList
import com.umcspot.spot.study.repository.StudyRepository
import com.umcspot.spot.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JJimViewModel @Inject constructor(
    private val studyRepository: StudyRepository
) : ViewModel() {

    data class ScrollPosition(
        val index: Int = 0,
        val offset: Int = 0
    )

    data class RecruitingStudyUiState(val studies: UiState<StudyResultList> = UiState.Empty)

    var scrollPosition: ScrollPosition = ScrollPosition()

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()

    private val _uiState = MutableStateFlow(RecruitingStudyUiState())
    val uiState: StateFlow<RecruitingStudyUiState> = _uiState.asStateFlow()

    fun load() {
        _uiState.update { it.copy(studies = UiState.Loading) }
        viewModelScope.launch {
            runCatching {
                studyRepository.getLikedStudies(
                    size = 10,
                    cursor = null
                ).getOrThrow()
            }.onSuccess { data ->
                if (data.studyList.isEmpty()) {
                    _uiState.update { it.copy(studies = UiState.Empty) }
                } else {
                    _uiState.update { it.copy(studies = UiState.Success(data)) }
                }
            }.onFailure { e ->
                Log.e("RecruitingStudyViewModel", "loadFristError", e)
//                UiState.Failure(e.message ?: e.toString())
            }
        }
    }

    fun loadNextPage() {
        val currentUi = _uiState.value.studies
        val success = currentUi as? UiState.Success ?: return
        val currentList = success.data

        if (!currentList.hasNext) return
        if (_isLoadingMore.value) return

        viewModelScope.launch {
            _isLoadingMore.value = true
            runCatching {
                studyRepository.getLikedStudies(
                    cursor = currentList.nextCursor,
                    size = 10
                ).getOrThrow()
            }.onSuccess { newPage ->
                val merged = currentList.copy(
                    studyList = currentList.studyList + newPage.studyList,
                    hasNext = newPage.hasNext,
                    nextCursor = newPage.nextCursor
                )
                _uiState.update { it.copy(studies = UiState.Success(merged)) }
            }.onFailure { e ->
                Log.e("RecruitingStudyViewModel", "loadNextpageError", e)
            }
            _isLoadingMore.value = false
        }
    }
}