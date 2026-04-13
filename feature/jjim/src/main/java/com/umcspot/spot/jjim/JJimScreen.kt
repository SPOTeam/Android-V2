package com.umcspot.spot.jjim

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.SpotSpinner
import com.umcspot.spot.designsystem.component.appBar.BackTopBar
import com.umcspot.spot.designsystem.component.empty.EmptyAlert
import com.umcspot.spot.designsystem.component.study.StudyListItem
import com.umcspot.spot.designsystem.theme.G300
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.study.model.StudyResult
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp
import com.umcspot.spot.ui.state.UiState
import kotlinx.coroutines.launch

@Composable
fun JJimRoute(
    contentPadding: PaddingValues,
    viewmodel: JJimViewModel = hiltViewModel(),
    onRegisterScrollToTop: ((() -> Unit)?) -> Unit,
    onItemClick: (Long) -> Unit
) {
    val state by viewmodel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

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
            if (successData?.hasNext == true) viewmodel.loadNextPage()
        }
    }

    LaunchedEffect(Unit) {
        viewmodel.load()
        onRegisterScrollToTop {
            scope.launch { listState.animateScrollToItem(0) }
        }
    }

    JJimScreen(
        contentPadding = contentPadding,
        ui = ui,
        itemList = itemList,
        listState = listState,
        onItemClick = onItemClick
    )
}

@Composable
private fun JJimScreen(
    contentPadding: PaddingValues,
    ui: UiState<*>,
    itemList: List<StudyResult>,
    listState: LazyListState,
    onItemClick: (Long) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SpotTheme.colors.white)
    ) {
        Spacer(modifier = Modifier.height(contentPadding.calculateTopPadding()))

        BackTopBar(
            title = "찜한 스터디",
            onBackClick = {},
            modifier = Modifier.fillMaxWidth()
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(
                    start = screenWidthDp(17.dp),
                    end = screenWidthDp(17.dp),
                    bottom = contentPadding.calculateBottomPadding()
                )
        ) {
            when (ui) {
                is UiState.Loading -> {
                    SpotSpinner(modifier = Modifier.align(Alignment.Center))
                }

                is UiState.Failure, is UiState.Empty -> {
                    EmptyAlert(
                        modifier = Modifier.align(Alignment.Center),
                        painter = painterResource(R.drawable.ic_like_count),
                        alertTitle = "찜한 스터디가 없어요.",
                        alertDes = "관심 있는 스터디를 찜해보세요."
                    )
                }

                is UiState.Success -> {
                    JJimScreenContent(
                        modifier = Modifier.fillMaxSize(),
                        studies = itemList,
                        listState = listState,
                        onItemClick = onItemClick
                    )
                }
            }
        }
    }
}

@Composable
private fun JJimScreenContent(
    modifier: Modifier = Modifier,
    studies: List<StudyResult>,
    listState: LazyListState,
    onItemClick: (Long) -> Unit
) {
    LazyColumn(
        state = listState,
        modifier = modifier
    ) {
        itemsIndexed(
            items = studies,
            key = { _, item -> item.id }
        ) { index, item ->
            Spacer(modifier = Modifier.height(screenHeightDp(12.dp)))

            StudyListItem(
                item = item,
                modifier = Modifier.fillMaxWidth(),
                onClick = { onItemClick(item.id) }
            )

            Spacer(modifier = Modifier.height(screenHeightDp(12.dp)))

            if (index != studies.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = SpotTheme.colors.G300,
                    thickness = 1.dp
                )
            }
        }
    }
}