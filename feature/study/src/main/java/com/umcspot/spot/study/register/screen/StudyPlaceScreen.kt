package com.umcspot.spot.study.register.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.umcspot.spot.common.location.LocationRow
import com.umcspot.spot.designsystem.component.bottomsheet.LocationBottomSheet
import com.umcspot.spot.designsystem.component.study.section.ActivityTypeSection
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.model.ActivityType
import com.umcspot.spot.study.register.component.SelectedRegionsSection
import com.umcspot.spot.ui.extension.screenHeightDp
import kotlinx.collections.immutable.ImmutableList

@Composable
fun StudyPlaceScreen(
    activityType: ActivityType?,
    isSheetVisible: Boolean,
    query: String,
    searchResults: List<LocationRow>,
    selectedRegions: ImmutableList<LocationRow>,
    onActivityTypeSelect: (ActivityType) -> Unit,
    onQueryChange: (String) -> Unit,
    onSheetOpen: () -> Unit,
    onSheetDismiss: () -> Unit,
    onAddSelected: (LocationRow) -> Unit,
    onRemoveSelected: (LocationRow) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    LocationBottomSheet(
        visible = isSheetVisible,
        query = query,
        results = searchResults,
        onQueryChange = onQueryChange,
        onDismiss = onSheetDismiss,
        selected = selectedRegions,
        onAddSelected = onAddSelected,
        onRemoveSelected = onRemoveSelected
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(top = screenHeightDp(65.dp))
    ) {
        Text(
            text = "스터디는 어디서 진행하나요?",
            style = SpotTheme.typography.h3
        )

        Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))

        ActivityTypeSection(
            activityType = activityType,
            onSelect = onActivityTypeSelect
        )

        if (activityType == ActivityType.OFFLINE) {
            Column {
                Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))
                SelectedRegionsSection(
                    selectedRegions = selectedRegions,
                    onRemoveClick = onRemoveSelected,
                    onAddClick = onSheetOpen
                )
            }
        }
    }
}