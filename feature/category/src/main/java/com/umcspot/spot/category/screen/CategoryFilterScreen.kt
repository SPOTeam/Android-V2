package com.umcspot.spot.category.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.umcspot.spot.category.CategoryViewModel
import com.umcspot.spot.category.component.ActivityFeeSection
import com.umcspot.spot.category.component.ActivityTypeSection
import com.umcspot.spot.category.component.RecruitingStatusSection
import com.umcspot.spot.category.component.ResetFilterText
import com.umcspot.spot.designsystem.component.appBar.BackTopBar
import com.umcspot.spot.designsystem.component.button.SpotActivationButton
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.model.ActivityType
import com.umcspot.spot.model.FeeRange
import com.umcspot.spot.model.RecruitingStatus
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun CategoryFilterScreen(
    contentPadding: PaddingValues,
    onAcceptFilterClick: () -> Unit,
    categoryViewModel: CategoryViewModel
) {
    val uiState by categoryViewModel.uiState.collectAsStateWithLifecycle()

    var draftRecruitingStatus by rememberSaveable { mutableStateOf(uiState.recruitingStatus) }
    var draftFee by rememberSaveable { mutableStateOf(uiState.feeRange) }
    var draftActivity by rememberSaveable { mutableStateOf(uiState.activityType) }

    BackHandler { onAcceptFilterClick() }

    CategoryFilterContent(
        contentPadding = contentPadding,
        recruitingStatus = draftRecruitingStatus,
        feeRange = draftFee,
        activityType = draftActivity,
        onBackClick = onAcceptFilterClick,
        onToggleRecruitingStatus = { status ->
            draftRecruitingStatus = if (draftRecruitingStatus == status) null else status
        },
        onToggleFeeRange = { fee ->
            draftFee = if (draftFee == fee) null else fee
        },
        onToggleActivityType = { activity ->
            draftActivity = if (draftActivity == activity) null else activity
        },
        onReset = {
            draftRecruitingStatus = null
            draftFee = null
            draftActivity = null
        },
        onApply = {
            categoryViewModel.applyFilter(
                recruitingStatus = draftRecruitingStatus,
                feeRange = draftFee,
                activityType = draftActivity
            )
            onAcceptFilterClick()
        }
    )
}

@Composable
private fun CategoryFilterContent(
    contentPadding: PaddingValues,
    recruitingStatus: RecruitingStatus?,
    feeRange: FeeRange?,
    activityType: ActivityType?,
    onBackClick: () -> Unit,
    onToggleRecruitingStatus: (RecruitingStatus) -> Unit,
    onToggleFeeRange: (FeeRange) -> Unit,
    onToggleActivityType: (ActivityType) -> Unit,
    onReset: () -> Unit,
    onApply: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SpotTheme.colors.white)
            .padding(top = contentPadding.calculateTopPadding())
    ) {
        BackTopBar(
            title = "스팟의 스터디",
            onBackClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        )

        Box(modifier = Modifier.weight(1f)) {
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
                        text = "검색 결과는 모든 카테고리에 공통 반영됩니다.",
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
                    RecruitingStatusSection(
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
                    ActivityTypeSection(
                        selectedType = activityType,
                        onToggle = onToggleActivityType
                    )

                    Spacer(modifier = Modifier.height(screenHeightDp(53.dp)))

                    Text(
                        text = "활동비",
                        style = SpotTheme.typography.h5,
                        color = SpotTheme.colors.black
                    )
                    Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))
                    ActivityFeeSection(
                        activityFee = feeRange,
                        onSelect = onToggleFeeRange
                    )

                    Spacer(modifier = Modifier.height(screenHeightDp(33.dp)))

                    ResetFilterText(onClick = onReset)

                    Spacer(Modifier.height(screenHeightDp(80.dp)))
                }
            }
        }

        SpotActivationButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = screenWidthDp(17.dp))
                .padding(
                    bottom = contentPadding.calculateBottomPadding() + screenHeightDp(12.dp),
                    top = screenHeightDp(12.dp)
                ),
            buttonText = "검색 결과 보기",
            isEnabled = true,
            onClick = onApply
        )
    }
}