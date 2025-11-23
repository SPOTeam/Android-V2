package com.umcspot.spot.study.recruiting

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.umcspot.spot.designsystem.component.button.TextButton
import com.umcspot.spot.designsystem.component.button.TextToggleButton
import com.umcspot.spot.designsystem.component.study.section.ActivityThemeSection
import com.umcspot.spot.designsystem.component.study.section.ActivityTypeSection
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.model.ActivityType
import com.umcspot.spot.model.FeeRange
import com.umcspot.spot.model.StudyTheme
import com.umcspot.spot.ui.extension.screenHeightDp

@Composable
fun RecruitingStudyFilterScreen(
    contentPadding: PaddingValues,
    onAcceptFilterClick: () -> Unit,
    vm: RecruitingStudyFilterViewModel = hiltViewModel(),
) {
    val activityType by vm.activity.collectAsStateWithLifecycle()
    val fee by vm.fee.collectAsStateWithLifecycle()
    val theme by vm.theme.collectAsStateWithLifecycle()
    val acceptEnabled by vm.notNull.collectAsStateWithLifecycle()


    val topPad = contentPadding.calculateTopPadding()
    val bottomPad = contentPadding.calculateBottomPadding()

    // 적용 이벤트 수신
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
        activityType = activityType,
        fee = fee,
        theme = theme,
        buttonEnabled = acceptEnabled,
        onSetActivity = vm::setActivity,
        onSetFee = vm::setFee,
        onSetTheme = vm::setTheme,
        onReset = vm::reset,
        onApply = vm::apply
    )
}

@Composable
fun RecruitingStudyFilterScreenContent(
    activityType: ActivityType?,                 // ✅ 단일 값 (nullable)
    fee: FeeRange?,                         // ✅ 단일 값 (nullable)
    theme: StudyTheme?,                     // ✅ 단일 값 (nullable)
    buttonEnabled: Boolean,
    onSetActivity: (ActivityType) -> Unit,  // ✅ set* 로직 (같은 값 다시 누르면 해제는 VM이 처리)
    onSetFee: (FeeRange?) -> Unit,
    onSetTheme: (StudyTheme) -> Unit,
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
                .verticalScroll(rememberScrollState()) // ✅ 스크롤
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "활동",
                style = SpotTheme.typography.medium_500.copy(fontSize = 15.sp),
                color = SpotTheme.colors.black
            )

            Spacer(modifier = Modifier.height(10.dp))

            ActivityTypeSection(
                activityType = activityType,
                onSelect = onSetActivity
            )

            Spacer(modifier = Modifier.height(30.dp))


            ActivityFeeSection(
                activityFee = fee,
                onSelect = onSetFee
            )

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "스터디 테마",
                style = SpotTheme.typography.h5,
                color = SpotTheme.colors.black
            )

            Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))


            ActivityThemeSection(
                activityTheme = theme,
                onSelect = onSetTheme
            )

            Spacer(modifier = Modifier.height(20.dp))

            ResetFilterText(
                onClick = onReset
            )

            Spacer(Modifier.height(80.dp))
        }

        Box(
            modifier = Modifier
                .align(alignment = Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .zIndex(1f) // 항상 앞
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
fun ActivityFeeSection(
    activityFee: FeeRange?,
    onSelect: (FeeRange) -> Unit             // 누르면 VM의 setActivity 호출
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
                TextToggleButton(
                    text = fee.label,
                    width = 71.dp,
                    checked = activityFee == fee,
                    onClick = { onSelect(fee) },
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
                indication = null,          // 리플 없애려면 유지, 리플 원하면 제거
                onClick = onClick
            )
            .padding(vertical = 4.dp)      // 터치 여유
    )
}

