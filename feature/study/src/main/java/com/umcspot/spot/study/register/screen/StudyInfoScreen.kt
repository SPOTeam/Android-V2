package com.umcspot.spot.study.register.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun StudyInfoScreen(modifier: Modifier = Modifier, onInfoValid: () -> Unit) {
    Column(modifier.fillMaxSize()) { Text("3단계: 스터디 정보 입력 화면") }
}
