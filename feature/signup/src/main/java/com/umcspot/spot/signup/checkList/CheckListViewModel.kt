package com.umcspot.spot.signup.checkList

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umcspot.spot.model.StudyTheme
import com.umcspot.spot.user.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository
) : ViewModel() {

    private val KEY_THEMES = "selected_themes"

    private val _uiState = MutableStateFlow(CheckListState())
    val uiState = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<CheckListSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    init {
        initializeData()
    }

    private fun initializeData() {
        val allThemes = StudyTheme.entries.toPersistentList()
        val savedSelection = savedStateHandle.get<List<StudyTheme>>(KEY_THEMES)?.toPersistentList()
            ?: persistentListOf()

        _uiState.update {
            it.copy(allThemes = allThemes, selectedThemes = savedSelection)
        }
    }

    fun onThemeCheckChange(theme: StudyTheme) {
        _uiState.update { state ->
            val currentList = state.selectedThemes
            val newList = if (currentList.contains(theme)) {
                currentList.remove(theme)
            } else {
                currentList.add(theme)
            }
            saveToHandle(newList)
            state.copy(selectedThemes = newList)
        }
    }

    private fun saveToHandle(list: List<StudyTheme>) {
        savedStateHandle[KEY_THEMES] = ArrayList(list)
    }

    fun onNextClick() = viewModelScope.launch {
        val currentThemes = _uiState.value.selectedThemes

        if (currentThemes.isEmpty()) {
            _sideEffect.emit(CheckListSideEffect.ShowSnackBar("관심 주제를 하나 이상 선택해주세요."))
            return@launch
        }
        userRepository.setUserTheme(currentThemes)
            .onSuccess {
                _sideEffect.emit(CheckListSideEffect.NavigateToSaving)
            }
            .onFailure { e ->
                _sideEffect.emit(CheckListSideEffect.ShowSnackBar("오류 발생: ${e.message}"))
            }
    }
}