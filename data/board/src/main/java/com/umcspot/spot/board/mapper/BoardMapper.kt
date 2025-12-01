package com.umcspot.spot.board.mapper

import com.umcspot.spot.board.dto.response.BestBoardItem
import com.umcspot.spot.board.dto.response.BestBoardResponseDto
import com.umcspot.spot.board.dto.response.PostItem
import com.umcspot.spot.board.dto.response.PostResponseDto
import com.umcspot.spot.board.dto.response.RecentBoardItem
import com.umcspot.spot.board.dto.response.RecentBoardResponseDto
import com.umcspot.spot.domain.board.model.board.BestPostResult
import com.umcspot.spot.domain.board.model.board.BestPostResultList
import com.umcspot.spot.domain.board.model.board.RecentPostResult
import com.umcspot.spot.domain.board.model.board.RecentPostResultList
import com.umcspot.spot.domain.board.model.post.PostResult
import com.umcspot.spot.domain.board.model.post.PostResultList

fun RecentBoardItem.toDomain() : RecentPostResult =
    RecentPostResult (
        postId = this.postId,
        postType = this.postType,
        title = this.title,
        commentCount = this.commentCount
    )


fun RecentBoardResponseDto.toDomainList(): RecentPostResultList =
    RecentPostResultList(
        recentPosts = this.recentPosts.map(RecentBoardItem::toDomain)
    )

fun BestBoardItem.toDomain() : BestPostResult =
    BestPostResult (
        postId = this.postId,
        postType = this.postType,
        title = this.title,
        content = this.content,
        commentCount = this.commentCount
    )


fun BestBoardResponseDto.toDomainList(): BestPostResultList =
    BestPostResultList(
        hotPosts = this.hotPosts.map(BestBoardItem::toDomain)
    )

fun PostItem.toDomain() : PostResult =
    PostResult (
        postId = this.postId,
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
