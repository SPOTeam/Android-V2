package com.umcspot.spot.domain.board.repository

import com.umcspot.spot.domain.board.model.board.BestPostResultList
import com.umcspot.spot.domain.board.model.board.RecentPostResultList
import com.umcspot.spot.domain.board.model.postList.PostResultList
import com.umcspot.spot.model.PostType
import com.umcspot.spot.model.SortType

interface BoardRepository {

    suspend fun getRecentBoard(): Result<RecentPostResultList>

    suspend fun getBestBoard(sortBy: SortType): Result<BestPostResultList>

    suspend fun getFilteredPosts(cursor : Long? = null, postType: PostType? = null, size : Int): Result<PostResultList>

    suspend fun postPostLike(postId : Long) : Result<Unit>

    suspend fun deletePostLike(postId : Long) : Result<Unit>
}