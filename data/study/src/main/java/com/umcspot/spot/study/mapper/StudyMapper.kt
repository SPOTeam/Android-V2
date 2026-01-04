package com.umcspot.spot.study.mapper

import com.umcspot.spot.study.dto.request.StudyRequestDto
import com.umcspot.spot.study.dto.response.Study as DTOStudy
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

fun DTOStudy.toDomain(): StudyResult = StudyResult(
    studyId = this.studyId,
    title = this.title,
    goal = this.goal,
    maxMember = this.maxMember,
    member = this.member,
    likes = this.likes,
    views = this.views,
    studyImage = this.studyImage
)

fun StudyResponseDto.toDomain(): StudyResultList = StudyResultList(
    studyList = this.studyList.map { it.toDomain() }
)