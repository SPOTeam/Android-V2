package com.umcspot.spot.study.detail.screen.attendance

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.appBar.BackTopBar
import com.umcspot.spot.designsystem.component.button.SpotActivationButton
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.study.component.SpotStudyDialog
import com.umcspot.spot.study.component.SpotStudyDialogIcon
import com.umcspot.spot.study.detail.StudyDetailViewModel
import com.umcspot.spot.study.detail.component.planner.attendance.StudyAttendanceListSection
import com.umcspot.spot.study.model.AttendanceStatus
import com.umcspot.spot.study.model.StudyAttendanceModel
import com.umcspot.spot.study.model.StudyMemberModel
import com.umcspot.spot.ui.extension.noRippleClickable
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp
import kotlinx.coroutines.delay

@Composable
fun StudyAttendanceRoute(
    studyId: Long,
    scheduleId: Long,
    onBackClick: () -> Unit,
    contentPadding: PaddingValues,
    viewModel: StudyDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showFinishDialog by remember { mutableStateOf(false) }
    var isFinished by remember { mutableStateOf(false) }

    LaunchedEffect(studyId, scheduleId) {
        viewModel.fetchMembersOnly(studyId)
        viewModel.fetchAttendanceQr(studyId, scheduleId)
        viewModel.fetchAttendanceList(studyId, scheduleId)
    }

    LaunchedEffect(uiState.attendanceState.isAttendanceActive) {
        if (uiState.attendanceState.isAttendanceActive) {
            while (true) {
                viewModel.refreshAttendanceStatus(studyId, scheduleId)
                delay(5000)
            }
        }
    }

    BackHandler { onBackClick() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SpotTheme.colors.white)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        when {
            isFinished -> {
                StudyAttendanceSummaryScreen(
                    attendances = uiState.attendanceState.attendanceList,
                    onBackClick = onBackClick,
                    contentPadding = contentPadding
                )
            }

            uiState.attendanceState.isAttendanceActive -> {
                StudyAttendanceProgressScreen(
                    attendances = uiState.attendanceState.attendanceList,
                    qrImageUrl = uiState.attendanceState.qrCodeImageUrl,
                    myUserId = uiState.myUserId,
                    onBackClick = onBackClick,
                    onFinishClick = { showFinishDialog = true },
                    contentPadding = contentPadding
                )
            }

            else -> {
                StudyAttendanceStartScreen(
                    members = uiState.homeState.members,
                    onBackClick = onBackClick,
                    onStartClick = { viewModel.startAttendance(studyId, scheduleId) },
                    onDeleteClick = {
                        viewModel.deleteSchedule(studyId, scheduleId)
                        onBackClick()
                    },
                    contentPadding = contentPadding
                )
            }
        }

        if (showFinishDialog) {
            SpotStudyDialog(
                onDismissRequest = { showFinishDialog = false },
                title = "출석체크를 마감할까요?",
                description = "이 작업은 되돌릴 수 없어요.\n미결인 스터디원은 결석 처리됩니다.",
                buttonText = "출석체크 마감",
                icon = SpotStudyDialogIcon.NONE,
                onButtonClick = {
                    showFinishDialog = false
                    viewModel.finishAttendance(studyId, scheduleId)
                    isFinished = true
                }
            )
        }
    }
}

@Composable
fun StudyAttendanceStartScreen(
    members: List<StudyMemberModel>,
    onBackClick: () -> Unit,
    onStartClick: () -> Unit,
    onDeleteClick: () -> Unit,
    contentPadding: PaddingValues
) {
    val attendanceList = remember(members) {
        members.map { member ->
            StudyAttendanceModel(
                memberId = member.id.toString(),
                name = member.name,
                profileUrl = member.profileUrl ?: "",
                status = AttendanceStatus.UNDECIDED,
                attendedAt = null
            )
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(contentPadding.calculateTopPadding()))
        BackTopBar(title = "일정 상세", onBackClick = onBackClick)

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = screenWidthDp(17.dp))
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(screenHeightDp(18.dp)))
            Text(text = "스터디 출석 QR", style = SpotTheme.typography.h5)
            Spacer(modifier = Modifier.height(screenHeightDp(8.dp)))
            Text(
                text = "출석체크를 시작하면 출석 QR코드가 생성됩니다.\n출석체크 시작 시간대 : 일정 시작 30분전 ~ 일정 종료",
                style = SpotTheme.typography.regular_500,
                color = SpotTheme.colors.gray400
            )

            Spacer(modifier = Modifier.height(screenHeightDp(24.dp)))

            StudyAttendanceListSection(
                attendances = attendanceList,
                showStatus = false,
                myUserId = ""
            )

            Spacer(modifier = Modifier.height(screenHeightDp(24.dp)))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "스터디 일정 관리", style = SpotTheme.typography.h5)
                Row(
                    modifier = Modifier.noRippleClickable { onDeleteClick() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.delete),
                        contentDescription = null,
                        modifier = Modifier.size(screenWidthDp(14.dp))
                    )
                    Spacer(modifier = Modifier.width(screenWidthDp(4.dp)))
                    Text(
                        text = "일정 삭제",
                        style = SpotTheme.typography.small_400,
                        color = SpotTheme.colors.black
                    )
                }
            }
            Spacer(modifier = Modifier.height(screenHeightDp(12.dp)))
            Text(
                text = "일정을 삭제하면 모든 스터디원의 일정에서 해당 일정이 삭제됩니다.",
                style = SpotTheme.typography.regular_500,
                color = SpotTheme.colors.gray400
            )
        }

        SpotActivationButton(
            buttonText = "출석체크 시작",
            isEnabled = true,
            onClick = onStartClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = screenWidthDp(17.dp))
                .padding(bottom = contentPadding.calculateBottomPadding() + screenHeightDp(13.dp))
        )
    }
}

@Composable
fun StudyAttendanceProgressScreen(
    attendances: List<StudyAttendanceModel>,
    qrImageUrl: String?,
    myUserId: String,
    onBackClick: () -> Unit,
    onFinishClick: () -> Unit,
    contentPadding: PaddingValues
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(contentPadding.calculateTopPadding()))
        BackTopBar(title = "출석체크", onBackClick = onBackClick)

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = screenWidthDp(20.dp)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(screenHeightDp(18.dp)))
                Text(
                    text = "스터디 출석 QR",
                    style = SpotTheme.typography.h5,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "QR코드를 인식하면 출석이 인증됩니다.\n오늘 스터디에 참석한 팀원들에게 QR코드를 보여주세요!",
                    style = SpotTheme.typography.regular_500,
                    color = SpotTheme.colors.gray400,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(screenHeightDp(36.dp)))

                Box(
                    modifier = Modifier.size(screenWidthDp(200.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (qrImageUrl == null) {
                        CircularProgressIndicator(color = SpotTheme.colors.primary)
                    } else {
                        AsyncImage(
                            model = qrImageUrl,
                            contentDescription = "출석 QR",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit,
                            placeholder = painterResource(id = R.drawable.spot_logo)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(screenHeightDp(24.dp)))
            }

            item {
                StudyAttendanceListSection(
                    attendances = attendances,
                    showStatus = true,
                    myUserId = myUserId
                )
            }
        }
        Spacer(modifier = Modifier.height(screenHeightDp(8.dp)))

        SpotActivationButton(
            buttonText = "출석체크 마감",
            isEnabled = true,
            onClick = onFinishClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = screenWidthDp(20.dp))
                .padding(bottom = contentPadding.calculateBottomPadding() + screenHeightDp(13.dp))
        )
    }
}

@Composable
fun StudyAttendanceSummaryScreen(
    attendances: List<StudyAttendanceModel>,
    onBackClick: () -> Unit,
    contentPadding: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SpotTheme.colors.white)
    ) {
        Spacer(modifier = Modifier.height(contentPadding.calculateTopPadding()))
        BackTopBar(title = "출석체크", onBackClick = onBackClick)

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = screenWidthDp(17.dp))
        ) {
            item {
                Spacer(modifier = Modifier.height(screenHeightDp(18.dp)))
            }

            items(attendances) { attendance ->
                StudyAttendanceListSection(
                    attendances = listOf(attendance),
                    showStatus = true,
                    myUserId = ""
                )
            }
        }
    }
}