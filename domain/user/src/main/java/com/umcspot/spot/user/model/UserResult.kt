package com.umcspot.spot.user.model

import com.umcspot.spot.model.StudyTheme

data class UserResult (
    val name : String
)

data class UserTheme(
    val userThemes : List<StudyTheme>
)