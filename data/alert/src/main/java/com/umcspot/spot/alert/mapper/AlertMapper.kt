package com.umcspot.spot.alert.mapper

import com.umcspot.spot.alert.dto.response.AlertItem
import com.umcspot.spot.alert.dto.response.AlertResponseDto
import com.umcspot.spot.alert.model.AlertInfo
import com.umcspot.spot.alert.model.AlertResult


fun AlertItem.toDomain() : AlertInfo =
    AlertInfo (
        applicationId = this.applicationId.toLong(),
        studyId = this.studyId.toLong(),
        title = this.title,
        studyImageRes = this.profileImageRef,
    )

fun AlertResponseDto.toDomainList(): AlertResult =
    AlertResult(
        studies = this.studies.map(AlertItem::toDomain)
    )
