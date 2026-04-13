package com.umcspot.spot.category

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umcspot.spot.model.ActivityType
import com.umcspot.spot.model.FeeRange
import com.umcspot.spot.model.RecruitingStatus
import com.umcspot.spot.model.RecruitingStudySort
import com.umcspot.spot.model.StudyTheme
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
class CategoryViewModel @Inject constructor(
    private val studyRepository: StudyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoryState())
    val uiState: StateFlow<CategoryState> = _uiState.asStateFlow()

    fun load() {
        val state = _uiState.value
        _uiState.update { it.copy(studies = UiState.Loading) }
        viewModelScope.launch {
            runCatching {
                studyRepository.getCategoryStudies(
                    recruitingStatus = state.recruitingStatus,
                    feeRange = state.feeRange,
                    category = state.selectedTab?.name,
                    isOnline = state.activityType.toIsOnline(),
                    sortBy = state.sortType,
                    cursor = null,
                    size = 20
                ).getOrThrow()
            }.onSuccess { firstPage ->
                _uiState.update {
                    it.copy(
                        studies = if (firstPage.studyList.isEmpty()) UiState.Empty
                        else UiState.Success(firstPage)
                    )
                }
            }.onFailure { e ->
                Log.e("CategoryViewModel", "load error", e)
                _uiState.update { it.copy(studies = UiState.Empty) }
            }
        }
    }

    fun loadNextPage() {
        val state = _uiState.value
        val current = (state.studies as? UiState.Success)?.data ?: return
        if (!current.hasNext || state.isLoadingMore) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingMore = true) }
            runCatching {
                studyRepository.getCategoryStudies(
                    recruitingStatus = state.recruitingStatus,
                    feeRange = state.feeRange,
                    category = state.selectedTab?.name,
                    isOnline = state.activityType.toIsOnline(),
                    sortBy = state.sortType,
                    cursor = current.nextCursor,
                    size = 20
                ).getOrThrow()
            }.onSuccess { newPage ->
                val merged = current.copy(
                    studyList = current.studyList + newPage.studyList,
                    hasNext = newPage.hasNext,
                    nextCursor = newPage.nextCursor
                )
                _uiState.update { it.copy(studies = UiState.Success(merged)) }
            }.onFailure { e ->
                Log.e("CategoryViewModel", "loadNextPage error", e)
            }
            _uiState.update { it.copy(isLoadingMore = false) }
        }
    }

    fun applyFilter(
        recruitingStatus: RecruitingStatus?,
        feeRange: FeeRange?,
        activityType: ActivityType?
    ) {
        _uiState.update {
            it.copy(
                recruitingStatus = recruitingStatus,
                feeRange = feeRange,
                activityType = activityType,
                isFiltered = recruitingStatus != null || feeRange != null || activityType != null
            )
        }
        load()
    }

    fun setSort(sort: RecruitingStudySort) {
        _uiState.update { it.copy(sortType = sort) }
        load()
    }

    fun setSelectedTab(tab: StudyTheme?) {
        _uiState.update { it.copy(selectedTab = tab) }
        load()
    }

    private fun ActivityType?.toIsOnline(): Boolean? = when (this) {
        ActivityType.ONLINE -> true
        ActivityType.OFFLINE -> false
        null -> null
    }
}