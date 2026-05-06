package com.umcspot.spot.study.detail.component.memoir

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import coil.compose.AsyncImage
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.study.detail.component.common.DeleteMenuPopup
import com.umcspot.spot.ui.extension.noRippleClickable
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun MemoirHeader(
    profileImageUrl: String?,
    nickname: String,
    isMyMemoir: Boolean,
    isDeleteMenuVisible: Boolean,
    onDeleteMenuToggle: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(screenWidthDp(33.dp))
                    .clip(CircleShape)
                    .background(SpotTheme.colors.gray300)
            ) {
                AsyncImage(
                    model = profileImageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.width(screenWidthDp(7.dp)))
            Text(
                text = nickname,
                style = SpotTheme.typography.medium_400,
                color = SpotTheme.colors.black
            )
        }
        if (isMyMemoir) {
            Box {
                Icon(
                    painter = painterResource(id = R.drawable.ic_meetball),
                    contentDescription = null,
                    modifier = Modifier
                        .size(screenWidthDp(20.dp))
                        .noRippleClickable { onDeleteMenuToggle() }
                )
                if (isDeleteMenuVisible) {
                    Popup(
                        alignment = Alignment.TopEnd,
                        offset = IntOffset(0, 70),
                        onDismissRequest = { onDeleteMenuToggle() }
                    ) {
                        DeleteMenuPopup(onDelete = onDeleteClick)
                    }
                }
            }
        }
    }
}