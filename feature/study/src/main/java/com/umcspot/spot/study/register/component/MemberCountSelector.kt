package com.umcspot.spot.study.register.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.ui.extension.noRippleClickable
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp
import kotlinx.collections.immutable.persistentListOf

@Composable
fun MemberCountSelector(
    memberCount: Int,
    onMemberCountChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val memberOptions = remember { persistentListOf(2, 3, 4, 5, 6, 7, 8, 9, 10) }
    val itemHeight = screenHeightDp(30.dp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = screenHeightDp(4.5.dp)),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier.height(itemHeight),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "총 인원 (팀장 포함)",
                style = SpotTheme.typography.h5
            )
        }

        Row(verticalAlignment = Alignment.Top) {
            Column(
                modifier = Modifier.width(screenWidthDp(71.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(itemHeight)
                        .border(
                            width = 1.dp,
                            color = SpotTheme.colors.gray200,
                            shape = RoundedCornerShape(6.dp)
                        )
                        .clip(RoundedCornerShape(6.dp))
                        .noRippleClickable { expanded = !expanded }
                        .padding(horizontal = screenWidthDp(10.dp)),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = memberCount.toString(),
                        style = SpotTheme.typography.regular_500,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Start
                    )
                    Icon(
                        painter = painterResource(
                            id = if (expanded) R.drawable.arrow_up else R.drawable.arrow_down
                        ),
                        contentDescription = null,
                        tint = SpotTheme.colors.B500,
                        modifier = Modifier.size(screenWidthDp(14.dp))
                    )
                }

                if (expanded) {
                    Spacer(modifier = Modifier.height(screenHeightDp(4.dp)))

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(itemHeight * 5)
                            .background(
                                color = SpotTheme.colors.white,
                                shape = RoundedCornerShape(6.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = SpotTheme.colors.gray200,
                                shape = RoundedCornerShape(6.dp)
                            )
                            .clip(RoundedCornerShape(6.dp))
                    ) {
                        itemsIndexed(memberOptions) { index, selectionOption ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(itemHeight)
                                    .noRippleClickable {
                                        onMemberCountChange(selectionOption)
                                        expanded = false
                                    }
                                    .padding(horizontal = screenWidthDp(10.dp)),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(
                                    text = selectionOption.toString(),
                                    style = SpotTheme.typography.regular_500
                                )
                            }
                            if (index < memberOptions.lastIndex) {
                                HorizontalDivider(
                                    color = SpotTheme.colors.gray300,
                                    thickness = 0.5.dp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(screenWidthDp(7.dp)))

            Box(
                modifier = Modifier.height(itemHeight),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "명",
                    style = SpotTheme.typography.regular_500
                )
            }
        }
    }
}