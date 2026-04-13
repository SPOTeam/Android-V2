package com.umcspot.spot.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.button.BlankButton
import com.umcspot.spot.designsystem.theme.Black
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.model.QuickMenuType
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun QuickMenu(
    items: List<QuickMenuType>,
    modifier: Modifier = Modifier,
    onItemClick: (QuickMenuType) -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { item ->
            BlankButton(
                modifier = Modifier.size(screenWidthDp(71.dp)),
                onClick = { onItemClick(item) }
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box {
                        Image(
                            painter = getIconForType(item),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(
                                SpotTheme.colors.black.copy(alpha = 0.15f)
                            ),
                            modifier = Modifier
                                .size(screenWidthDp(31.dp))
                                .graphicsLayer {
                                    translationY = 1.dp.toPx()
                                    renderEffect = BlurEffect(9f, 9f)
                                    clip = false
                                }
                        )
                        Image(
                            painter = getIconForType(item),
                            contentDescription = null,
                            modifier = Modifier.size(screenWidthDp(31.dp))
                        )
                    }
                    Spacer(Modifier.height(screenHeightDp(7.dp)))
                    Text(
                        text = item.label,
                        style = SpotTheme.typography.small_500,
                        color = Black,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
private fun getIconForType(type: QuickMenuType): Painter = when (type) {
    QuickMenuType.REGION -> painterResource(R.drawable.prefer_location)
    QuickMenuType.INTERESTS -> painterResource(R.drawable.heart_clear)
    QuickMenuType.RECRUITING -> painterResource(R.drawable.recruiting)
    QuickMenuType.BOARD -> painterResource(R.drawable.bulletin_board)
}