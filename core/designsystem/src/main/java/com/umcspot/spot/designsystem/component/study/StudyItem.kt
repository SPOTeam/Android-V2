package com.umcspot.spot.designsystem.component.study

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.button.ClickSurface
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.model.ImageRef
import com.umcspot.spot.study.model.StudyResult
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun StudyListItem(
    item: StudyResult,
    modifier: Modifier = Modifier,
    onClick: (StudyResult) -> Unit = {},
    meetballSlot: (@Composable () -> Unit)? = null,
    checkAppliedSlot: (@Composable () -> Unit)? = null
) {
    ClickSurface(
        onClick = { onClick(item) },
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(screenWidthDp(7.dp)),
            horizontalArrangement = Arrangement.spacedBy(13.dp),
            verticalAlignment = Alignment.Top
        ) {
            StudyThumbnail(
                imageRef = item.profileImageUrl,
                modifier = Modifier
                    .size(screenWidthDp(73.dp))
                    .clip(SpotShapes.Hard)
            )

            // 텍스트 + 통계
            Column(
                modifier = Modifier
                    .wrapContentWidth()
                    .height(screenHeightDp(73.dp))
                    .padding(screenHeightDp(4.dp))
            ) {
                Row{
                    Column{
                        Text(
                            text = item.name,
                            style = SpotTheme.typography.h5,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = item.description,
                            style = SpotTheme.typography.regular_400,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    if (checkAppliedSlot != null) {
                        Spacer(modifier = Modifier.weight(1f))

                        checkAppliedSlot()
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(horizontalArrangement = Arrangement.spacedBy(screenWidthDp(4.dp))) {
                    Stat(
                        iconRes = R.drawable.group,
                        count1 = item.currentMembers,
                        count2 = item.maxMembers
                    )
                    Stat(
                        iconRes = R.drawable.eye, count2 = item.hitCount
                    )
                    Stat(
                        iconRes = R.drawable.like_default, count2 = item.likeCount
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))

            if (meetballSlot != null) {
                meetballSlot()
            }
        }
    }
}

@Composable
private fun Stat(
    @DrawableRes iconRes: Int,
    count1: Int = 0,
    count2: Int
) {
    fun cap(n: Int) = if (n >= 1000) "999+" else n.toString()
    val display = if (count1 != 0) "${cap(count1)} / ${cap(count2)}" else cap(count2)

    Row(
        modifier = Modifier
            .width(screenWidthDp(56.dp))
            .height(screenHeightDp(17.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(screenWidthDp(4.dp))
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(screenWidthDp(14.dp))
        )

        Text(text = display, style = SpotTheme.typography.small_400)
    }
}

@Composable
fun StudyThumbnail(
    imageRef: ImageRef?,
    @DrawableRes placeholder: Int = R.drawable.spot_logo, // 적절한 플레이스홀더
    modifier: Modifier = Modifier
) {
    val ctx = LocalContext.current
    val context = LocalContext.current

    when (val img = imageRef) {
        is ImageRef.Name -> {
            val id = context.resources.getIdentifier(img.name, "drawable", context.packageName)
            val safeId = if (id != 0) id else placeholder
            Image(
                painter = painterResource(safeId),
                contentDescription = null,
                modifier = modifier
            )
        }

        is ImageRef.Url -> {
            AsyncImage(
                model = img.url,
                contentDescription = null,
                placeholder = painterResource(placeholder),
                error = painterResource(placeholder),
                modifier = modifier
            )
        }

        ImageRef.None, null -> {
            Image(
                painter = painterResource(placeholder),
                contentDescription = null,
                modifier = modifier
            )
        }

        is ImageRef.LocalUri -> {
            AsyncImage(
                model = img.uri,
                contentDescription = null,
                placeholder = painterResource(placeholder),
                error = painterResource(placeholder),
                modifier = modifier
            )

        }
    }
}

/* ============== Preview ============== */

@Preview(showBackground = true, widthDp = 326)
@Composable
private fun StudyListItemPreview() {
    SpotTheme {
        StudyListItem(
            item = StudyResult(
                id = 1,
                name = "Sample Study",
                description = "Sample Goal",
                maxMembers = 10,
                currentMembers = 5,
                likeCount = 400,
                isLiked = false,
                hitCount = 1200,
                profileImageUrl = ImageRef.Name("spot_logo"),
                isOwner = false,
                isAlone = false
            ),
            modifier = Modifier.padding(10.dp),
            onClick = {},
        )
    }
}

