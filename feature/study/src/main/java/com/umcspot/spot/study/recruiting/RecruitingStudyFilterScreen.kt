package com.umcspot.spot.study.recruiting

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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.umcspot.spot.designsystem.component.button.TextButton
import com.umcspot.spot.designsystem.component.button.TextButtonState
import com.umcspot.spot.designsystem.component.study.section.ActivityThemeSection
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.model.ActivityType
import com.umcspot.spot.model.FeeRange
import com.umcspot.spot.model.StudyTheme
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp
import kotlinx.collections.immutable.ImmutableList

@Composable
fun RecruitingStudyFilterScreen(
    contentPadding: PaddingValues,
    onAcceptFilterClick: () -> Unit,
    vm: RecruitingStudyFilterViewModel = hiltViewModel(),
) {

    val activities by vm.activities.collectAsStateWithLifecycle()
    val fees by vm.fees.collectAsStateWithLifecycle()
    val themes by vm.themes.collectAsStateWithLifecycle()
    val acceptEnabled by vm.notNull.collectAsStateWithLifecycle()

    val topPad = contentPadding.calculateTopPadding()
    val bottomPad = contentPadding.calculateBottomPadding()

    LaunchedEffect(Unit) {
        vm.events.collect { ev ->
            when (ev) {
                is RecruitingStudyFilterViewModel.Event.Applied -> onAcceptFilterClick()
            }
        }
    }

    RecruitingStudyFilterScreenContent(
        modifier = Modifier
            .padding(top = topPad, bottom = bottomPad),
        selectedActivities = activities,
        selectedFees = fees,
        selectedThemes = themes,
        buttonEnabled = acceptEnabled,
        onToggleActivity = vm::toggleActivity,
        onToggleFee = vm::toggleFee,
        onToggleTheme = vm::toggleTheme,
        onReset = vm::reset,
        onApply = vm::apply
    )
}

@Composable
fun RecruitingStudyFilterScreenContent(
    selectedActivities: ImmutableList<ActivityType>,
    selectedFees: ImmutableList<FeeRange>,
    selectedThemes: ImmutableList<StudyTheme>,
    buttonEnabled: Boolean,
    onToggleActivity: (ActivityType) -> Unit,
    onToggleFee: (FeeRange) -> Unit,
    onToggleTheme: (StudyTheme) -> Unit,
    onReset: () -> Unit,
    onApply: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SpotTheme.colors.white)
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "활동",
                style = SpotTheme.typography.medium_500.copy(fontSize = 15.sp),
                color = SpotTheme.colors.black
            )

            Spacer(modifier = Modifier.height(screenHeightDp(10.dp)))


            ActivityTypeMultiSection(
                selectedTypes = selectedActivities,
                onToggle = onToggleActivity
            )

            Spacer(modifier = Modifier.height(screenHeightDp(30.dp)))


            ActivityFeeSection(
                selectedFees = selectedFees,
                onToggle = onToggleFee
            )

            Spacer(modifier = Modifier.height(screenHeightDp(30.dp)))

            Text(
                text = "스터디 테마",
                style = SpotTheme.typography.h5,
                color = SpotTheme.colors.black
            )

            Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))

            ActivityThemeSection(
                selectedThemes = selectedThemes,
                onSelect = onToggleTheme,
                maxSelection = 10
            )

            Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))

            ResetFilterText(
                onClick = onReset
            )

            Spacer(Modifier.height(screenHeightDp(80.dp)))
        }

        Box(
            modifier = Modifier
                .align(alignment = Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .zIndex(1f)
        ) {
            TextButton(
                text = "검색 결과 보기",
                enabled = buttonEnabled,
                onClick = onApply
            )
        }
    }
}

@Composable
fun ActivityTypeMultiSection(
    selectedTypes: ImmutableList<ActivityType>,
    onToggle: (ActivityType) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        ActivityType.entries.forEach { type ->
            TextButton(
                modifier = Modifier.weight(1f),
                text = type.label,
                state = TextButtonState.Toggle,
                checked = selectedTypes.contains(type),
                onClick = { onToggle(type) }
            )
        }
    }
}

@Composable
fun ActivityFeeSection(
    selectedFees: ImmutableList<FeeRange>,
    onToggle: (FeeRange) -> Unit
) {
    Column(
        modifier = Modifier
            .wrapContentSize()
            .background(SpotTheme.colors.white)
    ) {
        Text(
            text = "활동비",
            style = SpotTheme.typography.medium_500.copy(fontSize = 15.sp),
            color = SpotTheme.colors.black
        )
        Spacer(modifier = Modifier.height(10.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            FeeRange.entries.forEach { fee ->
                TextButton(
                    text = fee.label,
                    modifier = Modifier
                        .width(screenWidthDp(71.dp))
                        .wrapContentHeight(),
                    state = TextButtonState.Toggle,
                    checked = selectedFees.contains(fee),
                    onClick = { onToggle(fee) },
                )
            }
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
        color = SpotTheme.colors.gray400,
        style = SpotTheme.typography.small_400.copy(
            fontSize = 13.sp,
            textDecoration = TextDecoration.Underline
        ),
        modifier = modifier
            .semantics { role = Role.Button }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(vertical = 4.dp)
    )
}