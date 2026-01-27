package com.umcspot.spot.mypage.recruiting.application

import com.umcspot.spot.study.model.StudyApplicationResultList
import com.umcspot.spot.ui.state.UiState

data class RecruitingStudyApplicationState (
    val applications : UiState<StudyApplicationResultList> = UiState.Empty,
)