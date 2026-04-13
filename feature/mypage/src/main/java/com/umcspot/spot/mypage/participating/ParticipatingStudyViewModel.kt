package com.umcspot.spot.mypage.participating

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umcspot.spot.study.repository.StudyRepository
import com.umcspot.spot.token.repository.TokenRepository
import com.umcspot.spot.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParticipatingStudyViewModel @Inject constructor(
    private val studyRepository: StudyRepository,
    private val tokenRepository: TokenRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ParticipatingStudyState())
    val uiState: StateFlow<ParticipatingStudyState> = _uiState.asStateFlow()

    private val _isLoadingMore = MutableStateFlow(false)

    private var currentUserId: String = ""

    fun loadParticipatingStudy() {
        _uiState.update { it.copy(participatingStudy = UiState.Loading) }
        viewModelScope.launch {
            studyRepository.getParticipatingStudy(cursor = null, size = 10)
                .onSuccess { info ->
                    _uiState.update {
                        it.copy(
                            participatingStudy = if (info.studyList.isEmpty()) UiState.Empty
                            else UiState.Success(info)
                        )
                    }
                }
                .onFailure { e ->
                    Log.e("ParticipatingStudyViewModel", "loadParticipatingStudy error", e)
                }
        }
    }

    fun loadNextPage() {
        val currentUi = _uiState.value.participatingStudy
        val success = currentUi as? UiState.Success ?: return
        val currentList = success.data

        if (!currentList.hasNext) return
        if (_isLoadingMore.value) return

        viewModelScope.launch {
            _isLoadingMore.value = true
            runCatching {
                studyRepository.getParticipatingStudy(
                    cursor = currentList.nextCursor,
                    size = 10
                ).getOrThrow()
            }.onSuccess { newPage ->
                val merged = currentList.copy(
                    studyList = currentList.studyList + newPage.studyList,
                    hasNext = newPage.hasNext,
                    nextCursor = newPage.nextCursor
                )
                _uiState.update { it.copy(participatingStudy = UiState.Success(merged)) }
            }.onFailure { e ->
                Log.e("ParticipatingStudyViewModel", "loadNextPageError", e)
            }
            _isLoadingMore.value = false
        }
    }

    fun openReportDialog(studyId: Long) {
        _uiState.update {
            it.copy(
                isReportDialogVisible = true,
                reportTargetStudyId = studyId,
                isReportStep = false,
                selectedMemberId = null,
                reportReason = "",
                studyMembers = emptyList()
            )
        }
        viewModelScope.launch {
            if (currentUserId.isEmpty()) {
                runCatching { tokenRepository.getUserId() }
                    .onSuccess { id -> currentUserId = id }
            }
            loadStudyMembers(studyId)
        }
    }

    fun dismissReportDialog() {
        _uiState.update { it.copy(isReportDialogVisible = false) }
    }

    private fun loadStudyMembers(studyId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isMemberLoading = true) }
            studyRepository.getStudyMembers(studyId)
                .onSuccess { members ->
                    _uiState.update {
                        it.copy(
                            studyMembers = members.filter { m -> m.id.toString() != currentUserId },
                            isMemberLoading = false
                        )
                    }
                }
                .onFailure { e ->
                    Log.e("ParticipatingStudyViewModel", "loadStudyMembers error", e)
                    _uiState.update { it.copy(isMemberLoading = false) }
                }
        }
    }

    fun onMemberSelect(memberId: Long) {
        _uiState.update { it.copy(selectedMemberId = memberId) }
    }

    fun goToReportReasonStep() {
        if (_uiState.value.selectedMemberId == null) return
        _uiState.update { it.copy(isReportStep = true) }
    }

    fun onReportReasonChange(reason: String) {
        _uiState.update { it.copy(reportReason = reason) }
    }

    fun submitReport() {
        val state = _uiState.value
        val studyId = state.reportTargetStudyId ?: return
        val targetMemberId = state.selectedMemberId ?: return

        viewModelScope.launch {
            studyRepository.reportStudyMember(
                studyId = studyId,
                targetMemberId = targetMemberId,
                reason = state.reportReason
            ).onSuccess {
                _uiState.update {
                    it.copy(
                        isReportDialogVisible = false,
                        isReportSuccess = true  // 성공 다이얼로그 표시
                    )
                }
            }.onFailure { e ->
                Log.e("ParticipatingStudyViewModel", "submitReport error", e)
            }
        }
    }

    fun dismissReportSuccess() {
        _uiState.update { it.copy(isReportSuccess = false) }
    }

    fun openDeleteDialog(studyId: Long) {
        _uiState.update {
            it.copy(
                isDeleteDialogVisible = true,
                deleteTargetStudyId = studyId
            )
        }
    }

    fun dismissDeleteDialog() {
        _uiState.update { it.copy(isDeleteDialogVisible = false) }
    }

    fun deleteStudy() {
        val studyId = _uiState.value.deleteTargetStudyId ?: return
        viewModelScope.launch {
            studyRepository.deleteStudy(studyId)
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            isDeleteDialogVisible = false,
                            isDeleteSuccess = true
                        )
                    }
                    loadParticipatingStudy()
                }
                .onFailure { e ->
                    Log.e("ParticipatingStudyViewModel", "deleteStudy error", e)
                }
        }
    }

    fun dismissDeleteSuccess() {
        _uiState.update { it.copy(isDeleteSuccess = false) }
    }
}