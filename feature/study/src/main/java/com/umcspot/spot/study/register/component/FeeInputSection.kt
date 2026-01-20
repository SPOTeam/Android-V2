package com.umcspot.spot.study.register.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun FeeInputSection(
    hasFee: Boolean?,
    feeAmount: String,
    onFeeTypeChange: (Boolean) -> Unit,
    onFeeAmountChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(
            if (hasFee == true) screenWidthDp(12.dp) else screenWidthDp(14.dp)
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SelectionChip(
            text = "없음",
            isSelected = hasFee == false,
            onClick = { onFeeTypeChange(false) },
            modifier = Modifier.weight(1f)
        )

        SelectionChip(
            text = "있음",
            isSelected = hasFee == true,
            onClick = { onFeeTypeChange(true) },
            modifier = Modifier.weight(1f)
        )

        if (hasFee == true) {
            PriceTextField(
                value = feeAmount,
                onValueChange = onFeeAmountChange,
                modifier = Modifier.weight(1f)
            )
        }
    }
}