package com.umcspot.spot.category.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.B100
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.G200
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.model.RecruitingStudySort
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun CategoryHeaderRow(
    size: Int,
    sortType: RecruitingStudySort,
    isFiltered: Boolean,
    onOpenSortSheet: () -> Unit,
    onFilterClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = screenWidthDp(17.dp)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "%02d건".format(size),
            style = SpotTheme.typography.regular_500,
            color = SpotTheme.colors.gray400
        )

        Row {
            OutlinedButton(
                onClick = onOpenSortSheet,
                shape = SpotShapes.Hard,
                border = BorderStroke(1.dp, SpotTheme.colors.G200),
                contentPadding = PaddingValues(
                    start = screenWidthDp(7.dp),
                    end = screenWidthDp(4.dp),
                    top = screenHeightDp(2.dp),
                    bottom = screenHeightDp(4.dp)
                ),
                modifier = Modifier
                    .wrapContentWidth()
                    .height(screenHeightDp(26.dp))
            ) {
                Text(
                    text = sortType.label,
                    color = SpotTheme.colors.black,
                    style = SpotTheme.typography.regular_500
                )
                Spacer(Modifier.width(screenWidthDp(7.dp)))
                Icon(
                    modifier = Modifier.size(screenWidthDp(14.dp)),
                    painter = painterResource(R.drawable.arrow_down),
                    tint = SpotTheme.colors.B500,
                    contentDescription = null
                )
            }

            Spacer(Modifier.width(screenWidthDp(10.dp)))

            Box(
                modifier = Modifier
                    .size(screenWidthDp(26.dp))
                    .clip(SpotShapes.Hard)
                    .background(
                        color = if (isFiltered) SpotTheme.colors.B100
                                else SpotTheme.colors.white
                    )
                    .clickable { onFilterClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.filter),
                    contentDescription = "필터",
                    modifier = Modifier.size(screenWidthDp(14.dp)),
                    tint = if (isFiltered) SpotTheme.colors.B500 else SpotTheme.colors.black
                )
            }
        }
    }
}