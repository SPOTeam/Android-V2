package com.umcspot.spot.study.repository

import com.umcspot.spot.study.model.Study
import com.umcspot.spot.study.model.StudyResult

interface StudyRepository {
    suspend fun getDummies(): Result<StudyResult>
}