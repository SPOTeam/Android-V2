package com.umcspot.spot.study.detail.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.study.detail.component.memoir.StudyDetailMemoirItem
import com.umcspot.spot.study.detail.component.planner.StudyDetailScheduleItem
import com.umcspot.spot.study.detail.component.common.StudyMemberItem
import com.umcspot.spot.study.detail.mapper.toUiTime
import com.umcspot.spot.study.model.StudyMemberModel
import com.umcspot.spot.study.model.StudyRecentMemoirModel
import com.umcspot.spot.study.model.StudyScheduleModel
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp
import kotlinx.collections.immutable.ImmutableList

@Composable
fun StudyDetailHomeScreen(
    description: String,
    members: ImmutableList<StudyMemberModel>,
    schedules: ImmutableList<StudyScheduleModel>,
    recentMemoirs: ImmutableList<StudyRecentMemoirModel>,
    isMember: Boolean,
    onAttendanceClick: (Long, Boolean) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, SpotTheme.colors.gray200, RoundedCornerShape(6.dp))
                .padding(horizontal = screenWidthDp(9.dp), vertical = screenHeightDp(7.dp))
        ) {
            Text(
                text = description,
                style = SpotTheme.typography.h5,
                color = SpotTheme.colors.black
            )
        }

        Spacer(modifier = Modifier.height(screenHeightDp(32.dp)))

        Text(text = "멤버", style = SpotTheme.typography.h4, color = SpotTheme.colors.black)
        Spacer(modifier = Modifier.height(screenHeightDp(8.dp)))
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(screenWidthDp(13.dp))
        ) {
            items(items = members, key = { it.id }) { member ->
                StudyMemberItem(
                    name = member.name,
                    profileUrl = member.profileUrl,
                    isLeader = member.isLeader
                )
            }
        }

        Spacer(modifier = Modifier.height(screenHeightDp(32.dp)))

        Text(text = "다가오는 일정", style = SpotTheme.typography.h4, color = SpotTheme.colors.black)
        Spacer(modifier = Modifier.height(screenHeightDp(8.dp)))

        if (schedules.isEmpty()) {
            Text(
                text = "일정이 없습니다.",
                style = SpotTheme.typography.medium_400,
                color = SpotTheme.colors.gray400,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = screenHeightDp(20.dp)),
                textAlign = TextAlign.Center
            )
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(screenHeightDp(16.dp))) {
                schedules.forEach { schedule ->
                    val timeRange = "${schedule.startAt.toUiTime()} - ${schedule.endAt.toUiTime()}"
                    StudyDetailScheduleItem(
                        scheduleId = schedule.id,
                        title = schedule.title,
                        timeRange = timeRange,
                        isNow = schedule.isNow,
                        isHost = schedule.isMine,
                        onScheduleClick = { id ->
                            if (isMember) onAttendanceClick(id, schedule.isNow)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(screenHeightDp(32.dp)))

        Text(text = "최근 회고록", style = SpotTheme.typography.h4, color = SpotTheme.colors.black)
        Spacer(modifier = Modifier.height(screenHeightDp(8.dp)))

        if (recentMemoirs.isEmpty()) {
            Text(
                text = "회고록이 없습니다.",
                style = SpotTheme.typography.medium_400,
                color = SpotTheme.colors.gray400,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = screenHeightDp(20.dp)),
                textAlign = TextAlign.Center
            )
        } else {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(screenWidthDp(8.dp))
            ) {
                items(items = recentMemoirs, key = { it.id }) { memoir ->
                    StudyDetailMemoirItem(
                        thumbnailUrl = memoir.thumbnailUrl,
                        description = if (memoir.isPrivate) "이 글은 스터디원에게만 노출됩니다." else memoir.activityContent,
                        writerName = memoir.writerNickname,
                        authorProfileUrl = memoir.writerProfileUrl
                    )
                }
            }
        }
    }
}