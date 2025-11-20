package com.umcspot.spot.domain.board.model.post

import com.umcspot.spot.model.BoardType
import com.umcspot.spot.model.toSpotForm
import java.sql.Date
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

data class PostResultList(
    val postList: List<PostResult>
) {
    companion object {
        @JvmStatic
        fun getPostDummies(count: Int = 30): List<PostResult> =
            List(count) { i -> PostResult.dummyPost(index = i, total = count) }

    }
}



data class PostResult(
    val id: Int,
    val label: BoardType,
    val title: String,
    val content : String,
    val likeNum: Int,
    val commentNum: Int,
    val viewNum: Int,
    val date: String,
    val time : String
) {
    companion object {
        internal fun dummyPost(index: Int, total: Int): PostResult {
            val labels = BoardType.values()
            val label = labels.random()
            val title = "Lorem ipsum dolor sit amet consectetur Lorem ipsum dolor sit amet consectetur Lorem ipsum dolor sit amet consectetur"

            val day = LocalDate.now().minusDays((0..90).random().toLong()).toSpotForm()

            // 랜덤 시간: 00:00:00 ~ 23:59:59
            val time = LocalTime.of(
                (0..23).random(),
                (0..59).random(),
                (0..59).random()
            ).toSpotForm()

            return PostResult(
                id = index,
                label = label,
                title = title,
                content = title,
                likeNum = (0..2000).random(),
                commentNum = (0..2000).random(),
                viewNum = (0..2000).random(),
                date = day,
                time = time
            )
        }
    }
}
