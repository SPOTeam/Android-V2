package com.umcspot.spot.mypage.editInterestStudy

import com.umcspot.spot.study.model.StudyResultList
import com.umcspot.spot.ui.state.UiState
import com.umcspot.spot.user.model.UserPreferredCategoryResult

data class EditInterestStudyState (
    val preferCategories : UiState<UserPreferredCategoryResult> = UiState.Empty,
)