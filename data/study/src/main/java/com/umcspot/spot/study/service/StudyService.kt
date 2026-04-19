package com.umcspot.spot.study.service

import com.umcspot.spot.model.FeeRange
import com.umcspot.spot.model.RecruitingStatus
import com.umcspot.spot.model.RecruitingStudySort
import com.umcspot.spot.model.StudyTheme
import com.umcspot.spot.network.model.BaseResponse
import com.umcspot.spot.study.dto.request.TodoCreateRequestDto
import com.umcspot.spot.network.model.NullResultResponse
import com.umcspot.spot.study.dto.request.ScheduleCreateRequestDto
import com.umcspot.spot.study.dto.request.StudyApplyRequestDto
import com.umcspot.spot.study.dto.request.StudyReportRequestDto
import com.umcspot.spot.study.dto.request.StudyWithdrawRequestDto
import com.umcspot.spot.study.dto.request.BoardCreateRequestDto
import com.umcspot.spot.study.dto.request.StudyPostCommentRequestDto
import com.umcspot.spot.study.dto.response.CreateStudyResponseDto
import com.umcspot.spot.study.dto.response.MemoirCreateResponseDto
import com.umcspot.spot.study.dto.response.ScheduleCreateResponseDto
import com.umcspot.spot.study.dto.response.BoardCreateResponseDto
import com.umcspot.spot.study.dto.response.SendStudyPostCommentResponseDto
import com.umcspot.spot.study.dto.response.StudyDetailResponseDto
import com.umcspot.spot.study.dto.response.StudyMemberResponseDto
import com.umcspot.spot.study.dto.response.StudyMemoirResponseDto
import com.umcspot.spot.study.dto.response.StudyMonthlyScheduleResponseDto
import com.umcspot.spot.study.dto.response.StudyApplicationResponseDto
import com.umcspot.spot.study.dto.response.StudyAttendanceQrResponseDto
import com.umcspot.spot.study.dto.response.StudyAttendanceResponseDto
import com.umcspot.spot.study.dto.response.StudyPostDetailResponseDto
import com.umcspot.spot.study.dto.response.StudyPostsResponseDto
import com.umcspot.spot.study.dto.response.StudyResponseDto
import com.umcspot.spot.study.dto.response.StudyScheduleResponseDto
import com.umcspot.spot.study.dto.response.TodoCreateResponseDto
import com.umcspot.spot.study.dto.response.TodoQueryResponseDto
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface StudyService {

    @GET("/api/studies/recommended")
    suspend fun getRecommendedStudies(
    ): BaseResponse<StudyResponseDto>

    @GET("/api/studies/recruiting")
    suspend fun getRecruitingStudies(
        @Query("feeCategory") feeCategory: FeeRange?,
        @Query("categories") categories:  List<String>?,
        @Query("isOnline") isOnline: Boolean?,
        @Query("sortBy") sortBy: RecruitingStudySort?,
        @Query("cursor") cursor: Long?,
        @Query("size") size: Int
    ): BaseResponse<StudyResponseDto>

    @GET("/api/studies/by-region")
    suspend fun getPreferLocationStudies(
        @Query("recruitingStatus") recruitingStatus: RecruitingStatus?,
        @Query("feeCategory") feeCategory: FeeRange?,
        @Query("categories") categories: List<String>?,
        @Query("isOnline") isOnline: Boolean?,
        @Query("sortBy") sortBy: RecruitingStudySort?,
        @Query("cursor") cursor: Long?,
        @Query("size") size: Int,
        @Query("regionCodes") regionCodes: List<String>?
    ): BaseResponse<StudyResponseDto>

    @GET("/api/studies/by-category")
    suspend fun getPreferCategoryStudies(
        @Query("category") category: StudyTheme?,
        @Query("recruitingStatus") recruitingStatus: RecruitingStatus?,
        @Query("feeCategory") feeCategory: FeeRange?,
        @Query("isOnline") isOnline: Boolean?,
        @Query("sortBy") sortBy: RecruitingStudySort?,
        @Query("cursor") cursor: Long?,
        @Query("size") size: Int,
    ): BaseResponse<StudyResponseDto>

    @GET("/api/studies/categories")
    suspend fun getCategoryStudies(
        @Query("recruitingStatus") recruitingStatus: RecruitingStatus?,
        @Query("feeCategory") feeCategory: FeeRange?,
        @Query("category") category: String?,
        @Query("isOnline") isOnline: Boolean?,
        @Query("sortBy") sortBy: RecruitingStudySort?,
        @Query("cursor") cursor: Long?,
        @Query("size") size: Int
    ): BaseResponse<StudyResponseDto>

    @GET("/api/studies/liked")
    suspend fun getLikedStudies(
        @Query("cursor") cursor: Long?,
        @Query("size") size: Int
    ): BaseResponse<StudyResponseDto>

    // 스터디 생성
    @Multipart
    @POST("/api/studies")
    suspend fun createStudy(
        @Part request: MultipartBody.Part,
        @Part imageFile: MultipartBody.Part?
    ): BaseResponse<CreateStudyResponseDto>

    // 스터디 수정하기
    @Multipart
    @PATCH("/api/studies/{studyId}")
    suspend fun updateStudy(
        @Path("studyId") studyId: Long,
        @Part request: MultipartBody.Part,
        @Part imageFile: MultipartBody.Part?
    ): NullResultResponse

    // 스터디 신청하기
    @POST("/api/studies/{studyId}/apply")
    suspend fun applyStudy(
        @Path("studyId") studyId: Long,
        @Body request: StudyApplyRequestDto
    ): NullResultResponse

    // 스터디 나가기
    @POST("/api/studies/{studyId}/withdraw")
    suspend fun withdrawStudy(
        @Path("studyId") studyId: Long,
        @Body request: StudyWithdrawRequestDto
    ): NullResultResponse

    // 스터디 디테일 조회
    @GET("/api/studies/{studyId}/info")
    suspend fun getStudyDetail(
        @Path("studyId") studyId: Long
    ): BaseResponse<StudyDetailResponseDto>

    // 스터디 멤버 조회
    @GET("/api/studies/{studyId}/members")
    suspend fun getStudyMembers(
        @Path("studyId") studyId: Long
    ): BaseResponse<StudyMemberResponseDto>

    // 스터디 좋아요
    @POST("/api/studies/{studyId}/like")
    suspend fun likeStudy(
        @Path("studyId") studyId: Long
    ): NullResultResponse

    // 스터디 좋아요 취소
    @DELETE("/api/studies/{studyId}/like")
    suspend fun unlikeStudy(
        @Path("studyId") studyId: Long
    ): NullResultResponse

    // 다가오는 일정 조회
    @GET("/api/studies/{studyId}/schedules/upcoming")
    suspend fun getUpcomingSchedules(
        @Path("studyId") studyId: Long
    ): BaseResponse<StudyScheduleResponseDto>

    // 해당 날짜 일정 조회
    @GET("/api/studies/{studyId}/schedules/monthly")
    suspend fun getMonthlySchedules(
        @Path("studyId") studyId: Long,
        @Query("year") year: Int,
        @Query("month") month: Int
    ): BaseResponse<StudyMonthlyScheduleResponseDto>

    // 일정 삭제
    @DELETE("/api/studies/{studyId}/schedules/{scheduleId}")
    suspend fun deleteSchedule(
        @Path("studyId") studyId: Long,
        @Path("scheduleId") scheduleId: Long
    ): BaseResponse<Unit?>

    // 투두 리스트 만들기
    @POST("/api/studies/{studyId}/todos")
    suspend fun createTodo(
        @Path("studyId") studyId: Long,
        @Body request: TodoCreateRequestDto
    ): BaseResponse<TodoCreateResponseDto>

    // 투두 리스트 완료
    @POST("/api/studies/{studyId}/todos/{todoId}/complete")
    suspend fun completeTodo(
        @Path("studyId") studyId: Long,
        @Path("todoId") todoId: Long
    ): BaseResponse<Unit?>

    // 투두 리스트 미완료
    @POST("/api/studies/{studyId}/todos/{todoId}/uncomplete")
    suspend fun uncompleteTodo(
        @Path("studyId") studyId: Long,
        @Path("todoId") todoId: Long
    ): BaseResponse<Unit?>

    // 투두 삭제
    @DELETE("/api/studies/{studyId}/todos/{todoId}")
    suspend fun deleteTodo(
        @Path("studyId") studyId: Long,
        @Path("todoId") todoId: Long
    ): BaseResponse<Unit?>

    // 투두 리스트 조회
    @GET("/api/studies/{studyId}/todos/members/{memberId}")
    suspend fun getMemberTodos(
        @Path("studyId") studyId: Long,
        @Path("memberId") memberId: Long,
        @Query("date") date: String
    ): BaseResponse<TodoQueryResponseDto>

    // 스터디 회고록 조회
    @GET("/api/studies/{studyId}/reviews")
    suspend fun getStudyMemoirs(
        @Path("studyId") studyId: Long,
        @Query("cursor") cursor: Long? = null,
        @Query("size") size: Int = 10
    ): BaseResponse<StudyMemoirResponseDto>

    // 스터디 회고록 삭제
    @DELETE("/api/studies/{studyId}/reviews/{reviewId}")
    suspend fun deleteMemoir(
        @Path("studyId") studyId: Long,
        @Path("reviewId") reviewId: Long
    ): BaseResponse<Unit?>

    // 회고록 작성
    @Multipart
    @POST("/api/studies/{studyId}/reviews")
    suspend fun postMemoir(
        @Path("studyId") studyId: Long,
        @Part request: MultipartBody.Part,
        @Part imageFile: List<MultipartBody.Part>?
    ): BaseResponse<MemoirCreateResponseDto>

    @POST("/api/studies/{studyId}/posts")
    suspend fun postBoard(
        @Path("studyId") studyId: Long,
        @Body request: BoardCreateRequestDto,
    ): BaseResponse<BoardCreateResponseDto>

    // 회고록 반응 추가
    @POST("/api/studies/{studyId}/reviews/{reviewId}/reactions")
    suspend fun postReviewReaction(
        @Path("studyId") studyId: Long,
        @Path("reviewId") reviewId: Long,
        @Query("reaction") reaction: String
    ): BaseResponse<Unit?>

    // 회고록 반응 삭제
    @DELETE("/api/studies/{studyId}/reviews/{reviewId}/reactions")
    suspend fun deleteReviewReaction(
        @Path("studyId") studyId: Long,
        @Path("reviewId") reviewId: Long,
        @Query("reaction") reaction: String
    ): BaseResponse<Unit?>

    @GET("/api/studies/me")
    suspend fun getMyPageStudy(
        @Query("statuses") statuses: List<String>,
        @Query("cursor") cursor: Long?,
        @Query("size") size: Int
    ): BaseResponse<StudyResponseDto>

    @GET("/api/studies/{studyId}/applications")
    suspend fun getStudyApplications(
        @Path("studyId") studyId: Long
    ): BaseResponse<StudyApplicationResponseDto>

    @POST("/api/studies/applications/{applicationId}")
    suspend fun entryAcceptance(
        @Path("applicationId") applicationId: Long,
        @Query("decision") decision: String
    ): NullResultResponse

    @POST("/api/studies/{studyId}/schedules")
    suspend fun createSchedule(
        @Path("studyId") studyId: Long,
        @Body request: ScheduleCreateRequestDto
    ): BaseResponse<ScheduleCreateResponseDto>

    @GET("/api/studies/{studyId}/schedules/{scheduleId}/attendance")
    suspend fun getAttendanceList(
        @Path("studyId") studyId: Long,
        @Path("scheduleId") scheduleId: Long
    ): BaseResponse<StudyAttendanceResponseDto>

    @POST("/api/studies/{studyId}/schedules/{scheduleId}/attendance")
    suspend fun startAttendance(
        @Path("studyId") studyId: Long,
        @Path("scheduleId") scheduleId: Long
    ): NullResultResponse

    @DELETE("/api/studies/{studyId}/schedules/{scheduleId}/attendance")
    suspend fun finishAttendance(
        @Path("studyId") studyId: Long,
        @Path("scheduleId") scheduleId: Long
    ): NullResultResponse

    @POST("/api/studies/{studyId}/schedules/{scheduleId}/attendance/check")
    suspend fun checkAttendance(
        @Path("studyId") studyId: Long,
        @Path("scheduleId") scheduleId: Long,
        @Query("token") token: String
    ): NullResultResponse

    @GET("/api/studies/{studyId}/schedules/{scheduleId}/attendance/qr")
    suspend fun getAttendanceQr(
        @Path("studyId") studyId: Long,
        @Path("scheduleId") scheduleId: Long
    ): BaseResponse<StudyAttendanceQrResponseDto>

    @POST("/api/studies/{studyId}/members/{targetMemberId}/report")
    suspend fun reportStudyMember(
        @Path("studyId") studyId: Long,
        @Path("targetMemberId") targetMemberId: Long,
        @Body request: StudyReportRequestDto
    ): NullResultResponse

    @DELETE("/api/studies/{studyId}")
    suspend fun deleteStudy(
        @Path("studyId") studyId: Long
    ): NullResultResponse

    @GET("/api/studies/{studyId}/posts")
    suspend fun getStudyPostsList(
        @Path("studyId") studyId: Long,
        @Query("cursor") cursor: Long?,
        @Query("size") size: Int
    ): BaseResponse<StudyPostsResponseDto>

    @GET("/api/studies/{studyId}/posts/{postId}")
    suspend fun getStudyPostDetail(
        @Path("studyId") studyId: Long,
        @Path("postId") postId: Long,
    ): BaseResponse<StudyPostDetailResponseDto>

    @POST("/api/studies/{studyId}/posts/{postId}/pin")
    suspend fun studyPostPin(
        @Path("studyId") studyId: Long,
        @Path("postId") postId: Long,
    ): NullResultResponse

    @DELETE("/api/studies/{studyId}/posts/{postId}/pin")
    suspend fun studyPostUnPin(
        @Path("studyId") studyId: Long,
        @Path("postId") postId: Long,
    ): NullResultResponse

    @POST("/api/studies/{studyId}/posts/{postId}/like")
    suspend fun studyPostLike(
        @Path("studyId") studyId: Long,
        @Path("postId") postId: Long,
    ): NullResultResponse

    @DELETE("/api/studies/{studyId}/posts/{postId}/like")
    suspend fun studyPostUnLike(
        @Path("studyId") studyId: Long,
        @Path("postId") postId: Long,
    ): NullResultResponse

    @POST("/api/studies/{studyId}/posts/{postId}/comments")
    suspend fun createStudyPostComment(
        @Path("studyId") studyId: Long,
        @Path("postId") postId: Long,
        @Body request: StudyPostCommentRequestDto
    ): BaseResponse<SendStudyPostCommentResponseDto>
}
