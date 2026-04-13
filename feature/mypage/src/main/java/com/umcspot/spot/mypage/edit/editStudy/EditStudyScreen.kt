package com.umcspot.spot.mypage.edit.editStudy

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.umcspot.spot.common.location.LocationRow
import com.umcspot.spot.designsystem.component.appBar.BackTopBar
import com.umcspot.spot.designsystem.component.button.SpotActivationButton
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.model.ActivityType
import com.umcspot.spot.model.StudyTheme
import com.umcspot.spot.study.component.SpotStudyDialog
import com.umcspot.spot.study.register.component.StepProgressBar
import com.umcspot.spot.study.register.model.RegisterStudySideEffect
import com.umcspot.spot.study.register.model.RegisterStudyState
import com.umcspot.spot.study.register.screen.StudyCategoryScreen
import com.umcspot.spot.study.register.screen.StudyInfoScreen
import com.umcspot.spot.study.register.screen.StudyIntroduceScreen
import com.umcspot.spot.study.register.screen.StudyPlaceScreen
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun EditStudyRoute(
    studyId: Long,
    contentPadding: PaddingValues,
    onBackClick: () -> Unit,
    navigateToStudyDetail: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditStudyViewModel = hiltViewModel()
) {
    val pagerState = rememberPagerState(pageCount = { 4 })
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(studyId) {
        viewModel.initWithStudyId(studyId)
    }

    val handleBackPress: () -> Unit = {
        if (pagerState.currentPage > 0) {
            coroutineScope.launch {
                pagerState.animateScrollToPage(pagerState.currentPage - 1)
            }
        } else {
            onBackClick()
        }
    }

    BackHandler { handleBackPress() }

    if (uiState.isSuccessModalVisible) {
        SpotStudyDialog(
            onDismissRequest = { navigateToStudyDetail(studyId) },
            title = "스터디 수정 완료",
            description = "스터디 정보가 수정되었어요!\n스팟을 계속 즐겨봐요!",
            buttonText = "내 스터디 보러가기",
            showCheckIcon = true,
            onButtonClick = { navigateToStudyDetail(studyId) }
        )
    }

    LaunchedEffect(viewModel.sideEffect) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                is RegisterStudySideEffect.ShowSnackBar -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SpotTheme.colors.white)
    ) {
        Spacer(modifier = Modifier.height(contentPadding.calculateTopPadding()))

        BackTopBar(
            title = "스터디 수정",
            onBackClick = handleBackPress,
            modifier = Modifier.fillMaxWidth()
        )

        EditStudyScreen(
            pagerState = pagerState,
            uiState = uiState,
            contentPadding = PaddingValues(bottom = contentPadding.calculateBottomPadding()),
            isStepValid = viewModel::isStepValid,
            onSubmit = viewModel::submit,
            modifier = modifier,
            onStudyNameChange = viewModel::onStudyNameChange,
            onThemeSelect = { theme ->
                val currentThemes = uiState.studyThemes.toMutableList()
                if (currentThemes.contains(theme)) currentThemes.remove(theme)
                else if (currentThemes.size < 3) currentThemes.add(theme)
                viewModel.onCategorySelect(currentThemes)
            },
            onActivityTypeSelect = viewModel::onActivityTypeSelect,
            onQueryChange = viewModel::onLocationQueryChange,
            onSheetOpen = viewModel::openLocationSheet,
            onSheetDismiss = viewModel::dismissLocationSheet,
            onAddSelected = viewModel::addSelectedRegion,
            onRemoveSelected = viewModel::removeSelectedRegion,
            onMemberCountChange = viewModel::onMemberCountChange,
            onFeeInfoChange = viewModel::onFeeInfoChange,
            onPersonalityChange = viewModel::onPersonalityChange,
            onDescriptionChange = viewModel::onDescriptionChange,
            onImageSelected = viewModel::onImageSelected
        )
    }
}

@Composable
private fun EditStudyScreen(
    pagerState: PagerState,
    uiState: RegisterStudyState,
    contentPadding: PaddingValues,
    isStepValid: (Int) -> Boolean,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
    onStudyNameChange: (String) -> Unit,
    onThemeSelect: (StudyTheme) -> Unit,
    onActivityTypeSelect: (ActivityType) -> Unit,
    onQueryChange: (String) -> Unit,
    onSheetOpen: () -> Unit,
    onSheetDismiss: () -> Unit,
    onAddSelected: (LocationRow) -> Unit,
    onRemoveSelected: (LocationRow) -> Unit,
    onMemberCountChange: (Int) -> Unit,
    onFeeInfoChange: (Boolean?, String) -> Unit,
    onPersonalityChange: (Int, Int) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onImageSelected: (String?) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SpotTheme.colors.white)
            .padding(contentPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StepProgressBar(currentStep = pagerState.currentPage + 1)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = screenWidthDp(17.dp)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f),
                userScrollEnabled = false
            ) { page ->
                when (page) {
                    0 -> StudyCategoryScreen(
                        studyName = uiState.studyName,
                        selectedThemes = uiState.studyThemes.toImmutableList(),
                        onStudyNameChange = onStudyNameChange,
                        onThemeSelect = onThemeSelect
                    )

                    1 -> StudyPlaceScreen(
                        activityType = uiState.activityType,
                        isSheetVisible = uiState.isSheetVisible,
                        query = uiState.locationQuery,
                        searchResults = uiState.locationResults,
                        selectedRegions = uiState.selectedRegions.toImmutableList(),
                        onActivityTypeSelect = onActivityTypeSelect,
                        onQueryChange = onQueryChange,
                        onSheetOpen = onSheetOpen,
                        onSheetDismiss = onSheetDismiss,
                        onAddSelected = onAddSelected,
                        onRemoveSelected = onRemoveSelected
                    )

                    2 -> StudyInfoScreen(
                        memberCount = uiState.memberCount,
                        onMemberCountChange = onMemberCountChange,
                        hasFee = uiState.hasFee,
                        feeAmount = uiState.feeAmount,
                        onFeeInfoChange = onFeeInfoChange,
                        selectedStyles = uiState.personalitySelections,
                        onPersonalityChange = onPersonalityChange
                    )

                    3 -> StudyIntroduceScreen(
                        description = uiState.description,
                        onDescriptionChange = onDescriptionChange,
                        selectedImageUri = uiState.studyImageUri,
                        onImageSelected = onImageSelected,
                        onIntroduceValid = { }
                    )
                }
            }

            Spacer(modifier = Modifier.height(screenHeightDp(16.dp)))

            val isValid = isStepValid(pagerState.currentPage)

            SpotActivationButton(
                modifier = Modifier.fillMaxWidth(),
                buttonText = if (pagerState.currentPage == 3) "수정 완료" else "다음",
                isEnabled = isValid,
                onClick = {
                    coroutineScope.launch {
                        if (pagerState.currentPage < 3) {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        } else {
                            onSubmit()
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(screenHeightDp(13.dp)))
        }
    }
}