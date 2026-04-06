package com.umcspot.spot.study.detail.component.planner.attendance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.designsystem.theme.White
import com.umcspot.spot.designsystem.theme.Y400
import com.umcspot.spot.study.model.AttendanceStatus
import com.umcspot.spot.study.model.StudyAttendanceModel
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun StudyAttendanceItem(
    attendance: StudyAttendanceModel,
    showStatus: Boolean,
    isHost: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = screenWidthDp(12.dp), vertical = screenHeightDp(6.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(contentAlignment = Alignment.BottomEnd) {
                AsyncImage(
                    model = attendance.profileUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(33.dp)
                        .clip(CircleShape)
                        .background(SpotTheme.colors.gray100),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.spot_logo),
                    error = painterResource(id = R.drawable.spot_logo)
                )


                if (isHost) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_leader),
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = Color.Unspecified
                    )
                }
            }

            Spacer(modifier = Modifier.width(screenWidthDp(8.dp)))

            Text(
                text = attendance.name,
                style = SpotTheme.typography.regular_500,
                color = SpotTheme.colors.black
            )
        }

        if (showStatus) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                attendance.attendedAt?.let { time ->
                    Text(
                        text = time,
                        style = SpotTheme.typography.small_500,
                        color = SpotTheme.colors.gray400
                    )
                }

                Spacer(modifier = Modifier.width(screenWidthDp(8.dp)))

                AttendanceBadge(status = attendance.status)
            }
        }
    }
}

@Composable
private fun AttendanceBadge(status: AttendanceStatus) {
    val (backgroundColor, textColor, label) = when (status) {
        AttendanceStatus.PRESENT -> Triple(
            SpotTheme.colors.B500,
            SpotTheme.colors.White,
            "출석"
        )

        AttendanceStatus.ABSENT -> Triple(
            SpotTheme.colors.error,
            SpotTheme.colors.White,
            "출석"
        )

        else -> Triple(
            SpotTheme.colors.Y400,
            SpotTheme.colors.White,
            "미결"
        )
    }

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(14.dp)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(
                horizontal = screenWidthDp(5.dp),
                vertical = screenHeightDp(1.dp)
            ),
            style = SpotTheme.typography.small_500,
            color = textColor
        )
    }
}