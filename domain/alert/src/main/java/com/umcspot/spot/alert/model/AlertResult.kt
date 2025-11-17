package com.umcspot.spot.alert.model

import com.umcspot.spot.model.AlertKind
import com.umcspot.spot.model.ImageRef
import com.umcspot.spot.model.WeatherType
import java.time.LocalTime

/******** 일반 알람 ********/
data class AppliedAlertResult (
    val alerts : List<AppliedAlertInfo>
) {
    companion object {
        @JvmStatic
        fun getAppliedAlertDummies(total: Int = 10): List<AppliedAlertInfo> = emptyList()
//            List(total) { i ->
//                AppliedAlertInfo.dummyAppliedAlert(id = i + 1, total = total)
//            }
    }
}

data class AppliedAlertInfo(
    val id: Int,
    val title: String,
    val subtitle: String = "",
    val studyImageRes: ImageRef = ImageRef.None,
) {
    companion object {
        fun dummyAppliedAlert(id: Int, total: Int): AppliedAlertInfo {

            return AppliedAlertInfo(
                id = id,
                title = "실시간 인기 글",
                subtitle = "Sample Post Title",
                studyImageRes = ImageRef.LocalName("ic_fire"),
            )
        }
    }
}


/******** 일반 알람 ********/
data class AlertResult (
    val alerts : List<AlertInfo>
) {
    companion object {
        @JvmStatic
        fun getAlertDummies(total: Int = 30): List<AlertInfo> = /*emptyList()*/
            List(total) { i ->
                AlertInfo.dummyAlert(id = i + 1, total = total)
            }
    }
}

data class AlertInfo(
    val id: Int,
    val kind: AlertKind,
    val title: String,
    val subtitle: String = "",
    val studyImageRes: ImageRef = ImageRef.None,
    val isRead: Boolean = false
) {
    companion object {
        fun dummyAlert(id: Int, total: Int): AlertInfo {
            val kind = AlertKind.values()[(id - 1) % AlertKind.values().size]

            return when (kind) {
                AlertKind.POPULAR_POST ->
                    AlertInfo(
                        id = id,
                        kind = kind,
                        title = "실시간 인기 글",
                        subtitle = "Sample Post Title",
                        studyImageRes = ImageRef.LocalName("ic_fire"),
                        isRead = false
                    )

                AlertKind.STUDY_NOTICE ->
                    AlertInfo(
                        id = id,
                        kind = kind,
                        title = "내 스터디 '공지' 업데이트",
                        subtitle = "\"Sample Study\"의 새로운 공지",
                        studyImageRes = ImageRef.LocalName("sample"),
                        isRead = false
                    )

                AlertKind.STUDY_SCHEDULE ->
                    AlertInfo(
                        id = id,
                        kind = kind,
                        title = "내 스터디 '새 일정' 등록",
                        subtitle = "\"Sample Study\"의 새로운 일정",
                        studyImageRes = ImageRef.LocalName("ic_study_default"),
                        isRead = false
                    )

                AlertKind.TODO_DONE ->
                    AlertInfo(
                        id = id,
                        kind = kind,
                        title = "‘사용자님’의 “Sample Todolist” 할 일 완료!",
                        subtitle = "\"Sample Study\"의 ‘사용자님’",
                        studyImageRes = ImageRef.LocalName("ic_check_filled"),
                        isRead = false
                    )
            }
        }
    }
}
