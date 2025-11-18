package com.umcspot.spot.domain.board.model

import com.umcspot.spot.model.BoardType
import com.umcspot.spot.model.SortType

data class LabeledBoardResultList(
    val boardList: List<LabeledBoardResult>
) {
    companion object {
        @JvmStatic
        fun getLabeledBoardDummies(count: Int = 5): List<LabeledBoardResult> =
            List(count) { i -> LabeledBoardResult.dummyBoard(index = i, total = count) }

        fun dummyList(count: Int = 5): LabeledBoardResultList =
            LabeledBoardResultList(
                boardList = getLabeledBoardDummies(count)
            )

    }
}



data class LabeledBoardResult(
    val id: Int,
    val label: BoardType,
    val title: String,
    val content : String,
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
                content = title,
                count = base
            )
        }
    }
}
