package com.umcspot.spot.study.my.model

import com.umcspot.spot.study.model.StudyResult
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class MyStudyState(
    val studyList: ImmutableList<StudyResult> = persistentListOf(),
    val isLoading: Boolean = false,
    val hasNext: Boolean = false,
    val nextCursor: Long? = null,
    val isError: Boolean = false,
    val errorMessage: String? = null
)

sealed interface MyStudySideEffect {
    data class ShowSnackBar(val message: String) : MyStudySideEffect
    data class NavigateToDetail(val studyId: Long) : MyStudySideEffect
}