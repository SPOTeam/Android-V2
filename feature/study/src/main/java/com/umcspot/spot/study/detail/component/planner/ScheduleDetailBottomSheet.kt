package com.umcspot.spot.study.detail.component.planner

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.ui.extension.noRippleClickable
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleDetailBottomSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
    isNow: Boolean,
    onAttendanceClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    if (!visible) return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = SpotShapes.RoundTop,
        containerColor = SpotTheme.colors.white,
        dragHandle = {},
        contentWindowInsets = { WindowInsets(0) },
        tonalElevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = screenWidthDp(17.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = screenHeightDp(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "일정 상세",
                    style = SpotTheme.typography.h4,
                    color = SpotTheme.colors.black
                )
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(screenWidthDp(24.dp))
                ) {
                    Icon(
                        painter = painterResource(R.drawable.dismiss),
                        contentDescription = null,
                        tint = SpotTheme.colors.black
                    )
                }
            }

            Text(
                text = "출석체크는 호스트가 QR을 생성한 이후 가능합니다.",
                style = SpotTheme.typography.regular_400,
                color = SpotTheme.colors.gray400
            )

            Spacer(modifier = Modifier.height(screenHeightDp(16.dp)))

            ScheduleDetailMenuItem(
                iconRes = R.drawable.ic_attendance,
                text = "출석체크",
                enabled = isNow,
                onClick = {
                    if (isNow) {
                        onDismiss()
                        onAttendanceClick()
                    }
                }
            )

            Spacer(modifier = Modifier.height(screenHeightDp(16.dp)))

            ScheduleDetailMenuItem(
                iconRes = R.drawable.ic_delete_date,
                text = "일정 삭제",
                enabled = !isNow,
                onClick = {
                    if (!isNow) {
                        onDismiss()
                        onDeleteClick()
                    }
                }
            )

            Spacer(modifier = Modifier.height(screenHeightDp(24.dp)))
        }
    }
}

@Composable
private fun ScheduleDetailMenuItem(
    iconRes: Int,
    text: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .noRippleClickable { onClick() }
            .padding(vertical = screenHeightDp(8.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(screenWidthDp(12.dp))
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(screenWidthDp(24.dp))
        )
        Text(
            text = text,
            style = SpotTheme.typography.h5,
            color = if (enabled) SpotTheme.colors.black else SpotTheme.colors.gray300
        )
    }
}