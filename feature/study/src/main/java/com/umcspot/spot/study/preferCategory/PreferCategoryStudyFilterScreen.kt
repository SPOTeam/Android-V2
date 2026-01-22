package com.umcspot.spot.study.preferCategory

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.button.MultiButton
import com.umcspot.spot.designsystem.component.button.TextButton
import com.umcspot.spot.designsystem.component.button.TextButtonState
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.model.ActivityType
import com.umcspot.spot.model.FeeRange
import com.umcspot.spot.model.RecruitingStatus
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun PreferCategoryStudyFilterScreen(
    contentPadding: PaddingValues,
    onAcceptFilterClick: () -> Unit,
    categoryVm: PreferCategoryStudyViewModel = hiltViewModel(),
) {
    val recruitingStatus by categoryVm.recruitingStatus.collectAsStateWithLifecycle()
    val fee by categoryVm.feeRange.collectAsStateWithLifecycle()
    val activity by categoryVm.activity.collectAsStateWithLifecycle()

    var draftRecruitingStatus by rememberSaveable { mutableStateOf(recruitingStatus) }
    var draftFee by rememberSaveable { mutableStateOf(fee) }
    var draftActivity by rememberSaveable { mutableStateOf(activity) }

    val acceptEnabled = true

    val topPad = contentPadding.calculateTopPadding()
    val bottomPad = contentPadding.calculateBottomPadding()

    BackHandler {
        onAcceptFilterClick()
    }

    PreferCategoryStudyFilterScreenContent(
        modifier = Modifier
            .background(SpotTheme.colors.white)
            .padding(top = topPad, bottom = bottomPad),
        recruitingStatus = draftRecruitingStatus,
        fee = draftFee,
        activity = draftActivity,
        buttonEnabled = acceptEnabled,
        onToggleRecruitingStatus = { type ->
            draftRecruitingStatus = if (draftRecruitingStatus == type) null else type
        },
        onToggleFee = { fee -> draftFee = if (draftFee == fee) null else fee },
        onToggleActivity = { activity -> draftActivity = if (draftActivity == activity) null else activity },
        onReset = {
            draftRecruitingStatus = null
            draftFee = null
            draftActivity = null
        },
        onApply = {
            categoryVm.applyFilter(
                fee = draftFee,
                recruitingStatus = draftRecruitingStatus,
                activityType = draftActivity
            )
            onAcceptFilterClick()
        }
    )
}

@Composable
fun PreferCategoryStudyFilterScreenContent(
    recruitingStatus: RecruitingStatus?,
    fee: FeeRange?,
    activity: ActivityType?,
    buttonEnabled: Boolean,
    onToggleRecruitingStatus: (RecruitingStatus) -> Unit,
    onToggleFee: (FeeRange) -> Unit,
    onToggleActivity: (ActivityType) -> Unit,
    onReset: () -> Unit,
    onApply: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeightDp(25.dp))
                    .background(SpotTheme.colors.gray100),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "검색 결과는 모든 관심사에 공통 반영됩니다.",
                    style = SpotTheme.typography.small_500,
                    color = SpotTheme.colors.black
                )
            }

            Column(
                modifier = Modifier
                    .padding(top = screenHeightDp(18.dp))
                    .padding(horizontal = screenWidthDp(17.dp))
            ) {
                Text(
                    text = "모집 상태",
                    style = SpotTheme.typography.h5,
                    color = SpotTheme.colors.black
                )

                Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))

                RecruitingStatusMultiSection(
                    recruitingStatus = recruitingStatus,
                    onSelect = onToggleRecruitingStatus
                )

                Spacer(modifier = Modifier.height(screenHeightDp(53.dp)))

                Text(
                    text = "활동",
                    style = SpotTheme.typography.h5,
                    color = SpotTheme.colors.black
                )

                Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))


                ActivityTypeMultiSection(
                    selectedTypes = activity,
                    onToggle = onToggleActivity
                )

                Spacer(modifier = Modifier.height(screenHeightDp(53.dp)))

                Text(
                    text = "활동비",
                    style = SpotTheme.typography.h5,
                    color = SpotTheme.colors.black
                )

                Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))

                ActivityFeeSection(
                    activityFee = fee,
                    onSelect = onToggleFee
                )

                Spacer(modifier = Modifier.height(screenHeightDp(33.dp)))

                ResetFilterText(
                    onClick = onReset
                )

                Spacer(Modifier.height(80.dp))
            }
        }

        Box(
            modifier = Modifier
                .align(alignment = Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = screenWidthDp(16.dp), vertical = screenHeightDp(12.dp))
                .zIndex(1f)
        ) {
            TextButton(
                text = "검색 결과 보기",
                modifier = Modifier
                    .width(screenWidthDp(326.dp))
                    .height(screenHeightDp(47.dp)),
                enabled = buttonEnabled,
                onClick = onApply
            )
        }
    }
}

@Composable
fun RecruitingStatusMultiSection(
    recruitingStatus: RecruitingStatus?,
    onSelect: (RecruitingStatus) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(screenWidthDp(14.dp))
    ) {
        RecruitingStatus.entries.forEach { type ->
            TextButton(
                modifier = Modifier
                    .width(screenWidthDp(71.dp))
                    .height(screenHeightDp(35.dp)),
                text = type.value,
                shape = SpotShapes.Hard,
                state = TextButtonState.Toggle,
                checked = (recruitingStatus == type),
                onClick = { onSelect(type) },
                style = SpotTheme.typography.medium_500
            )
        }
    }
}

@Composable
fun ActivityTypeMultiSection(
    selectedTypes: ActivityType?,
    onToggle: (ActivityType) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(screenWidthDp(14.dp))
    ) {
        ActivityType.entries.forEach { type ->
            MultiButton(
                modifier = Modifier.weight(1f),
                text = type.label,
                shape = SpotShapes.Soft,
                painter = getIconForType(type),
                checked = (selectedTypes == type),
                onClick = { onToggle(type) }
            )
        }
    }
}

@Composable
fun ActivityFeeSection(
    activityFee: FeeRange?,
    onSelect: (FeeRange) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(screenWidthDp(14.dp)),
        verticalArrangement = Arrangement.spacedBy(screenHeightDp(14.dp))
    ) {
        FeeRange.entries.forEach { fee ->
            TextButton(
                text = fee.label,
                modifier = Modifier
                    .width(screenWidthDp(71.dp))
                    .height(screenHeightDp(35.dp)),
                state = TextButtonState.Toggle,
                checked = activityFee == fee,
                onClick = { onSelect(fee) },
                shape = SpotShapes.Hard,
                style = SpotTheme.typography.medium_500
            )
        }
    }
}

@Composable
fun ResetFilterText(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text = "필터 초기화",
        color = SpotTheme.colors.gray500,
        style = SpotTheme.typography.regular_500,
        textDecoration = TextDecoration.Underline,
        modifier = modifier
            .semantics { role = Role.Button }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
    )
}

@Composable
private fun getIconForType(type: ActivityType) = when (type) {
    ActivityType.ONLINE -> painterResource(R.drawable.online)
    ActivityType.OFFLINE  -> painterResource(R.drawable.offline)
}

