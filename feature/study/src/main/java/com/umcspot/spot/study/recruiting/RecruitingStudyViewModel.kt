package com.umcspot.spot.study.recruiting

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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecruitingStudyViewModel @Inject constructor(
    private val studyRepository: StudyRepository
) : ViewModel() {

    data class RecruitingStudyUiState(
        val studies: UiState<StudyResultList> = UiState.Empty
    )

    private val _uiState = MutableStateFlow(RecruitingStudyUiState())
    val uiState: StateFlow<RecruitingStudyUiState> = _uiState.asStateFlow()

    private val _sortType = MutableStateFlow(RecruitingStudySort.LATEST)
    val sortType: StateFlow<RecruitingStudySort> = _sortType.asStateFlow()

    private val _activity = MutableStateFlow<ActivityType?>(null)
    val activity: StateFlow<ActivityType?> = _activity.asStateFlow()

    private val _fee = MutableStateFlow<FeeRange?>(null)
    val fee: StateFlow<FeeRange?> = _fee.asStateFlow()

    private val _theme = MutableStateFlow<StudyTheme?>(null)
    val theme: StateFlow<StudyTheme?> = _theme.asStateFlow()

    private fun fetch() {
        _uiState.update { it.copy(studies = UiState.Loading) }
        viewModelScope.launch {
            val res = studyRepository.getRecruitingStudies(
                sortType = _sortType.value,
                activityType = _activity.value,
                feeRange = _fee.value,
                theme = _theme.value
            )
            val newState: UiState<StudyResultList> = res.fold(
                onSuccess = { data ->
                    if (data.studyList.isEmpty()) UiState.Empty else UiState.Success(data)
                },
                onFailure = { e -> UiState.Failure(e.message ?: e.toString()) }
            )
            _uiState.update { it.copy(studies = newState) }
        }
    }

    /** 정렬 기준으로 목록 로드 */
    fun load(selected: RecruitingStudySort = _sortType.value) {
        _sortType.value = selected
        fetch()
    }

    /** 정렬 변경 */
    fun selectSort(type: RecruitingStudySort) {
        _sortType.value = type
        fetch()
    }

    /** ✅ 각 항목별 필터 변경 */
    fun setActivityFilter(type: ActivityType?, refresh: Boolean = true) {
        _activity.value = type
        if (refresh) fetch()
    }

    fun setFeeFilter(fee: FeeRange?, refresh: Boolean = true) {
        _fee.value = fee
        if (refresh) fetch()
    }

    fun setThemeFilter(theme: StudyTheme?, refresh: Boolean = true) {
        _theme.value = theme
        if (refresh) fetch()
    }

    /** ✅ 전체 초기화 */
    fun clearFilters(refresh: Boolean = true) {
        _activity.value = null
        _fee.value = null
        _theme.value = null
        if (refresh) fetch()
    }
}