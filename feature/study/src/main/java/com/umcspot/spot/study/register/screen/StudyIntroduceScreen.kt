package com.umcspot.spot.study.register.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun StudyIntroduceScreen(modifier: Modifier = Modifier, onIntroduceValid: () -> Unit) {
    Column(modifier.fillMaxSize()) { Text("4단계: 스터디 소개 입력 화면") }
}
