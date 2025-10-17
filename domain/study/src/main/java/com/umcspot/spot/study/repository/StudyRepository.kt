package com.umcspot.spot.study.repository

import com.umcspot.spot.study.model.Study
import com.umcspot.spot.study.model.StudyResult
import com.umcspot.spot.study.model.StudyResultList

interface StudyRepository {
    suspend fun getPopularStudies(): Result<StudyResultList>
    suspend fun getRecommendStudies(): Result<StudyResultList>
}