package com.umcspot.spot.study.detail.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.umcspot.spot.designsystem.theme.SpotTheme

@Composable
fun StudyDetailBoardScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "게시판 화면입니다.", style = SpotTheme.typography.h5)
    }
}