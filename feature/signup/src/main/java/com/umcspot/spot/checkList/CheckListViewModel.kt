package com.umcspot.spot.checkList

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umcspot.spot.model.StudyTheme
import com.umcspot.spot.user.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentList 
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList 
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
    private val MAX_SELECTION_COUNT = 10

    private val _themes = MutableStateFlow<PersistentList<StudyTheme>>(
        savedStateHandle.get<List<StudyTheme>>(KEY_THEMES)?.toPersistentList() ?: persistentListOf()
    )

    val themes: StateFlow<PersistentList<StudyTheme>> = _themes.asStateFlow()

    fun toggleTheme(theme: StudyTheme) {
        _themes.update { currentList ->
            
            val newList = if (currentList.contains(theme)) {
                currentList.remove(theme) 
            } else {
                if (currentList.size < MAX_SELECTION_COUNT) {
                    currentList.add(theme) 
                } else {
                    currentList
                }
            }
            saveToHandle(newList)
            newList
        }
    }

    private fun saveToHandle(list: List<StudyTheme>) {
        savedStateHandle[KEY_THEMES] = ArrayList(list)
    }

    fun submitThemes() {
        val selected = _themes.value
        if (selected.isEmpty()) return

        viewModelScope.launch {
            userRepository.setUserTheme(selected)
        }
    }
}