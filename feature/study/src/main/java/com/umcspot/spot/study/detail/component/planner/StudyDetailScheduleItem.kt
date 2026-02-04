package com.umcspot.spot.study.detail.component.planner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.theme.B100
import com.umcspot.spot.designsystem.theme.B50
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.R500
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun StudyDetailScheduleItem(
    title: String,
    timeRange: String,
    isNow: Boolean = false,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = SpotTheme.colors.B50,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(
                horizontal = screenWidthDp(12.dp),
                vertical = screenHeightDp(8.dp)
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(screenHeightDp(7.dp))) {
            Text(
                text = title,
                style = SpotTheme.typography.medium_500,
                color = SpotTheme.colors.black
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = SpotTheme.colors.B100,
                            shape = RoundedCornerShape(6.dp)
                        )
                        .padding(
                            horizontal = screenWidthDp(4.dp),
                            vertical = screenHeightDp(1.dp)
                        )
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

                Spacer(modifier = Modifier.weight(1f))

                if (isNow) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = SpotTheme.colors.R500,
                                shape = RoundedCornerShape(14.dp)
                            )
                            .padding(
                                horizontal = screenWidthDp(5.dp),
                                vertical = screenHeightDp(1.dp)
                            )
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