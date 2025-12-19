package com.umcspot.spot.feature.board.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.Spinner
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
import com.umcspot.spot.feature.board.main.BoardViewModel
import com.umcspot.spot.model.PostType
import com.umcspot.spot.model.SortType
import com.umcspot.spot.model.korean
import com.umcspot.spot.ui.state.UiState

@Composable
fun BoardScreen(
    viewmodel: BoardViewModel = hiltViewModel(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onMoveToBoardList: () -> Unit,
    onMoveToPostContent : (Long) -> Unit
) {
    val state by viewmodel.uiState.collectAsStateWithLifecycle()


    LaunchedEffect(Unit) {
        viewmodel.load(SortType.RECENT)
    }

    val topPad = contentPadding.calculateTopPadding()
    val bottomPad = contentPadding.calculateBottomPadding()

    when (val state = state.user) {
        is UiState.Loading -> {
            Spinner()
        }

        is UiState.Failure -> {
            Text(text = "에러: ${state.msg}", color = Color.Red)
        }

        UiState.Empty -> {
            Text(text = "데이터가 없습니다.")
        }

        is UiState.Success -> {
            val payload = state.data
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(top = topPad, start = 14.dp, end = 14.dp, bottom = bottomPad),
                verticalArrangement = Arrangement.spacedBy(18.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {

                /* 스터디 파트너들의 이야기 (라벨 리스트) */
                item {
                    SectionHeader(
                        title = "스터디 파트너들의 이야기",
                        onMoreClick = { onMoveToBoardList() }
                    )
                }

                item {
                    RecentCardList(
                        items = payload.recentBoards, // ← payload에서 가져오기
                        onItemClick = { onMoveToPostContent(it.postId) }
                    )
                }


                /* 🔥 + 탭(우측정렬) */
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Best 인기글",
                            style = SpotTheme.typography.h3
                        )
                        Box {
                            Image(
                                painter = painterResource(R.drawable.fire),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(
                                    SpotTheme.colors.black.copy(alpha = 0.25f) // 연한 검은색/회색
                                ),
                                modifier = Modifier
                                    .size(22.dp)
                                    .graphicsLayer {
                                        // 살짝 아래로 내리기 (드롭 쉐도우 느낌)
                                        translationY = 1.dp.toPx()
                                        // translationX = 0.5.dp.toPx()  // 살짝 오른쪽으로도 옮기고 싶으면

                                        // Blur + Glow 느낌
                                        renderEffect = BlurEffect(12f, 12f)

                                        // shadowElevation은 안 써도 됨 (우리는 renderEffect로만 처리)
                                        clip = false
                                    }
                            )

                            // 2) 실제 불꽃 아이콘 (앞에)
                            Image(
                                painter = painterResource(R.drawable.fire),
                                contentDescription = null,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Spacer(Modifier.weight(1f))
                        BoardTabs(
                            selected = payload.selected, // ← payload에서 가져오기
                            onSelect = viewmodel::selectSort // viewModel에 이 함수가 있어야 함
                        )
                    }
                }

                /* 실시간 인기글 카드 (탭 영향 받는 랭크 리스트: tagBoards) */
                item {
                    BestCardList(
                        items = payload.bestBoards, // ← payload에서 가져오기
                        onItemClick = { onMoveToPostContent(it.postId) }
                    )
                }
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
            .padding(horizontal = 6.dp, vertical = 4.dp),
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
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.height(IntrinsicSize.Min)
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
                .fillMaxHeight()
                .padding(vertical = 4.dp)
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
                .fillMaxHeight()
                .padding(vertical = 4.dp)
        )

        BoardTabChip(
            text = "댓글순",
            selected = selected == SortType.COMMENT_COUNT,
            onClick = { onSelect(SortType.COMMENT_COUNT) }
        )
    }
}

/** 공통 섹션 헤더 (제목 + >) */
@Composable
private fun SectionHeader(
    title: String,
    onMoreClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = SpotTheme.typography.medium_500.copy(fontSize = 18.sp),
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = onMoreClick, modifier = Modifier.size(28.dp)) {
            Icon(
                painter = painterResource(R.drawable.arrow_right),
                contentDescription = "더보기",
                tint = SpotTheme.colors.B500
            )
        }
    }
}

/** 랭크 카드 리스트 */
@Composable
private fun BestCardList(
    items: BestPostResultList,
    onItemClick: (BestPostResult) -> Unit
) {
    val borderStroke = BorderStroke(1.dp, SpotTheme.colors.G200)

    items.hotPosts.forEachIndexed { index, item ->
        Surface(
            shape = SpotShapes.Soft,
            color = Color.Transparent,
            tonalElevation = 0.dp,
            shadowElevation = 0.dp,
            border = borderStroke,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(vertical = 6.dp)
        ) {
            Column(
            ) {
                BestRow(
                    title = item.title,
                    count = item.commentCount,
                    content = item.content,
                    postType = item.postType,
                    onClick = { onItemClick(item) },
                    modifier = Modifier
                )
            }
        }
    }
}

/** 라벨이 있는 리스트 */
@Composable
private fun RecentCardList(
    items: RecentPostResultList,
    onItemClick: (RecentPostResult) -> Unit
) {
    val borderStroke = BorderStroke(1.dp, SpotTheme.colors.G200)

    Surface(
        shape = SpotShapes.Soft,
        color = Color.Transparent,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        border = borderStroke,
        modifier = Modifier
    ) {
        Column(
            modifier = Modifier.padding(vertical = 6.dp)
        ) {
            items.recentPosts.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onItemClick(item) }
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 왼쪽 라벨
                    Text(
                        text = item.postType.korean,
                        style = SpotTheme.typography.small_500.copy(fontSize = 14.sp),
                        color = SpotTheme.colors.B500,
                        modifier = Modifier.widthIn(min = 56.dp)
                    )
                    // 제목
                    Text(
                        text = item.title,
                        style = SpotTheme.typography.medium_500.copy(fontSize = 14.sp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    // 카운트
                    Text(
                        text = "(${cap(item.commentCount)})",
                        style = SpotTheme.typography.small_500.copy(fontSize = 14.sp),
                        color = SpotTheme.colors.B500
                    )
                }
            }
        }
    }
}

/** 999+ 포맷 */
private fun cap(n: Int): String = if (n >= 1000) "999+" else n.toString()

/* ---------- 랭크 행 ---------- */
@Composable
private fun BestRow(
    title: String,
    count: Int,
    postType: PostType,
    content: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = title,
            style = SpotTheme.typography.medium_500,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = content,
            style = SpotTheme.typography.regular_400,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Spacer(Modifier.height(4.dp))

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
