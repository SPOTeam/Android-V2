package com.umcspot.spot.model

enum class QuickMenuType (
    val label : String
) { 
    REGION("내 지역"),
    INTERESTS("내 관심사"),
    RECRUITING("모집 중"),
    BOARD("게시판")
}

enum class WeatherType { RAIN, SNOW, WIND, COLD, HOT, SUNNY }

enum class SortType { RECENT, RECOMMEND, COMMENT_COUNT }

enum class PostType { PASS_EXPERIENCE, INFORMATION_SHARING, COUNSELING, JOB_TALK, FREE_TALK }

val PostType.korean: String
    get() = when (this) {
        PostType.PASS_EXPERIENCE -> "합격후기"
        PostType.INFORMATION_SHARING -> "정보공유"
        PostType.COUNSELING -> "고민상담"
        PostType.JOB_TALK -> "취준토크"
        PostType.FREE_TALK -> "자유토크"
    }

enum class RecruitingStudySort(val label: String) {
    RECENT("최신 순"),
    HITS("조회수 높은 순"),
    LIKES("관심 많은 순")
}

enum class StudyTheme(
    val title: String
) {
    LANGUAGE("어학"),
    CERTIFICATION("자격증"),
    CAREER("취업"),
    DEBATE("토론"),
    CURRENT_AFFAIRS("시사 / 뉴스"),
    SELF_STUDY("자율 학습"),
    PROJECT("프로젝트"),
    COMPETITION("공모전"),
    MAJOR_CAREER("전공 / 진로 학습"),
    OTHER("기타");

    companion object {
        fun from(value: String): StudyTheme =
            StudyTheme.entries.first { it.name == value }
    }
}

enum class StudyStyle {
    NETWORKING,
    GOAL_OR_RULE_ORIENTED,
    SHORT_TERM,
    LONG_TERM,
    INDIVIDUAL_PLUS_DISCUSSION,
    GROUP_PLUS_SIMULTANEOUS,
    LEARNING_BASED,
    DISCUSSION_BASED,
    LIGHT_AND_FLEXIBLE,
    STRUCTURED_AND_PLANNED
}

enum class ActivityType(val label: String) {
    ONLINE("온라인"),
    OFFLINE("오프라인");

    companion object {
        fun from(value: String): ActivityType? =
            entries.firstOrNull { it.name == value }
    }
}

enum class FeeRange(val label: String) {
    NONE("없음"),
    BELOW_10K("1만원 미만"),
    FROM_10K_TO_20K("1만원대"),
    FROM_20K_TO_30K("2만원대"),
    FROM_30K_TO_40K("3만원대"),
    FROM_40K_TO_50K("4만원대"),
    ABOVE_50K("5만원 이상");

    companion object {
        fun from(value: String): FeeRange? =
            entries.firstOrNull { it.name == value }
    }
}

enum class RecruitingStatus(val value: String) {
    RECRUITING("모집중"),
    COMPLETED("모집완료");

    companion object {
        fun from(value: String): RecruitingStatus? =
            entries.firstOrNull { it.name == value }
    }
}

enum class SocialLoginType(
    val title: String
) {
    KAKAO("kakao"),
    NAVER("naver");

    companion object {
        fun from(value: String): SocialLoginType {
            val normalized = value.lowercase()

            return values().firstOrNull {
                it.title == normalized
            } ?: KAKAO
        }
    }
}
