//package com.umcspot.spot.study.preferLocation
//
//import android.content.Context
//import android.util.Log
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.umcspot.spot.common.location.LocationRow
//import com.umcspot.spot.common.location.LocationStore
//import com.umcspot.spot.common.location.searchLocations
//import com.umcspot.spot.model.ActivityType
//import com.umcspot.spot.model.FeeRange
//import com.umcspot.spot.model.RecruitingStudySort
//import com.umcspot.spot.model.StudyTheme
//import com.umcspot.spot.study.model.StudyResultList
//import com.umcspot.spot.study.repository.StudyRepository
//import com.umcspot.spot.ui.state.UiState
//import dagger.hilt.android.lifecycle.HiltViewModel
//import dagger.hilt.android.qualifiers.ApplicationContext
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.collectLatest
//import kotlinx.coroutines.flow.combine
//import kotlinx.coroutines.flow.debounce
//import kotlinx.coroutines.flow.distinctUntilChanged
//import kotlinx.coroutines.flow.onStart
//import kotlinx.coroutines.flow.update
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class PreferLocationStudyViewModel @Inject constructor(
//    private val studyRepository: StudyRepository,
//    @ApplicationContext private val appContext: Context
//) : ViewModel() {
//
//    data class PreferLocationStudyUiState(
//        val studies: UiState<StudyResultList> = UiState.Empty
//    )
//
//    private val _uiState = MutableStateFlow(PreferLocationStudyUiState())
//    val uiState: StateFlow<PreferLocationStudyUiState> = _uiState.asStateFlow()
//
//    private val _sortType = MutableStateFlow(RecruitingStudySort.RECENT)
//    val sortType: StateFlow<RecruitingStudySort> = _sortType.asStateFlow()
//
//    private val _activity = MutableStateFlow<ActivityType?>(null)
//    private val _fee = MutableStateFlow<FeeRange?>(null)
//    private val _theme = MutableStateFlow<StudyTheme?>(null)
//
//    /** ---------------- 행정구역 관련 ---------------- */
//    private var allLocations: List<LocationRow> = emptyList()
//
//    private val _query = MutableStateFlow("")
//    val query = _query.asStateFlow()
//
//    private val _results = MutableStateFlow<List<LocationRow>>(emptyList())
//    val results = _results.asStateFlow()
//
//    private val _selected = MutableStateFlow<List<String>>(emptyList())
//    val selected = _selected.asStateFlow()
//
//
//    /** ---------------- 초기 네트워크 fetch ---------------- */
//    init {
//        combine(_sortType, _activity, _fee, _theme) { s, a, f, t ->
//            Params(s, a, f, t)
//        }
//            .distinctUntilChanged()
//            .debounce(200)
//            .onStart { emit(Params(_sortType.value, _activity.value, _fee.value, _theme.value)) }
//            .collectLatestIn(viewModelScope) { params ->
//                fetch(params)
//            }
//    }
//
//    private suspend fun fetch(p: Params) {
//        _uiState.update { it.copy(studies = UiState.Loading) }
//        val newState: UiState<StudyResultList> = try {
//            val res = studyRepository.getRecruitingStudies(
//                sortType = p.sort,
//                activityType = p.activity,
//                feeRange = p.fee,
//                theme = p.theme
//            )
//            res.fold(
//                onSuccess = { data -> UiState.Success(data) },
//                onFailure = { e -> UiState.Failure(e.message ?: e.toString()) }
//            )
//        } catch (e: Exception) {
//            UiState.Failure(e.message ?: e.toString())
//        }
//        _uiState.update { it.copy(studies = newState) }
//    }
//
//    /** ---------------- 공개 API ---------------- */
//    fun load(selected: RecruitingStudySort = _sortType.value) { _sortType.value = selected }
//    fun selectSort(type: RecruitingStudySort) { _sortType.value = type }
//    fun setActivityFilter(type: ActivityType?) { _activity.value = type }
//    fun setFeeFilter(fee: FeeRange?) { _fee.value = fee }
//    fun setThemeFilter(theme: StudyTheme?) { _theme.value = theme }
//    fun clearFilters() {
//        _activity.value = null
//        _fee.value = null
//        _theme.value = null
//    }
//
//    fun add(name: String) = _selected.update { if (name in it || it.size>=10) it else it + name }
//    fun remove(name: String) = _selected.update { it - name }
//    fun clear() = _selected.update { emptyList() }
//
//    /** ---------------- 행정구역 검색용 메서드 ---------------- */
//    fun loadLocationData() {
//        viewModelScope.launch(Dispatchers.IO) {
//            allLocations = LocationStore.load(appContext)}
//    }
//
//    fun searchLocation(query: String) {
//        _query.value = query
//        viewModelScope.launch(Dispatchers.IO) {
//            if (query.isBlank()) {
//                _results.value = emptyList()
//                return@launch
//            }
//
//            if (allLocations.isEmpty()) {
//                allLocations = LocationStore.load(appContext)
//            }
//
//            val filtered = searchLocations(query, allLocations)
//
//            _results.value = filtered
//        }
//    }
//
//
//    private data class Params(
//        val sort: RecruitingStudySort,
//        val activity: ActivityType?,
//        val fee: FeeRange?,
//        val theme: StudyTheme?
//    )
//}
//
///** 작은 헬퍼: Flow collectLatest 축약 */
//private inline fun <T> Flow<T>.collectLatestIn(
//    scope: CoroutineScope,
//    crossinline block: suspend (T) -> Unit
//) = scope.launch {
//    collectLatest { block(it) }
//}
