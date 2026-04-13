package com.umcspot.spot.category.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.G300
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.model.RecruitingStudySort
import com.umcspot.spot.ui.extension.noRippleClickable
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortTypeBottomSheet(
    visible: Boolean,
    current: RecruitingStudySort?,
    onSelect: (RecruitingStudySort) -> Unit,
    onDismiss: () -> Unit
) {
    if (!visible) return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        modifier = Modifier.fillMaxWidth(),
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = SpotShapes.RoundTop,
        containerColor = SpotTheme.colors.white,
        dragHandle = {},
        contentWindowInsets = { WindowInsets(0) },
        tonalElevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(vertical = screenHeightDp(14.dp))
        ) {
            RecruitingStudySort.entries.forEachIndexed { index, option ->
                ListItem(
                    colors = ListItemDefaults.colors(
                        containerColor = SpotTheme.colors.white,
                        headlineColor = SpotTheme.colors.black,
                        trailingIconColor = SpotTheme.colors.B500
                    ),
                    headlineContent = {
                        Text(
                            text = option.label,
                            color = SpotTheme.colors.black,
                            style = SpotTheme.typography.medium_400
                        )
                    },
                    trailingContent = {
                        if (option == current) {
                            Icon(
                                painter = painterResource(R.drawable.success_default),
                                tint = SpotTheme.colors.B500,
                                modifier = Modifier.size(screenWidthDp(14.dp)),
                                contentDescription = "선택됨"
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .noRippleClickable{
                            onSelect(option)
                            onDismiss()
                        }
                        .padding(horizontal = screenWidthDp(17.dp))
                )
                if (index != RecruitingStudySort.entries.lastIndex) {
                    HorizontalDivider(
                        color = SpotTheme.colors.G300,
                        thickness = 1.dp
                    )
                }
            }
        }
    }
}