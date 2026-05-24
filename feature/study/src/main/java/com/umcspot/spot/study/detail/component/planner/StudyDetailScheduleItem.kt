package com.umcspot.spot.study.detail.component.planner

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.ui.extension.noRippleClickable
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun StudyDetailScheduleItem(
    scheduleId: Long,
    title: String,
    timeRange: String,
    isNow: Boolean = false,
    isHost: Boolean = false,
    onScheduleClick: (Long) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(SpotTheme.colors.primaryFaint)
            .noRippleClickable { onScheduleClick(scheduleId) }
            .padding(
                horizontal = screenWidthDp(12.dp),
                vertical = screenHeightDp(10.dp)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = SpotTheme.typography.medium_500,
                color = SpotTheme.colors.black,
            )

            Spacer(modifier = Modifier.height(screenHeightDp(4.dp)))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(screenWidthDp(4.dp))
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = SpotTheme.colors.primarySoftest,
                            shape = RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = screenWidthDp(4.dp), vertical = screenHeightDp(1.dp))
                ) {
                    Text(
                        text = "일시",
                        style = SpotTheme.typography.small_500,
                        color = SpotTheme.colors.primary
                    )
                }

                Text(
                    text = timeRange,
                    style = SpotTheme.typography.regular_500,
                    color = SpotTheme.colors.gray500
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isNow) {
                Box(
                    modifier = Modifier
                        .background(
                            color = SpotTheme.colors.error,
                            shape = RoundedCornerShape(14.dp)
                        )
                        .padding(horizontal = screenWidthDp(5.dp), vertical = screenHeightDp(1.dp))
                ) {
                    Text(
                        text = "출석 체크 가능",
                        style = SpotTheme.typography.small_500,
                        color = SpotTheme.colors.white
                    )
                }
                Spacer(modifier = Modifier.width(screenWidthDp(8.dp)))
            }

            Image(
                painter = painterResource(id = R.drawable.ic_arrow_right),
                contentDescription = "상세 보기",
                modifier = Modifier.size(screenWidthDp(24.dp), screenHeightDp(22.dp))
            )
        }
    }
}