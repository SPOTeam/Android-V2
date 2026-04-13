package com.umcspot.spot.mypage.leave

import com.umcspot.spot.study.model.StudyMemberModel

data class LeaveStudyState(
    val members: List<StudyMemberModel> = emptyList(),
    val isMemberLoading: Boolean = false,
    val isLeaveSuccess: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)