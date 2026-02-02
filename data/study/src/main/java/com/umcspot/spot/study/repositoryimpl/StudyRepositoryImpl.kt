package com.umcspot.spot.study.repositoryimpl

import com.umcspot.spot.model.ActivityType
import com.umcspot.spot.model.FeeRange
import com.umcspot.spot.model.RecruitingStudySort
import com.umcspot.spot.model.StudyTheme
import com.umcspot.spot.study.datasource.StudyDataSource
import com.umcspot.spot.study.mapper.toData
import com.umcspot.spot.study.mapper.toDetailModel
import com.umcspot.spot.study.mapper.toDomain
import com.umcspot.spot.study.model.MemoirCreateModel
import com.umcspot.spot.study.model.MemoirModel
import com.umcspot.spot.study.model.StudyCreateModel
import com.umcspot.spot.study.model.StudyDetailModel
import com.umcspot.spot.study.model.StudyMemberModel
import com.umcspot.spot.study.model.StudyRecentMemoirModel
import com.umcspot.spot.study.model.StudyResultList
import com.umcspot.spot.study.model.StudyScheduleModel
import com.umcspot.spot.study.model.TodoModel
import com.umcspot.spot.study.repository.StudyRepository
import java.io.File
import javax.inject.Inject

class StudyRepositoryImpl @Inject constructor(
    private val studyDataSource: StudyDataSource
) : StudyRepository {

    override suspend fun getPopularStudies(): Result<StudyResultList> =
        runCatching {
            val response = studyDataSource.getPopularStudies()
            response.result.toDomain()
        }

    override suspend fun getRecommendStudies(): Result<StudyResultList> =
        runCatching {
            val response = studyDataSource.getRecommendStudies()
            response.result.toDomain()
        }

    override suspend fun getRecruitingStudies(
        sortType: RecruitingStudySort,
        activityType: ActivityType?,
        theme: StudyTheme?,
        feeRange: FeeRange?
    ): Result<StudyResultList> =
        runCatching {
            val response = studyDataSource.getRecruitingStudies(
                sortType = sortType,
                activityType = activityType ?: ActivityType.OFFLINE,
                theme = theme ?: StudyTheme.OTHER,
                feeRange = feeRange ?: FeeRange.NONE
            )
            response.result.toDomain()
        }

    override suspend fun getPreferLocationStudies(
        sortType: RecruitingStudySort,
        activityType: ActivityType?,
        theme: StudyTheme?,
        feeRange: FeeRange?
    ): Result<StudyResultList> =
        runCatching {
            val response = studyDataSource.getRecruitingStudies(
                sortType = sortType,
                activityType = activityType ?: ActivityType.OFFLINE,
                theme = theme ?: StudyTheme.OTHER,
                feeRange = feeRange ?: FeeRange.NONE
            )
            response.result.toDomain()
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
            response.result.schedules.map { it.toDomain() }
        }

    override suspend fun getMonthlySchedules(studyId: Long, year: Int, month: Int): Result<List<StudyScheduleModel>> =
        runCatching {
            val response = studyDataSource.getMonthlySchedules(studyId, year, month)
            response.result.schedules.map { it.toDomain() }
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
}