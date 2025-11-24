package com.umcspot.spot.feature.board.boardList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.umcspot.spot.designsystem.component.post.PostListItem
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.domain.board.model.post.PostResultList
import com.umcspot.spot.feature.board.BoardViewModel
import com.umcspot.spot.model.BoardType
import com.umcspot.spot.model.SortType
import com.umcspot.spot.model.korean
import com.umcspot.spot.ui.state.UiState
import kotlinx.coroutines.launch

@Composable
fun BoardListScreen(
    viewmodel : BoardViewModel = hiltViewModel(),
    contentPadding: PaddingValues,
    onRegisterScrollToTop: ((() -> Unit)?) -> Unit,
) {
    val state by viewmodel.uiState.collectAsStateWithLifecycle()
    val selected by viewmodel.selected.collectAsStateWithLifecycle()

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val topPad = contentPadding.calculateTopPadding()
    val bottomPad = contentPadding.calculateBottomPadding()

    // ✅ 탭을 BoardType 전부 + "전체" 로 구성
    val tabs = remember {
        listOf("전체") + BoardType.values().map { it.korean }
    }
    var selectedTab by remember { mutableStateOf(0) } // 0 = 전체


    // 상단으로 스크롤 요청 핸들링
    LaunchedEffect(Unit) {
        onRegisterScrollToTop {
            scope.launch { listState.animateScrollToItem(0) }
        }
    }

    LaunchedEffect(state.user) {
        if (state.user is UiState.Empty) {
            viewmodel.load(SortType.LIVE)
        }
    }

    when (val state = state.user) {
        is UiState.Loading -> {
            Text(text = "로딩 중...", color = Color.Gray)
        }

        is UiState.Failure -> {
            Text(text = "에러: ${state.msg}", color = Color.Red)
        }

        is UiState.Empty -> {
            Text(text = "데이터가 없습니다.")
        }

        is UiState.Success -> {
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .background(SpotTheme.colors.white)
                    .padding(top = topPad, bottom = bottomPad),

            ) {
                SelectedLocationTabs(
                    tabs = tabs,
                    selectedIndex = selectedTab,
                    onTabSelected = { selectedTab = it }
                )

                // ✅ 선택된 탭에 따라 필터링
                val selectedType: BoardType? = if (selectedTab == 0) null
                else BoardType.values()[selectedTab - 1]

                val filtered = remember(selectedTab, state.data.posts.postList) {
                    if (selectedType == null) state.data.posts.postList
                    else state.data.posts.postList.filter { it.label == selectedType }
                }

                BoardListScreenContent(
                    listState = listState,
                    itemList = PostResultList(postList = filtered) // 그대로 래핑해서 전달
                )
            }
        }
    }
}

@Composable
fun BoardListScreenContent(
    listState : LazyListState,
    itemList : PostResultList,
) {
    LazyColumn(
        state = listState,
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .fillMaxSize(),
    ) {
        items(
            items = itemList.postList,
            key = { it.id }
        ) { item ->
            PostListItem(
                item = item,
                onClick = { }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
                        style = SpotTheme.typography.medium_500,
                        modifier = Modifier.padding(horizontal = horizPad, vertical = 10.dp)
                    )
                }
            }
        }
    }
}