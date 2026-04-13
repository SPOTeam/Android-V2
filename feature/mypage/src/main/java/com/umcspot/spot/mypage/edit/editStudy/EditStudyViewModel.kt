package com.umcspot.spot.mypage.edit.editStudy

import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umcspot.spot.common.location.LocationRow
import com.umcspot.spot.common.location.LocationStore
import com.umcspot.spot.common.location.searchLocations
import com.umcspot.spot.common.util.FileUtil
import com.umcspot.spot.model.ActivityType
import com.umcspot.spot.model.StudyTheme
import com.umcspot.spot.study.model.StudyPersonality
import com.umcspot.spot.study.model.StudyUpdateModel
import com.umcspot.spot.study.register.model.RegisterStudySideEffect
import com.umcspot.spot.study.register.model.RegisterStudyState
import com.umcspot.spot.study.repository.StudyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EditStudyViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val studyRepository: StudyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterStudyState())
    val uiState = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<RegisterStudySideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    private var allLocations: List<LocationRow> = emptyList()
    private var searchJob: Job? = null
    private var studyId: Long = -1L

    init {
        loadLocationData()
    }

    fun initWithStudyId(id: Long) {
        studyId = id
        viewModelScope.launch {
            if (allLocations.isEmpty()) {
                allLocations = withContext(Dispatchers.IO) {
                    LocationStore.load(appContext)
                }
            }

            studyRepository.getStudyDetail(id)
                .onSuccess { detail ->
                    val selectedRegions = allLocations.filter { location ->
                        detail.regionCodes.contains(location.code)
                    }

                    val personalitySelections = StudyPersonality.entriesList.mapNotNull { personality ->
                        val matchedStyle = detail.styles.find {
                            it == personality.option1 || it == personality.option2
                        }
                        matchedStyle?.let { personality to it }
                    }.toMap()

                    _uiState.update { state ->
                        state.copy(
                            studyName = detail.title,
                            studyThemes = detail.categories,
                            activityType = if (detail.isOnline) ActivityType.ONLINE else ActivityType.OFFLINE,
                            selectedRegions = selectedRegions,
                            memberCount = detail.maxMembers,
                            hasFee = detail.hasFee,
                            feeAmount = if (detail.amount == 0) "" else detail.amount.toString(),
                            personalitySelections = personalitySelections,
                            description = detail.description,
                            studyImageUri = detail.thumbnailUrl
                        )
                    }
                }
                .onFailure { e ->
                    _sideEffect.emit(
                        RegisterStudySideEffect.ShowSnackBar(
                            e.message ?: "스터디 정보를 불러오지 못했습니다."
                        )
                    )
                }
        }
    }

    fun onCategorySelect(themes: List<StudyTheme>) {
        _uiState.update { it.copy(studyThemes = themes) }
    }

    fun onStudyNameChange(name: String) {
        _uiState.update { it.copy(studyName = name) }
    }

    fun onActivityTypeSelect(type: ActivityType) {
        _uiState.update { state ->
            if (type == ActivityType.OFFLINE) {
                state.copy(activityType = type, isSheetVisible = true)
            } else {
                state.copy(
                    activityType = type,
                    selectedRegions = emptyList(),
                    isSheetVisible = false
                )
            }
        }
    }

    fun onLocationQueryChange(query: String) {
        _uiState.update { it.copy(locationQuery = query) }
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            delay(300L)
            searchLocation(query)
        }
    }

    fun addSelectedRegion(region: LocationRow) {
        if (_uiState.value.selectedRegions.size < 10 &&
            !_uiState.value.selectedRegions.contains(region)
        ) {
            _uiState.update { it.copy(selectedRegions = it.selectedRegions + region) }
        }
    }

    fun removeSelectedRegion(region: LocationRow) {
        _uiState.update { state ->
            state.copy(
                selectedRegions = state.selectedRegions.toMutableList().apply { remove(region) }
            )
        }
    }

    fun dismissLocationSheet() {
        _uiState.update { it.copy(isSheetVisible = false) }
    }

    fun openLocationSheet() {
        _uiState.update { it.copy(isSheetVisible = true) }
    }

    fun onMemberCountChange(count: Int) {
        _uiState.update { it.copy(memberCount = count) }
    }

    fun onFeeInfoChange(hasFee: Boolean?, amount: String) {
        _uiState.update { it.copy(hasFee = hasFee, feeAmount = amount) }
    }

    fun onPersonalityChange(categoryIndex: Int, selectedOptionIndex: Int) {
        val category = StudyPersonality.Companion.entriesList.getOrNull(categoryIndex) ?: return
        val selectedStyle = category.getStyle(selectedOptionIndex)
        _uiState.update {
            it.copy(
                personalitySelections = it.personalitySelections + (category to selectedStyle)
            )
        }
    }

    fun onDescriptionChange(desc: String) {
        _uiState.update { it.copy(description = desc) }
    }

    fun onImageSelected(uri: String?) {
        _uiState.update { it.copy(studyImageUri = uri) }
    }

    fun isStepValid(step: Int): Boolean {
        val state = _uiState.value
        return when (step) {
            0 -> state.studyName.isNotBlank() && state.studyThemes.isNotEmpty()
            1 -> {
                if (state.activityType == null) return false
                if (state.activityType == ActivityType.OFFLINE) state.selectedRegions.isNotEmpty() else true
            }
            2 -> {
                val hasFee = state.hasFee
                val isFeeValid = hasFee != null && (!hasFee || state.feeAmount.isNotBlank())
                val isPersonalityValid = state.personalitySelections.size == StudyPersonality.entries.size
                state.memberCount > 1 && isFeeValid && isPersonalityValid
            }
            3 -> state.description.isNotBlank()
            else -> false
        }
    }

    // ── 수정 제출 ─────────────────────────────────────────────
    fun submit() {
        viewModelScope.launch {
            val currentState = _uiState.value

            val imageFile = currentState.studyImageUri?.let { uriString ->
                // 기존 서버 이미지 URL이면 파일 변환 스킵
                if (uriString.startsWith("http")) null
                else FileUtil.createTempFileFromUri(appContext, uriString.toUri())
            }

            val regionCodes = if (currentState.activityType == ActivityType.ONLINE) {
                emptyList()
            } else {
                currentState.selectedRegions.mapNotNull { region ->
                    allLocations.find { it.fullName == region.fullName }?.code
                }
            }

            val styles = currentState.personalitySelections.values.toList()

            val updateModel = StudyUpdateModel(
                name = currentState.studyName,
                maxMembers = currentState.memberCount,
                hasFee = currentState.hasFee ?: false,
                amount = currentState.feeAmount.toIntOrNull() ?: 0,
                description = currentState.description,
                isOnline = currentState.activityType == ActivityType.ONLINE,
                categories = currentState.studyThemes,
                styles = styles,
                regionCodes = regionCodes
            )

            studyRepository.updateStudy(studyId, updateModel, imageFile)
                .onSuccess {
                    _uiState.update { it.copy(isSuccessModalVisible = true) }
                }
                .onFailure { exception ->
                    _sideEffect.emit(
                        RegisterStudySideEffect.ShowSnackBar(
                            exception.message ?: "수정 실패"
                        )
                    )
                }
        }
    }

    private fun loadLocationData() {
        viewModelScope.launch(Dispatchers.IO) {
            allLocations = LocationStore.load(appContext)
        }
    }

    private suspend fun searchLocation(query: String) {
        if (query.isBlank()) {
            _uiState.update { it.copy(locationResults = emptyList()) }
            return
        }
        if (allLocations.isEmpty()) {
            allLocations = LocationStore.load(appContext)
        }
        val filtered = searchLocations(query, allLocations)
        _uiState.update { it.copy(locationResults = filtered) }
    }
}