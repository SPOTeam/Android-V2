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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.umcspot.spot.designsystem.component.post.PostListItem
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.domain.board.model.postList.PostResult
import com.umcspot.spot.model.PostType
import com.umcspot.spot.model.korean
import com.umcspot.spot.ui.state.UiState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@Composable
fun BoardListScreen(
    viewmodel : BoardListViewModel = hiltViewModel(),
    contentPadding: PaddingValues,
    onRegisterScrollToTop: ((() -> Unit)?) -> Unit,
    onPostClicked: (Long) -> Unit
) {
    val state by viewmodel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val topPad = contentPadding.calculateTopPadding()
    val bottomPad = contentPadding.calculateBottomPadding()

    val tabItems = remember {
        listOf<PostType?>(null) + PostType.values().toList()
    }

    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, selectedTab) {
        val lifecycle = lifecycleOwner.lifecycle

        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                val selectedType = tabItems[selectedTab]
                viewmodel.selectType(selectedType)
            }
        }

        lifecycle.addObserver(observer)

        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(Unit) {
        onRegisterScrollToTop {
            scope.launch { listState.animateScrollToItem(0) }
        }
    }

    LaunchedEffect(state.data) {
        if (state.data is UiState.Empty) {
            viewmodel.load() // 전체
        }
    }

    val ui = state.data
    val itemList: List<PostResult> = when (ui) {
        is UiState.Success -> ui.data.posts
        else -> emptyList()
    }

    LaunchedEffect(ui is UiState.Success) {
        if (ui is UiState.Success) {
            // 1) 먼저 ViewModel에 저장된 위치로 복원
            val pos = viewmodel.scrollPosition
            if (pos.index != 0 || pos.offset != 0) {
                listState.scrollToItem(pos.index, pos.offset)
            }

            // 2) 그 다음부터 스크롤 변화를 ViewModel에 저장
            snapshotFlow {
                listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset
            }
                .distinctUntilChanged()
                .collectLatest { (index, offset) ->
                    viewmodel.saveScrollPosition(index, offset)
                }
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
        if (shouldLoadMore.value) {
            val successData = (ui as? UiState.Success)?.data
            if (successData?.hasNext == true) {
                viewmodel.loadNextPage()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SpotTheme.colors.white)
            .padding(top = topPad, bottom = bottomPad),
    ) {
        SelectedLocationTabs(
            tabs = tabItems.map { it?.korean ?: "전체" },
            selectedIndex = selectedTab,
            onTabSelected = { index ->
                selectedTab = index
                val selectedType = tabItems[index]
                viewmodel.selectType(selectedType)
                scope.launch { listState.scrollToItem(0) }
            }
        )

        // 리스트 화면은 항상 출력
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            BoardListScreenContent(
                listState = listState,
                itemList = itemList,
                onLikeClick = {
                    viewmodel.toggleLike(it)
                },
                onPostClicked = {
                    viewmodel.setPostInfo(it)
                    onPostClicked(it.postId)
                }
            )

            // 상태별 오버레이
            when (ui) {
                is UiState.Loading -> {
                    Text(
                        text = "로딩 중...",
                        color = Color.Gray,
                        modifier = Modifier
                            .align(androidx.compose.ui.Alignment.Center)
                    )
                }
                is UiState.Failure -> {
                    Text(
                        text = "에러: ${ui.msg}",
                        color = Color.Red,
                        modifier = Modifier
                            .align(androidx.compose.ui.Alignment.Center)
                    )
                }
                is UiState.Empty -> {
                    Text(
                        text = "데이터가 없습니다.",
                        color = Color.Gray,
                        modifier = Modifier
                            .align(androidx.compose.ui.Alignment.Center)
                    )
                }
                is UiState.Success -> {
                    // 아무 것도 안 그려도 됨 (리스트만 보여줌)
                }
            }
        }
    }
}


@Composable
fun BoardListScreenContent(
    listState : LazyListState,
    itemList : List<PostResult>,
    onLikeClick : (PostResult) -> Unit,
    onPostClicked : (PostResult) -> Unit
) {
    LazyColumn(
        state = listState,
        modifier = Modifier
            .padding(horizontal = 17.dp)
            .fillMaxSize(),
    ) {
        items(
            items = itemList,
            key = { it.postId }
        ) { item ->
            PostListItem(
                item = item,
                onLikeClick = { onLikeClick(item) },
                onClick = { onPostClicked(item) }
            )
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(),
                thickness = 1.dp,
                color = SpotTheme.colors.gray200
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