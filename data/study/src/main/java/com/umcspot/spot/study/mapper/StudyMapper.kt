package com.umcspot.spot.study.mapper

import com.umcspot.spot.study.dto.request.MemoirCreateRequestDto
import com.umcspot.spot.study.dto.request.StudyRequestDto
import com.umcspot.spot.study.dto.response.MemberDto
import com.umcspot.spot.study.dto.response.MemoirDto
import com.umcspot.spot.study.dto.response.ScheduleResponseDto
import com.umcspot.spot.study.dto.response.StudyDetailResponseDto
import com.umcspot.spot.study.dto.response.Study as DTOStudy
import com.umcspot.spot.study.dto.response.StudyResponseDto
import com.umcspot.spot.study.dto.response.TodoCreateResponseDto
import com.umcspot.spot.study.dto.response.TodoItemDto
import com.umcspot.spot.study.dto.response.TodoQueryResponseDto
import com.umcspot.spot.study.model.MemoirCreateModel
import com.umcspot.spot.study.model.MemoirModel
import com.umcspot.spot.study.model.MemoirReactionCounts
import com.umcspot.spot.study.model.MemoirReactionStatus
import com.umcspot.spot.study.model.StudyCreateModel
import com.umcspot.spot.study.model.StudyDetailModel
import com.umcspot.spot.study.model.StudyMemberModel
import com.umcspot.spot.study.model.StudyRecentMemoirModel
import com.umcspot.spot.study.model.StudyResult
import com.umcspot.spot.study.model.StudyResultList
import com.umcspot.spot.study.model.StudyScheduleModel
import com.umcspot.spot.study.model.TodoModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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

fun MemberDto.toDomain(): StudyMemberModel = StudyMemberModel(
    id = this.memberId.toLong(),
    name = this.nickname,
    profileUrl = this.profileImageUrl,
    isLeader = this.isOwner
)

fun StudyDetailResponseDto.toDomain(): StudyDetailModel = StudyDetailModel(
    id = this.id,
    title = this.title,
    description = this.description,
    thumbnailUrl = this.thumbnailUrl,
    categories = this.categories,
    totalMembers = this.statistics.totalMembers,
    currentMembers = this.statistics.currentMembers,
    likeCount = this.statistics.likeCount,
    hitCount = this.statistics.hitCount
)

fun ScheduleResponseDto.toDomain(): StudyScheduleModel = StudyScheduleModel(
    id = this.scheduleId,
    title = this.title,
    startAt = LocalDateTime.parse(this.startAt, DateTimeFormatter.ISO_DATE_TIME),
    endAt = LocalDateTime.parse(this.endAt, DateTimeFormatter.ISO_DATE_TIME),
    isNow = this.isNow
)

fun MemoirDto.toDetailModel(): MemoirModel = MemoirModel(
    memoirId = this.memoirId,
    memberId = this.writer.memberId, 
    nickname = this.writer.nickname,
    profileImageUrl = this.writer.profileImageUrl ?: "",
    activity = this.content.activity,
    learned = this.content.learned,
    encouragement = this.content.encouragement,
    imageUrl = this.content.imageUrl,
    reactionCounts = MemoirReactionCounts(
        fireCount = this.reactionCounts.fireCount,
        heartCount = this.reactionCounts.heartCount,
        starCount = this.reactionCounts.starCount,
        smileCount = this.reactionCounts.smileCount
    ),
    reactions = MemoirReactionStatus(
        isFired = this.reactions.isFired,
        isHearted = this.reactions.isHearted,
        isStarred = this.reactions.isStarred,
        isSmiled = this.reactions.isSmiled
    ),
    isPrivate = this.isPrivate,
    createdAt = this.createdAt, 
    isMyMemoir = false
)

fun MemoirDto.toDomain(): StudyRecentMemoirModel = StudyRecentMemoirModel(
    id = this.memoirId,
    writerNickname = this.writer.nickname,
    writerProfileUrl = this.writer.profileImageUrl,
    activityContent = this.content.activity,
    thumbnailUrl = this.content.imageUrl,
    isPrivate = this.isPrivate
)
fun TodoCreateResponseDto.toDomain(): Long {
    return this.todoId
}

fun TodoQueryResponseDto.toDomain(memberId: String): List<TodoModel> {
    return (pending + completed).map { it.toDomain(memberId) }
}

fun TodoItemDto.toDomain(memberId: String): TodoModel = TodoModel(
    id = this.id,
    memberId = memberId,
    content = this.content,
    isCompleted = this.isCompleted,
)

fun MemoirCreateModel.toData(): MemoirCreateRequestDto = MemoirCreateRequestDto(
    activity = this.activity,
    learned = this.learned,
    encouragement = this.encouragement,
    isPrivate = this.isPrivate
)