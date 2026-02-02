package com.umcspot.spot.home

import com.umcspot.spot.domain.board.model.board.BestPostResult
import com.umcspot.spot.study.model.StudyResultList
import com.umcspot.spot.ui.state.UiState
import com.umcspot.spot.weather.model.WeatherResult

data class HomeState(
    val weatherInfo: UiState<WeatherResult> = UiState.Loading,
    val popularPostInfo: UiState<BestPostResult> = UiState.Loading,
    val popularStudies: UiState<StudyResultList> = UiState.Loading,
    val recommendStudies: UiState<StudyResultList> = UiState.Loading,
)
