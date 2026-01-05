package com.umcspot.spot.study.repository

import com.umcspot.spot.model.ActivityType
import com.umcspot.spot.model.FeeRange
import com.umcspot.spot.model.RecruitingStudySort
import com.umcspot.spot.model.StudyTheme
import com.umcspot.spot.study.model.StudyCreateModel
import com.umcspot.spot.study.model.StudyResultList
import java.io.File

interface StudyRepository {
    suspend fun getPopularStudies(): Result<StudyResultList>
    suspend fun getRecommendStudies(): Result<StudyResultList>
    suspend fun getRecruitingStudies(
        feeCategory: FeeRange? = null,
        categories: List<String>? = null,
        isOnline: Boolean? = null,
        sortBy: RecruitingStudySort? = null,
        cursor: Long? = null, size: Int
    ): Result<StudyResultList>


    suspend fun getPreferLocationStudies(
        sortType: RecruitingStudySort,
        activityType: ActivityType?,
        theme: StudyTheme?,
        feeRange: FeeRange?
    ): Result<StudyResultList>

    suspend fun createStudy(studyCreateModel: StudyCreateModel, imageFile: File?): Result<Long>
}