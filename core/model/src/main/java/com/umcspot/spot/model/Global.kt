package com.umcspot.spot.model

enum class QuickMenuType { REGION, INTERESTS, RECRUITING, BOARD }

enum class WeatherType { HEAVYRAIN, RAIN, SNOW, WIND, COLD, HOT, SUNNY }

enum class SortType { LIVE, RECOMMEND, COMMENTS }

enum class BoardType { PASSREVIEW, INFOSHARE, CONSULT, JOBTALK, FREETALK}

enum class RecruitingStudySort(val label: String) {
    LATEST("최신 순"),
    VIEW("조회수 높은 순"),
    LIKE("관심 많은 순")
}

val BoardType.korean: String
    get() = when (this) {
        BoardType.PASSREVIEW -> "합격후기"
        BoardType.INFOSHARE  -> "정보공유"
        BoardType.CONSULT    -> "고민상담"
        BoardType.JOBTALK    -> "취준토크"
        BoardType.FREETALK   -> "자유토크"
    }

enum class AlertKind { POPULAR_POST, STUDY_NOTICE, STUDY_SCHEDULE, TODO_DONE }

enum class ActivityType(
    val label : String
) {
    ONLINE("온라인"),
    OFFLINE("오프라인")
}

enum class FeeRange(
    val label: String
){
    NONE("없음"),
    UNDER_10K("1만원 미만"),
    ABOUT10K("1만원대"),
    ABOUT20K("2만원대"),
    ABOUT30K("3만원대"),
    ABOUT40K("4만원대"),
    OVER50K("5만원 이상")
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
    OTHER("기타")
}

enum class SocialLoginType(
    val title: String
) {
    KAKAO("kakao"),
    NAVER("naver"),
}
