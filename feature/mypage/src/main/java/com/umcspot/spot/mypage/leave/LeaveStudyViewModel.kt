package com.umcspot.spot.mypage.leave

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umcspot.spot.study.repository.StudyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaveStudyViewModel @Inject constructor(
    private val studyRepository: StudyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LeaveStudyState())
    val uiState: StateFlow<LeaveStudyState> = _uiState.asStateFlow()

    fun loadMembers(studyId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isMemberLoading = true) }
            studyRepository.getStudyMembers(studyId)
                .onSuccess { members ->
                    _uiState.update {
                        it.copy(
                            members = members.filter { m -> !m.isLeader },
                            isMemberLoading = false
                        )
                    }
                }
                .onFailure {
                    _uiState.update { it.copy(isMemberLoading = false) }
                }
        }
    }

    fun withdrawStudy(
        studyId: Long,
        reason: String,
        nextOwnerId: Long?
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            studyRepository.withdrawStudy(
                studyId = studyId,
                withdrawReason = reason,
                nextOwnerId = nextOwnerId
            ).onSuccess {
                _uiState.update {
                    it.copy(isLoading = false, isLeaveSuccess = true)
                }
            }.onFailure { e ->
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = e.message)
                }
            }
        }
    }

    fun dismissLeaveSuccess() {
        _uiState.update { it.copy(isLeaveSuccess = false) }
    }
}