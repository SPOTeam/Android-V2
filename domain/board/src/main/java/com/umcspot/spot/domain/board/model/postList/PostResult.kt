package com.umcspot.spot.domain.board.model.postList

import com.umcspot.spot.model.PostType

data class PostResultList(
    val posts: List<PostResult>,
    val hasNext: Boolean,
    val nextCursor: Long?,
) {
    companion object {
        @JvmStatic
        fun getPostDummies(count: Int = 30): List<PostResult> =
            List(count) { i -> PostResult.dummyPost(index = i, total = count) }

    }
}


data class PostResult(
    val postId: Long,
    val postType: PostType,
    val title: String,
    val content : String,
    val likeNum: Long,
    val commentNum: Long,
    val viewNum: Long,
    val createdAt: String,
    val isLiked : Boolean

) {
    companion object {
        internal fun dummyPost(index: Int, total: Int): PostResult {
            val labels = PostType.values()
            val postType = labels.random()
            val title = "Lorem ipsum dolor sit amet consectetur Lorem ipsum dolor sit amet consectetur Lorem ipsum dolor sit amet consectetur"

            return PostResult(
                postId = index.toLong(),
                postType = postType,
                title = title,
                content = title,
                likeNum = (0..2000).random().toLong(),
                commentNum = (0..2000).random().toLong(),
                viewNum = (0..2000).random().toLong(),
                createdAt = "25.44.44 44:44",
                isLiked = index%2==0
            )
        }
    }
}
