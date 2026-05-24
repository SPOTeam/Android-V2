package com.umcspot.spot.study.register

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umcspot.spot.common.location.LocationRow
import com.umcspot.spot.common.location.LocationStore
import com.umcspot.spot.common.location.searchLocations
import com.umcspot.spot.common.util.FileUtil
import com.umcspot.spot.model.ActivityType
import com.umcspot.spot.study.model.StudyCreateModel
import com.umcspot.spot.study.model.StudyPersonality
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
import javax.inject.Inject
import androidx.core.net.toUri

@HiltViewModel
class RegisterStudyViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val studyRepository: StudyRepository
) : ViewModel() {

    private val CONSONANTS_VOWELS_ONLY_REGEX = Regex("^[ㄱ-ㅎㅏ-ㅣ]+$")

    private val _uiState = MutableStateFlow(RegisterStudyState())
    val uiState = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<RegisterStudySideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    private var allLocations: List<LocationRow> = emptyList()

    private var searchJob: Job? = null

    init {
        loadLocationData()
    }

    fun onCategorySelect(themes: List<com.umcspot.spot.model.StudyTheme>) {
        _uiState.update { it.copy(studyThemes = themes) }
    }

    fun onStudyNameChange(name: String) {
        _uiState.update { it.copy(studyName = name) }
    }

    fun onActivityTypeSelect(type: ActivityType) {
        _uiState.update { state ->
            if (type == ActivityType.OFFLINE) {
                state.copy(
                    activityType = type,
                    isSheetVisible = true
                )
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
        if (_uiState.value.selectedRegions.size < 3 && !_uiState.value.selectedRegions.contains(
                region
            )
        ) {
            _uiState.update {
                it.copy(
                    selectedRegions = it.selectedRegions + region
                )
            }
        }
    }

    fun removeSelectedRegion(region: LocationRow) {
        _uiState.update { currentState ->
            val updatedRegions = currentState.selectedRegions.toMutableList().apply {
                remove(region)
            }
            currentState.copy(selectedRegions = updatedRegions)
        }
    }

    fun dismissLocationSheet() {
        _uiState.update { it.copy(isSheetVisible = false) }
    }

    fun openLocationSheet() {
        _uiState.update { it.copy(isSheetVisible = true) }
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

    fun onMemberCountChange(count: Int) {
        _uiState.update { it.copy(memberCount = count) }
    }

    fun onFeeInfoChange(hasFee: Boolean?, amount: String) {
        _uiState.update { it.copy(hasFee = hasFee, feeAmount = amount) }
    }

    fun onPersonalityChange(categoryIndex: Int, selectedOptionIndex: Int) {
        val category = StudyPersonality.entriesList.getOrNull(categoryIndex) ?: return
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

    fun isStepValid(step: Int): Boolean {
        val state = _uiState.value
        return when (step) {
            0 -> {
                val isNameValid = state.studyName.isNotBlank() &&
                        !CONSONANTS_VOWELS_ONLY_REGEX.matches(state.studyName)
                isNameValid && state.studyThemes.isNotEmpty()
            }
            1 -> {
                if (state.activityType == null) return false
                if (state.activityType == ActivityType.OFFLINE) state.selectedRegions.isNotEmpty() else true
            }

            2 -> {
                val isFeeValid =
                    state.hasFee != null && (!state.hasFee || state.feeAmount.isNotBlank())
                val isPersonalityValid =
                    state.personalitySelections.size == StudyPersonality.entries.size

                state.memberCount > 1 && isFeeValid && isPersonalityValid
            }

            3 -> state.description.isNotBlank()
            else -> false
        }
    }

    fun onImageSelected(uri: String?) {
        _uiState.update { it.copy(studyImageUri = uri) }
    }

    fun submit() {
        viewModelScope.launch {
            val currentState = _uiState.value

            val imageFile = currentState.studyImageUri?.let { uriString ->
                FileUtil.createTempFileFromUri(appContext, uriString.toUri())
            }

            val regionCodes = if (currentState.activityType == ActivityType.ONLINE) {
                emptyList()
            } else {
                currentState.selectedRegions.mapNotNull { region ->
                    allLocations.find { it.fullName == region.fullName }?.code
                }
            }

            val styles = currentState.personalitySelections.values.toList()

            val createModel = StudyCreateModel(
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

            studyRepository.createStudy(createModel, imageFile)
                .onSuccess { id ->
                    _uiState.update {
                        it.copy(
                            isSuccessModalVisible = true,
                            createdStudyId = id
                        )
                    }
                }
                .onFailure { exception ->
                    _sideEffect.emit(
                        RegisterStudySideEffect.ShowSnackBar(
                            exception.message ?: "실패"
                        )
                    )
                }
        }
    }
}
