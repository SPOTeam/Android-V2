package com.umcspot.spot.checkList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.button.MultiButton
import com.umcspot.spot.designsystem.component.button.TextButton
import com.umcspot.spot.designsystem.component.button.TextButtonM
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.model.StudyTheme

@Composable
fun CheckListScreen(
    contentPadding: PaddingValues,
    onNextClick: () -> Unit,
    viewmodel: CheckListViewModel = hiltViewModel()
) {
    val topPad = contentPadding.calculateTopPadding()
    val bottomPad = contentPadding.calculateBottomPadding()

    // ✅ VM의 선택 상태 구독
    val theme by viewmodel.themes.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SpotTheme.colors.white)
            .padding(top = topPad, start = 14.dp, end = 14.dp, bottom = bottomPad),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(70.dp))
        Text(
            text = "내가 원하는 스터디를 선택해주세요",
            style = SpotTheme.typography.bodyMedium500.copy(fontSize = 16.sp),
            color = SpotTheme.colors.black
        )

        Spacer(Modifier.height(50.dp))

        ActivityThemeSection(
            selected = theme,
            onSelect = viewmodel::toggleTheme
        )

        Spacer(Modifier.weight(1f))

        // ✅ 다음 버튼: 선택이 있어야 활성화
        TextButton(
            text = "다음",
            enabled = theme != null,
            onClick = {
                viewmodel.submitThemes()
                onNextClick()
            }
        )
    }
}

@Composable
fun ActivityThemeSection(
    selected: Set<StudyTheme>,
    onSelect: (StudyTheme) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            StudyTheme.entries.forEach { theme ->
                val iconRes = when (theme) {
                    StudyTheme.LANGUAGE   -> painterResource(R.drawable.language)
                    StudyTheme.LICENSE    -> painterResource(R.drawable.license)
                    StudyTheme.EMPLOYMENT -> painterResource(R.drawable.employment)
                    StudyTheme.DISCUSSION  -> painterResource(R.drawable.discussion)
                    StudyTheme.NEWS       -> painterResource(R.drawable.news)
                    StudyTheme.SELFSTUDY  -> painterResource(R.drawable.self_study)
                    StudyTheme.PROJECT    -> painterResource(R.drawable.project)
                    StudyTheme.CONTEST    -> painterResource(R.drawable.contest)
                    StudyTheme.MAJOR      -> painterResource(R.drawable.major)
                    StudyTheme.ETC        -> painterResource(R.drawable.resource_else)
                }

                MultiButton(
                    text = theme.title,
                    painter = iconRes,
                    checked = theme in selected,
                    width = 156.dp,
                    onClick = { onSelect(theme) }
                )
            }
        }
    }
}
