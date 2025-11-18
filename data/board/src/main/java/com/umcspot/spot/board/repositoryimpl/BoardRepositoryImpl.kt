package com.umcspot.spot.board.repositoryimpl

import com.umcspot.spot.board.mapper.toDomainList
import com.umcspot.spot.board.service.BoardService
import com.umcspot.spot.domain.board.model.LabeledBoardResultList
import com.umcspot.spot.domain.board.repository.BoardRepository
import com.umcspot.spot.model.SortType
import javax.inject.Inject

class BoardRepositoryImpl @Inject constructor(
    private val boardService: BoardService
) : BoardRepository {

    override suspend fun getTagBoardData(sortType: SortType): Result<LabeledBoardResultList> =
        runCatching {
            val res = boardService.getTagBoardInfo(sortType)
            res.data.toDomainList()
        }.recoverCatching {
            labeledListDummies(3)   // 태그 보드도 랭크 리스트 형태로 더미 복구
        }

    override suspend fun getLabeledBoardData(): Result<LabeledBoardResultList> =
        runCatching {
            val res = boardService.getLabeledBoardInfo()
            res.data.toDomainList()
        }.recoverCatching {
            labeledListDummies()
        }

    private fun labeledListDummies(count: Int = 5): LabeledBoardResultList =
        LabeledBoardResultList(LabeledBoardResultList.getLabeledBoardDummies(count))
}
