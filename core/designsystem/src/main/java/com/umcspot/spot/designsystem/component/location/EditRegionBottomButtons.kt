package com.umcspot.spot.designsystem.component.location

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.component.button.TextButton
import com.umcspot.spot.designsystem.component.button.TextButtonState
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun EditRegionBottomButtons(
    editMode: Boolean,
    isSearching: Boolean,
    regionCount: Int,
    onEditClick: () -> Unit,
    onAddClick: () -> Unit,
    onCompleteClick: () -> Unit
) {
    val isMax = regionCount >= 10

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = screenHeightDp(30.dp)),
        horizontalArrangement = Arrangement.spacedBy(screenWidthDp(14.dp))
    ) {
        if (!editMode && regionCount > 0) {
            TextButton(
                modifier = Modifier.fillMaxWidth().height(screenHeightDp(47.dp)),
                text = "수정",
                state = TextButtonState.B500State,
                shape = SpotShapes.Soft,
                onClick = onEditClick
            )
        } else if (isSearching) {
            TextButton(
                modifier = Modifier.fillMaxWidth().height(screenHeightDp(47.dp)),
                text = "완료",
                state = TextButtonState.B500State,
                shape = SpotShapes.Soft,
                onClick = onCompleteClick
            )
        } else {

            TextButton(
                modifier = Modifier.weight(1f).height(screenHeightDp(47.dp)),
                text = "추가",
                state = TextButtonState.B500State,
                enabled = !isMax,
                shape = SpotShapes.Soft,
                onClick = onAddClick
            )

            if (regionCount > 0) {
                TextButton(
                    modifier = Modifier.weight(1f).height(screenHeightDp(47.dp)),
                    text = "완료",
                    state = TextButtonState.B500State,
                    shape = SpotShapes.Soft,
                    onClick = onCompleteClick
                )
            }
        }
    }
}