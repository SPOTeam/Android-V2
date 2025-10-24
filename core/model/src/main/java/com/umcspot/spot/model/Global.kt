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
    LICENSE("자격증"),
    EMPLOYMENT("취업"),
    DISCUSSION("토론"),
    NEWS("시사 / 뉴스"),
    SELFSTUDY("자율 학습"),
    PROJECT("프로젝트"),
    CONTEST("공모전"),
    MAJOR("전공 / 진로 학습"),
    ETC("기타")
}
