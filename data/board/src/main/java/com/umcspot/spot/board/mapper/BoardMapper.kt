package com.umcspot.spot.board.mapper

import com.umcspot.spot.board.dto.response.BestBoardItem
import com.umcspot.spot.board.dto.response.BestBoardResponseDto
import com.umcspot.spot.board.dto.response.PostItem
import com.umcspot.spot.board.dto.response.PostListResponseDto
import com.umcspot.spot.board.dto.response.RecentBoardItem
import com.umcspot.spot.board.dto.response.RecentBoardResponseDto
import com.umcspot.spot.domain.board.model.board.BestPostResult
import com.umcspot.spot.domain.board.model.board.BestPostResultList
import com.umcspot.spot.domain.board.model.board.RecentPostResult
import com.umcspot.spot.domain.board.model.board.RecentPostResultList
import com.umcspot.spot.domain.board.model.postList.PostResult
import com.umcspot.spot.domain.board.model.postList.PostResultList
import com.umcspot.spot.model.formatCreatedAt

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


fun PostListResponseDto.toDomainList(): PostResultList =
    PostResultList(
        posts = this.posts.map(PostItem::toDomain),
        hasNext = this.hasNext,
        nextCursor = this.nextCursor
    )


fun PostItem.toDomain() : PostResult =
    PostResult (
        postId = this.postId,
        postType = this.postType,
        title = this.title,
        content = this.content,
        likeNum = this.stats.likeCount,
        commentNum = this.stats.commentCount,
        viewNum = this.stats.viewCount,
        createdAt = this.createdAt.formatCreatedAt(),
        isLiked = this.isLiked
    )
