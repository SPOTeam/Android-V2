package com.example.core.ui.component.study

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
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.data.study.StudyItem
import com.example.core.ui.R
import com.example.core.ui.shapes.SpotShapes
import com.example.core.ui.theme.G300
import com.example.core.ui.theme.SpotTypography


@Composable
fun StudyListItem(
    item: StudyItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Column(modifier = modifier) {
        Row(
            modifier = modifier.clickable(onClick = onClick),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            val imageRes = item.studyImage ?: R.drawable.spot_logo

            Image(
                painter = painterResource(imageRes),
                contentDescription = null,
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
                    style = SpotTypography.bodyMedium500.copy(fontSize = 16.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = item.goal,
                    style = SpotTypography.bodySmall500.copy(fontSize = 14.sp),
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
            color = G300,
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

        Text(text = display, style = SpotTypography.bodySmall500.copy(fontSize = 12.sp))
    }
}

/* ============== Preview ============== */

@Preview(showBackground = true, widthDp = 300)
@Composable
private fun StudyListItemPreview() {
    StudyListItem(
        item = StudyItem(
            id = "1",
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
