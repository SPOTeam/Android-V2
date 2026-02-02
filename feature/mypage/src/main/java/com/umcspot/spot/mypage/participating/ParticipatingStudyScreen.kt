package com.umcspot.spot.mypage.participating

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.SpotSpinner
import com.umcspot.spot.designsystem.component.empty.EmptyAlertWithButton
import com.umcspot.spot.designsystem.component.study.StudyListItem
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.G300
import com.umcspot.spot.designsystem.theme.R500
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.study.model.StudyResult
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp
import com.umcspot.spot.ui.state.UiState
import kotlinx.coroutines.launch

@Composable
fun ParticipatingScreen(
    contentPadding : PaddingValues,
    onRegisterScrollToTop: ((() -> Unit)?) -> Unit,
    onStudyClick : (Long) -> Unit,
    moveToRecruitingStudy : () -> Unit,
    viewmodel : ParticipatingStudyViewModel = hiltViewModel()
) {
    val uiState by viewmodel.uiState.collectAsStateWithLifecycle()

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val ui = uiState.participatingStudy
    val itemList: List<StudyResult> = when (ui) {
        is UiState.Success -> ui.data.studyList
        else -> emptyList()
    }

    LaunchedEffect(Unit) {
        viewmodel.loadParticipatingStudy()
        onRegisterScrollToTop {
            scope.launch {
                listState.animateScrollToItem(0)
            }
        }
    }

    val shouldLoadMore = remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

            totalItems > 0 && lastVisibleItemIndex >= totalItems - 3
        }
    }

    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value) {
            val successData = (ui as? UiState.Success)?.data
            if (successData?.hasNext == true) {
                viewmodel.loadNextPage()
            }
        }
    }

    when (ui) {
        is UiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(SpotTheme.colors.white),
                contentAlignment = Alignment.Center
            ) {
                SpotSpinner(size = screenWidthDp(30.dp))
            }
        }
        is UiState.Empty, is UiState.Failure -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(SpotTheme.colors.white),
                contentAlignment = Alignment.Center
            ) {
                EmptyAlertWithButton(
                    alertTitle = "참여 중인 스터디가 아직 없어요!",
                    alertDes = "스팟에서 내 목표를 이뤄봐요",
                    buttonText = "스터디 둘러보기",
                    painter = painterResource(R.drawable.study_default),
                    onClick = { moveToRecruitingStudy() },
                )
            }
        }
        is UiState.Success -> {
            ParticipatingStudyScreenContent(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(top = contentPadding.calculateTopPadding(), bottom = contentPadding.calculateBottomPadding())
                    .padding(horizontal = screenWidthDp(17.dp)),
                studyList = itemList,
                listState = listState,
                onStudyClick = onStudyClick,
                onEditClick = {},
                onReportClick = {},
                onLeaveClick = {},
                onDeleteClick = {}
            )
        }
    }
}

@Composable
fun ParticipatingStudyScreenContent(
    modifier: Modifier = Modifier,
    studyList: List<StudyResult>,
    listState: LazyListState,
    onStudyClick: (Long) -> Unit,
    onEditClick: (Long) -> Unit,
    onReportClick: (Long) -> Unit,
    onLeaveClick: (Long) -> Unit,
    onDeleteClick: (Long) -> Unit
) {
    var expandedForId by remember { mutableStateOf<Long?>(null) }

    LazyColumn(
        state = listState,
        modifier = modifier,
    ) {
        items(
            items = studyList,
            key = { it.id }
        ) { item ->
            Spacer(Modifier.height(screenHeightDp(5.dp)))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopStart
            ) {
                StudyListItem(
                    item = item,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onStudyClick(item.id) },
                    meetballSlot = {
                        Box {
                            Box(
                                modifier = Modifier
                                    .padding(top = screenHeightDp(4.dp))
                                    .width(screenWidthDp(24.dp))
                                    .height(screenWidthDp(22.dp))
                                    .clip(SpotShapes.Hard)
                                    .clickable { expandedForId = item.id },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.meetball),
                                    contentDescription = null,
                                    modifier = Modifier.size(screenWidthDp(14.dp))
                                )
                            }

                            MeetballMenu(
                                isOwner = item.isOwner ,
                                isAlone = item.isAlone,
                                expanded = expandedForId == item.id,
                                onDismiss = { expandedForId = null },
                                onEdit = { expandedForId = null; onEditClick(item.id) },
                                onReport = { expandedForId = null; onReportClick(item.id) },
                                onLeave = { expandedForId = null; onLeaveClick(item.id) },
                                onDelete = { expandedForId = null; onDeleteClick(item.id) }
                            )
                        }
                    }
                )
            }

            if (studyList.indexOf(item) != studyList.lastIndex) {
                Spacer(Modifier.height(screenHeightDp(5.dp)))

                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = SpotTheme.colors.G300,
                    thickness = 1.dp
                )
            }
        }
    }
}

@Composable
fun MeetballMenu(
    isOwner: Boolean,
    isAlone: Boolean,
    expanded: Boolean,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onReport: () -> Unit,
    onLeave: () -> Unit,
    onDelete:() -> Unit
) {
    DropdownMenu(
        modifier = Modifier
            .background(SpotTheme.colors.white),
        shape = SpotShapes.Soft,
        expanded = expanded,
        onDismissRequest = onDismiss,
    ) {
        if (isOwner) {
            DropdownMenuItem(
                modifier = Modifier
                    .height(screenHeightDp(30.dp))
                    .wrapContentWidth(),
                text = {
                    Text(
                        text = "정보 수정하기",
                        style = SpotTheme.typography.regular_500,
                        color = SpotTheme.colors.black
                    )
                },
                onClick = {
                    onDismiss()
                    onEdit()
                }
            )
        }

        if(!isAlone) {
            if(isOwner) {
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp,
                    color = SpotTheme.colors.gray200
                )
            }

            DropdownMenuItem(
                modifier = Modifier
                    .height(screenHeightDp(30.dp))
                    .wrapContentWidth(),
                text = {
                    Text(
                        text = "스터디원 신고",
                        style = SpotTheme.typography.regular_500,
                        color = SpotTheme.colors.black
                    )
                },
                onClick = {
                    onDismiss()
                    onReport()
                }
            )
        }

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = SpotTheme.colors.gray200
        )

        DropdownMenuItem(
            modifier = Modifier
                .height(screenHeightDp(30.dp))
                .wrapContentWidth(),
            text = {
                Text(
                    text = if(isAlone) "스터디 삭제하기" else "스터디 나가기",
                    style = SpotTheme.typography.regular_500,
                    color = SpotTheme.colors.R500
                )
            },
            onClick = {
                onDismiss()
                if(isAlone) onDelete()
                else onLeave()
            }
        )
    }
}