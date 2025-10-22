package com.umcspot.spot.model

enum class QuickMenuType { REGION, INTERESTS, RECRUITING, BOARD }

enum class WeatherType { HEAVYRAIN, RAIN, SNOW, WIND, COLD, HOT, SUNNY }

enum class SortType { LIVE, RECOMMEND, COMMENTS }

enum class BoardType { PASSREVIEW, INFOSHARE, CONSULT, JOBTALK, FREETALK}

val BoardType.korean: String
    get() = when (this) {
        BoardType.PASSREVIEW -> "합격후기"
        BoardType.INFOSHARE  -> "정보공유"
        BoardType.CONSULT    -> "고민상담"
        BoardType.JOBTALK    -> "취준토크"
        BoardType.FREETALK   -> "자유토크"
    }

enum class AlertKind { POPULAR_POST, STUDY_NOTICE, STUDY_SCHEDULE, TODO_DONE }

