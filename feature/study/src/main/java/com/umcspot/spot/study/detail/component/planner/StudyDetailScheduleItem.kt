package com.umcspot.spot.study.detail.component.planner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.theme.B100
import com.umcspot.spot.designsystem.theme.B50
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.R500
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.study.detail.component.common.DeleteMenuPopup
import com.umcspot.spot.ui.extension.noRippleClickable
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun StudyDetailScheduleItem(
    scheduleId: Long,
    title: String,
    timeRange: String,
    isNow: Boolean = false,
    isMine: Boolean = false,
    showMenu: Boolean = false,
    isMenuExpanded: Boolean = false,
    onMenuClick: (Long) -> Unit = {},
    onDeleteClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = SpotTheme.colors.B50, shape = RoundedCornerShape(10.dp))
                .padding(horizontal = screenWidthDp(12.dp), vertical = screenHeightDp(12.dp)), // 패딩 소폭 조정
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = SpotTheme.typography.medium_500,
                    color = SpotTheme.colors.black
                )

                Spacer(modifier = Modifier.height(screenHeightDp(7.dp)))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(color = SpotTheme.colors.B100, shape = RoundedCornerShape(6.dp))
                            .padding(horizontal = screenWidthDp(4.dp), vertical = screenHeightDp(1.dp))
                    ) {
                        Text(
                            text = "일시",
                            style = SpotTheme.typography.small_500,
                            color = SpotTheme.colors.B500
                        )
                    }
                    Text(
                        text = timeRange,
                        style = SpotTheme.typography.regular_500,
                        color = SpotTheme.colors.gray500
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(screenHeightDp(8.dp))
            ) {
                if (showMenu && isMine) {
                    Box {
                        Icon(
                            painter = painterResource(id = R.drawable.meetball),
                            contentDescription = "일정 옵션",
                            tint = SpotTheme.colors.gray400,
                            modifier = Modifier
                                .size(screenWidthDp(20.dp))
                                .noRippleClickable { onMenuClick(scheduleId) }
                        )
                        if (isMenuExpanded) {
                            Popup(
                                alignment = Alignment.TopEnd,
                                offset = IntOffset(x = 0, y = 80),
                                onDismissRequest = { onMenuClick(scheduleId) },
                                properties = PopupProperties(focusable = true)
                            ) {
                                DeleteMenuPopup(onDelete = { onDeleteClick() })
                            }
                        }
                    }
                } else if (showMenu && !isMine) {
                    Spacer(modifier = Modifier.size(screenWidthDp(20.dp)))
                }

                if (isNow) {
                    Box(
                        modifier = Modifier
                            .background(color = SpotTheme.colors.R500, shape = RoundedCornerShape(14.dp))
                            .padding(horizontal = screenWidthDp(8.dp), vertical = screenHeightDp(2.dp))
                    ) {
                        Text(
                            text = "NOW",
                            style = SpotTheme.typography.small_500,
                            color = SpotTheme.colors.white
                        )
                    }
                }
            }
        }
    }
}