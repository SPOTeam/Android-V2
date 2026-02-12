package com.umcspot.spot.study.detail.model

import com.umcspot.spot.study.model.MemoirModel
import com.umcspot.spot.study.model.StudyMemberModel
import com.umcspot.spot.study.model.StudyRecentMemoirModel
import com.umcspot.spot.study.model.StudyScheduleModel
import com.umcspot.spot.study.model.TodoModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.time.LocalDate

data class StudyPostDetailState(
    val homeState: StudyHomeState = StudyHomeState(),

)

