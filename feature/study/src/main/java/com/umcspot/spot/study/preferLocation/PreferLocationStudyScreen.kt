package com.umcspot.spot.study.preferLocation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
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
import com.umcspot.spot.designsystem.component.bottomsheet.LocationBottomSheet
import com.umcspot.spot.designsystem.component.empty.EmptyAlertWithButton
import com.umcspot.spot.designsystem.component.study.StudyListItem
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.G200
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.model.RecruitingStudySort
import com.umcspot.spot.study.model.StudyResult
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp
import com.umcspot.spot.ui.state.UiState
import kotlinx.coroutines.launch

@Composable
fun PreferLocationStudyScreen(
    contentPadding: PaddingValues,
    viewmodel: PreferLocationStudyViewModel = hiltViewModel(),
    onRegisterScrollToTop: ((() -> Unit)?) -> Unit,
    onFilterClick: () -> Unit,
    onItemClick: (StudyResult) -> Unit
) {
    val ui by viewmodel.uiState.collectAsStateWithLifecycle()
    val sort by viewmodel.sortType.collectAsStateWithLifecycle()
    val query by viewmodel.query.collectAsStateWithLifecycle()
    val results by viewmodel.results.collectAsStateWithLifecycle()
    val selected by viewmodel.selected.collectAsStateWithLifecycle()
    val isLoadingMore by viewmodel.isLoadingMore.collectAsStateWithLifecycle()

    var showSheet by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(0) } // 0 = 전체
    val tabs: List<String> = remember(selected) { listOf("전체") + selected.map { it.fullName } }

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val topPad = contentPadding.calculateTopPadding()
    val bottomPad = contentPadding.calculateBottomPadding()

    // 최초 데이터 로드
    LaunchedEffect(Unit) {
        viewmodel.load()
        viewmodel.loadLocationData()
    }

    // 상단으로 스크롤 요청 핸들링
    LaunchedEffect(Unit) {
        onRegisterScrollToTop {
            scope.launch { listState.animateScrollToItem(0) }
        }
    }

    // 선택 칩 변화 시 탭 인덱스 보정
    LaunchedEffect(selected.size) {
        val maxIdx = (1 + selected.size) - 1 // "전체" 1개 + selected 크기 - 1
        if (selectedTab > maxIdx) selectedTab = maxIdx
    }

    LaunchedEffect(selected.size) {
        val maxIdx = (1 + selected.size) - 1
        if (selectedTab > maxIdx) {
            selectedTab = maxIdx
            viewmodel.selectTab(selectedTab) // ✅ 탭 인덱스 바뀌었으니 다시 호출
        }
    }

    val studiesForUi = when (val s = ui.data) {
        is UiState.Success -> s.data.studyList
        else -> emptyList()
    }

    LaunchedEffect(listState, studiesForUi.size, isLoadingMore) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisible ->
                val total = listState.layoutInfo.totalItemsCount
                if (lastVisible != null && total > 0) {
                    // 마지막에서 3개 전쯤 도달하면 로드
                    if (lastVisible >= total - 3) {
                        viewmodel.loadNextPage()
                    }
                }
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SpotTheme.colors.white)
            .padding(top = topPad, bottom = bottomPad, start = screenWidthDp(17.dp), end = screenWidthDp(17.dp))
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = screenHeightDp(9.dp)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 타이틀
            Text(
                text = "내 지역 스터디",
                style = SpotTheme.typography.h4
            )
        }

        // 선택 지역 탭
        SelectedLocationTabs(
            tabs = tabs,
            selectedIndex = selectedTab,
            onTabSelected = {
                selectedTab = it
                viewmodel.selectTab(it)
                scope.launch { listState.animateScrollToItem(0) }
            }
        )

        if (ui.data is UiState.Loading) {
            Text("로딩 중...", color = Color.Gray, modifier = Modifier.padding(top = screenHeightDp(8.dp)))
        } else if (ui.data is UiState.Failure) {
            Text("에러: ${(ui.data as UiState.Failure).msg}", color = Color.Red, modifier = Modifier.padding(top = screenHeightDp(8.dp)))
        } else if (studiesForUi.isEmpty()) {
            Box(Modifier.fillMaxSize()) {
                EmptyAlertWithButton(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(R.drawable.location_outline),
                    alertTitle = "내 지역이 아직 없어요!",
                    alertDes = "내 지역을 설정하고 스터디를 모아봐요.",
                    buttonText = "내 지역 설정하기",
                    onClick = { showSheet = true }
                )
            }
        } else {

            Spacer(modifier = Modifier.height(screenHeightDp(12.dp)))

            HeaderRow(
                size = studiesForUi.size,
                sortType = sort,
                onOpenSortSheet = { showSheet = true },
                onFilterClick = onFilterClick
            )

            StudyList(
                listState = listState,
                items = studiesForUi,
                onItemClick = onItemClick
            )
        }
    }

    // 지역 선택 바텀시트
    LocationBottomSheet(
        visible = showSheet,
        query = query,
        onQueryChange = { viewmodel.searchLocation(it) },
        onDismiss = {
            viewmodel.syncPreferredRegions()
            showSheet = false
        },
        results = results,
        selected = selected,
        onAddSelected = { viewmodel.addLocation(it) },
        onRemoveSelected = { viewmodel.removeLocation(it) }
    )
}

@Composable
private fun StudyList(
    listState: LazyListState,
    items: List<StudyResult>,
    onItemClick: (StudyResult) -> Unit
) {
    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize()
    ) {
        items(
            items = items,
            key = { it.id }
        ) { item ->
            StudyListItem(
                item = item,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = screenWidthDp(5.dp)),
                onClick = { onItemClick(item) }
            )
        }
    }
}

@Composable
fun HeaderRow(
    size: Int,
    sortType: RecruitingStudySort,
    onOpenSortSheet: () -> Unit,
    onFilterClick: () -> Unit
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

            IconButton(
                onClick = onFilterClick,
                modifier = Modifier.size(screenWidthDp(26.dp))
            ) {
                Icon(
                    painter = painterResource(R.drawable.filter),
                    contentDescription = "필터",
                    modifier = Modifier.size(screenWidthDp(14.dp))
                )
            }
        }
    }
}

@Composable
private fun SelectedLocationTabs(
    tabs: List<String>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit
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
            tabs.forEachIndexed { index, name ->
                Tab(
                    modifier = Modifier
                        .wrapContentWidth(),
                    selected = selectedIndex == index,
                    onClick = { onTabSelected(index) },
                    selectedContentColor = SpotTheme.colors.black,
                    unselectedContentColor = SpotTheme.colors.black
                ) {
                    Text(
                        text = name,
                        style = SpotTheme.typography.h5,
                        modifier = Modifier
                            .padding(horizontal = screenWidthDp(7.dp), vertical = screenHeightDp(4.dp))
                    )
                }
            }
        }
    }
}