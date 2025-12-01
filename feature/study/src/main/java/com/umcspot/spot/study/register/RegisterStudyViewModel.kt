package com.umcspot.spot.study.register

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umcspot.spot.common.location.LocationRow
import com.umcspot.spot.common.location.LocationStore
import com.umcspot.spot.common.location.searchLocations
import com.umcspot.spot.model.ActivityType
import com.umcspot.spot.study.register.model.RegisterStudySideEffect
import com.umcspot.spot.study.register.model.RegisterStudyState
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

@HiltViewModel
class RegisterStudyViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context
) : ViewModel() {

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

    fun addSelectedRegion(region: String) {
        if (_uiState.value.selectedRegions.size < 10 && !_uiState.value.selectedRegions.contains(region)) {
            _uiState.update {
                it.copy(
                    selectedRegions = it.selectedRegions + region
                )
            }
        }
    }

    fun removeSelectedRegion(region: String) {
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

    fun onPersonalityChange(categoryIndex: Int, value: Int) {
        _uiState.update {
            when (categoryIndex) {
                0 -> it.copy(networkingPreference = value)
                1 -> it.copy(goalDurationPreference = value)
                2 -> it.copy(discussionPreference = value)
                3 -> it.copy(learningPreference = value)
                4 -> it.copy(flexibilityPreference = value)
                else -> it
            }
        }
    }

    fun onDescriptionChange(desc: String) {
        _uiState.update { it.copy(description = desc) }
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
                val isFeeValid = state.hasFee != null && (!state.hasFee || state.feeAmount.isNotBlank())
                val isPersonalityValid = state.networkingPreference != null &&
                        state.goalDurationPreference != null &&
                        state.discussionPreference != null &&
                        state.learningPreference != null &&
                        state.flexibilityPreference != null

                state.memberCount > 1 && isFeeValid && isPersonalityValid
            }
            3 -> state.description.isNotBlank()
            else -> false
        }
    }

    fun submit() {
        viewModelScope.launch {
            _sideEffect.emit(RegisterStudySideEffect.NavigateToHome)
        }
    }
}