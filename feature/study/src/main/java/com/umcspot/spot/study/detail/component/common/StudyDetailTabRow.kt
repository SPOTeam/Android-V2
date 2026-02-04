package com.umcspot.spot.study.detail.component.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.study.detail.model.StudyDetailTab
import com.umcspot.spot.ui.extension.noRippleClickable
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun StudyDetailTabRow(
    selectedTab: StudyDetailTab,
    onTabSelected: (StudyDetailTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = StudyDetailTab.entries

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(
                screenWidthDp(42.dp),
                Alignment.CenterHorizontally
            )
        ) {
            tabs.forEach { tab ->
                val isSelected = selectedTab == tab

                Column(
                    modifier = Modifier
                        .width(screenWidthDp(50.dp))
                        .noRippleClickable { onTabSelected(tab) },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier.height(29.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tab.title,
                            style = SpotTheme.typography.h5,
                            color = if (isSelected) SpotTheme.colors.black else SpotTheme.colors.gray400
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(screenWidthDp(50.dp))
                            .height(screenHeightDp(1.dp))
                            .background(if (isSelected) SpotTheme.colors.B500 else Color.Transparent)
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeightDp(1.dp))
                .background(SpotTheme.colors.gray200)
        )
    }
}