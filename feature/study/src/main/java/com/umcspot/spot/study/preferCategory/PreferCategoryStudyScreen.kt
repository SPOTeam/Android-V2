package com.umcspot.spot.study.preferCategory

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
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
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
import com.umcspot.spot.model.StudyTheme
import com.umcspot.spot.study.model.StudyResult
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp
import com.umcspot.spot.ui.state.UiState
import kotlinx.coroutines.launch
import kotlin.collections.lastIndex
import kotlin.collections.orEmpty
import kotlin.text.format

@Composable
fun PreferCategoryStudyScreen(
    contentPadding: PaddingValues,
    viewmodel: PreferCategoryStudyViewModel = hiltViewModel(),
    onRegisterScrollToTop: ((() -> Unit)?) -> Unit,
    onFilterClick: () -> Unit,
    onItemClick: (Long) -> Unit
) {
    val ui by viewmodel.uiState.collectAsStateWithLifecycle()
    val sort by viewmodel.sortType.collectAsStateWithLifecycle()
    val isLoadingMore by viewmodel.isLoadingMore.collectAsStateWithLifecycle()
    val selectedTab by viewmodel.selectedTab.collectAsStateWithLifecycle()
    val preferCategories by viewmodel.preferCategory.collectAsStateWithLifecycle()


    var showSortSheet by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val topPad = contentPadding.calculateTopPadding()
    val bottomPad = contentPadding.calculateBottomPadding()

    val isFiltered by viewmodel.isFiltered.collectAsStateWithLifecycle()

    // 상단 스크롤 콜백 등록
    LaunchedEffect(Unit) {
        viewmodel.getPreferCategories()
        viewmodel.load()
        onRegisterScrollToTop {
            scope.launch { listState.animateScrollToItem(0) }
        }
    }

    val isSuccess = ui.data is UiState.Success
    val studiesForUi = (ui.data as? UiState.Success)?.data?.studyList.orEmpty()
    LaunchedEffect(isSuccess, studiesForUi.size, isLoadingMore) {
        if (!isSuccess) return@LaunchedEffect
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisible ->
                val total = listState.layoutInfo.totalItemsCount
                if (lastVisible != null && total > 0 && lastVisible >= total - 3) {
                    viewmodel.loadNextPage()
                }
            }
    }

    val allTabs: List<StudyTheme?> =  listOf(null) + preferCategories
    val selectedIndex = allTabs.indexOfFirst { it == selectedTab }.coerceAtLeast(0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SpotTheme.colors.white)
            .padding(top = topPad, bottom = bottomPad)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = screenHeightDp(9.dp), horizontal = screenWidthDp(17.dp)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 타이틀
            Text(
                text = "내 관심사 스터디",
                style = SpotTheme.typography.h4
            )
        }

        CategoryTabs(
            tabs = allTabs,
            selectedIndex = selectedIndex,
            onTabSelected = { category ->
                viewmodel.setSelectedTab(category)
                scope.launch { listState.animateScrollToItem(0) }
            }
        )

        Spacer(Modifier.height(screenHeightDp(12.dp)))

        HeaderRow(
            size = studiesForUi.size,
            sortType = sort,
            onOpenSortSheet = { showSortSheet = true },
            onFilterClick = onFilterClick,
            isFiltered = isFiltered
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            when (val state = ui.data) {
                is UiState.Success -> {
                    StudyList(
                        listState = listState,
                        items = state.data.studyList,
                        onItemClick = onItemClick
                    )
                }

                is UiState.Loading -> {
                    Surface(
                        color = SpotTheme.colors.white,
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            SpotSpinner()
                        }
                    }
                }

                is UiState.Empty, is UiState.Failure -> {
                    EmptyAlert(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(R.drawable.ic_sad),
                        alertTitle = "조건에 맞는 스터디가 없어요.",
                        alertDes = "필터를 재설정하고 스터디를 찾아보세요.",
                    )
                }
            }
        }
    }

    SortTypeBottomSheet(
        visible = showSortSheet,
        current = sort,
        onSelect = { viewmodel.setSort(it) },
        onDismiss = { showSortSheet = false }
    )
}

@Composable
private fun StudyList(
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
        items(
            items = items,
            key = { it.id }
        ) { item ->
            Spacer(Modifier.padding(screenHeightDp(5.dp)))

            StudyListItem(
                item = item,
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = { onItemClick(item.id) }
            )

            if(items.indexOf(item) != items.lastIndex) {
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
private fun CategoryTabs(
    tabs: List<StudyTheme?>,
    selectedIndex: Int,
    onTabSelected: (StudyTheme?) -> Unit
) {
    if (tabs.isEmpty()) return

    val scrimWidth = screenWidthDp(24.dp)
    val bg = SpotTheme.colors.white

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .drawWithContent {
                drawContent()

                val w = scrimWidth.toPx()
                // 왼쪽: 내부(흰색) → 바깥(투명)
                drawRect(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color.Transparent, bg),
                        startX = w,
                        endX = 0f
                    ),
                    size = Size(w, size.height),
                    topLeft = Offset(0f, 0f)
                )
                // 오른쪽: 내부(투명) → 바깥(흰색)
                drawRect(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color.Transparent, bg),
                        startX = size.width - w,
                        endX = size.width
                    ),
                    size = Size(w, size.height),
                    topLeft = Offset(size.width - w, 0f)
                )
            }
    ) {
        ScrollableTabRow(
            modifier = Modifier.fillMaxWidth(),
            selectedTabIndex = selectedIndex,
            edgePadding = 0.dp,
            containerColor = Color.Transparent,
            divider = {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = SpotTheme.colors.gray200
                )
            },
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[selectedIndex])
                        .padding(horizontal = screenWidthDp(17.dp))
                        .height(1.dp),
                    color = SpotTheme.colors.B500
                )
            }
        ) {
            tabs.forEachIndexed { index, theme ->

                Tab(
                    modifier = Modifier
                        .wrapContentWidth(),
                    selected = selectedIndex == index,
                    onClick = { onTabSelected(theme) },
                    selectedContentColor = SpotTheme.colors.black,
                    unselectedContentColor = SpotTheme.colors.black
                ) {
                    Text(
                        text = theme?.title ?: "전체",
                        style = SpotTheme.typography.h5,
                        modifier = Modifier
                            .padding(horizontal = screenWidthDp(7.dp), vertical = screenHeightDp(4.dp))
                    )
                }
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = screenWidthDp(17.dp)),
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
    visible: Boolean,
    current: RecruitingStudySort?,
    onSelect: (RecruitingStudySort) -> Unit,
    onDismiss: () -> Unit
) {
    if(!visible) return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        modifier = Modifier
            .fillMaxWidth(),
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = SpotShapes.RoundTop,
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
                        headlineColor = SpotTheme.colors.black,
                        trailingIconColor = SpotTheme.colors.B500
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