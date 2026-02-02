package com.umcspot.spot.study.recruiting

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
import androidx.compose.runtime.saveable.listSaver
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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.button.MultiButton
import com.umcspot.spot.designsystem.component.button.TextButton
import com.umcspot.spot.designsystem.component.button.TextButtonState
import com.umcspot.spot.designsystem.component.study.section.ActivityThemeSection
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.model.ActivityType
import com.umcspot.spot.model.FeeRange
import com.umcspot.spot.model.StudyTheme
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun RecruitingStudyFilterScreen(
    contentPadding: PaddingValues,
    onAcceptFilterClick: () -> Unit,
    viewModel: RecruitingStudyViewModel = hiltViewModel(),
) {

    val activity by viewModel.activity.collectAsStateWithLifecycle()
    val fee by viewModel.fee.collectAsStateWithLifecycle()
    val themes by viewModel.themes.collectAsStateWithLifecycle()

    var draftActivity by rememberSaveable { mutableStateOf(activity) }
    var draftFee by rememberSaveable { mutableStateOf(fee) }

    val themeSaver = listSaver<List<StudyTheme>, String>(
        save = { list -> list.map { it.name } },
        restore = { names -> names.map { StudyTheme.valueOf(it) } }
    )
    var draftThemes by rememberSaveable(stateSaver = themeSaver) { mutableStateOf(themes) }

    val acceptEnabled = true // draftActivity != null || draftFee != null || draftThemes.isNotEmpty()


    val topPad = contentPadding.calculateTopPadding()
    val bottomPad = contentPadding.calculateBottomPadding()

    BackHandler {
        onAcceptFilterClick()
    }

    RecruitingStudyFilterScreenContent(
        modifier = Modifier
            .background(SpotTheme.colors.white)
            .padding(top = topPad, bottom = bottomPad),
        selectedActivity = draftActivity,
        selectedFee = draftFee,
        selectedThemes = draftThemes,
        buttonEnabled = acceptEnabled,
        onToggleActivity = { type -> draftActivity = if (draftActivity == type) null else type },
        onToggleFee = { fee -> draftFee = if (draftFee == fee) null else fee },
        onToggleTheme = { theme -> draftThemes = if (draftThemes.contains(theme)) draftThemes - theme else draftThemes + theme },
        onReset = {
            draftActivity = null
            draftFee = null
            draftThemes = emptyList()
        },
        onApply = {
            viewModel.applyFilter(
                fee = draftFee,
                activity = draftActivity,
                themes = draftThemes
            )
            onAcceptFilterClick()
        }
    )
}

@Composable
fun RecruitingStudyFilterScreenContent(
    selectedActivity: ActivityType?,
    selectedFee: FeeRange?,
    selectedThemes: List<StudyTheme>,
    buttonEnabled: Boolean,
    onToggleActivity: (ActivityType) -> Unit,
    onToggleFee: (FeeRange) -> Unit,
    onToggleTheme: (StudyTheme) -> Unit,
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = screenWidthDp(17.dp))
        ) {
            Text(
                text = "활동",
                style = SpotTheme.typography.h5,
                color = SpotTheme.colors.black
            )

            Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))


            ActivityTypeMultiSection(
                selectedTypes = selectedActivity,
                onToggle = onToggleActivity
            )

            Spacer(modifier = Modifier.height(screenHeightDp(53.dp)))

            Text(
                text = "활동비",
                style = SpotTheme.typography.medium_500.copy(fontSize = 15.sp),
                color = SpotTheme.colors.black
            )

            Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))

            ActivityFeeSection(
                selectedFee = selectedFee,
                onToggle = onToggleFee
            )

            Spacer(modifier = Modifier.height(screenHeightDp(53.dp)))

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

            Spacer(modifier = Modifier.height(screenHeightDp(33.dp)))

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
                .padding(horizontal = screenWidthDp(16.dp), vertical = screenHeightDp(12.dp))
                .zIndex(1f)
        ) {
            TextButton(
                modifier = Modifier
                    .width(screenWidthDp(326.dp))
                    .height(screenHeightDp(47.dp)),
                text = "검색 결과 보기",
                enabled = buttonEnabled,
                onClick = onApply
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
    selectedFee: FeeRange?,
    onToggle: (FeeRange) -> Unit
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
                checked = (selectedFee == fee),
                onClick = { onToggle(fee) },
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