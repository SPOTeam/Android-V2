package com.umcspot.spot.designsystem.component.study

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.G300
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.model.ImageRef
import com.umcspot.spot.study.model.StudyResult

@Composable
fun StudyListItem(
    item: StudyResult,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Column(modifier = modifier) {
        Row(
            modifier = modifier.clickable(onClick = onClick),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            StudyThumbnail(
                imageRef = item.studyImage,
                modifier = Modifier
                    .size(56.dp)
                    .clip(SpotShapes.Hard)
            )

            // 텍스트 + 통계
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = item.title,
                    style = SpotTheme.typography.medium_500,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = item.goal,
                    style = SpotTheme.typography.medium_500,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Stat(
                        iconRes = R.drawable.group, count1 = item.member, count2 = item.maxMember
                    )
                    Stat(
                        iconRes = R.drawable.like_default, count2 = item.likes
                    )
                    Stat(
                        iconRes = R.drawable.eye, count2 = item.views
                    )
                }
            }
        }
        Spacer(Modifier.padding(5.dp))

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth(),
            color = SpotTheme.colors.G300,
            thickness = 0.5.dp
        )
    }
}

@Composable
private fun Stat(
    @DrawableRes iconRes: Int,
    count1: Int = 0,
    count2: Int
) {
    fun cap(n: Int) = if (n >= 1000) "999+" else n.toString()
    val display = if (count1 != 0) "${cap(count1)}/${cap(count2)}" else cap(count2)

    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(14.dp)
        )

        Text(text = display, style = SpotTheme.typography.medium_500)
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
        is ImageRef.LocalName -> {
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

        is ImageRef.LocalPath -> {
            val id = ctx.resources.getIdentifier(img.path, "drawable", ctx.packageName)
            val safeId = if (id != 0) id else placeholder
            Image(
                painter = painterResource(safeId),
                contentDescription = null,
                modifier = modifier
            )
        }
    }
}

/* ============== Preview ============== */

@Preview(showBackground = true, widthDp = 300)
@Composable
private fun StudyListItemPreview() {
    SpotTheme{
        StudyListItem(
            item = StudyResult(
                studyId = "1",
                title = "Sample Study",
                goal = "Sample Goal",
                maxMember = 10,
                member = 5,
                likes = 400,
                views = 1200,
            ),
            modifier = Modifier.padding(10.dp),
            onClick = {}
        )
    }
}

