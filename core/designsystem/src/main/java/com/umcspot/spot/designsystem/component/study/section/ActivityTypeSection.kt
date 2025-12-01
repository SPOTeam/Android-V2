package com.umcspot.spot.designsystem.component.study.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.button.MultiButton
import com.umcspot.spot.model.ActivityType
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun ActivityTypeSection(
    activityType: ActivityType?,
    onSelect: (ActivityType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(screenWidthDp(14.dp))
    ) {
        ActivityType.entries.forEach { type ->
            val iconRes = when (type) {
                ActivityType.ONLINE -> painterResource(R.drawable.online)
                ActivityType.OFFLINE -> painterResource(R.drawable.offline)
            }

            MultiButton(
                text = type.label,
                painter = iconRes,
                checked = activityType == type,
                onClick = {
                    onSelect(type)
                },
            )
        }
    }
}