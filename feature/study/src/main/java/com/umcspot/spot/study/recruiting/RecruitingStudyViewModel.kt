package com.umcspot.spot.study.recruiting

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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecruitingStudyViewModel @Inject constructor(
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

    private val _sortType = MutableStateFlow(RecruitingStudySort.RECENT)
    val sortType: StateFlow<RecruitingStudySort> = _sortType.asStateFlow()

    /**  Filter  **/
    private val _activity = MutableStateFlow<ActivityType?>(null)
    val activity: StateFlow<ActivityType?> = _activity.asStateFlow()

    private val _fee = MutableStateFlow<FeeRange?>(null)
    val fee: StateFlow<FeeRange?> = _fee.asStateFlow()

    private val _themes = MutableStateFlow<List<StudyTheme>>(emptyList())
    val themes: StateFlow<List<StudyTheme>> = _themes.asStateFlow()

    private fun calcNotNull(): Boolean =
        _activity.value != null || _fee.value != null || _themes.value.isNotEmpty()

    private val _notNull = MutableStateFlow(calcNotNull())
    val notNull: StateFlow<Boolean> = _notNull.asStateFlow()

    fun load() {
        _uiState.update { it.copy(studies = UiState.Loading) }
        viewModelScope.launch {
            runCatching {
                studyRepository.getRecruitingStudies(
                    feeCategory = _fee.value,
                    categories = _themes.value.map { it.name },
                    isOnline = _activity.value.toIsOnline(),
                    sortBy = _sortType.value,
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
            runCatching {
                studyRepository.getRecruitingStudies(
                    feeCategory = _fee.value,
                    categories = _themes.value.map { it.name },
                    isOnline = _activity.value.toIsOnline(),
                    sortBy = _sortType.value,
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
        }
    }

    /** 정렬 변경 */
    fun selectSort(type: RecruitingStudySort) {
        _sortType.value = type
        load()
    }


    /** Filter 변경 **/
    private fun updateNotNull() {
        _notNull.value = calcNotNull()
    }


    fun toggleActivity(type: ActivityType) {
        _activity.value = if (_activity.value == type) null else type
        updateNotNull()
    }

    fun toggleFee(fee: FeeRange) {
        _fee.value = if (_fee.value == fee) null else fee
        updateNotNull()
    }

    fun toggleTheme(theme: StudyTheme) {
        val cur = _themes.value
        _themes.value = if (cur.contains(theme)) cur - theme else cur + theme
        updateNotNull()
    }

    fun resetFilter() {
        _fee.value = null
        _activity.value = null
        _themes.value = emptyList()
        updateNotNull()
    }

    fun applyFilter(fee: FeeRange?, activity: ActivityType?, themes: List<StudyTheme>) {
        _fee.value = fee
        _activity.value = activity
        _themes.value = themes
        load()
    }

    private fun ActivityType?.toIsOnline(): Boolean? = when (this) {
        ActivityType.ONLINE -> true
        ActivityType.OFFLINE -> false
        null -> null
    }
}