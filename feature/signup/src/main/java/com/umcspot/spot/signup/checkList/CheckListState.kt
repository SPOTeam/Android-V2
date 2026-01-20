package com.umcspot.spot.signup.checkList

import com.umcspot.spot.model.StudyTheme
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

data class CheckListState(
    val allThemes: PersistentList<StudyTheme> = persistentListOf(),
    val selectedThemes: PersistentList<StudyTheme> = persistentListOf(),
)

sealed interface CheckListSideEffect {
    data object NavigateToSaving : CheckListSideEffect
    data class ShowSnackBar(val message: String) : CheckListSideEffect
}