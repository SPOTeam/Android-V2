package com.umcspot.spot.study.model

import com.umcspot.spot.model.ImageRef

data class StudyApplicationResultList (
    val applies: List<StudyApplicationResult>,
)

data class StudyApplicationResult (
    val applicantId : Long,
    val memberId : Long,
    val nickname : String,
    val description: String,
    val profileImageUrl: ImageRef,
)