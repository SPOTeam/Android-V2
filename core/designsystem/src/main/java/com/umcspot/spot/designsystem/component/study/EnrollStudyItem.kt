package com.umcspot.spot.designsystem.component.study

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.button.ButtonState
import com.umcspot.spot.designsystem.component.button.TextButtonM
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.G300
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.study.model.StudyResult

@Composable
fun EnrollStudyListItem(
    item: StudyResult,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Column(modifier = modifier) {
        Row(
            modifier = modifier.clickable(onClick = onClick),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {

            val imageRes = item.studyImage ?: R.drawable.spot_logo

            Image(
                painter = painterResource(imageRes as Int),
                contentDescription = null,
                modifier = Modifier
                    .size(56.dp)
                    .clip(SpotShapes.Hard),

            )

            // 텍스트 + 통계
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = item.title,
                    style = SpotTheme.typography.bodyMedium500.copy(fontSize = 16.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "호스트가 스터디 참여를 수락했어요.\n스터디 참여 여부를 최종 픽스해주세요.",
                    style = SpotTheme.typography.bodySmall500.copy(fontSize = 12.sp)
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButtonM(
                        text = "불참",
                        onClick = {},
                        state = ButtonState.R500State,
                        modifier = Modifier.weight(1f)
                    )

                    TextButtonM(
                        text = "참여",
                        onClick = {},
                        state = ButtonState.B400State,
                        modifier = Modifier.weight(1f)
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

/* ============== Preview ============== */

@Preview(showBackground = true, widthDp = 300)
@Composable
private fun EnrollStudyListItemPreview() {
    EnrollStudyListItem(
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
