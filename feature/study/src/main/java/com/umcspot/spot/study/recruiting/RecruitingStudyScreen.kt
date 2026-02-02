package com.umcspot.spot.study.recruiting

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle.Event
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.SpotSpinner
import com.umcspot.spot.designsystem.component.empty.EmptyAlert
import com.umcspot.spot.designsystem.component.study.StudyListItem
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.B100
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.G200
import com.umcspot.spot.designsystem.theme.G300
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.model.RecruitingStudySort
import com.umcspot.spot.study.model.StudyResult
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp
import com.umcspot.spot.ui.state.UiState
import kotlinx.coroutines.launch

@Composable
fun RecruitingStudyScreen(
    contentPadding: PaddingValues,
    viewmodel: RecruitingStudyViewModel = hiltViewModel(),
    onRegisterScrollToTop: ((() -> Unit)?) -> Unit,
    onFilterClick: () -> Unit,
    onItemClick: (StudyResult) -> Unit
) {
    val state by viewmodel.uiState.collectAsStateWithLifecycle()
    val sort by viewmodel.sortType.collectAsStateWithLifecycle()

    var showSortSheet by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val topPad = contentPadding.calculateTopPadding()
    val bottomPad = contentPadding.calculateBottomPadding()

    val lifecycleOwner = LocalLifecycleOwner.current

    val ui = state.studies
    val itemList: List<StudyResult> = when (ui) {
        is UiState.Success -> ui.data.studyList
        else -> emptyList()
    }
    val isFiltered by viewmodel.isFiltered.collectAsStateWithLifecycle()

    val shouldLoadMore = remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

            totalItems > 0 && lastVisibleItemIndex >= totalItems - 3
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Event.ON_RESUME) {
                scope.launch { listState.scrollToItem(0) }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
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
        // 타이틀
        Row(
            modifier = Modifier
                .padding(vertical = screenHeightDp(9.dp)),
        ) {
            Text(
                text = "모집중 스터디",
                style = SpotTheme.typography.h4
            )
        }

        Spacer(Modifier.height(screenHeightDp(12.dp)))

        HeaderRow(
            size = itemList.size,
            sortType = sort,
            onOpenSortSheet = { showSortSheet = true },
            onFilterClick = onFilterClick,
            isFiltered = isFiltered
        )
        Spacer(Modifier.height(screenHeightDp(10.dp)))

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            RecruitingStudyScreenContent(
                modifier = Modifier
                    .fillMaxSize(),
                studies = itemList,
                listState = listState,
                onItemClick = onItemClick,
            )

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
                    EmptyAlert(
                        painter = painterResource(R.drawable.emoji_sad),
                        alertTitle = "조건에 맞는 스터디가 없어요.",
                        alertDes = "필터를 재설정하고 스터디를 찾아보세요."
                    )
                }

                is UiState.Empty -> {
                    EmptyAlert(
                        painter = painterResource(R.drawable.emoji_sad),
                        alertTitle = "조건에 맞는 스터디가 없어요.",
                        alertDes = "필터를 재설정하고 스터디를 찾아보세요."
                    )
                }

                is UiState.Success -> Unit
            }
        }

    }

    if (showSortSheet) {
        SortTypeBottomSheet(
            current = sort,
            onSelect = { viewmodel.selectSort(it) },
            onDismiss = { showSortSheet = false }
        )
    }
}

@Composable
private fun RecruitingStudyScreenContent(
    modifier: Modifier = Modifier,
    studies: List<StudyResult>,
    listState: LazyListState,
    onItemClick: (StudyResult) -> Unit,
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
                onClick = { onItemClick(item) }
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

@Composable
fun HeaderRow(
    size: Int,
    sortType: RecruitingStudySort,
    onOpenSortSheet: () -> Unit,
    onFilterClick: () -> Unit,
    isFiltered: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "%02d건".format(size),
            style = SpotTheme.typography.regular_500,
            color = SpotTheme.colors.gray400
        )

        Row{
            OutlinedButton(
                onClick = onOpenSortSheet,
                shape = SpotShapes.Hard,
                border = BorderStroke(1.dp, SpotTheme.colors.G200),
                contentPadding = PaddingValues(start = screenWidthDp(7.dp), end = screenWidthDp(4.dp), top = screenHeightDp(2.dp), bottom = screenHeightDp(4.dp)),
                modifier = Modifier
                    .wrapContentWidth()
                    .height(screenHeightDp(26.dp))
            ) {
                Text(
                    text = sortType.label,
                    color = SpotTheme.colors.black,
                    style = SpotTheme.typography.regular_500
                )
                Spacer(Modifier.width(screenWidthDp(7.dp)))
                Icon(
                    modifier = Modifier.size(screenWidthDp(14.dp)),
                    painter = painterResource(R.drawable.arrow_down),
                    tint = SpotTheme.colors.B500,
                    contentDescription = null
                )
            }

            Spacer(Modifier.width(screenWidthDp(10.dp)))

            Box(
                modifier = Modifier
                    .size(screenWidthDp(26.dp))
                    .clip(SpotShapes.Hard)
                    .background(
                        color = if (isFiltered) SpotTheme.colors.B100 else SpotTheme.colors.white
                    )
                    .clickable(
                        onClick = onFilterClick
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.filter),
                    contentDescription = "필터",
                    modifier = Modifier.size(screenWidthDp(14.dp)),
                    tint = if (isFiltered) SpotTheme.colors.B500 else SpotTheme.colors.black
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortTypeBottomSheet(
    current: RecruitingStudySort?,
    onSelect: (RecruitingStudySort) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        modifier = Modifier
            .fillMaxWidth(),
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = SpotTheme.colors.white,
        dragHandle = { },
        contentWindowInsets = { WindowInsets(0) },
        tonalElevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(vertical = screenHeightDp(14.dp))
        ) {
            RecruitingStudySort.entries.forEachIndexed { index, option ->
                ListItem(
                    colors = ListItemDefaults.colors(
                        containerColor = SpotTheme.colors.white,
                        headlineColor = SpotTheme.colors.black,   // (선택) 텍스트 색 명시
                        trailingIconColor = SpotTheme.colors.B500 // (선택)
                    ),
                    headlineContent = {
                        Text(
                            modifier = Modifier,
                            text = option.label,
                            color = SpotTheme.colors.black,
                            style = SpotTheme.typography.medium_400
                        )
                    },
                    trailingContent = {
                        if (option == current) {
                            Icon(
                                painter = painterResource(R.drawable.success_default),
                                tint = SpotTheme.colors.B500,
                                modifier = Modifier
                                    .size(screenWidthDp(14.dp)),
                                contentDescription = "선택됨",
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onSelect(option)
                            onDismiss()
                        }
                        .padding(horizontal = screenWidthDp(17.dp))
                )
                if (index != RecruitingStudySort.entries.lastIndex) {
                    HorizontalDivider(
                        color = SpotTheme.colors.G300,
                        thickness = 1.dp
                    )
                }
            }
        }
    }
}