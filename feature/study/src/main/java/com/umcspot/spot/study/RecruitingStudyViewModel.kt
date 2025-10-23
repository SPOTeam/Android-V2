package com.umcspot.spot.study

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umcspot.spot.model.RecruitingStudySort
import com.umcspot.spot.study.model.StudyResultList
import com.umcspot.spot.study.repository.StudyRepository
import com.umcspot.spot.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecruitingStudyViewModel @Inject constructor(
    private val studyRepository: StudyRepository
) : ViewModel() {

    data class RecruitingStudyUiState(
        val studies: UiState<StudyResultList> = UiState.Empty
    )

    private val _uiState = MutableStateFlow(RecruitingStudyUiState())
    val uiState: StateFlow<RecruitingStudyUiState> = _uiState.asStateFlow()

    private val _sortType = MutableStateFlow(RecruitingStudySort.LATEST)
    val sortType: StateFlow<RecruitingStudySort> = _sortType.asStateFlow()

    /** 정렬 기준으로 목록 로드 */
    fun load(selected: RecruitingStudySort) {

        _sortType.value = selected

        _uiState.update { it.copy(studies = UiState.Loading) }

        viewModelScope.launch {
            val res: Result<StudyResultList> = studyRepository.getRecruitingStudies(selected)

            val newState: UiState<StudyResultList> = res.fold(
                onSuccess = { data ->
                    // 필요 시 서버 응답에 정렬값 주입
                    val fixed = data /* .copy(selected = selected) */
                    if (fixed.studyList.isEmpty()) UiState.Empty else UiState.Success(fixed)
                },
                onFailure = { e ->
                    UiState.Failure(e.message ?: e.toString())
                }
            )

            _uiState.update { it.copy(studies = newState) }
        }
    }

    /** 정렬 변경 */
    fun selectSort(type: RecruitingStudySort) {
        val current = (uiState.value.studies as? UiState.Success<StudyResultList>)?.data

        _sortType.value = type

        // 1) UX를 위해 즉시 선택 값만 반영(데이터는 그대로)
        if (current != null) {
            _uiState.update {
                it.copy(studies = UiState.Success(current.copy()))
            }
        } else {
            _uiState.update { it.copy(studies = UiState.Loading) }
        }

        // 2) 서버에서 새 정렬로 목록 재요청 → 결과 반영
        viewModelScope.launch {
            val res = studyRepository.getRecruitingStudies(type)
            val newState: UiState<StudyResultList> = res.fold(
                onSuccess = { data ->
                    if (data.studyList.isEmpty()) UiState.Empty else UiState.Success(data)
                },
                onFailure = { e ->
                    UiState.Failure(e.message ?: e.toString())
                }
            )
            _uiState.update { it.copy(studies = newState) }
        }
    }
}
