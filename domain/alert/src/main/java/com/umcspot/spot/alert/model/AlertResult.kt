package com.umcspot.spot.alert.model

import com.umcspot.spot.model.AlertKind
import com.umcspot.spot.model.WeatherType
import java.time.LocalTime


sealed interface ImageRef {
    data object None : ImageRef
    data class Url(val url: String) : ImageRef
    data class LocalName(val name: String) : ImageRef // "ic_study_default" 같은 이름
}

data class AlertResult(
    val id: Int,
    val kind: AlertKind,
    val title: String,
    val subtitle: String = "",
    val studyImageRes: ImageRef = ImageRef.None,
    val isRead: Boolean = false
) {
    companion object {
        fun dummyFrom(): AlertResult =
            AlertResult(
                id = TODO(),
                kind = TODO(),
                title = TODO(),
                subtitle = TODO(),
                studyImageRes = TODO(),
                isRead = TODO()
            )
    }
}
