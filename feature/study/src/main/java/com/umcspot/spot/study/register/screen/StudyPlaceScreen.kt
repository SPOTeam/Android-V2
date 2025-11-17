package com.umcspot.spot.study.register.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun StudyPlaceScreen(modifier: Modifier = Modifier, onPlaceSelect: () -> Unit) {
    Column(modifier.fillMaxSize()) { Text("2단계: 스터디 장소 선택 화면") }
}