package com.umcspot.spot.study.model

import com.umcspot.spot.model.ImageRef

data class StudyResultList (
    val studyList: List<StudyResult>
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
    val studyId: String,
    val title: String,
    val goal: String,
    val maxMember : Int,
    val member: Int = 0,
    val likes: Int = 0,
    val views: Int = 0,
    val studyImage: ImageRef = ImageRef.None
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
                studyId = "$idPrefix-$index",
                title = title,
                goal = "주 2회 진행, 코드리뷰",
                maxMember = max,
                member = mem,
                likes = 10 + index * 2,
                views = 150 + index * 20,
                studyImage = ImageRef.Name("ic_study_default") // 필요시 Url로 교체
            )
        }
    }
}
