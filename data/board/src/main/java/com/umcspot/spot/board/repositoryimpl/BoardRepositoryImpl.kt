package com.umcspot.spot.board.repositoryimpl

import android.util.Log
import com.umcspot.spot.board.mapper.toDomainList
import com.umcspot.spot.board.service.BoardService
import com.umcspot.spot.domain.board.model.board.BestPostResultList
import com.umcspot.spot.domain.board.model.board.RecentPostResultList
import com.umcspot.spot.domain.board.model.postList.PostResultList
import com.umcspot.spot.domain.board.repository.BoardRepository
import com.umcspot.spot.model.PostType
import com.umcspot.spot.model.SortType
import javax.inject.Inject

class BoardRepositoryImpl @Inject constructor(
    private val boardService: BoardService
) : BoardRepository {
    override suspend fun getRecentBoard(): Result<RecentPostResultList> =
        runCatching {
            val res = boardService.getRecentBoard()
            Log.d("BoardRepository", "getRecentBoard res = $res")
            val domain = res.result.toDomainList()
            Log.d("BoardRepository", "getRecentBoard mapped = $domain")
            domain
        }.onFailure { e ->
            Log.e("BoardRepository", "getRecentBoard failed", e)
        }.recoverCatching { e ->
            Log.w("BoardRepository", "getRecentBoard using dummy because: ${e.message}")
            recentPostDummies(3)
        }

    override suspend fun getBestBoard(
        sortBy: SortType
    ): Result<BestPostResultList> =
        runCatching {
            val res = boardService.getBestBoard(sortBy)
            res.result.toDomainList()
        }.recoverCatching {
            bestPostDummies()
        }

    override suspend fun getFilteredPosts(
        cursor: Long?,
        postType: PostType?,
        size: Int
    ): Result<PostResultList> =
        runCatching {
            val res = boardService.getFilteredPosts(cursor = cursor, postType = postType, size = size )
            res.result.toDomainList()
        }.onFailure { e ->
            Log.e("BoardRepository", "getFilteredBoardList failed", e)
        }.recoverCatching {
            postDummies()
        }

    /** ---- DUMMY HELPERS ---- */

    private fun recentPostDummies(count: Int = 5): RecentPostResultList =
        RecentPostResultList(RecentPostResultList.getRecentPostDummies(count))

    private fun bestPostDummies(count: Int = 5): BestPostResultList =
        BestPostResultList(BestPostResultList.getBestPostDummies(count))

    private fun postDummies(): PostResultList =
        PostResultList(PostResultList.getPostDummies(), hasNext = false, nextCursor = null)
}
