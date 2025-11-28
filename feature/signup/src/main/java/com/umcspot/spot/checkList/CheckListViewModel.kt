package com.umcspot.spot.checkList

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umcspot.spot.model.StudyTheme
import com.umcspot.spot.ui.state.UiState
import com.umcspot.spot.user.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CheckListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository
) : ViewModel() {

    private val KEY_THEMES = "recruit_filter_themes"

    private val _themes = MutableStateFlow(
        (savedStateHandle.get<List<StudyTheme>>(KEY_THEMES) ?: emptyList()).toCollection(LinkedHashSet())
    )
    val themes: StateFlow<Set<StudyTheme>> = _themes.asStateFlow()

    fun toggleTheme(theme: StudyTheme) {
        val next = LinkedHashSet(_themes.value)
        if (!next.add(theme)) next.remove(theme)
        _themes.value = next
        savedStateHandle[KEY_THEMES] = next.toList()
    }

    fun submitThemes() {
        val selected = _themes.value.toList()
        if (selected.isEmpty()) return
        viewModelScope.launch {
            userRepository.setUserTheme(selected)
        }
    }
}