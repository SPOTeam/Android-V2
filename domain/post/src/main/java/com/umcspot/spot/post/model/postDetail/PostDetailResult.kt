package com.umcspot.spot.post.model.postDetail

import com.umcspot.spot.model.ImageRef
import com.umcspot.spot.model.PostType

data class PostDetailResult(
    val postId: Long,
    val title: String,
    val content : String,
    val imageUrl: ImageRef,
    val postType: PostType,

    val writerId : Long,
    val nickname : String,
    val profileImageUrl : ImageRef,

    val likeCount: Long,
    val viewCount: Long,
    val commentCount: Long,

    val createdAt: String,

    val comments : List<CommentResult>,
) {
    companion object {
        fun dummyPostDetail(index: Int, total: Int): PostDetailResult {
            val labels = PostType.values()
            val postType = labels.random()
            val title = "Sample Post Title"
            val content = "Lorem ipsum dolor sit amet consectetur. Lorem lorem elementum ultrices pellentesque duis ultrices fermentum. Orci eu eu est pellentesque elit. A nunc massa orci pellentesque integer ut. Pellentesque amet fames diam quisque ac. Massa turpis potenti eget tristique leo. Enim nulla nunc id ipsum lectus sed sed auctor dolor. Habitant tortor gravida eget aliquet tristique ac blandit."

            return PostDetailResult(
                postId = index.toLong(),
                title = title,
                content = content,
                imageUrl = ImageRef.Name("sample"),        // ✅ 로컬 리소스
                postType = postType,

                writerId = 0,
                nickname = "nickname",
                profileImageUrl = ImageRef.Name("sample"),

                likeCount = (0..2000).random().toLong(),
                viewCount = (0..2000).random().toLong(),
                commentCount = (0..2000).random().toLong(),
                createdAt = "25.44.44 44:44",
                comments = List(5) { idx ->                       // ✅ 더미 댓글 생성
                    CommentResult.dummyComment(idx, total)
                }
            )
        }
    }
}

data class CommentResult (
    val commentId : Long,
    val content : String,
    val writerId : Long,
    val nickname : String,
    val profileImageUrl : ImageRef,
    val createdAt : String,
) {
    companion object {
        fun dummyComment(index: Int, total: Int): CommentResult {
            return CommentResult(
                commentId = index.toLong(),
                content = "샘플 댓글 내용 $index / $total",
                writerId = (index + 1).toLong(),
                nickname = "댓글 작성자 $index",
                profileImageUrl = ImageRef.Name("sample"), // 로컬 리소스 아이콘
                createdAt = "25.12.08 12:${(10 + index) % 60}"     // 대충 예쁜 더미 시간
            )
        }
    }
}
