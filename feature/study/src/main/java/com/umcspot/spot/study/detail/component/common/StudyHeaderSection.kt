package com.umcspot.spot.study.detail.component.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.theme.G400
import com.umcspot.spot.designsystem.theme.R500
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.study.detail.mapper.formatCount
import com.umcspot.spot.study.detail.mapper.toCategoryString
import com.umcspot.spot.study.detail.model.StudyHomeState
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun StudyHeaderSection(
    homeState: StudyHomeState,
    onLikeClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = screenHeightDp(18.dp), horizontal = screenWidthDp(17.dp))
    ) {
        Text(text = homeState.studyTitle, style = SpotTheme.typography.h2)

        Spacer(modifier = Modifier.height(screenHeightDp(7.dp)))

        Row(horizontalArrangement = Arrangement.spacedBy(screenWidthDp(4.dp))) {
            StudyDetailInfoItem(
                iconRes = R.drawable.ic_member,
                text = "${homeState.currentMembers} / ${homeState.totalMembers}"
            )
            StudyDetailInfoItem(
                iconRes = R.drawable.ic_hit_count,
                text = homeState.hitCount.formatCount
            )
            StudyDetailInfoItem(
                iconRes = if (homeState.isLiked) R.drawable.ic_like_filled
                else R.drawable.ic_like_count,
                text = homeState.likeCount.formatCount,
                tint = if (homeState.isLiked) SpotTheme.colors.R500
                else SpotTheme.colors.G400,
                onClick = onLikeClick
            )
        }

        Spacer(modifier = Modifier.height(screenHeightDp(7.dp)))
        StudyDetailCategoryChip(text = homeState.categories.toCategoryString())
    }
}