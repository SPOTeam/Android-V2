package com.umcspot.spot.alert.model

import com.umcspot.spot.model.ImageRef

/******** 일반 알람 ********/
data class AlertResult (
    val studies : List<AlertInfo>
)

data class AlertInfo(
    val applicationId: Long,
    val studyId: Long,
    val title: String,
    val studyImageRes: ImageRef,
)