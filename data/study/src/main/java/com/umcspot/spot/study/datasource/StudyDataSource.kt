package com.umcspot.spot.study.datasource

import com.umcspot.spot.model.FeeRange
import com.umcspot.spot.model.RecruitingStatus
import com.umcspot.spot.model.RecruitingStudySort
import com.umcspot.spot.model.StudyTheme
import com.umcspot.spot.network.model.BaseResponse
import com.umcspot.spot.study.dto.request.MemoirCreateRequestDto
import com.umcspot.spot.network.model.NullResultResponse
import com.umcspot.spot.study.dto.request.ScheduleCreateRequestDto
import com.umcspot.spot.study.dto.request.StudyRequestDto
import com.umcspot.spot.study.dto.response.CreateStudyResponseDto
import com.umcspot.spot.study.dto.response.MemoirCreateResponseDto
import com.umcspot.spot.study.dto.response.ScheduleCreateResponseDto
import com.umcspot.spot.study.dto.response.StudyDetailResponseDto
import com.umcspot.spot.study.dto.response.StudyMemberResponseDto
import com.umcspot.spot.study.dto.response.StudyMemoirResponseDto
import com.umcspot.spot.study.dto.response.StudyMonthlyScheduleResponseDto
import com.umcspot.spot.study.dto.response.StudyApplicationResponseDto
import com.umcspot.spot.study.dto.response.StudyAttendanceQrResponseDto
import com.umcspot.spot.study.dto.response.StudyAttendanceResponseDto
import com.umcspot.spot.study.dto.response.StudyResponseDto
import com.umcspot.spot.study.dto.response.StudyScheduleResponseDto
import com.umcspot.spot.study.dto.response.TodoCreateResponseDto
import com.umcspot.spot.study.dto.response.TodoQueryResponseDto
import java.io.File

interface StudyDataSource {
    suspend fun getRecommendedStudies(): BaseResponse<StudyResponseDto>
    suspend fun getRecruitingStudies(feeCategory: FeeRange?, categories: List<String>?, isOnline: Boolean?, sortBy: RecruitingStudySort?, cursor: Long?, size: Int): BaseResponse<StudyResponseDto>
    suspend fun getPreferLocationStudies(
        recruitingStatus : RecruitingStatus?,
        feeCategory: FeeRange?,
        categories: List<String>?,
        sortType: RecruitingStudySort?,
        cursor: Long?,
        size: Int,
        regionCodes : List<String>?
    ): BaseResponse<StudyResponseDto>

    suspend fun getPreferCategoryStudies(
        category : StudyTheme?,
        recruitingStatus : RecruitingStatus?,
        feeCategory: FeeRange?,
        isOnline : Boolean?,
        sortType: RecruitingStudySort?,
        cursor: Long?,
        size: Int,
    ): BaseResponse<StudyResponseDto>

    suspend fun createStudy(
        request: StudyRequestDto,
        imageFile: File?
    ): BaseResponse<CreateStudyResponseDto>

    suspend fun applyStudy(
        studyId: Long,
        message: String
    ): NullResultResponse

    suspend fun getStudyDetail(studyId: Long): BaseResponse<StudyDetailResponseDto>

    suspend fun getStudyMembers(studyId: Long): BaseResponse<StudyMemberResponseDto>

    suspend fun getUpcomingSchedules(studyId: Long): BaseResponse<StudyScheduleResponseDto>

    suspend fun getMonthlySchedules(
        studyId: Long,
        year: Int,
        month: Int
    ): BaseResponse<StudyMonthlyScheduleResponseDto>

    suspend fun deleteSchedule(
        studyId: Long,
        scheduleId: Long
    ): BaseResponse<Unit?>

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

    suspend fun getCategoryStudies(
        recruitingStatus : RecruitingStatus?,
        feeCategory: FeeRange?,
        category: String?,
        isOnline: Boolean?,
        sortBy: RecruitingStudySort?,
        cursor: Long?,
        size: Int
    ): BaseResponse<StudyResponseDto>

    suspend fun getLikedStudies(
        cursor: Long?,
        size: Int
    ): BaseResponse<StudyResponseDto>

    suspend fun getMyPageStudy(
        statuses : List<String>,
        cursor: Long?,
        size: Int
    ): BaseResponse<StudyResponseDto>

    suspend fun getStudyApplications(
        studyId: Long
    ): BaseResponse<StudyApplicationResponseDto>

    suspend fun entryAcceptance(
        applicationId : Long,
        decision : String
    ) : NullResultResponse

    suspend fun createSchedule(
        studyId: Long,
        request: ScheduleCreateRequestDto
    ): BaseResponse<ScheduleCreateResponseDto>

    suspend fun getAttendanceList(
        studyId: Long,
        scheduleId: Long
    ): BaseResponse<StudyAttendanceResponseDto>

    suspend fun startAttendance(studyId: Long, scheduleId: Long): NullResultResponse

    suspend fun finishAttendance(studyId: Long, scheduleId: Long): NullResultResponse

    suspend fun checkAttendance(studyId: Long, scheduleId: Long, token: String): NullResultResponse

    suspend fun getAttendanceQr(studyId: Long, scheduleId: Long): BaseResponse<StudyAttendanceQrResponseDto>

}