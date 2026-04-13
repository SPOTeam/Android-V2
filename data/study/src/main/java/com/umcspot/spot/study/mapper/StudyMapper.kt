package com.umcspot.spot.study.mapper

import com.umcspot.spot.model.formatCreatedAt
import com.umcspot.spot.study.dto.request.MemoirCreateRequestDto
import com.umcspot.spot.model.toImageRef
import com.umcspot.spot.study.dto.request.BoardCreateRequestDto
import com.umcspot.spot.study.dto.request.ScheduleCreateRequestDto
import com.umcspot.spot.study.dto.request.StudyRequestDto
import com.umcspot.spot.study.dto.response.AttendanceDto
import com.umcspot.spot.study.dto.response.Comment
import com.umcspot.spot.study.dto.response.MemberDto
import com.umcspot.spot.study.dto.response.MemoirDto
import com.umcspot.spot.study.dto.response.ScheduleResponseDto
import com.umcspot.spot.study.dto.response.StudyDetailResponseDto
import com.umcspot.spot.study.dto.response.Study
import com.umcspot.spot.study.dto.response.StudyApplication
import com.umcspot.spot.study.dto.response.StudyApplicationResponseDto
import com.umcspot.spot.study.dto.response.StudyPost
import com.umcspot.spot.study.dto.response.StudyPostDetailResponseDto
import com.umcspot.spot.study.dto.response.StudyPostsResponseDto
import com.umcspot.spot.study.dto.response.StudyAttendanceQrResponseDto
import com.umcspot.spot.study.dto.response.StudyAttendanceResponseDto
import com.umcspot.spot.study.dto.response.StudyResponseDto
import com.umcspot.spot.study.dto.response.TodoCreateResponseDto
import com.umcspot.spot.study.dto.response.TodoItemDto
import com.umcspot.spot.study.dto.response.TodoQueryResponseDto
import com.umcspot.spot.study.model.AttendanceStatus
import com.umcspot.spot.study.model.BoardCreateModel
import com.umcspot.spot.study.model.CommentResult
import com.umcspot.spot.study.model.MemoirCreateModel
import com.umcspot.spot.study.model.MemoirModel
import com.umcspot.spot.study.model.MemoirReactionCounts
import com.umcspot.spot.study.model.MemoirReactionStatus
import com.umcspot.spot.study.model.StudyApplicationResult
import com.umcspot.spot.study.model.StudyApplicationResultList
import com.umcspot.spot.study.model.StudyAttendanceListModel
import com.umcspot.spot.study.model.StudyAttendanceModel
import com.umcspot.spot.study.model.StudyAttendanceQrModel
import com.umcspot.spot.study.model.StudyCreateModel
import com.umcspot.spot.study.model.StudyDetailModel
import com.umcspot.spot.study.model.StudyMemberModel
import com.umcspot.spot.study.model.StudyPostDetailResult
import com.umcspot.spot.study.model.StudyPostResult
import com.umcspot.spot.study.model.StudyPostsResultList
import com.umcspot.spot.study.model.StudyRecentMemoirModel
import com.umcspot.spot.study.model.StudyResult
import com.umcspot.spot.study.model.StudyResultList
import com.umcspot.spot.study.model.StudyScheduleCreateModel
import com.umcspot.spot.study.model.StudyScheduleModel
import com.umcspot.spot.study.model.TodoModel
import com.umcspot.spot.study.model.ViewerStatus
import java.time.LocalDateTime
import java.time.ZonedDateTime
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
    hitCount = this.statistics.hitCount,
    viewerStatus = when (this.viewerStatus) {
        "NOT_APPLIED" -> ViewerStatus.NOT_APPLIED
        "APPLIED" -> ViewerStatus.APPLIED
        "APPROVED" -> ViewerStatus.APPROVED
        "OWNER" -> ViewerStatus.OWNER
        else -> ViewerStatus.UNKNOWN
    }
)

fun ScheduleResponseDto.toDomain(): StudyScheduleModel = StudyScheduleModel(
    id = this.scheduleId,
    title = this.title,
    startAt = LocalDateTime.parse(this.startAt, DateTimeFormatter.ISO_DATE_TIME),
    endAt = LocalDateTime.parse(this.endAt, DateTimeFormatter.ISO_DATE_TIME),
    isNow = this.isNow,
    isMine = this.isMine,
    isAttendanceStartable = this.isAttendanceStartable
)

fun StudyScheduleCreateModel.toData(): ScheduleCreateRequestDto = ScheduleCreateRequestDto(
    title = this.title,
    locationInfo = this.locationInfo,
    startAt = this.startAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "Z",
    endAt = this.endAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "Z"
)

fun MemoirDto.toDetailModel(): MemoirModel = MemoirModel(
    memoirId = this.memoirId,
    memberId = this.writer.memberId,
    nickname = this.writer.nickname,
    profileImageUrl = this.writer.profileImageUrl ?: "",
    activity = this.content.activity,
    learned = this.content.learned,
    encouragement = this.content.encouragement,
    imageUrls = this.content.imageUrls,
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
    writerProfileUrl = this.writer.profileImageUrl ?: "",
    activityContent = this.content.activity,
    thumbnailUrl = this.content.imageUrls.firstOrNull() ?: "",
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

fun BoardCreateModel.toData(): BoardCreateRequestDto = BoardCreateRequestDto(
    title = this.title,
    content = this.content,
    isPrivate = this.isPrivate
)
fun StudyApplicationResponseDto.toDomainList(): StudyApplicationResultList =
    StudyApplicationResultList(applies = this.applies.map {it.toDomain()})

fun StudyApplication.toDomain() : StudyApplicationResult =
    StudyApplicationResult(
        applicantId = this.applicantId.toLong(),
        memberId = this.memberId.toLong(),
        nickname = this.nickname,
        description = this.description,
        profileImageUrl = this.profileImageUrl.toImageRef()
    )

fun StudyAttendanceResponseDto.toDomain(): StudyAttendanceListModel =
    StudyAttendanceListModel(
        attendances = this.attendances.map { it.toDomain() },
        totalCount = this.totalCount
    )

fun AttendanceDto.toDomain(): StudyAttendanceModel {
    val formattedDate = this.attendedAt?.let { rawDate ->
        try {
            val parsedDate = ZonedDateTime.parse(rawDate)
            parsedDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd. HH:mm"))
        } catch (e: Exception) {
            try {
                val parsedLocal = LocalDateTime.parse(rawDate)
                parsedLocal.format(DateTimeFormatter.ofPattern("yyyy.MM.dd. HH:mm"))
            } catch (e2: Exception) {
                rawDate
            }
        }
    }

    return StudyAttendanceModel(
        memberId = this.member.memberId,
        name = this.member.memberName,
        profileUrl = this.member.memberProfileImageUrl,
        status = when (this.attendanceStatus) {
            "PRESENT" -> AttendanceStatus.PRESENT
            "ABSENT" -> AttendanceStatus.ABSENT
            else -> AttendanceStatus.UNDECIDED
        },
        attendedAt = formattedDate
    )
}

fun StudyAttendanceQrResponseDto.toDomain(): StudyAttendanceQrModel =
    StudyAttendanceQrModel(
        attendanceActive = this.attendanceActive,
        qrCodeImageUrl = this.qrCodeImageUrl
    )

fun StudyPostsResponseDto.toDomainList() : StudyPostsResultList =
    StudyPostsResultList(
        studyPostsList = this.posts.map{it.toDomain()},
        hasNext = this.hasNext,
        nextCursor = this.nextCursor?.toLong(),
    )

fun StudyPost.toDomain() : StudyPostResult =
    StudyPostResult(
        postId = this.postId.toLong(),
        title = this.title,
        content = this.content,
        isPinned = this.isPinned,
        isLiked = this.isLiked,
        likeCount = this.stats.likeCount,
        viewCount = this.stats.viewCount,
        commentCount = this.stats.commentCount,
        createdAt = this.createdAt.formatCreatedAt(),
    )

fun StudyPostDetailResponseDto.toDomain() : StudyPostDetailResult =
    StudyPostDetailResult (
        postId = this.postId.toLong(),
        title = this.title,
        content = this.content,
        isPinned = this.isPinned,
        isOwner = this.isOwner,
        isLiked = this.isLiked,
        writerMemberId = this.writer?.writerId ?: -1L,
        writerNickname = this.writer?.nickname ?: "",
        writerProfileUrl = this.writer?.profileImageUrl.toImageRef(),
        likeCount = this.stats.likeCount,
        viewCount = this.stats.viewCount,
        commentCount = this.stats.commentCount,
        createdAt = this.createdAt.formatCreatedAt(),
        comments = this.comments.map{it.toDomain()}
    )

fun Comment.toDomain() : CommentResult =
    CommentResult(
        commentId = this.commentId.toLong(),
        content = this.content,
        isOwner = this.isOwner,
        commentMemberId = this.writer?.writerId ?: -1L,
        commentNickname = this.writer?.nickname ?: "",
        commentProfileUrl = this.writer?.profileImageUrl.toImageRef(),
        createdAt = this.createdAt.formatCreatedAt()
    )
