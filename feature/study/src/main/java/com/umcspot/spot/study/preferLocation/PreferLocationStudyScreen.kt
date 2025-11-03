package com.umcspot.spot.study.preferLocation

import PreferLocationBottomSheet
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.empty.EmptyAlertWithButton
import com.umcspot.spot.designsystem.component.study.StudyListItem
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.model.RecruitingStudySort
import com.umcspot.spot.study.model.StudyResult
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

    var showSheet by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(0) } // 0 = 전체
    val tabs: List<String> = remember(selected) { listOf("전체") + selected }

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val topPad = contentPadding.calculateTopPadding()
    val bottomPad = contentPadding.calculateBottomPadding()

    // 최초 데이터 로드
    LaunchedEffect(ui.studies) {
        if (ui.studies is UiState.Empty) viewmodel.load(RecruitingStudySort.LATEST)
    }
    LaunchedEffect(Unit) { viewmodel.loadLocationData() }

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

    // 리스트 데이터 준비 (+ 선택된 탭 기준 필터 적용 지점)
    val studiesAll = when (val s = ui.studies) {
        is UiState.Success -> s.data.studyList
        else -> emptyList()
    }
    val currentLocation = selected.getOrNull(selectedTab)
    // 🔧 여기서 실제 StudyResult의 지역 필드명으로 필터하세요 (예: item.location / item.address 등)
    val studiesForUi = if (currentLocation.isNullOrBlank()) {
        studiesAll
    } else {
        studiesAll.filter { item ->
            // 예시) item.location?.contains(currentLocation) == true
            // 필드명이 다르면 위 라인만 고치면 됨
            false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SpotTheme.colors.white)
            .padding(top = topPad, bottom = bottomPad, start = 16.dp, end = 16.dp)
    ) {
        // 타이틀
        Text(
            text = "내 지역 스터디",
            style = SpotTheme.typography.bodySmall400.copy(fontSize = 20.sp)
        )

        Spacer(Modifier.height(8.dp))

        // 선택 지역 탭
        SelectedLocationTabs(
            tabs = tabs,
            selectedIndex = selectedTab,
            onTabSelected = { selectedTab = it }
        )

        HeaderRow(
            size = studiesForUi.size,
            sortType = sort,
            onOpenSortSheet = { showSheet = true },
            onFilterClick = onFilterClick
        )

        if (ui.studies is UiState.Loading) {
            Text("로딩 중...", color = Color.Gray, modifier = Modifier.padding(top = 8.dp))
        } else if (ui.studies is UiState.Failure) {
            Text("에러: ${(ui.studies as UiState.Failure).msg}", color = Color.Red, modifier = Modifier.padding(top = 8.dp))
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
            StudyList(
                listState = listState,
                items = studiesForUi,
                onItemClick = onItemClick
            )
        }
    }

    // 지역 선택 바텀시트
    PreferLocationBottomSheet(
        contentPadding = contentPadding,
        visible = showSheet,
        query = query,
        onQueryChange = { viewmodel.searchLocation(it) },
        onDismiss = { showSheet = false },
        results = results,
        selected = selected,
        onAddSelected = viewmodel::add,
        onRemoveSelected = viewmodel::remove
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
            key = { it.studyId }
        ) { item ->
            StudyListItem(
                item = item,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                onClick = { onItemClick(item) }
            )
        }
    }
}

@Composable
private fun HeaderRow(
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
            style = SpotTheme.typography.bodyMedium500.copy(fontSize = 12.sp),
            color = SpotTheme.colors.gray500
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedButton(
                onClick = onOpenSortSheet,
                shape = SpotShapes.Soft,
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                modifier = Modifier.height(32.dp)
            ) {
                Text(
                    text = sortType.label,
                    color = SpotTheme.colors.black,
                    style = SpotTheme.typography.bodyMedium500.copy(fontSize = 12.sp)
                )
                Spacer(Modifier.width(5.dp))
                Icon(
                    painter = painterResource(R.drawable.arrow_down),
                    tint = SpotTheme.colors.B500,
                    contentDescription = null
                )
            }

            IconButton(onClick = onFilterClick) {
                Icon(
                    painter = painterResource(R.drawable.filter),
                    contentDescription = "필터",
                    modifier = Modifier.size(22.dp)
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

    val horizPad = 12.dp
    val scrimWidth = 24.dp
    val bg = SpotTheme.colors.white // 배경색(화면 배경과 맞추기)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            // 스크롤 가능 영역의 좌우에 페이드 오버레이를 그리되, 입력은 통과시킴
            .drawWithContent {
                drawContent()

                val w = scrimWidth.toPx()
                drawRect(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color.Transparent, bg),
                        // 내부가 투명, 바깥이 흰색이 되도록 방향 지정
                        startX = w,    // 투명 쪽(내부)
                        endX = 0f      // 흰색 쪽(바깥)
                    ),
                    size = Size(w, size.height),
                    topLeft = Offset(0f, 0f)
                )
                // 오른쪽: 내부(투명) → 바깥(흰색)
                drawRect(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color.Transparent, bg),
                        startX = size.width - w, // 투명(내부)
                        endX = size.width        // 흰색(바깥)
                    ),
                    size = Size(w, size.height),
                    topLeft = Offset(size.width - w, 0f)
                )
            }
    ) {
        ScrollableTabRow(
            selectedTabIndex = selectedIndex,
            edgePadding = 0.dp,
            containerColor = Color.Transparent,
            divider = {},
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[selectedIndex])
                        .padding(horizontal = horizPad)
                        .height(2.dp),
                    color = SpotTheme.colors.B500
                )
            }
        ) {
            tabs.forEachIndexed { index, name ->
                Tab(
                    selected = selectedIndex == index,
                    onClick = { onTabSelected(index) },
                    selectedContentColor = SpotTheme.colors.black,
                    unselectedContentColor = SpotTheme.colors.black
                ) {
                    Text(
                        text = name,
                        style = SpotTheme.typography.bodyMedium600.copy(fontSize = 14.sp),
                        modifier = Modifier.padding(horizontal = horizPad, vertical = 10.dp)
                    )
                }
            }
        }
    }
}


