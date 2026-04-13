package com.umcspot.spot.category

import com.umcspot.spot.model.ActivityType
import com.umcspot.spot.model.FeeRange
import com.umcspot.spot.model.RecruitingStatus
import com.umcspot.spot.model.RecruitingStudySort
import com.umcspot.spot.model.StudyTheme
import com.umcspot.spot.study.model.StudyResultList
import com.umcspot.spot.ui.state.UiState

data class CategoryState(
    val studies: UiState<StudyResultList> = UiState.Empty,
    val sortType: RecruitingStudySort = RecruitingStudySort.RECENT,
    val selectedTab: StudyTheme? = null,
    val isLoadingMore: Boolean = false,
    val isFiltered: Boolean = false,
    val recruitingStatus: RecruitingStatus? = null,
    val feeRange: FeeRange? = null,
    val activityType: ActivityType? = null
)