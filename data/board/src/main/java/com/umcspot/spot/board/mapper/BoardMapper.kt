package com.umcspot.spot.board.mapper

import com.umcspot.spot.board.dto.response.LabeledBoardItem
import com.umcspot.spot.board.dto.response.LabeledBoardResponseDto
import com.umcspot.spot.board.dto.response.PostItem
import com.umcspot.spot.board.dto.response.PostResponseDto
import com.umcspot.spot.domain.board.model.board.LabeledBoardResult
import com.umcspot.spot.domain.board.model.board.LabeledBoardResultList
import com.umcspot.spot.domain.board.model.post.PostResult
import com.umcspot.spot.domain.board.model.post.PostResultList

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

fun PostItem.toDomain() : PostResult =
    PostResult (
        id = this.id,
        label = this.label,
        title = this.title,
        content = this.content,
        likeNum = this.likeNum,
        commentNum = this.commentNum,
        viewNum = this.viewNum,
        date = this.date,
        time = this.time
    )


fun PostResponseDto.toDomainList(): PostResultList =
    PostResultList(
        postList = this.postItems.map(PostItem::toDomain)
    )
