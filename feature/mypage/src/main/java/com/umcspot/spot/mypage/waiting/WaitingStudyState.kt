package com.umcspot.spot.mypage.waiting

import com.umcspot.spot.study.model.StudyResultList
import com.umcspot.spot.ui.state.UiState

data class WaitingStudyState (
    val waitingStudy : UiState<StudyResultList> = UiState.Empty,
)