package com.umcspot.spot.mypage.participating

import com.umcspot.spot.study.model.StudyResultList
import com.umcspot.spot.ui.state.UiState

data class ParticipatingStudyState (
    val participatingStudy : UiState<StudyResultList> = UiState.Empty,
)