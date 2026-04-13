package com.umcspot.spot.study.my.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.component.study.StudyListItem
import com.umcspot.spot.designsystem.theme.G300
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.study.model.StudyResult
import com.umcspot.spot.ui.extension.screenHeightDp

@Composable
fun MyStudyListContent(
    studies: List<StudyResult>,
    listState: LazyListState,
    onItemClick: (Long) -> Unit
) {
    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize()
    ) {
        itemsIndexed(
            items = studies,
            key = { _, item -> item.id }
        ) { index, item ->
            Spacer(Modifier.height(screenHeightDp(12.dp)))

            StudyListItem(
                item = item,
                modifier = Modifier.fillMaxWidth(),
                onClick = { onItemClick(item.id) }
            )

            Spacer(Modifier.height(screenHeightDp(12.dp)))

            if (index != studies.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = SpotTheme.colors.G300,
                    thickness = 1.dp
                )
            }
        }
    }
}