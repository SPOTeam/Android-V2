package com.umcspot.spot.category.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.button.MultiButton
import com.umcspot.spot.designsystem.component.button.TextButton
import com.umcspot.spot.designsystem.component.button.TextButtonState
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.G400
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.model.ActivityType
import com.umcspot.spot.model.FeeRange
import com.umcspot.spot.model.RecruitingStatus
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun RecruitingStatusSection(
    recruitingStatus: RecruitingStatus?,
    onSelect: (RecruitingStatus) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(screenWidthDp(14.dp))
    ) {
        RecruitingStatus.entries.forEach { type ->
            TextButton(
                modifier = Modifier
                    .width(screenWidthDp(71.dp))
                    .height(screenHeightDp(35.dp)),
                text = type.value,
                shape = SpotShapes.Hard,
                state = TextButtonState.Toggle,
                checked = recruitingStatus == type,
                onClick = { onSelect(type) },
                style = SpotTheme.typography.medium_500
            )
        }
    }
}

@Composable
fun ActivityTypeSection(
    selectedType: ActivityType?,
    onToggle: (ActivityType) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(screenWidthDp(14.dp))
    ) {
        ActivityType.entries.forEach { type ->
            MultiButton(
                modifier = Modifier.weight(1f),
                text = type.label,
                shape = SpotShapes.Soft,
                painter = when (type) {
                    ActivityType.ONLINE -> painterResource(R.drawable.online)
                    ActivityType.OFFLINE -> painterResource(R.drawable.offline)
                },
                checked = selectedType == type,
                onClick = { onToggle(type) }
            )
        }
    }
}

@Composable
fun ActivityFeeSection(
    activityFee: FeeRange?,
    onSelect: (FeeRange) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(screenWidthDp(14.dp)),
        verticalArrangement = Arrangement.spacedBy(screenHeightDp(14.dp))
    ) {
        FeeRange.entries.forEach { fee ->
            TextButton(
                text = fee.label,
                modifier = Modifier
                    .width(screenWidthDp(71.dp))
                    .height(screenHeightDp(35.dp)),
                state = TextButtonState.Toggle,
                checked = activityFee == fee,
                onClick = { onSelect(fee) },
                shape = SpotShapes.Hard,
                style = SpotTheme.typography.medium_500
            )
        }
    }
}

@Composable
fun ResetFilterText(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text = "필터 초기화",
        color = SpotTheme.colors.G400,
        style = SpotTheme.typography.regular_500,
        textDecoration = TextDecoration.Underline,
        modifier = modifier
            .semantics { role = Role.Button }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
    )
}