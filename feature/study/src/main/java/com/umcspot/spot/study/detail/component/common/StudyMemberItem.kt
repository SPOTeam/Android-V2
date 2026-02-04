package com.umcspot.spot.study.detail.component.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.ui.extension.noRippleClickable

@Composable
fun StudyMemberItem(
    name: String,
    profileUrl: String?,
    isLeader: Boolean = false,
    showLeaderIcon: Boolean = true,
    isSelected: Boolean = true,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .then(if (onClick != null) Modifier.noRippleClickable { onClick() } else Modifier)
            .graphicsLayer(alpha = if (isSelected) 1f else 0.5f)
    ) {
        Box(contentAlignment = Alignment.BottomEnd) {
            AsyncImage(
                model = profileUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(SpotTheme.colors.gray100),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.spot_logo),
                error = painterResource(id = R.drawable.spot_logo)
            )


            if (showLeaderIcon && isLeader) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_leader),
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = Color.Unspecified
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = name,
            style = SpotTheme.typography.small_500,
            color = if (isSelected) SpotTheme.colors.black else SpotTheme.colors.gray400
        )
    }
}