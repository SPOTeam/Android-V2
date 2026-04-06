package com.umcspot.spot.designsystem.component.location

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.ui.extension.screenHeightDp

@Composable
fun SearchResultItem(
    name: String,
    isEnabled: Boolean,
    isAlreadySelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text = name,
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = isEnabled) { onClick() }
            .padding(vertical = screenHeightDp(16.dp)),
        style = SpotTheme.typography.h5,
        color = if (isAlreadySelected || !isEnabled) {
            SpotTheme.colors.gray400
        } else {
            SpotTheme.colors.black
        }
    )
}