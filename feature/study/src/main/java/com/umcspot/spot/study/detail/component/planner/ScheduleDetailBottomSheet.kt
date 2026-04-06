package com.umcspot.spot.study.detail.component.planner

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.ui.extension.noRippleClickable
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun ScheduleDetailBottomSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
    isNow: Boolean,
    onAttendanceClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    if (!visible) return

    val navBarPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false,
            dismissOnClickOutside = true
        )
    ) {
        BackHandler { onDismiss() }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(SpotTheme.colors.black.copy(alpha = 0.4f))
                    .noRippleClickable { onDismiss() }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(SpotShapes.RoundTop)
                    .background(SpotTheme.colors.white)
                    .padding(horizontal = screenWidthDp(17.dp))
                    .padding(bottom = navBarPadding)
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
            tint = if (enabled) SpotTheme.colors.black else SpotTheme.colors.gray300,
            modifier = Modifier.size(screenWidthDp(24.dp))
        )
        Text(
            text = text,
            style = SpotTheme.typography.h5,
            color = if (enabled) SpotTheme.colors.black else SpotTheme.colors.gray300
        )
    }
}