package com.umcspot.spot.domain.board.model.board

import com.umcspot.spot.model.PostType

data class BestPostResultList(
    val hotPosts: List<BestPostResult>
) {
    companion object {
        @JvmStatic
        fun getBestPostDummies(count: Int = 5): List<BestPostResult> =
            List(count) { i -> BestPostResult.dummyBestPost(index = i, total = count) }

        fun dummyList(count: Int = 5): BestPostResultList =
            BestPostResultList(
                hotPosts = getBestPostDummies(count)
            )

    }
}

data class BestPostResult(
    val postId: Long,
    val title: String,
    val content: String,
    val commentCount : Int,
    val postType: PostType,
) {
    companion object {
        internal fun dummyBestPost(index: Int, total : Int): BestPostResult {
            val labels = PostType.entries.toTypedArray()                 // 5개 모두 사용
            val label = labels[index% labels.size]
            val title = "Lorem ipsum dolor sit amet consectetur Lorem ipsum dolor sit amet consectetur Lorem ipsum dolor sit amet consectetur"

            return BestPostResult(
                postId = index.toLong(),
                postType = label,
                title = title,
                commentCount =(0..2000).random(),
                content = title,
            )
        }
    }
}
