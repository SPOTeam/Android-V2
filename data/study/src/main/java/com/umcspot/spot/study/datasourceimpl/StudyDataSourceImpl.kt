package com.umcspot.spot.study.datasourceimpl

import com.umcspot.spot.model.FeeRange
import com.umcspot.spot.model.RecruitingStatus
import com.umcspot.spot.model.RecruitingStudySort
import com.umcspot.spot.model.StudyTheme
import com.umcspot.spot.network.model.BaseResponse
import com.umcspot.spot.network.model.NullResultResponse
import com.umcspot.spot.study.datasource.StudyDataSource
import com.umcspot.spot.study.dto.request.MemoirCreateRequestDto
import com.umcspot.spot.study.dto.request.StudyRequestDto
import com.umcspot.spot.study.dto.request.TodoCreateRequestDto
import com.umcspot.spot.study.dto.response.CreateStudyResponseDto
import com.umcspot.spot.study.dto.response.MemoirCreateResponseDto
import com.umcspot.spot.study.dto.response.StudyDetailResponseDto
import com.umcspot.spot.study.dto.response.StudyMemberResponseDto
import com.umcspot.spot.study.dto.response.StudyMemoirResponseDto
import com.umcspot.spot.study.dto.response.StudyMonthlyScheduleResponseDto
import com.umcspot.spot.study.dto.response.StudyApplicationResponseDto
import com.umcspot.spot.study.dto.response.StudyResponseDto
import com.umcspot.spot.study.dto.response.StudyScheduleResponseDto
import com.umcspot.spot.study.dto.response.TodoCreateResponseDto
import com.umcspot.spot.study.service.StudyService
import com.umcspot.spot.ui.extension.toMultipartBodyPart
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class StudyDataSourceImpl @Inject constructor(
    private val studyService: StudyService
) : StudyDataSource {

    override suspend fun getRecommendedStudies(
    ): BaseResponse<StudyResponseDto> =
        studyService.getRecommendedStudies()

    override suspend fun getRecruitingStudies(
        feeCategory: FeeRange?,
        categories: List<String>?,
        isOnline: Boolean?,
        sortBy: RecruitingStudySort?,
        cursor: Long?,
        size: Int
    ): BaseResponse<StudyResponseDto> =
        studyService.getRecruitingStudies(feeCategory, categories, isOnline, sortBy, cursor, size)

    override suspend fun getPreferLocationStudies(
        recruitingStatus: RecruitingStatus?,
        feeCategory: FeeRange?,
        categories: List<String>?,
        sortType: RecruitingStudySort?,
        cursor: Long?,
        size: Int,
        regionCodes: List<String>?
    ): BaseResponse<StudyResponseDto> =
        studyService.getPreferLocationStudies(recruitingStatus, feeCategory, categories, null, sortType, cursor, size, regionCodes)

    override suspend fun getPreferCategoryStudies(
        category: StudyTheme?,
        recruitingStatus: RecruitingStatus?,
        feeCategory: FeeRange?,
        isOnline : Boolean?,
        sortType: RecruitingStudySort?,
        cursor: Long?,
        size: Int,
    ): BaseResponse<StudyResponseDto> =
        studyService.getPreferCategoryStudies(category, recruitingStatus, feeCategory, isOnline, sortType, cursor, size)

    override suspend fun createStudy(
        request: StudyRequestDto,
        imageFile: File?
    ): BaseResponse<CreateStudyResponseDto> {
        val requestPart = request.toMultipartBodyPart("request")
        val imagePart = imageFile?.let { file ->
            val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("imageFile", file.name, requestBody)
        }
        return studyService.createStudy(requestPart, imagePart)
    }

    override suspend fun getStudyDetail(studyId: Long): BaseResponse<StudyDetailResponseDto> =
        studyService.getStudyDetail(studyId)

    override suspend fun getStudyMembers(studyId: Long): BaseResponse<StudyMemberResponseDto> =
        studyService.getStudyMembers(studyId)

    override suspend fun getUpcomingSchedules(studyId: Long): BaseResponse<StudyScheduleResponseDto> =
        studyService.getUpcomingSchedules(studyId)

    override suspend fun getMonthlySchedules(
        studyId: Long,
        year: Int,
        month: Int
    ): BaseResponse<StudyMonthlyScheduleResponseDto> =
        studyService.getMonthlySchedules(studyId, year, month)

    override suspend fun createTodo(
        studyId: Long,
        content: String,
        dueDate: String
    ): BaseResponse<TodoCreateResponseDto> {
        val request = TodoCreateRequestDto(
            content = content,
            dueDate = dueDate
        )
        return studyService.createTodo(studyId, request)
    }

    override suspend fun completeTodo(studyId: Long, todoId: Long): BaseResponse<Unit?> =
        studyService.completeTodo(studyId, todoId)

    override suspend fun uncompleteTodo(studyId: Long, todoId: Long): BaseResponse<Unit?> =
        studyService.uncompleteTodo(studyId, todoId)

    override suspend fun deleteTodo(studyId: Long, todoId: Long): BaseResponse<Unit?> =
        studyService.deleteTodo(studyId, todoId)

    override suspend fun getMemberTodos(studyId: Long, memberId: Long, date: String) =
        studyService.getMemberTodos(studyId, memberId, date)

    override suspend fun getStudyMemoirs(studyId: Long, cursor: Long?, size: Int): BaseResponse<StudyMemoirResponseDto> =
        studyService.getStudyMemoirs(studyId, cursor, size)

    override suspend fun deleteMemoir(studyId: Long, memoirId: Long): BaseResponse<Unit?> =
        studyService.deleteMemoir(studyId, memoirId)

    override suspend fun postMemoir(
        studyId: Long,
        request: MemoirCreateRequestDto,
        imageFiles: List<File>
    ): BaseResponse<MemoirCreateResponseDto> {
        val requestPart = request.toMultipartBodyPart("request")

        val imageParts = imageFiles.map { file ->
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("imageFile", file.name, requestFile)
        }

        return studyService.postMemoir(
            studyId = studyId,
            request = requestPart,
            imageFile = imageParts.ifEmpty { null }
        )
    }

    override suspend fun postReviewReaction(studyId: Long, reviewId: Long, reaction: String): BaseResponse<Unit?> {
        return studyService.postReviewReaction(studyId, reviewId, reaction)
    }

    override suspend fun deleteReviewReaction(studyId: Long, reviewId: Long, reaction: String): BaseResponse<Unit?> {
        return studyService.deleteReviewReaction(studyId, reviewId, reaction)
    }
    override suspend fun getCategoryStudies(
        recruitingStatus: RecruitingStatus?,
        feeCategory: FeeRange?,
        category: String?,
        isOnline: Boolean?,
        sortBy: RecruitingStudySort?,
        cursor: Long?,
        size: Int
    ): BaseResponse<StudyResponseDto> =
        studyService.getCategoryStudies(
            recruitingStatus = recruitingStatus,
            feeCategory = feeCategory,
            category = category,
            isOnline = isOnline,
            sortBy = sortBy,
            cursor = cursor,
            size = size
        )

    override suspend fun getLikedStudies(
        cursor: Long?,
        size: Int
    ): BaseResponse<StudyResponseDto> =
        studyService.getLikedStudies(
            cursor = cursor,
            size = size
        )

    override suspend fun getMyPageStudy(
        statuses: List<String>,
        cursor: Long?,
        size: Int
    ): BaseResponse<StudyResponseDto> =
        studyService.getMyPageStudy(
            statuses = statuses,
            cursor = cursor,
            size = size
        )

    override suspend fun getStudyApplications(studyId: Long): BaseResponse<StudyApplicationResponseDto> =
        studyService.getStudyApplications(studyId)

    override suspend fun entryAcceptance(
        applicationId: Long,
        decision: String
    ): NullResultResponse =
        studyService.entryAcceptance(applicationId,decision)
}