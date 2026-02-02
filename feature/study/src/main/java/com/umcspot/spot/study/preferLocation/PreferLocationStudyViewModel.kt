package com.umcspot.spot.study.preferLocation

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umcspot.spot.common.location.LocationRow
import com.umcspot.spot.common.location.LocationStore
import com.umcspot.spot.common.location.searchLocations
import com.umcspot.spot.model.FeeRange
import com.umcspot.spot.model.RecruitingStatus
import com.umcspot.spot.model.RecruitingStudySort
import com.umcspot.spot.model.StudyTheme
import com.umcspot.spot.study.model.StudyResultList
import com.umcspot.spot.study.repository.StudyRepository
import com.umcspot.spot.ui.state.UiState
import com.umcspot.spot.user.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
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
class PreferLocationStudyViewModel @Inject constructor(
    private val studyRepository: StudyRepository,
    private val userRepository: UserRepository,
    @ApplicationContext private val appContext: Context,
) : ViewModel() {

    data class ScrollPosition(
        val index: Int = 0,
        val offset: Int = 0
    )

    data class PreferLocationStudyUiState(
        val data: UiState<StudyResultList> = UiState.Empty
    )

    var scrollPosition: ScrollPosition = ScrollPosition()

    private val _uiState = MutableStateFlow(PreferLocationStudyUiState())
    val uiState: StateFlow<PreferLocationStudyUiState> = _uiState.asStateFlow()

    /** ---------------- 행정구역 관련 ---------------- */
    private var allLocations: List<LocationRow> = emptyList()

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    private val _results = MutableStateFlow<List<LocationRow>>(emptyList())
    val results = _results.asStateFlow()

    private val _selectedRegion = MutableStateFlow<List<LocationRow>>(emptyList())
    val selected = _selectedRegion.asStateFlow()

    /** 탭/페이징 상태 */
    private var currentRegionCode: String? = null
    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()

    private val _sortType = MutableStateFlow(RecruitingStudySort.RECENT)
    val sortType: StateFlow<RecruitingStudySort> = _sortType.asStateFlow()

    /** Filter **/
    private val _recruitingStatus = MutableStateFlow<RecruitingStatus?>(null)
    val recruitingStatus: StateFlow<RecruitingStatus?> = _recruitingStatus.asStateFlow()

    private val _fee = MutableStateFlow<FeeRange?>(null)
    val fee: StateFlow<FeeRange?> = _fee.asStateFlow()

    private val _themes = MutableStateFlow<List<StudyTheme>>(emptyList())
    val themes: StateFlow<List<StudyTheme>> = _themes.asStateFlow()

    val isFiltered: StateFlow<Boolean> =
        combine(_recruitingStatus, _fee, _themes) { recruitingStatus, fee, themes ->
            recruitingStatus != null || fee != null || themes.isNotEmpty()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    private val _isNullPreferLocation = MutableStateFlow(false)
    val isNullPreferLocation: StateFlow<Boolean> = _isNullPreferLocation.asStateFlow()

    /** ========== BoardListViewModel의 load() 역할 ========== */
    fun load(regionCode: String? = null) {
        currentRegionCode = regionCode

        viewModelScope.launch {
            runCatching {
                if (allLocations.isEmpty()) {
                    allLocations = LocationStore.load(appContext)
                }

                val preferredCodes = userRepository.getUserPreferredRegion()
                    .getOrThrow()
                    .regionCodes
                    .map { it.trim() }
                    .filter { it.isNotBlank() }

                val preferredRows = preferredCodes.mapNotNull { code ->
                    allLocations.find { it.code == code }
                }

                _selectedRegion.value = preferredRows

                if (preferredCodes.isEmpty()) {
                    _isNullPreferLocation.value = true
                    _uiState.update {
                        it.copy(data = UiState.Empty)
                    }
                    return@launch
                }

                _isNullPreferLocation.value = false

                val regionCodesForRequest =
                    if (regionCode.isNullOrBlank()) preferredCodes else listOf(regionCode)

                _uiState.update { it.copy(data = UiState.Loading) }

                studyRepository.getPreferLocationStudies(
                    recruitingStatus = _recruitingStatus.value,
                    feeRange = _fee.value,
                    categories = _themes.value.map { it.name },
                    sortBy = _sortType.value,
                    cursor = null,
                    size = 20,
                    regionCodes = regionCodesForRequest
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
                Log.e("PreferLocationStudyViewModel", "load error", e)
                _uiState.update { it.copy(data = UiState.Empty) }
            }
        }
    }

    fun loadNextPage() {
        val currentUi = _uiState.value.data
        val success = currentUi as? UiState.Success ?: return
        val currentList = success.data

        if (!currentList.hasNext) return
        if (_isLoadingMore.value) return

        viewModelScope.launch {
            _isLoadingMore.value = true

            runCatching {
                val preferredCodes = userRepository.getUserPreferredRegion()
                    .getOrThrow()
                    .regionCodes
                    .map { it.trim() }

                val regionCodesForRequest =
                    if (currentRegionCode.isNullOrBlank()) preferredCodes else listOf(currentRegionCode!!)

                studyRepository.getPreferLocationStudies(
                    recruitingStatus = _recruitingStatus.value,
                    feeRange = _fee.value,
                    categories = _themes.value.map { it.name },
                    sortBy = _sortType.value,
                    cursor = currentList.nextCursor,
                    size = 20,
                    regionCodes = regionCodesForRequest
                ).getOrThrow()
            }.onSuccess { newPage ->
                val merged = currentList.copy(
                    studyList = currentList.studyList + newPage.studyList,
                    hasNext = newPage.hasNext,
                    nextCursor = newPage.nextCursor
                )
                _uiState.update { it.copy(data = UiState.Success(merged)) }
            }.onFailure { e ->
                Log.e("PreferLocationStudyViewModel", "loadNextPage error", e)
            }

            _isLoadingMore.value = false
        }
    }

    fun selectTab(selectedTabIndex: Int) {
        val code = if (selectedTabIndex == 0) null else _selectedRegion.value.getOrNull(selectedTabIndex - 1)?.code
        load(code)
    }

    /** Filter 적용 후 현재 탭 기준으로 다시 load */
    fun applyFilter(
        recruitingStatus: RecruitingStatus?,
        fee: FeeRange?,
        themes: List<StudyTheme>,
    ) {
        _recruitingStatus.value = recruitingStatus
        _fee.value = fee
        _themes.value = themes

        load(currentRegionCode)
    }

    fun setSort(sort: RecruitingStudySort) {
        _sortType.value = sort
        load(currentRegionCode)
    }

    /** ---------------- 행정구역 검색용 메서드 ---------------- */
    fun loadLocationData() {
        viewModelScope.launch(Dispatchers.IO) {
            allLocations = LocationStore.load(appContext)
        }
    }

    fun searchLocation(query: String) {
        _query.value = query
        viewModelScope.launch(Dispatchers.IO) {
            if (query.isBlank()) {
                _results.value = emptyList()
                return@launch
            }

            if (allLocations.isEmpty()) {
                allLocations = LocationStore.load(appContext)
            }

            _results.value = searchLocations(query, allLocations)
        }
    }

    fun addLocation(row: LocationRow) {
        _selectedRegion.update { list ->
            if (list.any { it.code == row.code }) list else list + row
        }
    }

    fun removeLocation(row: LocationRow) {
        _selectedRegion.update { list ->
            list.filterNot { it.code == row.code }
        }
    }

    fun clearLocationSearch() {
        _query.value = ""
        _results.value = emptyList()
    }

    fun syncPreferredRegions() {
        viewModelScope.launch {
            runCatching {
                val regionCodes = _selectedRegion.value.map { it.code }
                userRepository.setUserPreferredRegion(regionCodes).getOrThrow()
            }.onSuccess {
                load(currentRegionCode)
            }.onFailure { e ->
                Log.e("PreferLocationStudyViewModel", "setUserPreferredRegion failed", e)
            }
        }
    }
}
