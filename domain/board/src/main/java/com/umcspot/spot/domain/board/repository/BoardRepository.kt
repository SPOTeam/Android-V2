package com.umcspot.spot.domain.board.repository

import com.umcspot.spot.domain.board.model.board.LabeledBoardResultList
import com.umcspot.spot.domain.board.model.post.PostResultList
import com.umcspot.spot.model.SortType

interface BoardRepository {

    suspend fun getLabeledBoardData(): Result<LabeledBoardResultList>

    suspend fun getTagBoardData(sortType : SortType): Result<LabeledBoardResultList>

    suspend fun getPosts(): Result<PostResultList>

}