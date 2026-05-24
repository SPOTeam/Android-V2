package com.umcspot.spot.designsystem.component.location

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun RegionItem(
    regionName: String,
    isReadOnly: Boolean,
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
            .padding(all = screenWidthDp(10.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f, fill = false)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_location),
                contentDescription = "지역",
                tint = SpotTheme.colors.B500,
                modifier = Modifier.size(screenWidthDp(18.dp))
            )
            Spacer(modifier = Modifier.width(screenWidthDp(8.dp)))
            Text(
                text = regionName,
                style = SpotTheme.typography.h5,
                color = SpotTheme.colors.B500
            )
        }

        if (!isReadOnly) {
            IconButton(
                onClick = onRemoveClick,
                modifier = Modifier.size(screenWidthDp(18.dp))
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.dismiss),
                    contentDescription = "삭제",
                    tint = SpotTheme.colors.B500,
                    modifier = Modifier.size(screenWidthDp(18.dp))
                )
            }
        }
    }
}