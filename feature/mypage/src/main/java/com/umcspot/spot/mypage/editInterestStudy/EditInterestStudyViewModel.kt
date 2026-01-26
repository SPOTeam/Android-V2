package com.umcspot.spot.mypage.editInterestStudy

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umcspot.spot.model.StudyTheme
import com.umcspot.spot.study.model.Study
import com.umcspot.spot.user.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditInterestStudyViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _preferCategories = MutableStateFlow<List<StudyTheme>>(emptyList())
    val preferCategories : StateFlow<List<StudyTheme>> = _preferCategories.asStateFlow()

    fun loadPreferCategories() {
        viewModelScope.launch {
            userRepository.getUserPreferredCategory()
                .onSuccess {
                    _preferCategories.value = it.categories
                }
                .onFailure {
                    Log.e("EditInterestStudyViewModel", "loadPreferCategories: ${it.message}")
                }
        }
    }

    fun updatePreferCategories(categories: List<StudyTheme>) {
        viewModelScope.launch {
            userRepository.setUserTheme(categories)
                .onSuccess {
                    _preferCategories.value = categories
                }
                .onFailure {
                    Log.e("EditInterestStudyViewModel", "updatePreferCategories: ${it.message}")
                }
        }
    }
}

