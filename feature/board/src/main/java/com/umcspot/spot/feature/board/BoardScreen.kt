package com.umcspot.spot.feature.board

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.domain.board.model.Board
import com.umcspot.spot.domain.board.model.Labeled
import com.umcspot.spot.model.SortType

@Composable
fun BoardScreen(
    viewmodel: BoardViewModel = hiltViewModel(),
    contentPadding : PaddingValues = PaddingValues(0.dp)
) {
    val state by viewmodel.uiState.collectAsStateWithLifecycle()

    val topPad = contentPadding.calculateTopPadding()
    val bottomPad = contentPadding.calculateBottomPadding()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = topPad, start = 14.dp, end = 14.dp, bottom = bottomPad),
        verticalArrangement = Arrangement.spacedBy(18.dp),
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
                onItemClick = { }
            )
        }

        /* 스터디 파트너들의 이야기 (섹션 헤더 + 라벨 리스트) */
        item {
            SectionHeader(
                title = "스터디 파트너들의 이야기",
                onMoreClick = { }
            )
        }
        item {
            LabeledCardList(
                items = state.partners,
                onItemClick = { }
            )
        }

        /* SPOT 공지 (섹션 헤더 + 랭크 리스트) */
        item {
            SectionHeader(
                title = "SPOT 공지",
                onMoreClick = { }
            )
        }
        item {
            RankCardList(
                items = state.notice,
                onItemClick = { }
            )
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
            style = SpotTheme.typography.bodySmall500.copy(fontSize = 13.sp),
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
            style = SpotTheme.typography.bodyMedium500.copy(fontSize = 18.sp),
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
    items: List<Board>,
    onItemClick: (Board) -> Unit
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
                    onClick = { onItemClick(item) },   // ⬅️ 여기: 아이템 전달!
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
    items: List<Labeled>,
    onItemClick: (Labeled) -> Unit
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
                        .clickable{ onItemClick(item) }
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 왼쪽 라벨
                    Text(
                        text = item.label,
                        style = SpotTheme.typography.bodySmall500.copy(fontSize = 14.sp),
                        color = B500,
                        modifier = Modifier.widthIn(min = 56.dp)
                    )
                    // 제목
                    Text(
                        text = item.title,
                        style = SpotTheme.typography.bodyMedium500.copy(fontSize = 14.sp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    // 카운트
                    Text(
                        text = "(${cap(item.count)})",
                        style = SpotTheme.typography.bodySmall500.copy(fontSize = 14.sp),
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
        modifier = modifier
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,

        ) {
        Text(
            text = rank.toString().padStart(2, '0'),
            style = SpotTheme.typography.bodySmall500.copy(fontSize = 14.sp),
            color = B500,
            modifier = Modifier.width(28.dp)
        )
        Text(
            text = title,
            style = SpotTheme.typography.bodyMedium500.copy(fontSize = 14.sp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "(${cap(count)})",
            style = SpotTheme.typography.bodySmall500.copy(fontSize = 14.sp),
            color = B500
        )
    }
}


