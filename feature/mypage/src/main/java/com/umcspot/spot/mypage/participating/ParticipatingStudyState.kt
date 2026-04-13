package com.umcspot.spot.mypage.participating

import com.umcspot.spot.study.model.StudyMemberModel
import com.umcspot.spot.study.model.StudyResultList
import com.umcspot.spot.ui.state.UiState

data class ParticipatingStudyState(
    val participatingStudy: UiState<StudyResultList> = UiState.Loading,
    val isReportDialogVisible: Boolean = false,
    val reportTargetStudyId: Long? = null,
    val studyMembers: List<StudyMemberModel> = emptyList(),
    val isMemberLoading: Boolean = false,
    val selectedMemberId: Long? = null,
    val reportReason: String = "",
    val isReportStep: Boolean = false,
    val isReportSuccess: Boolean = false,
    val isDeleteDialogVisible: Boolean = false,
    val deleteTargetStudyId: Long? = null,
    val isDeleteSuccess: Boolean = false
)