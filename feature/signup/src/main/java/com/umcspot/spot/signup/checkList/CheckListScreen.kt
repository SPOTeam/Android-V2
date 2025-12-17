package com.umcspot.spot.signup.checkList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.umcspot.spot.designsystem.component.button.SpotActivationButton
import com.umcspot.spot.designsystem.component.study.section.ActivityThemeSection
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.model.StudyTheme
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CheckListRoute(
    contentPadding: PaddingValues,
    navigateToSaving: () -> Unit,
    viewModel: CheckListViewModel = hiltViewModel(),
    snackBarHostState: SnackbarHostState? = null
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.sideEffect) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                is CheckListSideEffect.NavigateToSaving -> navigateToSaving()
                is CheckListSideEffect.ShowSnackBar -> {
                    snackBarHostState?.showSnackbar(effect.message)
                }
            }
        }
    }

    CheckListScreen(
        contentPadding = contentPadding,
        selectedThemes = uiState.selectedThemes, 
        onThemeSelect = viewModel::onThemeCheckChange, 
        onNextClick = viewModel::onNextClick 
    )
}

@Composable
fun CheckListScreen(
    contentPadding: PaddingValues,
    selectedThemes: ImmutableList<StudyTheme>,
    onThemeSelect: (StudyTheme) -> Unit,
    onNextClick: () -> Unit
) {
    val topPad = contentPadding.calculateTopPadding()
    val bottomPad = contentPadding.calculateBottomPadding()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SpotTheme.colors.white)
            .padding(
                top = topPad,
                start = screenWidthDp(17.dp),
                end = screenWidthDp(17.dp),
                bottom = bottomPad
            )
    ) {
        Spacer(Modifier.height(screenHeightDp(68.dp)))

        Text(
            text = "내가 원하는 스터디를 선택해주세요!",
            style = SpotTheme.typography.h3,
            color = SpotTheme.colors.black
        )

        Spacer(Modifier.height(screenHeightDp(67.dp)))

        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            ActivityThemeSection(
                selectedThemes = selectedThemes,
                onSelect = onThemeSelect,
                modifier = Modifier.fillMaxWidth(),
                maxSelection = 10
            )
        }

        SpotActivationButton(
            modifier = Modifier.fillMaxWidth(),
            buttonText = "다음",
            isEnabled = selectedThemes.isNotEmpty(),
            onClick = onNextClick
        )

        Spacer(Modifier.height(screenHeightDp(13.dp)))
    }
}