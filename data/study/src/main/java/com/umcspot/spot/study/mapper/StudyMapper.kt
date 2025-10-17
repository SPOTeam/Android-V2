package com.umcspot.spot.study.mapper

import com.umcspot.spot.study.dto.request.StudyRequestDto
import com.umcspot.spot.study.dto.response.Study as DTOStudy
import com.umcspot.spot.study.dto.response.StudyResponseDto
import com.umcspot.spot.study.model.Study as DomainStudy
import com.umcspot.spot.study.model.StudyResult
import com.umcspot.spot.study.model.StudyResultList

// Domain -> Request DTO
fun DomainStudy.toData(): StudyRequestDto =
    StudyRequestDto(
        studyId = this.studyId
    )

fun DTOStudy.toDomain() : StudyResult =
    StudyResult (
        studyId = this.studyId,
        title = this.title,
        goal = this.goal,
        maxMember = this.maxMember,
        member = this.member,
        likes = this.likes,
        views = this.views,
        studyImage = this.studyImage
    )

fun StudyResponseDto.toDomainList(): StudyResultList =
    StudyResultList(
        studyList = this.studyList.map(DTOStudy::toDomain)
    )