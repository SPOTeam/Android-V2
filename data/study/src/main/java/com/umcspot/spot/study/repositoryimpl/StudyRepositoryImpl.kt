package com.umcspot.spot.study.repositoryimpl

import android.util.Log
import com.umcspot.spot.model.FeeRange
import com.umcspot.spot.model.RecruitingStatus
import com.umcspot.spot.model.RecruitingStudySort
import com.umcspot.spot.model.StudyTheme
import com.umcspot.spot.study.datasource.StudyDataSource
import com.umcspot.spot.study.mapper.toData
import com.umcspot.spot.study.mapper.toDetailModel
import com.umcspot.spot.study.mapper.toDomain
import com.umcspot.spot.study.model.MemoirCreateModel
import com.umcspot.spot.study.model.MemoirModel
import com.umcspot.spot.study.mapper.toDomainList
import com.umcspot.spot.study.model.StudyApplicationResultList
import com.umcspot.spot.study.model.StudyAttendanceListModel
import com.umcspot.spot.study.model.StudyAttendanceQrModel
import com.umcspot.spot.study.model.StudyCreateModel
import com.umcspot.spot.study.model.StudyDetailModel
import com.umcspot.spot.study.model.StudyMemberModel
import com.umcspot.spot.study.model.StudyRecentMemoirModel
import com.umcspot.spot.study.model.StudyResultList
import com.umcspot.spot.study.model.StudyScheduleCreateModel
import com.umcspot.spot.study.model.StudyScheduleModel
import com.umcspot.spot.study.model.TodoModel
import com.umcspot.spot.study.repository.StudyRepository
import java.io.File
import java.time.LocalDateTime
import javax.inject.Inject

class StudyRepositoryImpl @Inject constructor(
    private val studyDataSource: StudyDataSource,
) : StudyRepository {

    private fun setRecommendDummies(count: Int = 5): StudyResultList =
        StudyResultList(StudyResultList.getRecommendedDummies(count), hasNext = false, nextCursor = null)


    override suspend fun getRecruitingStudies(
        feeCategory: FeeRange?,
        categories: List<String>?,
        isOnline: Boolean?,
        sortBy: RecruitingStudySort?,
        cursor: Long?,
        size: Int
    ): Result<StudyResultList> =
        runCatching {
            val response = studyDataSource.getRecruitingStudies(
                feeCategory = feeCategory,
                categories = categories,
                sortBy = sortBy,
                isOnline = isOnline,
                cursor = cursor,
                size = size
            )
            response.result.toDomainList()
        }.onFailure { e ->
            Log.e("StudyRepository", "getRecruitingStudies failed", e)
        }.recoverCatching {
            setRecommendDummies(30)
        }

    override suspend fun getPreferLocationStudies(
        recruitingStatus: RecruitingStatus?,
        feeRange: FeeRange?,
        categories: List<String>?,
        sortBy: RecruitingStudySort?,
        cursor: Long?,
        size: Int,
        regionCodes : List<String>?
    ): Result<StudyResultList> =
        runCatching {
            val response = studyDataSource.getPreferLocationStudies(
                recruitingStatus = recruitingStatus,
                feeCategory = feeRange,
                categories = categories,
                sortType = sortBy,
                cursor = cursor,
                size = size,
                regionCodes = regionCodes
            )
            response.result.toDomainList()
        }.onFailure {
            Log.e("StudyRepository", "getPreferLocationStudies failed", it)
        }

    override suspend fun getPreferCategoryStudies(
        category: StudyTheme?,
        recruitingStatus: RecruitingStatus?,
        feeRange: FeeRange?,
        isOnline : Boolean?,
        sortBy: RecruitingStudySort?,
        cursor: Long?,
        size: Int,
    ): Result<StudyResultList> =
        runCatching {
            val response = studyDataSource.getPreferCategoryStudies(
                category = category,
                recruitingStatus = recruitingStatus,
                feeCategory = feeRange,
                isOnline = isOnline,
                sortType = sortBy,
                cursor = cursor,
                size = size,
            )
            response.result.toDomainList()
        }.onFailure {
            Log.e("StudyRepository", "getPreferCategoryStudies failed", it)
        }

    override suspend fun getRecommendedStudies(): Result<StudyResultList> =
        runCatching {
            studyDataSource.getRecommendedStudies().result.toDomainList()
        }.onFailure {
            Log.e("StudyRepository", "getRecommendedStudies failed", it)
        }

    override suspend fun createStudy(
        studyCreateModel: StudyCreateModel,
        imageFile: File?
    ): Result<Long> = runCatching {
        val requestDto = studyCreateModel.toData()

        val response = studyDataSource.createStudy(requestDto, imageFile)

        if (!response.isSuccess) {
            throw Exception(response.message ?: "스터디 생성 실패")
        }

        response.result.studyId
    }

    override suspend fun applyStudy(studyId: Long, message: String): Result<Unit> = runCatching {
        val response = studyDataSource.applyStudy(studyId, message)
        if (response.isSuccess) {
            Unit
        } else {
            throw Exception(response.message ?: "신청에 실패했습니다.")
        }
    }

    override suspend fun getStudyDetail(studyId: Long): Result<StudyDetailModel> =
        runCatching {
            val response = studyDataSource.getStudyDetail(studyId)
            response.result.toDomain()
        }

    override suspend fun getStudyMembers(studyId: Long): Result<List<StudyMemberModel>> =
        runCatching {
            val response = studyDataSource.getStudyMembers(studyId)
            response.result.members.map { it.toDomain() }
        }

    override suspend fun getUpcomingSchedules(studyId: Long): Result<List<StudyScheduleModel>> =
        runCatching {
            val response = studyDataSource.getUpcomingSchedules(studyId)
            response.result?.schedules?.map { it.toDomain() } ?: emptyList()
        }

    override suspend fun getMonthlySchedules(studyId: Long, year: Int, month: Int): Result<List<StudyScheduleModel>> =
        runCatching {
            val response = studyDataSource.getMonthlySchedules(studyId, year, month)
            response.result.schedules.map { it.toDomain() }
        }

    override suspend fun deleteSchedule(studyId: Long, scheduleId: Long): Result<Unit> = runCatching {
        val response = studyDataSource.deleteSchedule(studyId, scheduleId)

        if (!response.isSuccess) {
            throw Exception(response.message ?: "일정 삭제 실패")
        }
        Unit
    }.onFailure { e ->
        Log.e("StudyRepository", "deleteSchedule failed: studyId=$studyId, scheduleId=$scheduleId", e)
    }

    override suspend fun createTodo(
        studyId: Long,
        content: String,
        dueDate: String
    ): Result<Long> = runCatching {
        val response = studyDataSource.createTodo(
            studyId = studyId,
            content = content,
            dueDate = dueDate
        )

        if (!response.isSuccess) {
            throw Exception(response.message ?: "투두 생성 실패")
        }

        response.result.toDomain()
    }

    override suspend fun completeTodo(studyId: Long, todoId: Long): Result<Unit> = runCatching {
        val response = studyDataSource.completeTodo(studyId, todoId)
        if (!response.isSuccess) throw Exception(response.message ?: "완료 처리 실패")
        Unit
    }

    override suspend fun uncompleteTodo(studyId: Long, todoId: Long): Result<Unit> = runCatching {
        val response = studyDataSource.uncompleteTodo(studyId, todoId)
        if (!response.isSuccess) throw Exception(response.message ?: "미완료 처리 실패")
        Unit
    }

    override suspend fun deleteTodo(studyId: Long, todoId: Long): Result<Unit> = runCatching {
        val response = studyDataSource.deleteTodo(studyId, todoId)
        if (!response.isSuccess) throw Exception(response.message ?: "삭제 처리에 실패했습니다.")
        Unit
    }

    override suspend fun getMemberTodos(
        studyId: Long,
        memberId: Long,
        date: String
    ): Result<List<TodoModel>> = runCatching {
        val response = studyDataSource.getMemberTodos(studyId, memberId, date)
        if (response.isSuccess && response.result != null) {
            response.result.toDomain(memberId.toString())
        } else {
            throw Exception(response.message ?: "조회 실패")
        }
    }

    override suspend fun getFullStudyMemoirs(
        studyId: Long,
        cursor: Long?,
        size: Int
    ): Result<List<MemoirModel>> = runCatching {
        val response = studyDataSource.getStudyMemoirs(studyId, cursor, size)
        if (!response.isSuccess) throw Exception(response.message)

        response.result.memoirs.map { it.toDetailModel() }
    }

    override suspend fun getStudyRecentMemoirs(studyId: Long): Result<List<StudyRecentMemoirModel>> =
        runCatching {
            val response = studyDataSource.getStudyMemoirs(studyId, cursor = null, size = 5)
            response.result.memoirs.map { it.toDomain() }
        }

    override suspend fun deleteMemoir(studyId: Long, reviewId: Long): Result<Unit> = runCatching {
        val response = studyDataSource.deleteMemoir(studyId, reviewId)
        if (!response.isSuccess) throw Exception(response.message ?: "회고록 삭제 실패")
        Unit
    }

    override suspend fun postMemoir(
        studyId: Long,
        memoir: MemoirCreateModel, 
        imageFiles: List<File>
    ): Result<Long> = runCatching {
        val requestDto = memoir.toData()

        val response = studyDataSource.postMemoir(
            studyId = studyId,
            request = requestDto,
            imageFiles = imageFiles
        )

        if (!response.isSuccess) {
            throw Exception(response.message ?: "회고록 작성 실패")
        }

        response.result.reviewId
    }

    override suspend fun postReviewReaction(studyId: Long, reviewId: Long, reaction: String): Result<Unit?> {
        return runCatching {
            val response = studyDataSource.postReviewReaction(studyId, reviewId, reaction)
            if (response.isSuccess) response.result else throw Exception(response.message)
        }
    }

    override suspend fun deleteReviewReaction(studyId: Long, reviewId: Long, reaction: String): Result<Unit?> {
        return runCatching {
            val response = studyDataSource.deleteReviewReaction(studyId, reviewId, reaction)
            if (response.isSuccess) response.result else throw Exception(response.message)
        }
    }

    override suspend fun getCategoryStudies(
        recruitingStatus: RecruitingStatus?,
        feeRange: FeeRange?,
        category: String?,
        isOnline: Boolean?,
        sortBy: RecruitingStudySort,
        cursor: Long?,
        size: Int
    ): Result<StudyResultList> =
        runCatching {
            val response = studyDataSource.getCategoryStudies(
                recruitingStatus = recruitingStatus,
                feeCategory = feeRange,
                category = category,
                sortBy = sortBy,
                isOnline = isOnline,
                cursor = cursor,
                size = size
            )
            response.result.toDomainList()
        }.onFailure { e ->
            Log.e("StudyRepository", "getCategoryStudies failed", e)
        }

    override suspend fun getLikedStudies(
        cursor: Long?,
        size: Int
    ): Result<StudyResultList> =
        runCatching {
            val response = studyDataSource.getLikedStudies(
                cursor = cursor,
                size = size
            )
            response.result.toDomainList()
        }.onFailure { e ->
            Log.e("StudyRepository", "getLikedStudies failed", e)
        }

    override suspend fun getParticipatingStudy(
        cursor: Long?,
        size: Int
    ): Result<StudyResultList> =
        runCatching {
            val response = studyDataSource.getMyPageStudy(
                statuses = listOf("OWNER","APPROVED"),
                cursor = cursor,
                size = size
            )
            response.result.toDomainList()
        }.onFailure { e ->
            Log.e("StudyRepository", "getParticipatingStudy failed", e)
        }

    override suspend fun getRecruitingStudy(
        cursor: Long?,
        size: Int
    ): Result<StudyResultList> =
        runCatching {
            val response = studyDataSource.getMyPageStudy(
                statuses = listOf("OWNER"),
                cursor = cursor,
                size = size
            )
            response.result.toDomainList()
        }.onFailure { e ->
            Log.e("StudyRepository", "getRecruitingStudy failed", e)
        }

    override suspend fun getWaitingStudy(
        cursor: Long?,
        size: Int
    ): Result<StudyResultList> =
        runCatching {
            val response = studyDataSource.getMyPageStudy(
                statuses = listOf("APPLIED"),
                cursor = cursor,
                size = size
            )
            response.result.toDomainList()
        }.onFailure { e ->
            Log.e("StudyRepository", "getRecruitingStudy failed", e)
        }

    override suspend fun getStudyApplications(studyId: Long): Result<StudyApplicationResultList> =
        runCatching {
            val response = studyDataSource.getStudyApplications(studyId)
            response.result.toDomainList()
        }.onFailure {
            Log.e("StudyRepository", "getStudyApplicationMembers failed", it)
        }

    override suspend fun entryAcceptance(
        applicationId: Long,
        decision: String
    ): Result<Unit> =
        runCatching {
            studyDataSource.entryAcceptance(applicationId, decision)
        }

    override suspend fun createSchedule(
        studyId: Long,
        title: String,
        location: String,
        startAt: LocalDateTime,
        endAt: LocalDateTime
    ): Result<Unit> = runCatching {
        val createModel = StudyScheduleCreateModel(
            title = title,
            locationInfo = location,
            startAt = startAt,
            endAt = endAt
        )
        val response = studyDataSource.createSchedule(studyId, createModel.toData())

        if (!response.isSuccess) {
            throw Exception(response.message ?: "일정 생성 실패")
        }
        Unit
    }

    override suspend fun getAttendanceList(
        studyId: Long,
        scheduleId: Long
    ): Result<StudyAttendanceListModel> = runCatching {
        val response = studyDataSource.getAttendanceList(studyId, scheduleId)
        if (!response.isSuccess) throw Exception(response.message ?: "출석 목록 조회 실패")

        response.result.toDomain()
    }.onFailure { e ->
        Log.e("StudyRepository", "getAttendanceList failed: studyId=$studyId, scheduleId=$scheduleId", e)

    }

    override suspend fun startAttendance(studyId: Long, scheduleId: Long): Result<Unit> = runCatching {
        val response = studyDataSource.startAttendance(studyId, scheduleId)
        if (!response.isSuccess) {
            throw Exception(response.message ?: "출석체크 시작 실패")
        }
        Unit
    }.onFailure { e ->
        Log.e("StudyRepository", "startAttendance failed: studyId=$studyId, scheduleId=$scheduleId", e)
    }

    override suspend fun finishAttendance(studyId: Long, scheduleId: Long): Result<Unit> = runCatching {
        val response = studyDataSource.finishAttendance(studyId, scheduleId)
        if (!response.isSuccess) {
            throw Exception(response.message ?: "출석체크 종료 실패")
        }
        Unit
    }.onFailure { e ->
        Log.e("StudyRepository", "finishAttendance failed: studyId=$studyId, scheduleId=$scheduleId", e)
    }

    override suspend fun checkAttendance(studyId: Long, scheduleId: Long, token: String): Result<Unit> = runCatching {
        val response = studyDataSource.checkAttendance(studyId, scheduleId, token)
        if (!response.isSuccess) {
            throw Exception(response.message ?: "출석 처리 실패")
        }
        Unit
    }.onFailure { e ->
        Log.e("StudyRepository", "checkAttendance failed: studyId=$studyId, scheduleId=$scheduleId", e)
    }

    override suspend fun getAttendanceQr(studyId: Long, scheduleId: Long): Result<StudyAttendanceQrModel> =
        runCatching {
            val response = studyDataSource.getAttendanceQr(studyId, scheduleId)
            if (!response.isSuccess) throw Exception(response.message ?: "QR 조회 실패")
            response.result.toDomain()
        }
}