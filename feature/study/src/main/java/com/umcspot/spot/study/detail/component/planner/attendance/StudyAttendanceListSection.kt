package com.umcspot.spot.study.detail.component.planner.attendance

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.study.model.StudyAttendanceModel
import com.umcspot.spot.ui.extension.screenHeightDp

@Composable
fun StudyAttendanceListSection(
    attendances: List<StudyAttendanceModel>,
    showStatus: Boolean,
    myUserId: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "스터디 출석 정보",
            style = SpotTheme.typography.h5,
            color = SpotTheme.colors.black,
            modifier = Modifier.padding(bottom = screenHeightDp(12.dp))
        )

        attendances.forEach { attendance ->
            StudyAttendanceItem(
                attendance = attendance,
                showStatus = showStatus,
                isHost = true
            )
        }
    }
}