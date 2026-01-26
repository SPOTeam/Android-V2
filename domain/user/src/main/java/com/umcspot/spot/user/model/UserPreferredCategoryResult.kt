package com.umcspot.spot.user.model

import com.umcspot.spot.model.StudyTheme

data class UserPreferredCategoryResult(
    val categories : List<StudyTheme>,
    val totalCount: Int
)