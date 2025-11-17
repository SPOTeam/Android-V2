package com.umcspot.spot.domain.board.model

import com.umcspot.spot.model.BoardType
import com.umcspot.spot.model.SortType

data class RankedBoardResultList(
    val boardList: List<RankedBoardResult>
) {
    companion object {
        @JvmStatic
        fun getRankedBoardDummies(count: Int = 5): List<RankedBoardResult> =
            List(count) { i -> RankedBoardResult.dummyBoard(rank = i + 1, total = count) }

        @JvmStatic
        fun getRankedBoardDummies(sortType: SortType, count: Int = 5): List<RankedBoardResult> {
            val base = List(count) { i -> RankedBoardResult.dummyBoard(rank = i + 1, total = count) }
            return base.shuffled() // ← 탭 누를 때마다 순서 랜덤
        }
    }
}

data class LabeledBoardResultList(
    val boardList: List<LabeledBoardResult>
) {
    companion object {
        @JvmStatic
        fun getLabeledBoardDummies(count: Int = 5): List<LabeledBoardResult> =
            List(count) { i -> LabeledBoardResult.dummyBoard(index = i, total = count) }
    }
}

data class RankedBoardResult(
    val id: Int,
    val title: String,
    val count: Int
) {
    companion object {
        internal fun dummyBoard(rank: Int, total: Int): RankedBoardResult {
            val idx0 = rank - 1

            val title = "안드로이드 스터디 ${rank+4}주차".repeat(rank.coerceIn(1,5))   // 주차 반영

            // 1~5위 느낌의 단순 카운트
            val counts = listOf(1200, 1100, 1000, 900, 80)
            val count = counts[idx0.coerceIn(0, counts.lastIndex)]

            return RankedBoardResult(
                id = idx0,
                title = title,
                count = count
            )
        }
    }
}

data class LabeledBoardResult(
    val id: Int,
    val label: BoardType,
    val title: String,
    val count: Int
) {
    companion object {
        internal fun dummyBoard(index: Int, total: Int): LabeledBoardResult {
            val labels = BoardType.values()                 // 5개 모두 사용
            val label = labels[index % labels.size]
            val title = "Lorem ipsum dolor sit amet consectetur Lorem ipsum dolor sit amet consectetur Lorem ipsum dolor sit amet consectetur"

            val base = when (label) {
                BoardType.PASSREVIEW -> 1500
                BoardType.INFOSHARE  -> 1100
                BoardType.CONSULT    -> 880
                BoardType.JOBTALK    -> 860
                BoardType.FREETALK   -> 10
            }

            return LabeledBoardResult(
                id = index,
                label = label,
                title = title,
                count = base
            )
        }
    }
}
