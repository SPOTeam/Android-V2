package com.umcspot.spot.domain.board.model.board

import com.umcspot.spot.model.PostType

data class RecentPostResultList(
    val recentPosts: List<RecentPostResult>
) {
    companion object {
        @JvmStatic
        fun getRecentPostDummies(count: Int = 5): List<RecentPostResult> =
            List(count) { i -> RecentPostResult.dummyRecentPost(index = i, total = count) }

        fun dummyList(count: Int = 5): RecentPostResultList =
            RecentPostResultList(
                recentPosts = getRecentPostDummies(count)
            )
    }
}

data class RecentPostResult(
    val postId: Long,
    val title: String,
    val commentCount: Int,
    val postType: PostType,
) {
    companion object {
        internal fun dummyRecentPost(index: Int, total : Int): RecentPostResult {
            val labels = PostType.entries.toTypedArray()                 // 5개 모두 사용
            val label = labels[index % labels.size]
            val title = "Lorem ipsum dolor sit amet consectetur Lorem ipsum dolor sit amet consectetur Lorem ipsum dolor sit amet consectetur"

            return RecentPostResult(
                postId = index.toLong(),
                postType = label,
                title = title,
                commentCount =(0..2000).random(),
            )
        }
    }
}
