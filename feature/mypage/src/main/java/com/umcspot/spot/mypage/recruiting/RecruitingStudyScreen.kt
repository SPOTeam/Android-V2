package com.umcspot.spot.mypage.recruiting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.SpotSpinner
import com.umcspot.spot.designsystem.component.button.TextButton
import com.umcspot.spot.designsystem.component.button.TextButtonState
import com.umcspot.spot.designsystem.component.empty.EmptyAlertWithButton
import com.umcspot.spot.designsystem.component.study.StudyListItem
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.G300
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.study.model.StudyResult
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp
import com.umcspot.spot.ui.state.UiState
import kotlinx.coroutines.launch

@Composable
fun RecruitingStudyScreen(
    contentPadding : PaddingValues,
    onRegisterScrollToTop: ((() -> Unit)?) -> Unit,
    onStudyClick : (Long) -> Unit,
    moveToMakeStudy : () -> Unit,
    moveToCheckApplied: () -> Unit,
    viewmodel : RecruitingStudyViewModel = hiltViewModel()
) {
    val uiState by viewmodel.uiState.collectAsStateWithLifecycle()

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val ui = uiState.recruitingStudy
    val itemList: List<StudyResult> = when (ui) {
        is UiState.Success -> ui.data.studyList
        else -> emptyList()
    }

    LaunchedEffect(Unit) {
        viewmodel.loadRecruitingStudy()
        onRegisterScrollToTop {
            scope.launch {
                listState.animateScrollToItem(0)
            }
        }
    }

    val shouldLoadMore = remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

            totalItems > 0 && lastVisibleItemIndex >= totalItems - 3
        }
    }

    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value) {
            val successData = (ui as? UiState.Success)?.data
            if (successData?.hasNext == true) {
                viewmodel.loadNextPage()
            }
        }
    }

    when (ui) {
        is UiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(SpotTheme.colors.white),
                contentAlignment = Alignment.Center
            ) {
                SpotSpinner(size = screenWidthDp(30.dp))
            }
        }
        is UiState.Empty, is UiState.Failure -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(SpotTheme.colors.white),
                contentAlignment = Alignment.Center
            ) {
                EmptyAlertWithButton(
                    alertTitle = "모집 중인 스터디가 아직 없어요!",
                    alertDes = "스터디 파트너들과 함께 목표를 이뤄보세요!",
                    buttonText = "스터디 만들기",
                    painter = painterResource(R.drawable.study_default),
                    onClick = { moveToMakeStudy() },
                )
            }
        }
        is UiState.Success -> {
            RecruitingStudyScreenContent(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(top = contentPadding.calculateTopPadding(), bottom = contentPadding.calculateBottomPadding())
                    .padding(horizontal = screenWidthDp(17.dp)),
                studyList = itemList,
                listState = listState,
                onStudyClick = onStudyClick,
                moveToCheckApplied = moveToCheckApplied
            )
        }
    }
}

@Composable
fun RecruitingStudyScreenContent(
    modifier: Modifier = Modifier,
    studyList: List<StudyResult>,
    listState: LazyListState,
    onStudyClick: (Long) -> Unit,
    moveToCheckApplied: () -> Unit
) {
    LazyColumn(
        state = listState,
        modifier = modifier,
    ) {
        items(
            items = studyList,
            key = { it.id }
        ) { item ->
            Spacer(Modifier.padding(screenHeightDp(5.dp)))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopStart
            ) {
                StudyListItem(
                    item = item,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onStudyClick(item.id) },
                    checkAppliedSlot = {
                        TextButton(
                            modifier = Modifier
                                .width(screenWidthDp(60.dp))
                                .height(screenHeightDp(26.dp)),
                            text = "신청 확인",
                            style = SpotTheme.typography.regular_500,
                            onClick = moveToCheckApplied,
                            state = TextButtonState.B500State,
                            shape = SpotShapes.Hard
                        )
                    }
                )
            }

            if (studyList.indexOf(item) != studyList.lastIndex) {
                Spacer(Modifier.padding(screenHeightDp(5.dp)))

                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = SpotTheme.colors.G300,
                    thickness = 1.dp
                )
            }
        }
    }
}
