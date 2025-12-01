package com.umcspot.spot.user.model

import com.umcspot.spot.model.StudyTheme

data class StudyThemeRequestBody(
    val categories : List<StudyTheme>
)