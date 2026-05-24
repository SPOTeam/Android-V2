package com.umcspot.spot.category

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
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.umcspot.spot.category.component.CategoryHeaderRow
import com.umcspot.spot.category.component.CategoryTabs
import com.umcspot.spot.category.component.SortTypeBottomSheet
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.SpotSpinner
import com.umcspot.spot.designsystem.component.empty.EmptyAlert
import com.umcspot.spot.designsystem.component.empty.EmptyAlertNeutral
import com.umcspot.spot.designsystem.component.study.StudyListItem
import com.umcspot.spot.designsystem.theme.G300
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.model.RecruitingStudySort
import com.umcspot.spot.model.StudyTheme
import com.umcspot.spot.study.model.StudyResult
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp
import com.umcspot.spot.ui.state.UiState
import kotlinx.coroutines.launch

@Composable
fun CategoryRoute(
    contentPadding: PaddingValues,
    onRegisterScrollToTop: ((() -> Unit)?) -> Unit,
    onFilterClick: () -> Unit,
    onItemClick: (Long) -> Unit,
    onRegisterStudyClick: () -> Unit,
    viewModel: CategoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val studyList = (uiState.studies as? UiState.Success)?.data?.studyList.orEmpty()

    LaunchedEffect(Unit) {
        viewModel.load()
        onRegisterScrollToTop {
            scope.launch { listState.animateScrollToItem(0) }
        }
    }

    LaunchedEffect(uiState.studies, studyList.size, uiState.isLoadingMore) {
        if (uiState.studies !is UiState.Success) return@LaunchedEffect
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisible ->
                val total = listState.layoutInfo.totalItemsCount
                if (lastVisible != null && total > 0 && lastVisible >= total - 3) {
                    viewModel.loadNextPage()
                }
            }
    }

    CategoryScreen(
        contentPadding = contentPadding,
        uiState = uiState,
        listState = listState,
        studyList = studyList,
        onTabSelected = { tab ->
            viewModel.setSelectedTab(tab)
            scope.launch { listState.animateScrollToItem(0) }
        },
        onSortSelected = viewModel::setSort,
        onFilterClick = onFilterClick,
        onItemClick = onItemClick,
        onRegisterStudyClick = onRegisterStudyClick
    )
}

@Composable
private fun CategoryScreen(
    contentPadding: PaddingValues,
    uiState: CategoryState,
    listState: LazyListState,
    studyList: List<StudyResult>,
    onTabSelected: (StudyTheme?) -> Unit,
    onSortSelected: (RecruitingStudySort) -> Unit,
    onFilterClick: () -> Unit,
    onItemClick: (Long) -> Unit,
    onRegisterStudyClick: () -> Unit
) {
    var showSortSheet by remember { mutableStateOf(false) }

    val allTabs: List<StudyTheme?> = remember { listOf(null) + StudyTheme.entries }
    val selectedIndex = remember(uiState.selectedTab) {
        allTabs.indexOfFirst { it == uiState.selectedTab }.coerceAtLeast(0)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SpotTheme.colors.white)
            .padding(
                top = contentPadding.calculateTopPadding(),
                bottom = contentPadding.calculateBottomPadding()
            )
    ) {
        Spacer(Modifier.height(screenHeightDp(18.dp)))

        CategoryTabs(
            tabs = allTabs,
            selectedIndex = selectedIndex,
            onTabSelected = onTabSelected
        )

       Spacer(Modifier.height(screenHeightDp(12.dp)))

        CategoryHeaderRow(
            size = studyList.size,
            sortType = uiState.sortType,
            isFiltered = uiState.isFiltered,
            onOpenSortSheet = { showSortSheet = true },
            onFilterClick = onFilterClick
        )

        Box(modifier = Modifier.fillMaxSize()) {
            when (val state = uiState.studies) {
                is UiState.Success -> {
                    CategoryStudyList(
                        listState = listState,
                        items = state.data.studyList,
                        onItemClick = onItemClick
                    )
                }
                is UiState.Loading -> {
                    Surface(
                        color = SpotTheme.colors.white,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            SpotSpinner()
                        }
                    }
                }
                is UiState.Empty -> {
                    EmptyAlertNeutral(
                        modifier = Modifier.fillMaxSize(),
                        alertTitle = "스터디가 없어요.",
                        alertDes = "원하는 스터디를 만들어보세요.",
                        buttonText = "스터디 만들기",
                        onClick = onRegisterStudyClick
                    )
                }
                is UiState.Failure -> {
                    EmptyAlert(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(R.drawable.ic_sad),
                        alertTitle = "조건에 맞는 스터디가 없어요.",
                        alertDes = "필터를 재설정하고 스터디를 찾아보세요."
                    )
                }
            }
        }
    }

    SortTypeBottomSheet(
        visible = showSortSheet,
        current = uiState.sortType,
        onSelect = onSortSelected,
        onDismiss = { showSortSheet = false }
    )
}

@Composable
private fun CategoryStudyList(
    listState: LazyListState,
    items: List<StudyResult>,
    onItemClick: (Long) -> Unit
) {
    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = screenWidthDp(17.dp))
    ) {
        itemsIndexed(
            items = items,
            key = { _, item -> item.id }
        ) { index, item ->
            Spacer(Modifier.height(screenHeightDp(12.dp)))
            StudyListItem(
                item = item,
                modifier = Modifier.fillMaxWidth(),
                onClick = { onItemClick(item.id) }
            )
            Spacer(Modifier.height(screenHeightDp(12.dp)))
            if (index != items.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = SpotTheme.colors.G300,
                    thickness = 1.dp
                )
            }
        }
    }
}