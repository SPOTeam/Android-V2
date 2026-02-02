package com.umcspot.spot.study.datasource

import com.umcspot.spot.model.ActivityType
import com.umcspot.spot.model.FeeRange
import com.umcspot.spot.model.RecruitingStudySort
import com.umcspot.spot.model.StudyTheme
import com.umcspot.spot.network.model.BaseResponse
import com.umcspot.spot.study.dto.request.MemoirCreateRequestDto
import com.umcspot.spot.study.dto.request.StudyRequestDto
import com.umcspot.spot.study.dto.response.CreateStudyResponseDto
import com.umcspot.spot.study.dto.response.MemoirCreateResponseDto
import com.umcspot.spot.study.dto.response.StudyDetailResponseDto
import com.umcspot.spot.study.dto.response.StudyMemberResponseDto
import com.umcspot.spot.study.dto.response.StudyMemoirResponseDto
import com.umcspot.spot.study.dto.response.StudyMonthlyScheduleResponseDto
import com.umcspot.spot.study.dto.response.StudyResponseDto
import com.umcspot.spot.study.dto.response.StudyScheduleResponseDto
import com.umcspot.spot.study.dto.response.TodoCreateResponseDto
import com.umcspot.spot.study.dto.response.TodoQueryResponseDto
import java.io.File

interface StudyDataSource {
    suspend fun getPopularStudies(): BaseResponse<StudyResponseDto>

    suspend fun getRecommendStudies(): BaseResponse<StudyResponseDto>

    suspend fun getRecruitingStudies(
        sortType: RecruitingStudySort,
        activityType: ActivityType,
        theme: StudyTheme,
        feeRange: FeeRange
    ): BaseResponse<StudyResponseDto>

    suspend fun createStudy(
        request: StudyRequestDto,
        imageFile: File?
    ): BaseResponse<CreateStudyResponseDto>

    suspend fun getStudyDetail(studyId: Long): BaseResponse<StudyDetailResponseDto>

    suspend fun getStudyMembers(studyId: Long): BaseResponse<StudyMemberResponseDto>

    suspend fun getUpcomingSchedules(studyId: Long): BaseResponse<StudyScheduleResponseDto>

    suspend fun getMonthlySchedules(
        studyId: Long,
        year: Int,
        month: Int
    ): BaseResponse<StudyMonthlyScheduleResponseDto>

    suspend fun createTodo(
        studyId: Long,
        content: String,
        dueDate: String
    ): BaseResponse<TodoCreateResponseDto>

    suspend fun completeTodo(studyId: Long, todoId: Long): BaseResponse<Unit?>

    suspend fun uncompleteTodo(studyId: Long, todoId: Long): BaseResponse<Unit?>

    suspend fun deleteTodo(studyId: Long, todoId: Long): BaseResponse<Unit?>

    suspend fun getMemberTodos(studyId: Long, memberId: Long, date: String): BaseResponse<TodoQueryResponseDto>

    suspend fun getStudyMemoirs(studyId: Long, cursor: Long?, size: Int): BaseResponse<StudyMemoirResponseDto>

    suspend fun deleteMemoir(studyId: Long, memoirId: Long): BaseResponse<Unit?>

    suspend fun postMemoir(
        studyId: Long,
        request: MemoirCreateRequestDto,
        imageFiles: List<File>
    ): BaseResponse<MemoirCreateResponseDto>

    suspend fun postReviewReaction(studyId: Long, reviewId: Long, reaction: String): BaseResponse<Unit?>

    suspend fun deleteReviewReaction(studyId: Long, reviewId: Long, reaction: String): BaseResponse<Unit?>
}