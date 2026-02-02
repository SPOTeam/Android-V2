package com.umcspot.spot.mypage.recruiting

import com.umcspot.spot.study.model.StudyResultList
import com.umcspot.spot.ui.state.UiState

data class RecruitingStudyState (
    val recruitingStudy : UiState<StudyResultList> = UiState.Empty,
)