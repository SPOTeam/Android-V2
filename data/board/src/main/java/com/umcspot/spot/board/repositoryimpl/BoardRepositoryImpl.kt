package com.umcspot.spot.board.repositoryimpl

import com.umcspot.spot.board.mapper.toDomainList
import com.umcspot.spot.board.service.BoardService
import com.umcspot.spot.domain.board.model.LabeledBoardResultList
import com.umcspot.spot.domain.board.model.RankedBoardResultList
import com.umcspot.spot.domain.board.repository.BoardRepository
import com.umcspot.spot.model.SortType
import javax.inject.Inject

class BoardRepositoryImpl @Inject constructor(
    private val boardService: BoardService
) : BoardRepository {

    override suspend fun getTagBoardData(sortType: SortType): Result<RankedBoardResultList> =
        runCatching {
            val res = boardService.getTagBoardInfo(sortType)
            res.data.toDomainList()
        }.recoverCatching {
            rankedListDummies(sortType)   // 태그 보드도 랭크 리스트 형태로 더미 복구
        }

    override suspend fun getRankedBoardData(): Result<RankedBoardResultList> =
        runCatching {
            val res = boardService.getRankedBoardInfo()
            res.data.toDomainList()
        }.recoverCatching {
            rankedListDummies()
        }

    override suspend fun getLabeledBoardData(): Result<LabeledBoardResultList> =
        runCatching {
            val res = boardService.getLabeledBoardInfo()
            res.data.toDomainList()
        }.recoverCatching {
            labeledListDummies()
        }

    /** ---- DUMMY HELPERS ---- */

    private fun rankedListDummies(sortType : SortType, count: Int = 5): RankedBoardResultList =
        RankedBoardResultList(RankedBoardResultList.getRankedBoardDummies(sortType, count))

    private fun rankedListDummies(count: Int = 5): RankedBoardResultList =
        RankedBoardResultList(RankedBoardResultList.getRankedBoardDummies(count))


    private fun labeledListDummies(count: Int = 5): LabeledBoardResultList =
        LabeledBoardResultList(LabeledBoardResultList.getLabeledBoardDummies(count))
}
