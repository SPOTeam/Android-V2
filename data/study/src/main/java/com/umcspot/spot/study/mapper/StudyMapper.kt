package com.umcspot.spot.study.mapper

import com.umcspot.spot.study.dto.request.StudyRequestDto
import com.umcspot.spot.study.dto.response.StudyResponseDto
import com.umcspot.spot.study.model.Study
import com.umcspot.spot.study.model.StudyResult

// Domain -> DTO
fun Study.toData(): StudyRequestDto =
    StudyRequestDto(
        studyId = this.studyId
    )

// DTO -> Domain
fun StudyResponseDto.toDomain( ): StudyResult =
    StudyResult(
        studyId = this.studyId,
        title = this.title,
        goal = this.goal,
        maxMember = this.maxMember,
        member = this.member,
        likes = this.likes,
        views = this.views,
        studyImage = this.studyImage
    )

// 단일 DTO를 리스트(1원소)로 감싸기
fun StudyResponseDto.toList(): List<StudyResult> =
    listOf(this.toDomain())

// 여러 DTO를 한 번에 변환하고 싶을 때(권장)
fun Iterable<StudyResponseDto>.toDomainList(): List<StudyResult> =
    this.map { it.toDomain() }