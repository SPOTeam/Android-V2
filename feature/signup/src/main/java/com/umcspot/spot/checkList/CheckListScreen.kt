package com.umcspot.spot.checkList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.umcspot.spot.designsystem.component.button.TextButton
import com.umcspot.spot.designsystem.component.study.section.ActivityThemeSection
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.signup.SignUpViewModel

@Composable
fun CheckListScreen(
    contentPadding: PaddingValues,
    onNextClick: () -> Unit,
    signUpViewModel: SignUpViewModel = hiltViewModel(),
    viewmodel: CheckListViewModel = hiltViewModel()
) {
    val topPad = contentPadding.calculateTopPadding()
    val bottomPad = contentPadding.calculateBottomPadding()

    
    val themes by viewmodel.themes.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SpotTheme.colors.white)
            .padding(top = topPad, start = 14.dp, end = 14.dp, bottom = bottomPad),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(70.dp))

        Text(
            text = "내가 원하는 스터디를 선택해주세요",
            style = SpotTheme.typography.h3,
            color = SpotTheme.colors.black
        )

        Spacer(Modifier.height(50.dp))

        ActivityThemeSection(
            selectedThemes = themes,
            onSelect = viewmodel::toggleTheme,
            modifier = Modifier.fillMaxWidth(),
            maxSelection = 10 
        )

        Spacer(Modifier.weight(1f))

        TextButton(
            text = "다음",
            enabled = themes.isNotEmpty(),
            onClick = {
                signUpViewModel.saveNameIfChanged()
                viewmodel.submitThemes()
                onNextClick()
            }
        )
    }
}