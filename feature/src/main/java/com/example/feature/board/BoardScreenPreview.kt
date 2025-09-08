package com.example.feature.board

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.core.data.board.BoardItem
import com.example.core.data.board.BoardUiState
import com.example.core.data.board.LabeledItem
import com.example.core.data.global.SortType

/* ---------- Preview ---------- */
@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun BoardScreenPreview() {
    val sample = BoardUiState(
        selected = SortType.LIVE,
        hot = List(5) { i ->
            BoardItem(
                "hot$i",
                "Lorem ipsum dolor sit amet consectetur…",
                listOf(10, 100, 9999, 10, 10)[i]
            )
        },
        partners = listOf(
            LabeledItem("p1", "합격후기", "Lorem ipsum dolor sit amet consectetur…", 10),
            LabeledItem("p2", "정보공유", "Lorem ipsum dolor sit amet consectetur…", 100),
            LabeledItem("p3", "고민상담", "Lorem ipsum dolor sit amet consectetur…", 9999),
            LabeledItem("p4", "취준토크", "Lorem ipsum dolor sit amet consectetur…", 10),
            LabeledItem("p5", "자유토크", "Lorem ipsum dolor sit amet consectetur…", 10),
        ),
        notice = List(5) { i ->
            BoardItem(
                "n$i",
                "Lorem ipsum dolor sit amet consectetur…",
                listOf(10, 100, 1100, 10, 10)[i]
            )
        }
    )
    BoardScreen(state = sample)
}