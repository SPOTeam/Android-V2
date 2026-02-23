package com.umcspot.spot.study.repository

import com.umcspot.spot.model.FeeRange
import com.umcspot.spot.model.RecruitingStatus
import com.umcspot.spot.model.RecruitingStudySort
import com.umcspot.spot.model.StudyTheme
import com.umcspot.spot.study.model.MemoirCreateModel
import com.umcspot.spot.study.model.MemoirModel
import com.umcspot.spot.study.model.StudyApplicationResultList
import com.umcspot.spot.study.model.StudyCreateModel
import com.umcspot.spot.study.model.StudyDetailModel
import com.umcspot.spot.study.model.StudyMemberModel
import com.umcspot.spot.study.model.StudyRecentMemoirModel
import com.umcspot.spot.study.model.StudyResultList
import com.umcspot.spot.study.model.StudyScheduleModel
import com.umcspot.spot.study.model.TodoModel
import java.io.File
import java.time.LocalDateTime

interface StudyRepository {
    suspend fun getRecommendedStudies(): Result<StudyResultList>
    suspend fun getRecruitingStudies(
        feeCategory: FeeRange?,
        categories: List<String>?,
        isOnline: Boolean?,
        sortBy: RecruitingStudySort?,
        cursor: Long?,
        size: Int
    ): Result<StudyResultList>

    suspend fun getPreferLocationStudies(
        recruitingStatus : RecruitingStatus?,
        feeRange: FeeRange?,
        categories: List<String>?,
        sortBy: RecruitingStudySort?,
        cursor: Long?,
        size: Int,
        regionCodes : List<String>?
    ): Result<StudyResultList>

    suspend fun getPreferCategoryStudies(
        category : StudyTheme?,
        recruitingStatus : RecruitingStatus?,
        feeRange: FeeRange?,
        isOnline : Boolean?,
        sortBy: RecruitingStudySort?,
        cursor: Long?,
        size: Int,
    ): Result<StudyResultList>

    suspend fun createStudy(studyCreateModel: StudyCreateModel, imageFile: File?): Result<Long>

    suspend fun getStudyDetail(studyId: Long): Result<StudyDetailModel>

    suspend fun getStudyMembers(studyId: Long): Result<List<StudyMemberModel>>

    suspend fun getUpcomingSchedules(studyId: Long): Result<List<StudyScheduleModel>>

    suspend fun getMonthlySchedules(
        studyId: Long,
        year: Int,
        month: Int
    ): Result<List<StudyScheduleModel>>

    suspend fun createTodo(
        studyId: Long,
        content: String,
        dueDate: String
    ): Result<Long>

    suspend fun completeTodo(studyId: Long, todoId: Long): Result<Unit>

    suspend fun uncompleteTodo(studyId: Long, todoId: Long): Result<Unit>

    suspend fun deleteTodo(studyId: Long, todoId: Long): Result<Unit>

    suspend fun getMemberTodos(studyId: Long, memberId: Long, date: String): Result<List<TodoModel>>

    suspend fun getFullStudyMemoirs(
        studyId: Long,
        cursor: Long?,
        size: Int
    ): Result<List<MemoirModel>>

    suspend fun deleteMemoir(studyId: Long, reviewId: Long): Result<Unit>

    suspend fun getStudyRecentMemoirs(studyId: Long): Result<List<StudyRecentMemoirModel>>

    suspend fun postMemoir(
        studyId: Long,
        memoir: MemoirCreateModel,
        imageFiles: List<File>
    ): Result<Long>

    suspend fun postReviewReaction(studyId: Long, reviewId: Long, reaction: String): Result<Unit?>

    suspend fun deleteReviewReaction(studyId: Long, reviewId: Long, reaction: String): Result<Unit?>


    suspend fun getCategoryStudies(
        recruitingStatus: RecruitingStatus?,
        feeRange: FeeRange?,
        category: String?,
        isOnline : Boolean?,
        sortBy: RecruitingStudySort,
        cursor: Long?,
        size: Int,
    ): Result<StudyResultList>

    suspend fun getLikedStudies(
        cursor: Long?,
        size: Int,
    ): Result<StudyResultList>

    suspend fun getParticipatingStudy(
        cursor: Long?,
        size: Int,
    ): Result<StudyResultList>

    suspend fun getRecruitingStudy(
        cursor: Long?,
        size: Int,
    ): Result<StudyResultList>

    suspend fun getWaitingStudy(
        cursor: Long?,
        size: Int,
    ): Result<StudyResultList>

    suspend fun getStudyApplications(
        studyId: Long
    ) : Result<StudyApplicationResultList>

    suspend fun entryAcceptance(
        applicationId: Long,
        decision: String
    ) : Result<Unit>

    suspend fun createSchedule(
        studyId: Long,
        title: String,
        location: String,
        startAt: LocalDateTime,
        endAt: LocalDateTime
    ): Result<Unit>
}