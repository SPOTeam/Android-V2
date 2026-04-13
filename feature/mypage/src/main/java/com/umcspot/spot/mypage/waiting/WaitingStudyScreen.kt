package com.umcspot.spot.mypage.waiting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.SpotSpinner
import com.umcspot.spot.designsystem.component.appBar.BackTopBar
import com.umcspot.spot.designsystem.component.empty.EmptyAlertWithButton
import com.umcspot.spot.designsystem.component.study.StudyListItem
import com.umcspot.spot.designsystem.theme.G300
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.study.model.StudyResult
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp
import com.umcspot.spot.ui.state.UiState
import kotlinx.coroutines.launch

@Composable
fun WaitingStudyRoute(
    contentPadding: PaddingValues,
    onBackClick: () -> Unit,
    onRegisterScrollToTop: ((() -> Unit)?) -> Unit,
    onStudyClick: (Long) -> Unit,
    moveToRecruitingStudy: () -> Unit,
    viewmodel: WaitingStudyViewModel = hiltViewModel()
) {
    val uiState by viewmodel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val ui = uiState.waitingStudy
    val itemList: List<StudyResult> = when (ui) {
        is UiState.Success -> ui.data.studyList
        else -> emptyList()
    }

    LaunchedEffect(Unit) {
        viewmodel.loadWaitingStudy()
        onRegisterScrollToTop {
            scope.launch { listState.animateScrollToItem(0) }
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
            if (successData?.hasNext == true) viewmodel.loadNextPage()
        }
    }

    WaitingStudyScreen(
        contentPadding = contentPadding,
        uiState = ui,
        itemList = itemList,
        listState = listState,
        onBackClick = onBackClick,
        onStudyClick = onStudyClick,
        moveToRecruitingStudy = moveToRecruitingStudy
    )
}

@Composable
private fun WaitingStudyScreen(
    contentPadding: PaddingValues,
    uiState: UiState<*>,
    itemList: List<StudyResult>,
    listState: LazyListState,
    onBackClick: () -> Unit,
    onStudyClick: (Long) -> Unit,
    moveToRecruitingStudy: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SpotTheme.colors.white)
            .padding(
                top = contentPadding.calculateTopPadding(),
                bottom = contentPadding.calculateBottomPadding()
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            
            BackTopBar(
                title = "대기 중인 스터디",
                onBackClick = onBackClick,
                modifier = Modifier.fillMaxWidth()
            )

            when (uiState) {
                is UiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        SpotSpinner(size = screenWidthDp(30.dp))
                    }
                }

                is UiState.Empty, is UiState.Failure -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        EmptyAlertWithButton(
                            alertTitle = "대기 중인 스터디가 아직 없어요.",
                            alertDes = "스팟에서 내 목표를 이뤄봐요",
                            buttonText = "스터디 둘러보기",
                            painter = painterResource(R.drawable.study_default),
                            onClick = { moveToRecruitingStudy() },
                        )
                    }
                }

                is UiState.Success -> {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = screenWidthDp(17.dp)),
                        contentPadding = PaddingValues(bottom = screenHeightDp(20.dp))
                    ) {
                        items(
                            items = itemList,
                            key = { it.id }
                        ) { item ->
                            Spacer(Modifier.height(screenHeightDp(5.dp)))

                            StudyListItem(
                                item = item,
                                modifier = Modifier.fillMaxWidth(),
                                onClick = { onStudyClick(item.id) }
                            )

                            if (itemList.indexOf(item) != itemList.lastIndex) {
                                Spacer(Modifier.height(screenHeightDp(5.dp)))
                                HorizontalDivider(
                                    modifier = Modifier.fillMaxWidth(),
                                    color = SpotTheme.colors.G300,
                                    thickness = 1.dp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}