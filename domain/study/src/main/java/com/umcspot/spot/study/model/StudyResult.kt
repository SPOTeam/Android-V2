package com.umcspot.spot.study.model

import com.umcspot.spot.model.ImageRef

data class StudyResultList (
    val studyList: List<StudyResult>,
    val hasNext: Boolean,
    val nextCursor: Long?
) {
    companion object {
        @JvmStatic
        fun getPopularDummies(count: Int = 3): List<StudyResult> =
            List(count) { i -> StudyResult.dummyStudy(i, idPrefix = "popular") }

        /** 추천 스터디 더미 */
        @JvmStatic
        fun getRecommendedDummies(count: Int = 3): List<StudyResult> =
            List(count) { i -> StudyResult.dummyStudy(i + 100, idPrefix = "reco") }
    }
}

data class StudyResult(
    val id: Long,
    val name: String,
    val description: String,
    val maxMembers : Int,
    val currentMembers: Int = 0,
    val likeCount: Int = 0,
    val isLiked : Boolean,
    val isOwner : Boolean,
    val hitCount: Int = 0,
    val profileImageUrl: ImageRef
) {
    companion object {
        private val titles = listOf(
            "알고리즘 스터디",
            "모바일 앱 클론",
            "면접 대비 CS",
            "Spring Boot 토이",
            "코틀린 코루틴"
        )

        internal fun dummyStudy(index: Int, idPrefix: String): StudyResult {
            val title = titles[index % titles.size]
            val max = 8 + (index % 5)                // 8~12
            val mem = (3 + (index % 6)).coerceAtMost(max)
            return StudyResult(
                id = index.toLong(),
                name = title,
                description = "주 2회 진행, 코드리뷰",
                maxMembers = max,
                currentMembers = mem,
                likeCount = 10 + index * 2,
                isLiked = index%2 == 0,
                isOwner = index%2 == 1,
                hitCount = 150 + index * 20,
                profileImageUrl = ImageRef.Name("ic_study_default")
            )
        }
    }
}
