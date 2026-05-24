package com.umcspot.spot.mypage.leave

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.appBar.BackTopBar
import com.umcspot.spot.designsystem.component.button.SpotActivationButton
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.Black
import com.umcspot.spot.designsystem.theme.G100
import com.umcspot.spot.designsystem.theme.G200
import com.umcspot.spot.designsystem.theme.G300
import com.umcspot.spot.designsystem.theme.G400
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.study.component.LeaveStudyDialog
import com.umcspot.spot.study.component.SpotStudyDialog
import com.umcspot.spot.study.component.SpotStudyDialogIcon
import com.umcspot.spot.study.detail.component.common.StudyMemberItem
import com.umcspot.spot.study.model.LeaveReason
import com.umcspot.spot.study.model.StudyMemberModel
import com.umcspot.spot.ui.extension.noRippleClickable
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun LeaveStudyRoute(
    studyId: Long,
    isOwner: Boolean,
    studyName: String,
    studyDescription: String,
    profileImageUrl: String?,
    contentPadding: PaddingValues,
    onBackClick: () -> Unit,
    onLeaveSuccess: () -> Unit,
    viewModel: LeaveStudyViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(studyId) {
        if (isOwner) viewModel.loadMembers(studyId)
    }

    if (uiState.isLeaveSuccess) {
        SpotStudyDialog(
            onDismissRequest = { viewModel.dismissLeaveSuccess(); onLeaveSuccess() },
            title = "나가기 완료",
            description = "",
            buttonText = "확인",
            icon = SpotStudyDialogIcon.CHECK,
            onButtonClick = { viewModel.dismissLeaveSuccess(); onLeaveSuccess() }
        )
    }

    LeaveStudyScreen(
        isOwner = isOwner,
        studyName = studyName,
        studyDescription = studyDescription,
        profileImageUrl = profileImageUrl,
        members = uiState.members,
        contentPadding = contentPadding,
        onBackClick = onBackClick,
        onLeaveClick = { delegateId, reason ->
            viewModel.withdrawStudy(
                studyId = studyId,
                reason = reason,
                nextOwnerId = delegateId
            )
        }
    )
}

@Composable
private fun LeaveStudyScreen(
    isOwner: Boolean,
    studyName: String,
    studyDescription: String,
    profileImageUrl: String?,
    members: List<StudyMemberModel>,
    contentPadding: PaddingValues,
    onBackClick: () -> Unit,
    onLeaveClick: (delegateId: Long?, reason: String) -> Unit
) {
    val scrollState = rememberScrollState()
    var selectedMemberId by remember { mutableStateOf<Long?>(null) }
    var selectedReason by remember { mutableStateOf<LeaveReason?>(null) }
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var isLeaveDialogVisible by remember { mutableStateOf(false) }

    val isValid = if (isOwner) {
        selectedMemberId != null && selectedReason != null
    } else {
        selectedReason != null
    }

    LeaveStudyDialog(
        visible = isLeaveDialogVisible,
        onDismissRequest = { isLeaveDialogVisible = false },
        onButtonClick = {
            isLeaveDialogVisible = false
            onLeaveClick(selectedMemberId, selectedReason?.apiValue ?: "")
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SpotTheme.colors.white)
    ) {
        Spacer(modifier = Modifier.height(contentPadding.calculateTopPadding()))

        BackTopBar(
            title = "스터디 나가기",
            onBackClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = screenWidthDp(17.dp))
        ) {
            Spacer(modifier = Modifier.height(screenHeightDp(18.dp)))

            Text(
                text = "나가기 전, 스터디 확인",
                style = SpotTheme.typography.h5,
                color = SpotTheme.colors.black
            )

            Spacer(modifier = Modifier.height(screenHeightDp(12.dp)))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, SpotTheme.colors.G200, RoundedCornerShape(10.dp))
                    .background(SpotTheme.colors.white, RoundedCornerShape(10.dp))
                    .padding(screenWidthDp(7.dp)),
                horizontalArrangement = Arrangement.spacedBy(screenWidthDp(13.dp)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = profileImageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(screenWidthDp(47.dp))
                        .clip(RoundedCornerShape(6.dp))
                        .background(SpotTheme.colors.G100),
                    contentScale = ContentScale.Crop,
                    error = painterResource(R.drawable.spot_logo)
                )

                Column {
                    Text(
                        text = studyName,
                        style = SpotTheme.typography.h5,
                        color = SpotTheme.colors.Black
                    )
                    Text(
                        text = studyDescription,
                        style = SpotTheme.typography.regular_400,
                        color = SpotTheme.colors.Black,
                        maxLines = 1
                    )
                }
            }

            Spacer(modifier = Modifier.height(screenHeightDp(32.dp)))

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                color = SpotTheme.colors.G300,
                thickness = 0.5.dp
            )

            Spacer(modifier = Modifier.height(screenHeightDp(32.dp)))

            if (isOwner) {
                Text(
                    text = "호스트 위임 (필수)",
                    style = SpotTheme.typography.h5,
                    color = SpotTheme.colors.black
                )

                Spacer(modifier = Modifier.height(screenHeightDp(3.dp)))

                Text(
                    text = "스터디를 나가기 전,\n다음 호스트를 맡을 스터디원을 선택해 주세요.",
                    style = SpotTheme.typography.regular_500,
                    color = SpotTheme.colors.black
                )

                Spacer(modifier = Modifier.height(screenHeightDp(12.dp)))

                val nonLeaderMembers = members.filter { !it.isLeader }

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(screenWidthDp(20.dp))
                ) {
                    items(items = nonLeaderMembers, key = { it.id }) { member ->
                        val isSelected = selectedMemberId == member.id
                        StudyMemberItem(
                            name = member.name,
                            profileUrl = member.profileUrl,
                            isLeader = member.isLeader,
                            showLeaderIcon = true,
                            isSelected = isSelected,
                            iconSize = screenWidthDp(33.dp),
                            selectedBorderColor = SpotTheme.colors.B500,
                            selectedTextColor = SpotTheme.colors.B500,
                            unselectedTextColor = SpotTheme.colors.G400,
                            onClick = { selectedMemberId = member.id }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(screenHeightDp(32.dp)))

                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = SpotTheme.colors.G300,
                    thickness = 0.5.dp
                )

                Spacer(modifier = Modifier.height(screenHeightDp(32.dp)))
            }

            Text(
                text = "스터디를 나오는 이유를 선택해주세요.",
                style = SpotTheme.typography.regular_500,
                color = SpotTheme.colors.black
            )

            Spacer(modifier = Modifier.height(screenHeightDp(16.dp)))

            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.dp,
                            SpotTheme.colors.G200,
                            if (isDropdownExpanded) RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                            else RoundedCornerShape(8.dp)
                        )
                        .padding(
                            horizontal = screenWidthDp(10.dp),
                            vertical = screenHeightDp(6.dp)
                        )
                        .noRippleClickable { isDropdownExpanded = !isDropdownExpanded },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedReason?.label ?: "이유 선택",
                        style = SpotTheme.typography.regular_500,
                        color = if (selectedReason != null) SpotTheme.colors.Black else SpotTheme.colors.G400
                    )
                    Icon(
                        painter = painterResource(
                            if (isDropdownExpanded) R.drawable.arrow_up else R.drawable.arrow_down
                        ),
                        contentDescription = null,
                        tint = SpotTheme.colors.B500,
                        modifier = Modifier.size(screenWidthDp(14.dp))
                    )
                }

                if (isDropdownExpanded) {
                    Spacer(modifier = Modifier.height(screenHeightDp(3.dp)))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                1.dp,
                                SpotTheme.colors.G200,
                                RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)
                            )
                    ) {
                        LeaveReason.entries.forEachIndexed { index, reason ->
                            Text(
                                text = reason.label,
                                style = SpotTheme.typography.regular_500,
                                color = SpotTheme.colors.Black,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .noRippleClickable {
                                        selectedReason = reason
                                        isDropdownExpanded = false
                                    }
                                    .padding(
                                        horizontal = screenWidthDp(10.dp),
                                        vertical = screenHeightDp(6.dp)
                                    )
                            )
                            if (index < LeaveReason.entries.lastIndex) {
                                HorizontalDivider(
                                    color = SpotTheme.colors.G300,
                                    thickness = 0.5.dp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))
        }

        SpotActivationButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = screenWidthDp(17.dp))
                .padding(bottom = contentPadding.calculateBottomPadding() + screenHeightDp(13.dp)),
            buttonText = "스터디 나가기",
            isEnabled = isValid,
            onClick = { isLeaveDialogVisible = true },
            style = SpotTheme.typography.h5
        )
    }
}