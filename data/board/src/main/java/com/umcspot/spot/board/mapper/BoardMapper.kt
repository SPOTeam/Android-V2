package com.umcspot.spot.board.mapper

import com.umcspot.spot.board.dto.response.LabeledBoardItem
import com.umcspot.spot.board.dto.response.LabeledBoardResponseDto
import com.umcspot.spot.domain.board.model.LabeledBoardResult
import com.umcspot.spot.domain.board.model.LabeledBoardResultList

fun LabeledBoardItem.toDomain() : LabeledBoardResult =
    LabeledBoardResult (
        id = this.id,
        label = this.label,
        title = this.title,
        content = this.content,
        count = this.count
    )


fun LabeledBoardResponseDto.toDomainList(): LabeledBoardResultList =
    LabeledBoardResultList(
        boardList = this.boardItems.map(LabeledBoardItem::toDomain)
    )
