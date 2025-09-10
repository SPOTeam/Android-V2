// BoardScreen.kt
package com.example.feature.board

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.core.data.board.BoardItem
import com.example.core.data.board.BoardUiState
import com.example.core.data.board.LabeledItem
import com.example.core.data.global.SortType
import com.example.core.ui.R
import com.example.core.ui.theme.SpotTypography
import com.example.core.ui.theme.B500
import com.example.core.ui.theme.G300
import com.example.core.ui.theme.White
import com.example.core.ui.shapes.SpotShapes
import com.example.core.ui.component.appBar.AppBarHome

@Composable
fun BoardScreen(
    navController: NavController,
    viewmodel: BoardViewModel = hiltViewModel(),
    onItemClick: (BoardItem) -> Unit = {},
    onLabeledItemClick: (LabeledItem) -> Unit = {},
    onMorePartnersClick: () -> Unit = {},
    onMoreNoticeClick: () -> Unit = {},
) {
    val state by viewmodel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            AppBarHome(
                hasNotification = false,
                onSearchClick = { /* TODO */ },
                onNotificationClick = { /* TODO */ }
            )
        }
    ) { inner ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(horizontal = 14.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {

            /* 🔥 + 탭(우측정렬) */
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 30.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.fire),
                        contentDescription = null,
                        modifier = Modifier
                            .size(22.dp)
                            .padding(end = 6.dp)
                    )
                    Spacer(Modifier.weight(1f))
                    BoardTabs(
                        selected = state.selected,
                        onSelect = viewmodel::selectSort
                    )
                }
            }

            /* 실시간 인기글 카드 (랭크 리스트) */
            item {
                RankCardList(
                    items = state.hot,
                    onItemClick = onItemClick
                )
            }

            /* 스터디 파트너들의 이야기 (섹션 헤더 + 라벨 리스트) */
            item {
                SectionHeader(
                    title = "스터디 파트너들의 이야기",
                    onMoreClick = onMorePartnersClick
                )
            }
            item {
                LabeledCardList(
                    items = state.partners,
                    onItemClick = onLabeledItemClick
                )
            }

            /* SPOT 공지 (섹션 헤더 + 랭크 리스트) */
            item {
                SectionHeader(
                    title = "SPOT 공지",
                    onMoreClick = onMoreNoticeClick
                )
            }
            item {
                RankCardList(
                    items = state.notice,
                    onItemClick = onItemClick
                )
            }
        }
    }
}

/* ---------- 컴포넌트들 ---------- */

@Composable
private fun BoardTabChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val bg = if (selected) B500.copy(alpha = 0.12f) else Color.Transparent
    val fg = if (selected) B500 else Color(0xFF666B73)

    Box(
        modifier = Modifier
            .clip(SpotShapes.Round)
            .background(bg)
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = SpotTypography.bodySmall500.copy(fontSize = 13.sp),
            color = fg
        )
    }
}


@Composable
private fun BoardTabs(
    selected: SortType,
    onSelect: (SortType) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        BoardTabChip(
            text = "실시간",
            selected = selected == SortType.LIVE,
            onClick = { onSelect(SortType.LIVE) }
        )
        BoardTabChip(
            text = "추천순",
            selected = selected == SortType.RECOMMEND,
            onClick = { onSelect(SortType.RECOMMEND) }
        )
        BoardTabChip(
            text = "댓글순",
            selected = selected == SortType.COMMENTS,
            onClick = { onSelect(SortType.COMMENTS) }
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
            style = SpotTypography.bodyMedium500.copy(fontSize = 18.sp),
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = onMoreClick, modifier = Modifier.size(28.dp)) {
            Icon(
                painter = painterResource(R.drawable.arrow_right),
                contentDescription = "더보기",
                tint = B500
            )
        }
    }
}

/** 랭크 카드 리스트 (BoardSection 참고해서 동일 스타일) */
@Composable
private fun RankCardList(
    items: List<BoardItem>,
    onItemClick: (BoardItem) -> Unit
) {
    Surface(
        shape = SpotShapes.Hard,
        color = Color.Transparent,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(vertical = 6.dp)) {
            items.forEachIndexed { index, item ->
                RankRow(
                    rank = index + 1,
                    title = item.title,
                    count = item.count,
                    onClick = {

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                )
            }
        }
    }
}

/** 라벨이 있는 리스트 (파트너들 이야기) */
@Composable
private fun LabeledCardList(
    items: List<LabeledItem>,
    onItemClick: (LabeledItem) -> Unit
) {
    Surface(
        shape = SpotShapes.Hard,
        color = Color.Transparent,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(vertical = 6.dp)) {
            items.forEachIndexed { index, item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 왼쪽 라벨
                    Text(
                        text = item.label,
                        style = SpotTypography.bodySmall500.copy(fontSize = 14.sp),
                        color = B500,
                        modifier = Modifier.widthIn(min = 56.dp)
                    )
                    // 제목
                    Text(
                        text = item.title,
                        style = SpotTypography.bodyMedium500.copy(fontSize = 14.sp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    // 카운트
                    Text(
                        text = "(${cap(item.count)})",
                        style = SpotTypography.bodySmall500.copy(fontSize = 14.sp),
                        color = B500
                    )
                }
            }
        }
    }
}

/** 999+ 포맷 */
private fun cap(n: Int): String = if (n >= 1000) "999+" else n.toString()

/* ---------- 랭크 행 (이미 프로젝트에 있으면 그걸 사용하세요) ---------- */
@Composable
private fun RankRow(
    rank: Int,
    title: String,
    count: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,

    ) {
        Text(
            text = rank.toString().padStart(2, '0'),
            style = SpotTypography.bodySmall500.copy(fontSize = 14.sp),
            color = B500,
            modifier = Modifier.width(28.dp)
        )
        Text(
            text = title,
            style = SpotTypography.bodyMedium500.copy(fontSize = 14.sp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "(${cap(count)})",
            style = SpotTypography.bodySmall500.copy(fontSize = 14.sp),
            color = B500
        )
    }
}


//Preview용 Compose
@Composable
fun BoardScreen(
    state: BoardUiState,
    onSelectTab: (SortType) -> Unit = {},
    onRetry: () -> Unit = {},
    onItemClick: (BoardItem) -> Unit = {},
    onLabeledItemClick: (LabeledItem) -> Unit = {},
    onMorePartnersClick: () -> Unit = {},
    onMoreNoticeClick: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            AppBarHome(
                hasNotification = false,
                onSearchClick = { /* TODO */ },
                onNotificationClick = { /* TODO */ }
            )
        }
    ) { inner ->
        when {
            state.isLoading -> Box(Modifier
                .fillMaxSize()
                .padding(inner), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            state.error != null -> Box(Modifier
                .fillMaxSize()
                .padding(inner), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("문제가 발생했어요.\n${state.error}")
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = onRetry) { Text("다시 시도") }
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(inner)
                        .padding(horizontal = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp),
                    contentPadding = PaddingValues(bottom = 88.dp)
                ) {
                    // 🔥 + 우측 정렬 탭
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 30.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(R.drawable.fire),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(30.dp)
                            )
                            Spacer(Modifier.weight(1f))
                            BoardTabs(selected = state.selected, onSelect = onSelectTab)
                        }
                    }

                    // 실시간 인기글
                    item { RankCardList(state.hot, onItemClick) }

                    // 파트너들의 이야기
                    item { SectionHeader("스터디 파트너들의 이야기", onMorePartnersClick) }
                    item { LabeledCardList(state.partners, onLabeledItemClick) }

                    // SPOT 공지
                    item { SectionHeader("SPOT 공지", onMoreNoticeClick) }
                    item { RankCardList(state.notice, onItemClick) }
                }
            }
        }
    }
}
