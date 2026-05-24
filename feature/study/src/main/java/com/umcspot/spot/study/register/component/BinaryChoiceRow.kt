package com.umcspot.spot.study.register.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun BinaryChoiceRow(
    leftText: String,
    rightText: String,
    selectedIndex: Int?,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SelectionChip(
            text = leftText,
            isSelected = selectedIndex == 0,
            onClick = { onSelect(0) },
            modifier = Modifier.weight(1f)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(screenWidthDp(6.dp)))
            VerticalDivider(
                modifier = Modifier.height(screenHeightDp(27.dp)),
                thickness = 1.dp,
                color = SpotTheme.colors.gray300
            )
            Spacer(modifier = Modifier.width(screenWidthDp(6.dp)))
        }

        SelectionChip(
            text = rightText,
            isSelected = selectedIndex == 1,
            onClick = { onSelect(1) },
            modifier = Modifier.weight(1f)
        )
    }
}