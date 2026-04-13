package com.umcspot.spot.category.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.model.StudyTheme
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun CategoryTabs(
    tabs: List<StudyTheme?>,
    selectedIndex: Int,
    onTabSelected: (StudyTheme?) -> Unit
) {
    if (tabs.isEmpty()) return

    val scrimWidth = screenWidthDp(24.dp)
    val bg = SpotTheme.colors.white

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .drawWithContent {
                drawContent()
                val w = scrimWidth.toPx()
                drawRect(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color.Transparent, bg),
                        startX = w,
                        endX = 0f
                    ),
                    size = Size(w, size.height),
                    topLeft = Offset(0f, 0f)
                )
                drawRect(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color.Transparent, bg),
                        startX = size.width - w,
                        endX = size.width
                    ),
                    size = Size(w, size.height),
                    topLeft = Offset(size.width - w, 0f)
                )
            }
    ) {
        ScrollableTabRow(
            modifier = Modifier.fillMaxWidth(),
            selectedTabIndex = selectedIndex,
            edgePadding = 0.dp,
            containerColor = Color.Transparent,
            divider = {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = SpotTheme.colors.gray200
                )
            },
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[selectedIndex])
                        .padding(horizontal = screenWidthDp(17.dp))
                        .height(1.dp),
                    color = SpotTheme.colors.B500
                )
            }
        ) {
            tabs.forEachIndexed { index, theme ->
                Tab(
                    modifier = Modifier
                        .wrapContentWidth(),
                    selected = selectedIndex == index,
                    onClick = { onTabSelected(theme) },
                    selectedContentColor = SpotTheme.colors.black,
                    unselectedContentColor = SpotTheme.colors.black
                ) {
                    Text(
                        text = theme?.title ?: "전체",
                        style = SpotTheme.typography.h5,
                        modifier = Modifier.padding(
                            horizontal = screenWidthDp(7.dp),
                            vertical = screenHeightDp(4.dp)
                        )
                    )
                }
            }
        }
    }
}