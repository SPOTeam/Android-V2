package com.umcspot.spot.category.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
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
    val density = LocalDensity.current
    val textWidths = remember(tabs) { mutableStateMapOf<Int, androidx.compose.ui.unit.Dp>() }
    val minTabWidth = screenWidthDp(50.dp)


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
            edgePadding = screenWidthDp(17.dp),
            containerColor = Color.Transparent,
            divider = {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = SpotTheme.colors.gray200
                )
            },
            indicator = { tabPositions ->
                val currentTab = tabPositions.getOrNull(selectedIndex) ?: return@ScrollableTabRow
                val textWidth = textWidths[selectedIndex] ?: 0.dp
                val indicatorWidth = if (textWidth > 0.dp) {
                    maxOf(minTabWidth, textWidth)
                } else {
                    minTabWidth
                }
                val indicatorOffsetX = currentTab.left + (currentTab.width - indicatorWidth) / 2

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.BottomStart)
                        .offset(x = indicatorOffsetX)
                        .width(indicatorWidth)
                        .height(1.dp)
                        .background(SpotTheme.colors.B500)
                )
            }
        ) {
            tabs.forEachIndexed { index, theme ->
                val textWidth = textWidths[index] ?: 0.dp
                val tabWidth = if (textWidth > 0.dp) maxOf(minTabWidth, textWidth) else minTabWidth

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
                        onTextLayout = { textLayoutResult ->
                            textWidths[index] = with(density) { textLayoutResult.size.width.toDp() }
                        },
                        modifier = Modifier.padding(
                            horizontal = screenWidthDp(7.dp),
                            vertical = screenHeightDp(4.dp)
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}