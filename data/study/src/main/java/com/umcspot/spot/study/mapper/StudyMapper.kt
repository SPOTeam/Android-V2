package com.umcspot.spot.study.mapper

import com.umcspot.spot.model.toImageRef
import com.umcspot.spot.study.dto.request.StudyRequestDto
import com.umcspot.spot.study.dto.response.Study
import com.umcspot.spot.study.dto.response.StudyResponseDto
import com.umcspot.spot.study.model.StudyCreateModel
import com.umcspot.spot.study.model.StudyResult
import com.umcspot.spot.study.model.StudyResultList

fun StudyCreateModel.toData(): StudyRequestDto = StudyRequestDto(
    name = this.name,
    maxMembers = this.maxMembers,
    hasFee = this.hasFee,
    amount = this.amount,
    description = this.description,
    categories = this.categories,
    styles = this.styles,
    regionCodes = this.regionCodes
)

fun Study.toDomain() : StudyResult =
    StudyResult (
        id = this.id.toLong(),
        name = this.name,
        description = this.description,
        maxMembers = this.maxMembers,
        currentMembers = this.currentMembers,
        likeCount = this.likeCount,
        isLiked = this.isLiked,
        isOwner = this.isOwner,
        isAlone = this.isAlone,
        hitCount = this.hitCount,
        profileImageUrl = this.profileImageUrl.toImageRef()
    )

fun StudyResponseDto.toDomainList(): StudyResultList =
    StudyResultList(
        studyList = this.content.map(Study::toDomain),
        hasNext = this.hasNext,
        nextCursor = this.nextCursor?.toLong()
    )