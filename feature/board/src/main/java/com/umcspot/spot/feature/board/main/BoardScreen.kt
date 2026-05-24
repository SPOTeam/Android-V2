package com.umcspot.spot.feature.board.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.SpotSpinner
import com.umcspot.spot.designsystem.shapes.ShapeBox
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.B100
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.G200
import com.umcspot.spot.designsystem.theme.G300
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.domain.board.model.board.BestPostResult
import com.umcspot.spot.domain.board.model.board.BestPostResultList
import com.umcspot.spot.domain.board.model.board.RecentPostResult
import com.umcspot.spot.domain.board.model.board.RecentPostResultList
import com.umcspot.spot.model.PostType
import com.umcspot.spot.model.SortType
import com.umcspot.spot.model.cap
import com.umcspot.spot.model.korean
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp
import com.umcspot.spot.ui.state.UiState

@Composable
fun BoardScreen(
    viewmodel: BoardViewModel = hiltViewModel(),
    contentPadding: PaddingValues,
    onMoveToBoardList: () -> Unit,
    onMoveToPostContent: (Long) -> Unit
) {
    val uiState by viewmodel.uiState.collectAsStateWithLifecycle()
    val sortType by viewmodel.sortType.collectAsStateWithLifecycle()

    val topPad = contentPadding.calculateTopPadding()
    val bottomPad = contentPadding.calculateBottomPadding()

    LaunchedEffect(Unit) {
        viewmodel.load()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SpotTheme.colors.white)
            .padding(top = topPad, bottom = bottomPad)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = screenWidthDp(17.dp))
                .padding(top = screenHeightDp(18.dp)),
            contentPadding = PaddingValues(bottom = screenHeightDp(24.dp))
        ) {
            item {
                SectionHeader(
                    title = "스터디 파트너들의 이야기",
                    onMoreClick = { onMoveToBoardList() }
                )
                Spacer(modifier = Modifier.height(screenHeightDp(12.dp)))
            }
            item {
                RecentCardList(
                    items = uiState.recentBoards,
                    onItemClick = { onMoveToPostContent(it.postId) }
                )

                Spacer(modifier = Modifier.height(screenHeightDp(30.dp)))
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Best 인기글", style = SpotTheme.typography.h3)

                    Box { // 그림자 넣기 위함
                        Image(
                            painter = painterResource(R.drawable.fire),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(
                                SpotTheme.colors.black.copy(alpha = 0.25f)
                            ),
                            modifier = Modifier
                                .size(screenWidthDp(22.dp))
                                .graphicsLayer {
                                    translationY = 1.dp.toPx()
                                    renderEffect = BlurEffect(12f, 12f)
                                    clip = false
                                }
                        )
                        Image(
                            painter = painterResource(R.drawable.fire),
                            contentDescription = null,
                            modifier = Modifier.size(screenWidthDp(22.dp))
                        )
                    }

                    Spacer(Modifier.weight(1f))

                    BoardTabs(
                        selected = sortType,
                        onSelect = viewmodel::selectSort
                    )
                }

                Spacer(Modifier.height(screenHeightDp(12.dp)))
            }

            item {
                BestCardList(
                    items = uiState.bestBoards,
                    onItemClick = { onMoveToPostContent(it.postId) }
                )
            }
        }
    }
}


@Composable
private fun BoardTabChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val bg = if (selected) SpotTheme.colors.B100 else Color.Transparent
    val fg = if (selected) SpotTheme.colors.B500 else SpotTheme.colors.gray500

    Box(
        modifier = Modifier
            .clip(SpotShapes.Hard)
            .background(bg)
            .clickable(onClick = onClick)
            .padding(horizontal = screenWidthDp(6.dp), vertical = screenHeightDp(4.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = SpotTheme.typography.regular_500,
            color = fg
        )
    }
}

@Composable
private fun BoardTabs(
    selected: SortType,
    onSelect: (SortType) -> Unit
) {
    Row(
        modifier = Modifier.wrapContentSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(screenWidthDp(7.dp)),
    ) {
        BoardTabChip(
            text = "실시간",
            selected = selected == SortType.RECENT,
            onClick = { onSelect(SortType.RECENT) }
        )

        VerticalDivider(
            color = SpotTheme.colors.G300,
            thickness = 1.dp,
            modifier = Modifier
                .height(screenHeightDp(14.dp))
        )

        BoardTabChip(
            text = "추천순",
            selected = selected == SortType.RECOMMEND,
            onClick = { onSelect(SortType.RECOMMEND) }
        )

        VerticalDivider(
            color = SpotTheme.colors.G300,
            thickness = 1.dp,
            modifier = Modifier
                .height(screenHeightDp(14.dp))
        )

        BoardTabChip(
            text = "댓글순",
            selected = selected == SortType.COMMENT_COUNT,
            onClick = { onSelect(SortType.COMMENT_COUNT) }
        )
    }
}

@Composable
private fun SectionHeader(
    title: String,
    onMoreClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = SpotTheme.typography.h3,
            modifier = Modifier.wrapContentSize()
        )

        Spacer(Modifier.weight(1f))

        Box(
            modifier = Modifier
                .wrapContentWidth()
                .height(screenHeightDp(24.dp))
                .clip(SpotShapes.Hard)
                .clickable(onClick = onMoreClick)
                .padding(
                    horizontal = screenWidthDp(5.dp),
                    vertical = screenHeightDp(2.dp)
                ),
            contentAlignment = Alignment.CenterStart
        ) {
            Icon(
                painter = painterResource(R.drawable.arrow_right),
                contentDescription = "더보기",
                tint = SpotTheme.colors.black,
                modifier = Modifier
                    .size(screenWidthDp(14.dp))
            )
        }

        Spacer(Modifier.width(screenWidthDp(4.dp)))
    }
}

@Composable
private fun BestCardList(
    items: UiState<BestPostResultList>,
    onItemClick: (BestPostResult) -> Unit
) {
    when(items) {
        is UiState.Loading, is UiState.Empty, is UiState.Failure -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeightDp(326.dp)),
                contentAlignment = Alignment.Center
            ) {
                SpotSpinner()
            }
        }
        is UiState.Success -> {
            val list: List<BestPostResult> = items.data.hotPosts

            list.forEachIndexed { index, item ->
                ShapeBox(
                    shape = SpotShapes.Round,
                    color = SpotTheme.colors.white,
                    borderWidth = 1.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clip(SpotShapes.Round)
                        .clickable{ onItemClick(item) },
                    borderColor = SpotTheme.colors.G200,
                ) {
                    BestRow(
                        title = item.title,
                        count = item.commentCount,
                        content = item.content,
                        postType = item.postType,
                        modifier = Modifier
                    )
                }

                if(index != list.lastIndex)
                    Spacer(Modifier.height(screenHeightDp(10.dp)))
            }
        }
    }
}

@Composable
private fun RecentCardList(
    items: UiState<RecentPostResultList>,
    onItemClick: (RecentPostResult) -> Unit
) {
    ShapeBox(
        modifier = Modifier.fillMaxWidth(),
        shape = SpotShapes.Soft,
        color = SpotTheme.colors.white,
        borderWidth = 1.dp,
        borderColor = SpotTheme.colors.G200,
    ) {
        when (items) {
            is UiState.Loading, is UiState.Empty, is UiState.Failure -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    SpotSpinner()
                }
            }
            is UiState.Success -> {
                val list: List<RecentPostResult> = items.data.recentPosts

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(screenWidthDp(7.dp)),
                    verticalArrangement = Arrangement.spacedBy(screenHeightDp(7.dp))
                ) {
                    list.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onItemClick(item) }
                                .padding(horizontal = screenWidthDp(7.dp), vertical = screenHeightDp(3.dp)),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(screenWidthDp(7.dp))
                        ) {
                            // 왼쪽 라벨
                            Text(
                                text = item.postType.korean,
                                style = SpotTheme.typography.regular_500,
                                color = SpotTheme.colors.B500,
                                modifier = Modifier.widthIn(min = screenWidthDp(41.dp))
                            )
                            // 제목
                            Text(
                                text = item.title,
                                style = SpotTheme.typography.regular_400,
                                color = SpotTheme.colors.black,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                            // 카운트
                            Text(
                                text = "( ${cap(item.commentCount)} )",
                                style = SpotTheme.typography.small_400,
                                color = SpotTheme.colors.B500
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BestRow(
    title: String,
    count: Int,
    postType: PostType,
    content: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = screenWidthDp(13.dp),
                vertical = screenHeightDp(8.dp)
            ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = title,
            style = SpotTheme.typography.medium_500,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Spacer(Modifier.height(screenHeightDp(3.dp)))

        Text(
            text = content,
            style = SpotTheme.typography.regular_400,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Spacer(Modifier.height(screenHeightDp(7.dp)))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = postType.korean,
                style = SpotTheme.typography.small_400,
                color = SpotTheme.colors.B500
            )

            Text(
                text = "( ${cap(count)} )",
                style = SpotTheme.typography.small_400,
                color = SpotTheme.colors.B500
            )
        }
    }
}
