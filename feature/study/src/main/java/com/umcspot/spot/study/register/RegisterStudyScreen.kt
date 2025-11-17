package com.umcspot.spot.study.register

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.component.button.SpotActivationButton
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.study.register.component.StepProgressBar
import com.umcspot.spot.study.register.screen.StudyCategoryScreen
import com.umcspot.spot.study.register.screen.StudyInfoScreen
import com.umcspot.spot.study.register.screen.StudyIntroduceScreen
import com.umcspot.spot.study.register.screen.StudyPlaceScreen
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp
import kotlinx.coroutines.launch

@Composable
fun RegisterStudyRoute(
    contentPadding: PaddingValues,
    onBackClick: () -> Unit,
//    navigateToNext: () -> Unit,
    navigateToHome: () -> Unit,
    modifier: Modifier = Modifier

) {
    val pagerState = rememberPagerState(pageCount = { 4 })

    RegisterStudyScreen(
        pagerState = pagerState,
        contentPadding = contentPadding,
        onBackClick = onBackClick,
        isStepValid = { true },
        onSubmit = { navigateToHome() },
        modifier = modifier
    )
}

@Composable
private fun RegisterStudyScreen(
    pagerState: PagerState,
    contentPadding: PaddingValues,
    onBackClick: () -> Unit,
    isStepValid: (Int) -> Boolean,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()

    if (pagerState.currentPage > 0) {
        BackHandler {
            coroutineScope.launch {
                pagerState.animateScrollToPage(pagerState.currentPage - 1)
            }
        }
    } else {

        BackHandler {
            onBackClick()
        }
    }
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
                    0 -> StudyCategoryScreen(onCategorySelect = { })
                    1 -> StudyPlaceScreen(onPlaceSelect = { })
                    2 -> StudyInfoScreen(onInfoValid = { })
                    3 -> StudyIntroduceScreen(onIntroduceValid = { })
                }
            }

            Spacer(modifier = Modifier.height(screenHeightDp(16.dp)))

            SpotActivationButton(
                modifier = Modifier.fillMaxWidth(),
                buttonText = if (pagerState.currentPage == 3) "스터디 만들기" else "다음",
                isEnabled = isStepValid(pagerState.currentPage),
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
