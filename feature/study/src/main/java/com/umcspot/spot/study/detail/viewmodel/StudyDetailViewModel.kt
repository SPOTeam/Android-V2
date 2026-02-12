package com.umcspot.spot.study.detail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umcspot.spot.study.detail.model.StudyDetailSideEffect
import com.umcspot.spot.study.detail.model.StudyDetailState
import com.umcspot.spot.study.model.MemoirCreateModel
import com.umcspot.spot.study.model.TodoModel
import com.umcspot.spot.study.repository.StudyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.collections.map

@HiltViewModel
class StudyPostDetailViewModel @Inject constructor(
    private val studyRepository: StudyRepository
) : ViewModel() {


}