package com.umcspot.spot.study.register.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.Black
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp
import kotlinx.collections.immutable.ImmutableList

@Composable
fun SelectedRegionsSection(
    selectedRegions: ImmutableList<String>,
    onRemoveClick: (String) -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(screenHeightDp(10.dp))
    ) {
        selectedRegions.forEach { region ->
            RegionItem(
                regionName = region,
                onRemoveClick = { onRemoveClick(region) }
            )
        }

        if (selectedRegions.size < 3) {
            AddRegionButton(onClick = onAddClick)
        }
    }
}

@Composable
private fun RegionItem(
    regionName: String,
    onRemoveClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = SpotTheme.colors.B500,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(horizontal = screenHeightDp(10.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.ic_location),
                contentDescription = "지역",
                tint = SpotTheme.colors.B500,
                modifier = Modifier.size(screenWidthDp(18.dp))
            )
            Spacer(modifier = Modifier.size(screenWidthDp(8.dp)))
            Text(
                text = regionName,
                style = SpotTheme.typography.h5,
                color = SpotTheme.colors.B500
            )
        }
        IconButton(onClick = onRemoveClick) {
            Icon(
                painter = painterResource(id = R.drawable.dismiss),
                contentDescription = "삭제",
                tint = SpotTheme.colors.B500,
                modifier = Modifier.size(screenWidthDp(18.dp))
            )
        }
    }
}

@Composable
private fun AddRegionButton(
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = screenWidthDp(5.dp),
                vertical = screenHeightDp(9.dp)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.multiple),
            contentDescription = "추가",
            tint = SpotTheme.colors.Black,
            modifier = Modifier.size(screenWidthDp(14.dp))
        )
        Spacer(modifier = Modifier.size(screenWidthDp(4.dp)))
        Text(
            text = "지역 추가",
            style = SpotTheme.typography.regular_500,
            color = SpotTheme.colors.Black
        )
    }
}