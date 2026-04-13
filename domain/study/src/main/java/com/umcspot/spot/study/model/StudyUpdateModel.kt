package com.umcspot.spot.study.model

import com.umcspot.spot.model.StudyStyle
import com.umcspot.spot.model.StudyTheme

data class StudyUpdateModel(
    val name: String,
    val maxMembers: Int,
    val hasFee: Boolean,
    val amount: Int,
    val description: String,
    val isOnline: Boolean,
    val categories: List<StudyTheme>,
    val styles: List<StudyStyle>,
    val regionCodes: List<String>
)