package com.umcspot.spot.study.register.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.component.study.section.ActivityThemeSection
import com.umcspot.spot.designsystem.theme.G400
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.model.StudyTheme
import com.umcspot.spot.study.register.component.StudyNameTextField
import com.umcspot.spot.ui.extension.screenHeightDp
import kotlinx.collections.immutable.ImmutableList

@Composable
fun StudyCategoryScreen(
    studyName: String,
    selectedThemes: ImmutableList<StudyTheme>,
    onStudyNameChange: (String) -> Unit,
    onThemeSelect: (StudyTheme) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = screenHeightDp(65.dp))
    ) {
        Text(
            text = "어떤 스터디인가요?",
            style = SpotTheme.typography.h3
        )

        Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))

        StudyNameTextField(
            value = studyName,
            onValueChange = onStudyNameChange
        )

        Spacer(modifier = Modifier.height(screenHeightDp(40.dp)))

        Text(
            text = "카테고리를 선택해주세요",
            style = SpotTheme.typography.h3,
            color = SpotTheme.colors.black
        )

        Spacer(modifier = Modifier.height(screenHeightDp(4.dp)))

        Text(
            text = "최대 3개까지 선택할 수 있어요",
            style = SpotTheme.typography.regular_500,
            color = SpotTheme.colors.G400
        )

        Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))

        ActivityThemeSection(
            selectedThemes = selectedThemes,
            onSelect = onThemeSelect
        )
    }
}
