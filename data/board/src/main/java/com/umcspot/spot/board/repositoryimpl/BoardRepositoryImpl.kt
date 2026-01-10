package com.umcspot.spot.board.repositoryimpl

import android.util.Log
import com.umcspot.spot.board.mapper.toDomainList
import com.umcspot.spot.board.service.BoardService
import com.umcspot.spot.domain.board.model.board.BestPostResult
import com.umcspot.spot.domain.board.model.board.BestPostResultList
import com.umcspot.spot.domain.board.model.board.RecentPostResultList
import com.umcspot.spot.domain.board.model.postList.PostResultList
import com.umcspot.spot.domain.board.repository.BoardRepository
import com.umcspot.spot.model.PostType
import com.umcspot.spot.model.SortType
import com.umcspot.spot.network.model.NullResultResponse
import javax.inject.Inject

class BoardRepositoryImpl @Inject constructor(
    private val boardService: BoardService
) : BoardRepository {
    override suspend fun getRecentBoard(): Result<RecentPostResultList> =
        runCatching {
            boardService.getRecentBoard().result.toDomainList()
        }.onFailure { e ->
            Log.e("BoardRepository", "getRecentBoard failed", e)
        }.recoverCatching {
            recentPostDummies(3)
        }

    override suspend fun getBestBoard(
        sortBy: SortType
    ): Result<BestPostResultList> =
        runCatching {
            boardService.getBestBoard(sortBy).result.toDomainList()
        }.onFailure { e ->
            Log.e("BoardRepository", "getBestBoard failed", e)
        }.recoverCatching {
            bestPostDummies()
        }

    override suspend fun getBestSinglePost(
    ): Result<BestPostResult> =
        runCatching {
            val lists = boardService.getBestBoard(SortType.RECENT).result.toDomainList()
            lists.hotPosts[0]
        }.onFailure { e ->
            Log.e("BoardRepository", "getBestBoard failed", e)
        }

    override suspend fun getFilteredPosts(
        cursor: Long?,
        postType: PostType?,
        size: Int
    ): Result<PostResultList> =
        runCatching {
            boardService.getFilteredPosts(cursor = cursor, postType = postType, size = size ).result.toDomainList()
        }.onFailure { e ->
            Log.e("BoardRepository", "getFilteredBoardList failed", e)
        }.recoverCatching {
            postDummies()
        }

    override suspend fun postPostLike(postId: Long): Result<Unit> =
        runCatching {
            boardService.postPostLike(postId)
        }


    override suspend fun deletePostLike(postId: Long): Result<Unit> =
        runCatching {
            boardService.deletePostLike(postId)
        }


    /** ---- DUMMY HELPERS ---- */

    private fun recentPostDummies(count: Int = 5): RecentPostResultList =
        RecentPostResultList(RecentPostResultList.getRecentPostDummies(count))

    private fun bestPostDummies(count: Int = 5): BestPostResultList =
        BestPostResultList(BestPostResultList.getBestPostDummies(count))

    private fun postDummies(): PostResultList =
        PostResultList(PostResultList.getPostDummies(), hasNext = false, nextCursor = null)
}
