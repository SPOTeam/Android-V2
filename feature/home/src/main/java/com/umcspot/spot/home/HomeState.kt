package com.umcspot.spot.home

import com.umcspot.spot.home.model.HomeResult
import com.umcspot.spot.ui.state.UiState

data class HomeState(
    var user: UiState<HomeResult> = UiState.Loading
)