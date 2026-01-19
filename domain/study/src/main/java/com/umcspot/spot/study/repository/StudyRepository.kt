package com.umcspot.spot.study.repository

import com.umcspot.spot.model.FeeRange
import com.umcspot.spot.model.RecruitingStatus
import com.umcspot.spot.model.RecruitingStudySort
import com.umcspot.spot.model.StudyTheme
import com.umcspot.spot.study.model.StudyCreateModel
import com.umcspot.spot.study.model.StudyResultList
import java.io.File

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

    suspend fun createStudy(studyCreateModel: StudyCreateModel, imageFile: File?): Result<Long>

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
}