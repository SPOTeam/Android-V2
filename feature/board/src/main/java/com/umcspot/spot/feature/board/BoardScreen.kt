package com.umcspot.spot.feature.board

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.umcspot.spot.designsystem.theme.G300
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.domain.board.model.LabeledBoardResult
import com.umcspot.spot.domain.board.model.LabeledBoardResultList
import com.umcspot.spot.domain.board.model.RankedBoardResult
import com.umcspot.spot.domain.board.model.RankedBoardResultList
import com.umcspot.spot.model.SortType
import com.umcspot.spot.model.korean
import com.umcspot.spot.ui.state.UiState

@Composable
fun BoardScreen(
    viewmodel: BoardViewModel = hiltViewModel(),
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val state by viewmodel.uiState.collectAsStateWithLifecycle()

    // 최초 진입 시 한 번 로드
    LaunchedEffect(state.user) {
        if (state.user is UiState.Empty) {
            viewmodel.load(SortType.LIVE)
        }
    }

    val topPad = contentPadding.calculateTopPadding()
    val bottomPad = contentPadding.calculateBottomPadding()

    when (val state = state.user) {
        is UiState.Loading -> {
            Text(text = "로딩 중...", color = Color.Gray)
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
                            selected = payload.selected, // ← payload에서 가져오기
                            onSelect = viewmodel::selectSort // viewModel에 이 함수가 있어야 함
                        )
                    }
                }

                /* 실시간 인기글 카드 (탭 영향 받는 랭크 리스트: tagBoards) */
                item {
                    RankCardList(
                        items = payload.tagBoards, // ← payload에서 가져오기
                        onItemClick = {  }
                    )
                }

                /* 스터디 파트너들의 이야기 (라벨 리스트) */
                item {
                    SectionHeader(
                        title = "스터디 파트너들의 이야기",
                        onMoreClick = {  }
                    )
                }
                item {
                    LabeledCardList(
                        items = payload.labeledBoards, // ← payload에서 가져오기
                        onItemClick = {  }
                    )
                }

                /* SPOT 공지 (랭크 리스트: noticeBoards) */
                item {
                    SectionHeader(
                        title = "SPOT 공지",
                        onMoreClick = {  }
                    )
                }
                item {
                    RankCardList(
                        items = payload.rankedBoards,
                        onItemClick = {  }
                    )
                }
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
    val bg = if (selected) SpotTheme.colors.B500.copy(alpha = 0.12f) else Color.Transparent
    val fg = if (selected) SpotTheme.colors.B500 else Color(0xFF666B73)

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
                tint = SpotTheme.colors.B500
            )
        }
    }
}

/** 랭크 카드 리스트 */
@Composable
private fun RankCardList(
    items: RankedBoardResultList,
    onItemClick: (RankedBoardResult) -> Unit
) {
    val borderStroke = BorderStroke(1.dp, SpotTheme.colors.G300)

    Surface(
        shape = SpotShapes.Soft,
        color = Color.Transparent,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        border = borderStroke,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(vertical = 6.dp)) {
            items.boardList.forEachIndexed { index, item ->
                RankRow(
                    rank = index + 1,
                    title = item.title,
                    count = item.count,
                    onClick = { onItemClick(item) },
                    modifier = Modifier
                )
            }
        }
    }
}

/** 라벨이 있는 리스트 */
@Composable
private fun LabeledCardList(
    items: LabeledBoardResultList,
    onItemClick: (LabeledBoardResult) -> Unit
) {
    val borderStroke = BorderStroke(1.dp, SpotTheme.colors.G300)

    Surface(
        shape = SpotShapes.Soft,
        color = Color.Transparent,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        border = borderStroke,
        modifier = Modifier
    ) {
        Column(modifier = Modifier.padding(vertical = 6.dp)) {
            items.boardList.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onItemClick(item) }
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 왼쪽 라벨
                    Text(
                        text = item.label.korean,
                        style = SpotTheme.typography.bodySmall500.copy(fontSize = 14.sp),
                        color = SpotTheme.colors.B500,
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
private fun RankRow(
    rank: Int,
    title: String,
    count: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = rank.toString().padStart(2, '0'),
            style = SpotTheme.typography.bodySmall500.copy(fontSize = 14.sp),
            color = SpotTheme.colors.B500,
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
            color = SpotTheme.colors.B500
        )
    }
}
