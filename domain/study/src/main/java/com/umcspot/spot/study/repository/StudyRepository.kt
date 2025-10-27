package com.umcspot.spot.study.repository

import com.umcspot.spot.model.ActivityType
import com.umcspot.spot.model.FeeRange
import com.umcspot.spot.model.RecruitingStudySort
import com.umcspot.spot.model.StudyTheme
import com.umcspot.spot.study.model.Study
import com.umcspot.spot.study.model.StudyResult
import com.umcspot.spot.study.model.StudyResultList

interface StudyRepository {
    suspend fun getPopularStudies(): Result<StudyResultList>
    suspend fun getRecommendStudies(): Result<StudyResultList>
    suspend fun getRecruitingStudies(sortType : RecruitingStudySort, activityType: ActivityType?, theme: StudyTheme?, feeRange: FeeRange?): Result<StudyResultList>
}