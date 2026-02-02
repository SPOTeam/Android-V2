package com.umcspot.spot.study.detail.component.memoir

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.theme.G300
import com.umcspot.spot.designsystem.theme.G400
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.ui.extension.noRippleClickable
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun MemoirImageSection(
    images: List<Uri>,
    onAddClick: () -> Unit,
    onRemoveClick: (Int) -> Unit
) {
    val isFull = images.size >= 3

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .noRippleClickable { if (!isFull) onAddClick() }
                .padding(vertical = screenHeightDp(2.dp)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.camera),
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = if (isFull) SpotTheme.colors.G300 else Color.Unspecified
            )
            Spacer(modifier = Modifier.width(screenWidthDp(4.dp)))
            Text(
                text = "사진 추가 (최대 3장)",
                style = SpotTheme.typography.regular_500,
                color = if (isFull) SpotTheme.colors.G400 else SpotTheme.colors.black
            )
        }

        if (images.isNotEmpty()) {
            Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(screenWidthDp(8.dp)),
                contentPadding = PaddingValues(end = screenWidthDp(17.dp))
            ) {
                itemsIndexed(images) { index, uri ->
                    Box(modifier = Modifier.size(80.dp)) {
                        AsyncImage(
                            model = uri,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(6.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.dismiss),
                            contentDescription = null,
                            tint = SpotTheme.colors.white,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(6.dp)
                                .noRippleClickable { onRemoveClick(index) }
                        )
                    }
                }
            }
        }
    }
}