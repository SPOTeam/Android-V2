package com.umcspot.spot.study.my

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
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
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.study.my.component.MyStudyListContent
import com.umcspot.spot.study.my.model.MyStudyState
import com.umcspot.spot.ui.extension.screenWidthDp
import kotlinx.coroutines.launch

@Composable
fun MyStudyRoute(
    contentPadding: PaddingValues,
    navigateToStudyDetail: (Long) -> Unit,
    onRegisterScrollToTop: ((() -> Unit)?) -> Unit,
    viewModel: MyStudyViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.refresh()
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
        if (shouldLoadMore.value && state.hasNext && !state.isLoading) {
            viewModel.loadMoreStudies()
        }
    }

    MyStudyScreen(
        contentPadding = contentPadding,
        state = state,
        listState = listState,
        onItemClick = navigateToStudyDetail
    )
}

@Composable
private fun MyStudyScreen(
    contentPadding: PaddingValues,
    state: MyStudyState,
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
            title = "내 스터디",
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
            when {
                state.isLoading && state.studyList.isEmpty() -> {
                    SpotSpinner(modifier = Modifier.align(Alignment.Center))
                }

                (state.isError || state.studyList.isEmpty()) && !state.isLoading -> {
                    EmptyAlert(
                        modifier = Modifier.align(Alignment.Center),
                        painter = painterResource(R.drawable.ic_write),
                        alertTitle = "내 스터디가 없어요.",
                        alertDes = "관심 있는 스터디에 가입해보세요.",
                        content = {}
                    )
                }

                else -> {
                    MyStudyListContent(
                        studies = state.studyList,
                        listState = listState,
                        onItemClick = onItemClick
                    )

                    if (state.isLoading && state.studyList.isNotEmpty()) {
                        SpotSpinner(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}