package com.umcspot.spot.study.detail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.component.appBar.BackTopBar
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun StudyDetailRoute(
    contentPadding: PaddingValues,
    studyId: Long,
    onBackClick: () -> Unit
) {
    BackHandler {
        onBackClick()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SpotTheme.colors.white)
    ) {
        Spacer(modifier = Modifier.height(contentPadding.calculateTopPadding()))

        BackTopBar(
            title = "스터디",
            onBackClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        )

        StudyDetailScreen(
            studyId = studyId,
            modifier = Modifier.padding(bottom = contentPadding.calculateBottomPadding())
        )
    }
}

@Composable
private fun StudyDetailScreen(
    studyId: Long,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = screenWidthDp(17.dp)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "스터디 상세 화면",
            style = SpotTheme.typography.h1,
            color = SpotTheme.colors.black
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "생성된 스터디 ID: $studyId",
            style = SpotTheme.typography.regular_500,
            color = SpotTheme.colors.B500
        )
    }
}