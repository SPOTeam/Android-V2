package com.umcspot.spot.mypage.main

import com.umcspot.spot.ui.state.UiState
import com.umcspot.spot.user.model.MyPageResult

data class MyPageState (
    val preferRegions : UiState<List<String?>> = UiState.Empty,
    val preferCategories : UiState<List<String?>> = UiState.Empty,
    val memberInfo : UiState<MyPageResult> = UiState.Empty,
    val appVersion: UiState<String> = UiState.Empty
)