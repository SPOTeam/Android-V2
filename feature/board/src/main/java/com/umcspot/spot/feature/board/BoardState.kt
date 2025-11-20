package com.umcspot.spot.feature.board

import com.umcspot.spot.domain.board.model.board.LabeledBoardResultList
import com.umcspot.spot.domain.board.model.post.PostResultList
import com.umcspot.spot.model.SortType

data class BoardPayload(
    val tagBoards: LabeledBoardResultList,
    val labeledBoards: LabeledBoardResultList,
    val selected: SortType,
    val posts : PostResultList
)
