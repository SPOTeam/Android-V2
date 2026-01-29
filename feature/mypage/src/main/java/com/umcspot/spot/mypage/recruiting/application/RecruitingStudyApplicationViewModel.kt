package com.umcspot.spot.mypage.recruiting.application

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umcspot.spot.study.repository.StudyRepository
import com.umcspot.spot.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecruitingStudyApplicationViewModel @Inject constructor(
    private val studyRepository: StudyRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(RecruitingStudyApplicationState())
    val uiState: StateFlow<RecruitingStudyApplicationState> = _uiState

    private var currentStudyId: Long = 0L

    fun load(studyId : Long) {
        _uiState.update { it.copy(applications = UiState.Loading) }

        viewModelScope.launch {
            currentStudyId = studyId
            studyRepository.getStudyApplications(
                studyId = studyId
            )
            .onSuccess { info ->
                _uiState.update {
                    it.copy(
                        applications = if (info.applies.isEmpty()) {
                            UiState.Empty
                        } else {
                            UiState.Success(info)
                        }
                    )
                }
            }
            .onFailure { e ->
                Log.e("RecruitingStudyApplicationViewModel", "loadStudyApplicationsError", e)
            }
        }
    }

    fun accept(applicationId: Long) {
        viewModelScope.launch {
            studyRepository.entryAcceptance(applicationId = applicationId, decision = "APPROVE")
                .onSuccess {
                    load(currentStudyId)
                }
                .onFailure { e ->
                    Log.e("RecruitingStudyApplicationViewModel", "acceptError", e)
                }
        }
    }

    fun reject(applicationId: Long) {
        viewModelScope.launch {
            studyRepository.entryAcceptance(applicationId = applicationId, decision = "REJECT")
                .onSuccess {
                    load(currentStudyId)
                }
                .onFailure { e ->
                    Log.e("RecruitingStudyApplicationViewModel", "rejectError", e)
                }
        }
    }
}

