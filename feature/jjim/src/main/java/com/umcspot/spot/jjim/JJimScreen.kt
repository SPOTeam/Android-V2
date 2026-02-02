package com.umcspot.spot.jjim

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.SpotSpinner
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
fun JJimScreen(
    contentPadding: PaddingValues,
    viewmodel: JJimViewModel = hiltViewModel(),
    onRegisterScrollToTop: ((() -> Unit)?) -> Unit,
    onMoveToStudyClick: () -> Unit,
    onItemClick: (Long) -> Unit
) {
    val state by viewmodel.uiState.collectAsStateWithLifecycle()

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val topPad = contentPadding.calculateTopPadding()
    val bottomPad = contentPadding.calculateBottomPadding()

    val ui = state.studies
    val itemList: List<StudyResult> = when (ui) {
        is UiState.Success -> ui.data.studyList
        else -> emptyList()
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

    LaunchedEffect(Unit) {
        viewmodel.load()
        onRegisterScrollToTop {
            scope.launch {
                listState.animateScrollToItem(0)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SpotTheme.colors.white)
            .padding(top = topPad, bottom = bottomPad, start = screenWidthDp(17.dp), end = screenWidthDp(17.dp))
    ) {
        when(ui) {
            is UiState.Loading -> {
                Surface(
                    color = SpotTheme.colors.white,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        SpotSpinner()
                    }
                }
            }

            is UiState.Failure -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    EmptyAlertWithButton(
                        painter = painterResource(R.drawable.emoji_sad),
                        alertTitle = "찜한 스터디가 없어요.",
                        alertDes = "SPOT과 함께 다양한 스터디를 만나봐요",
                        buttonText = "스터디 둘러보기",
                        onClick = { onMoveToStudyClick() }
                    )
                }
            }

            is UiState.Empty -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    EmptyAlertWithButton(
                        painter = painterResource(R.drawable.emoji_sad),
                        alertTitle = "찜한 스터디가 없어요.",
                        alertDes = "SPOT과 함께 다양한 스터디를 만나봐요",
                        buttonText = "스터디 둘러보기",
                        onClick = { onMoveToStudyClick() }
                    )
                }
            }

            is UiState.Success -> {
                JJimScreenContent (
                    modifier = Modifier
                        .fillMaxSize(),
                    studies = itemList,
                    listState = listState,
                    onItemClick = onItemClick,
                )
            }
        }
    }
}

@Composable
private fun JJimScreenContent(
    modifier: Modifier = Modifier,
    studies: List<StudyResult>,
    listState: LazyListState,
    onItemClick: (Long) -> Unit,
) {
    LazyColumn(
        state = listState,
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(screenHeightDp(0.dp))
    ) {
        items(
            items = studies,
            key = { it.id }
        ) { item ->
            Spacer(Modifier.padding(screenHeightDp(5.dp)))

            StudyListItem(
                item = item,
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = { onItemClick(item.id) }
            )

            if(studies.indexOf(item) != studies.lastIndex) {
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