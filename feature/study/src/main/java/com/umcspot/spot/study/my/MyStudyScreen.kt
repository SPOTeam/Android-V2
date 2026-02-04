package com.umcspot.spot.study.my

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.SpotSpinner
import com.umcspot.spot.designsystem.component.empty.EmptyAlert
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.study.my.component.MyStudyListContent
import com.umcspot.spot.study.my.model.MyStudyState

@Composable
fun MyStudyRoute(
    contentPadding: PaddingValues,
    navigateToStudyDetail: (Long) -> Unit,
    viewModel: MyStudyViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SpotTheme.colors.white)
    ) {
        Spacer(modifier = Modifier.height(contentPadding.calculateTopPadding()))

        MyStudyScreen(
            state = state,
            listState = listState,
            onItemClick = navigateToStudyDetail,
            modifier = Modifier.padding(bottom = contentPadding.calculateBottomPadding())
        )
    }
}

@Composable
private fun MyStudyScreen(
    state: MyStudyState,
    listState: LazyListState,
    onItemClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize().background(SpotTheme.colors.white)
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
                    SpotSpinner(modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp))
                }
            }
        }
    }
}