package com.example.feature.alert

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.data.alert.AlertItem
import com.example.core.data.alert.AlertUiState
import com.example.core.data.global.AlertKind
import com.example.core.ui.R


@Preview(showBackground = true)
@Composable
fun PopularPostAlertIsReadPreview() {
    val test = AlertItem(
        id = 1,
        kind = AlertKind.POPULAR_POST,
        title = "PostTitle",
        isRead = true
    )

    PopularPostAlert(
        modifier = Modifier.padding(10.dp),
        data = test,
        onClick = { }
    )
}

@Preview(showBackground = true)
@Composable
fun PopularPostAlertPreview() {
    val test = AlertItem(
        id = 1,
        kind = AlertKind.POPULAR_POST,
        title = "PostTitle",
    )

    PopularPostAlert(
        modifier = Modifier.padding(10.dp),
        data = test,
        onClick = { }
    )
}

@Preview(showBackground = true)
@Composable
fun StudyNotiAlertIsReadPreview() {
    val test = AlertItem(
        id = 1,
        studyImageRes = R.drawable.sample,
        kind = AlertKind.STUDY_NOTICE,
        title = "PostTitle",
        isRead = true
    )

    StudyNotiAlert(
        modifier = Modifier.padding(10.dp),
        data = test,
        onClick = { }
    )
}

@Preview(showBackground = true)
@Composable
fun StudyNotiAlertPreview() {
    val test = AlertItem(
        id = 1,
        studyImageRes = R.drawable.sample,
        kind = AlertKind.TODO_DONE,
        title = "PostTitle",
    )

    StudyNotiAlert(
        modifier = Modifier.padding(10.dp),
        data = test,
        onClick = { }
    )
}

@Preview(showBackground = true)
@Composable
fun EnrollStudyCardDisabledPreview() {
    EnrollStudyCard(
        modifier = Modifier.padding(10.dp),
        isAvailable = false,
        onClick = { }
    )
}

@Preview(showBackground = true)
@Composable
fun EnrollStudyCardPreview() {
    EnrollStudyCard(
        modifier = Modifier.padding(10.dp),
        isAvailable = true,
        onClick = { }
    )
}

@Preview(showBackground = true)
@Composable
fun AlertScreenPreview() {
    val sampleUi = AlertUiState(
        alerts = listOf(
            AlertItem(
                id = 1,
                kind = AlertKind.POPULAR_POST,
                title = "실시간 인기글: Compose로 알림 화면 만들기",
                isRead = false
            ),
            AlertItem(
                id = 2,
                kind = AlertKind.STUDY_NOTICE,
                title = "Sample Study",
                studyImageRes = R.drawable.sample,
                isRead = false
            ),
            AlertItem(
                id = 3,
                kind = AlertKind.TODO_DONE,
                title = "Sample Study",
                studyImageRes = R.drawable.sample,
                isRead = true
            ),
            AlertItem(
                id = 4,
                kind = AlertKind.POPULAR_POST,
                title = "실시간 인기글: Compose로 알림 화면 만들기",
                isRead = false
            ),
            AlertItem(
                id = 5,
                kind = AlertKind.STUDY_NOTICE,
                title = "Sample Study",
                studyImageRes = R.drawable.sample,
                isRead = false
            ),
            AlertItem(
                id = 6,
                kind = AlertKind.TODO_DONE,
                title = "Sample Study",
                studyImageRes = R.drawable.sample,
                isRead = true
            ),
            AlertItem(
                id = 7,
                kind = AlertKind.POPULAR_POST,
                title = "실시간 인기글: Compose로 알림 화면 만들기",
                isRead = false
            ),
            AlertItem(
                id = 8,
                kind = AlertKind.STUDY_NOTICE,
                title = "Sample Study",
                studyImageRes = R.drawable.sample,
                isRead = false
            ),
            AlertItem(
                id = 9,
                kind = AlertKind.TODO_DONE,
                title = "Sample Study",
                studyImageRes = R.drawable.sample,
                isRead = true
            )
        ),
        showAppliedStudyCard = true
    )
    val listState = rememberLazyListState() // ✅ 프리뷰 전용 리스트 상태


    AlertScreenContent(
        uiState = sampleUi,
        onBack = {},
        onClickAppliedStudyCard = {},
        onClickAlert = {},
        listState = listState
    )
}
