package com.umcspot.spot.study.detail.component.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.theme.B100
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.ui.extension.noRippleClickable

@Composable
fun StudyDetailCreateButton(
    text: String,
    isStudyMember: Boolean,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true 
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(
                if (enabled) SpotTheme.colors.B100 else SpotTheme.colors.B100.copy(alpha = 0.5f)
            )
            .noRippleClickable {
                if (enabled) onButtonClick() 
            }
            .padding(
                start = 10.dp,
                end = if (isStudyMember) 4.dp else 10.dp,
                top = 3.5.dp,
                bottom = 3.5.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = text,
            style = SpotTheme.typography.h5,
            
            color = if (enabled) SpotTheme.colors.gray500 else SpotTheme.colors.gray400
        )

        if (isStudyMember) {
            Spacer(modifier = Modifier.width(4.dp))

            Image(
                painter = painterResource(id = R.drawable.ic_plus_study),
                contentDescription = null,
                alpha = if (enabled) 1.0f else 0.5f
            )
        }
    }
}