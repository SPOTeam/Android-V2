package com.umcspot.spot.board.mapper

import com.umcspot.spot.board.dto.response.LabeledBoardItem
import com.umcspot.spot.board.dto.response.LabeledBoardResponseDto
import com.umcspot.spot.board.dto.response.RankedBoardItem
import com.umcspot.spot.board.dto.response.RankedBoardResponseDto
import com.umcspot.spot.domain.board.model.LabeledBoardResult
import com.umcspot.spot.domain.board.model.LabeledBoardResultList
import com.umcspot.spot.domain.board.model.RankedBoardResult
import com.umcspot.spot.domain.board.model.RankedBoardResultList

// Domain -> DTO
//fun Board.toData(): BoardRequestDto =
//    BoardRequestDto(
//        sortType = this.sortType
//    )

fun LabeledBoardItem.toDomain() : LabeledBoardResult =
    LabeledBoardResult (
        id = this.id,
        label = this.label,
        title = this.title,
        count = this.count
    )

fun RankedBoardItem.toDomain() : RankedBoardResult =
    RankedBoardResult (
        id = this.id,
        title = this.title,
        count = this.count
    )


fun LabeledBoardResponseDto.toDomainList(): LabeledBoardResultList =
    LabeledBoardResultList(
        boardList = this.boardItems.map(LabeledBoardItem::toDomain)
    )

fun RankedBoardResponseDto.toDomainList(): RankedBoardResultList =
    RankedBoardResultList(
        boardList = this.boardItems.map(RankedBoardItem::toDomain)
    )