package com.umcspot.spot.study.preferCategory

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umcspot.spot.common.location.LocationStore
import com.umcspot.spot.model.ActivityType
import com.umcspot.spot.model.FeeRange
import com.umcspot.spot.model.RecruitingStatus
import com.umcspot.spot.model.RecruitingStudySort
import com.umcspot.spot.model.StudyTheme
import com.umcspot.spot.study.model.StudyResultList
import com.umcspot.spot.study.repository.StudyRepository
import com.umcspot.spot.ui.state.UiState
import com.umcspot.spot.user.repository.UserRepository
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
class PreferCategoryStudyViewModel @Inject constructor(
    private val studyRepository: StudyRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    data class ScrollPosition(val index: Int = 0, val offset: Int = 0)
    data class PreferCategoryUiState(val data: UiState<StudyResultList> = UiState.Empty)

    var scrollPosition: ScrollPosition = ScrollPosition()

    private val _uiState = MutableStateFlow(PreferCategoryUiState())
    val uiState: StateFlow<PreferCategoryUiState> = _uiState.asStateFlow()

    private val _preferCategory = MutableStateFlow<List<StudyTheme?>>(emptyList())
    val preferCategory: StateFlow<List<StudyTheme?>> = _preferCategory.asStateFlow()

    private val _selectedTab = MutableStateFlow<StudyTheme?>(null)
    val selectedTab: StateFlow<StudyTheme?> = _selectedTab.asStateFlow()

    private val _recruitingStatus = MutableStateFlow<RecruitingStatus?>(null)
    val recruitingStatus: StateFlow<RecruitingStatus?> = _recruitingStatus.asStateFlow()

    private val _feeRange = MutableStateFlow<FeeRange?>(null)
    val feeRange: StateFlow<FeeRange?> = _feeRange.asStateFlow()

    private val _activity = MutableStateFlow<ActivityType?>(null)
    val activity: StateFlow<ActivityType?> = _activity.asStateFlow()

    private val _sortType = MutableStateFlow(RecruitingStudySort.RECENT)
    val sortType: StateFlow<RecruitingStudySort> = _sortType.asStateFlow()

    /** 로딩 중 페이징 플래그 */
    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()

    val isFiltered: StateFlow<Boolean> =
        combine(_recruitingStatus, _feeRange, _activity) { status, fee, activity ->
            status != null || fee != null || activity != null
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    /** ========== 최초/새로고침 로드 ========== */

    fun getPreferCategories() {
        viewModelScope.launch {
            userRepository.getUserPreferredCategory()
                .onSuccess { info ->
                    _preferCategory.value =  info.categories
                }
        }
    }

    fun load() {
        viewModelScope.launch {
            runCatching {
                _uiState.update { it.copy(data = UiState.Loading) }

                studyRepository.getPreferCategoryStudies(
                    category = _selectedTab.value,
                    recruitingStatus = _recruitingStatus.value,
                    feeRange = _feeRange.value,
                    isOnline = _activity.value.toIsOnline(),
                    sortBy = _sortType.value,
                    cursor = null,
                    size = 20,
                ).getOrThrow()
            }.onSuccess { firstPage ->
                _uiState.update {
                    it.copy(
                        data = if (firstPage.studyList.isEmpty())
                            UiState.Empty
                        else
                            UiState.Success(firstPage)
                    )
                }
            }.onFailure { e ->
                Log.e("PreferCategoryStudyViewModel", "load error", e)
                _uiState.update { it.copy(data = UiState.Empty) }
            }
        }
    }

    fun loadNextPage() {
        val current = (_uiState.value.data as? UiState.Success)?.data ?: return
        if (!current.hasNext || _isLoadingMore.value) return

        viewModelScope.launch {
            _isLoadingMore.value = true

            runCatching {
                studyRepository.getPreferCategoryStudies(
                    category = _selectedTab.value,
                    recruitingStatus = _recruitingStatus.value,
                    feeRange = _feeRange.value,
                    isOnline = _activity.value.toIsOnline(),
                    sortBy = _sortType.value,
                    cursor = current.nextCursor,
                    size = 20
                ).getOrThrow()
            }.onSuccess { newPage ->
                val merged = current.copy(
                    studyList = current.studyList + newPage.studyList,
                    hasNext = newPage.hasNext,
                    nextCursor = newPage.nextCursor
                )
                _uiState.update { it.copy(data = UiState.Success(merged)) }
            }.onFailure { e ->
                Log.e("PreferCategoryStudyViewModel", "loadNextPage error", e)
            }

            _isLoadingMore.value = false
        }
    }

    fun applyFilter(recruitingStatus: RecruitingStatus?, fee: FeeRange?, activityType: ActivityType?) {
        _recruitingStatus.value = recruitingStatus
        _feeRange.value = fee
        _activity.value = activityType
        load()
    }

    fun setSort(sort: RecruitingStudySort) {
        _sortType.value = sort
        load()
    }

    fun setSelectedTab(theme: StudyTheme?) {
        _selectedTab.value = theme
        load()
    }


    private fun ActivityType?.toIsOnline(): Boolean? = when (this) {
        ActivityType.ONLINE -> true
        ActivityType.OFFLINE -> false
        null -> null
    }
}