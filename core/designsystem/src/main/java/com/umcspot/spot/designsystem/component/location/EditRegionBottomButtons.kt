package com.umcspot.spot.designsystem.component.location

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.component.button.SpotActivationButton
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
        when {
            !editMode && regionCount > 0 -> {
                SpotActivationButton(
                    modifier = Modifier.fillMaxWidth(),
                    buttonText = "수정",
                    isEnabled = true,
                    onClick = onEditClick
                )
            }

            isSearching -> {
                SpotActivationButton(
                    modifier = Modifier.fillMaxWidth(),
                    buttonText = "완료",
                    isEnabled = true,
                    onClick = onCompleteClick
                )
            }

            else -> {
                SpotActivationButton(
                    modifier = Modifier.weight(1f),
                    buttonText = "추가",
                    isEnabled = !isMax,
                    onClick = onAddClick
                )

                if (regionCount > 0) {
                    SpotActivationButton(
                        modifier = Modifier.weight(1f),
                        buttonText = "완료",
                        isEnabled = true,
                        onClick = onCompleteClick
                    )
                }
            }
        }
    }
}