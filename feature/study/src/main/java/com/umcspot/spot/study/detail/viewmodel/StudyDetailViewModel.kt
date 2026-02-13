package com.umcspot.spot.study.detail.viewmodel

import androidx.lifecycle.ViewModel
import com.umcspot.spot.study.detail.model.StudyPostContentState
import com.umcspot.spot.study.repository.StudyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class StudyPostDetailViewModel @Inject constructor(
    private val studyRepository: StudyRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(StudyPostContentState())
    val uiState = _uiState.asStateFlow()
}