package com.umcspot.spot.feature.board.boardList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.umcspot.spot.designsystem.component.SpotSpinner
import com.umcspot.spot.designsystem.component.post.PostListItem
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.domain.board.model.postList.PostResult
import com.umcspot.spot.model.PostType
import com.umcspot.spot.model.korean
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp
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
    val isLoadingMore by viewmodel.isLoadingMore.collectAsStateWithLifecycle()

    val topPad = contentPadding.calculateTopPadding()
    val bottomPad = contentPadding.calculateBottomPadding()

    val tabItems = remember {
        listOf<PostType?>(null) + PostType.values().toList()
    }

    val ui = state.data
    val itemList: List<PostResult> = when (ui) {
        is UiState.Success -> ui.data.posts
        else -> emptyList()
    }

    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    val lifecycleOwner = LocalLifecycleOwner.current

    val shouldLoadMore = remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

            totalItems > 0 && lastVisibleItemIndex >= totalItems - 3
        }
    }

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

    LaunchedEffect(ui is UiState.Success) {
        if (ui is UiState.Success) {

            val pos = viewmodel.scrollPosition
            if (pos.index != 0 || pos.offset != 0) {
                listState.scrollToItem(pos.index, pos.offset)
            }

            snapshotFlow {
                listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset
            }
                .distinctUntilChanged()
                .collectLatest { (index, offset) ->
                    viewmodel.saveScrollPosition(index, offset)
                }
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
        Spacer(modifier = Modifier.height(screenHeightDp(18.dp)))

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

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            BoardListScreenContent(
                listState = listState,
                itemList = itemList,
                isLoadingMore = isLoadingMore,
                onLikeClick = {
                    viewmodel.toggleLike(it)
                },
                onPostClicked = {
                    viewmodel.setPostInfo(it)
                    onPostClicked(it.postId)
                }
            )

            when (ui) {
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
                    Text(
                        text = "에러: ${ui.msg}",
                        color = Color.Red,
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }
                is UiState.Empty -> {
                    Text(
                        text = "데이터가 없습니다.",
                        color = Color.Gray,
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }
                is UiState.Success -> Unit
            }
        }
    }
}

@Composable
fun BoardListScreenContent(
    listState : LazyListState,
    itemList : List<PostResult>,
    isLoadingMore: Boolean,
    onLikeClick : (PostResult) -> Unit,
    onPostClicked : (PostResult) -> Unit
) {
    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = screenWidthDp(17.dp))
            .padding(top = screenHeightDp(12.dp)),
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

            Spacer(modifier = Modifier.height(screenHeightDp(5.dp)))
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(),
                thickness = 1.dp,
                color = SpotTheme.colors.gray200
            )
            Spacer(modifier = Modifier.height(screenHeightDp(5.dp)))
        }

        if (isLoadingMore) {
            item(key = "loading_more") {
                Spacer(modifier = Modifier.height(screenHeightDp(12.dp)))
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    SpotSpinner()
                }
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
    val density = LocalDensity.current
    val textWidths = remember(tabs) { mutableStateMapOf<Int, androidx.compose.ui.unit.Dp>() }
    val minTabWidth = screenWidthDp(50.dp)

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
            selectedTabIndex = selectedIndex,
            edgePadding = screenWidthDp(17.dp),
            containerColor = Color.Transparent,
            divider = {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = SpotTheme.colors.gray200
                )
            },
            indicator = { tabPositions ->
                val currentTab = tabPositions.getOrNull(selectedIndex) ?: return@ScrollableTabRow
                val textWidth = textWidths[selectedIndex] ?: 0.dp
                val indicatorWidth = if (textWidth > 0.dp) {
                    maxOf(minTabWidth, textWidth)
                } else {
                    minTabWidth
                }
                val indicatorOffsetX = currentTab.left + (currentTab.width - indicatorWidth) / 2

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.BottomStart)
                        .offset(x = indicatorOffsetX)
                        .width(indicatorWidth)
                        .height(1.dp)
                        .background(SpotTheme.colors.B500)
                )
            }
        ) {
            tabs.forEachIndexed { index, name ->
                val textWidth = textWidths[index] ?: 0.dp
                val tabWidth = if (textWidth > 0.dp) maxOf(minTabWidth, textWidth) else minTabWidth

                Tab(
                    modifier = Modifier
                        .width(tabWidth)
                        .padding(horizontal = screenWidthDp(7.dp)), // 1) 탭 간 간격
                    selected = selectedIndex == index,
                    onClick = { onTabSelected(index) },
                    selectedContentColor = SpotTheme.colors.black,
                    unselectedContentColor = SpotTheme.colors.black
                ) {
                    Text(
                        text = name,
                        style = SpotTheme.typography.h5,
                        onTextLayout = { textLayoutResult ->
                            textWidths[index] = with(density) { textLayoutResult.size.width.toDp() }
                        },
                        modifier = Modifier
                            .padding(
                                vertical = screenHeightDp(4.dp)       // 4) 탭 높이(세로 여백)
                            ),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
