package com.umcspot.spot.study.my

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umcspot.spot.study.my.model.MyStudyState
import com.umcspot.spot.study.repository.StudyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyStudyViewModel @Inject constructor(
    private val repository: StudyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyStudyState())
    val uiState = _uiState.asStateFlow()

    fun refresh() {
        _uiState.update { MyStudyState() }
        loadMoreStudies()
    }

    fun loadMoreStudies() {
        if (_uiState.value.isLoading || (_uiState.value.nextCursor != null && !_uiState.value.hasNext)) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            repository.getParticipatingStudy(_uiState.value.nextCursor, 10)
                .onSuccess { resultList ->
                    _uiState.update { state ->
                        state.copy(
                            studyList = (state.studyList + resultList.studyList).toImmutableList(),
                            hasNext = resultList.hasNext,
                            nextCursor = resultList.nextCursor,
                            isLoading = false,
                            isError = false
                        )
                    }
                }
                .onFailure { t ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isError = true,
                            errorMessage = t.message
                        )
                    }
                }
        }
    }
}