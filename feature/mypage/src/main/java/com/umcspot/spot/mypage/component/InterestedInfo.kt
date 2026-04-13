package com.umcspot.spot.mypage.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.component.button.BlankButton
import com.umcspot.spot.designsystem.component.button.ImageButtonState
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.G400
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun InterestedInfo(
    title: String,
    icon: Painter,
    interest: List<String?>,
    onClick: () -> Unit
) {
    val interestText = remember(interest) {
        interest.joinToString(separator = " ") { "#$it" }
    }

    BlankButton(
        modifier = Modifier
            .fillMaxWidth()
            .height(screenHeightDp(41.dp)),
        state = ImageButtonState.XOUTLINEB100State,
        shape = SpotShapes.Hard,
        align = Alignment.CenterStart,
        onClick = onClick
    ) {
        Row(modifier = Modifier.padding(screenWidthDp(10.dp))) {
            Image(
                painter = icon,
                contentDescription = null,
                modifier = Modifier.size(screenWidthDp(18.dp)),
                colorFilter = ColorFilter.tint(SpotTheme.colors.B500)
            )
            Spacer(modifier = Modifier.width(screenWidthDp(7.dp)))
            Text(text = title, style = SpotTheme.typography.medium_500)
            Spacer(modifier = Modifier.width(screenWidthDp(7.dp)))
            Text(
                text = interestText,
                modifier = Modifier.weight(1f),
                color = SpotTheme.colors.G400,
                maxLines = 1,
                style = SpotTheme.typography.medium_500,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}