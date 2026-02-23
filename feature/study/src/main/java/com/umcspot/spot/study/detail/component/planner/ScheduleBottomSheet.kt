package com.umcspot.spot.study.detail.component.planner

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.button.SpotActivationButton
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.B100
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.study.detail.StudyDetailViewModel
import com.umcspot.spot.study.detail.model.StudyDetailSideEffect
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.roundToInt

@Composable
fun ScheduleBottomSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
    studyId: Long?,
    viewModel: StudyDetailViewModel = hiltViewModel()
) {
    if (!visible) return

    val context = LocalContext.current
    val keyboard = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()

    var title by remember { mutableStateOf("") }
    var locationMemo by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf(LocalDate.now()) }
    var startTime by remember { mutableStateOf(LocalTime.now()) }
    var endDate by remember { mutableStateOf(LocalDate.now()) }
    var endTime by remember { mutableStateOf(LocalTime.now().plusHours(1)) }

    val startDateTime = LocalDateTime.of(startDate, startTime)
    val endDateTime = LocalDateTime.of(endDate, endTime)
    val isEnabled = title.isNotBlank() && !endDateTime.isBefore(startDateTime)

    val density = LocalDensity.current
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val navBarPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    // 핵심: 배경 높이를 533dp + 네비바 높이만큼 잡음
    val sheetHeight = screenHeightDp(533.dp) + navBarPadding
    val sheetOffset = remember { Animatable(with(density) { screenHeight.toPx() }) }

    fun animateAndDismiss() {
        scope.launch {
            keyboard?.hide()
            sheetOffset.animateTo(with(density) { screenHeight.toPx() }, tween(250))
            onDismiss()
        }
    }

    LaunchedEffect(visible) {
        if (visible) {
            val screenHeightPx = with(density) { screenHeight.toPx() }
            val sheetHeightPx = with(density) { sheetHeight.toPx() }
            val openY = screenHeightPx - sheetHeightPx
            sheetOffset.snapTo(screenHeightPx)
            sheetOffset.animateTo(openY, tween(300))
        }
    }

    Dialog(
        onDismissRequest = { animateAndDismiss() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false, // 이 옵션이 꺼져있어야 바닥 끝까지 배경을 칠함
            dismissOnClickOutside = false
        )
    ) {
        BackHandler { animateAndDismiss() }

        Box(Modifier.fillMaxSize()) {
            // 딤 처리
            Box(
                Modifier
                    .matchParentSize()
                    .background(SpotTheme.colors.black.copy(alpha = 0.4f))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }) { animateAndDismiss() }
            )

            // 시트 본체 (이 놈이 바닥 회색을 덮어야 함)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(sheetHeight)
                    .offset { IntOffset(0, sheetOffset.value.roundToInt()) }
                    .clip(SpotShapes.RoundTop)
                    .background(SpotTheme.colors.white)
                    .imePadding()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = screenWidthDp(17.dp))
                ) {
                    // Header
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = screenHeightDp(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("새로운 일정", style = SpotTheme.typography.h4)
                        IconButton(
                            onClick = { animateAndDismiss() },
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .size(screenWidthDp(24.dp))
                        ) {
                            Icon(painterResource(R.drawable.dismiss), contentDescription = null)
                        }
                    }

                    // Content 영역
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Spacer(Modifier.height(screenHeightDp(24.dp)))
                        Text("일정", style = SpotTheme.typography.h5)
                        Spacer(Modifier.height(screenHeightDp(7.dp)))
                        ScheduleInputField(title, { if (it.length <= 20) title = it }, "제목")
                        Spacer(Modifier.height(screenHeightDp(8.dp)))
                        ScheduleInputField(
                            locationMemo,
                            { if (it.length <= 20) locationMemo = it },
                            "위치 메모"
                        )

                        Spacer(Modifier.height(screenHeightDp(32.dp)))
                        Text("일시", style = SpotTheme.typography.h4)
                        Spacer(Modifier.height(screenHeightDp(12.dp)))

                        // 시작 일시 선택 (기능 추가)
                        DateTimeSelectorRow(
                            "시작", startDate, startTime,
                            onDateClick = {
                                DatePickerDialog(
                                    context,
                                    { _, y, m, d -> startDate = LocalDate.of(y, m + 1, d) },
                                    startDate.year,
                                    startDate.monthValue - 1,
                                    startDate.dayOfMonth
                                ).show()
                            },
                            onTimeClick = {
                                TimePickerDialog(
                                    context,
                                    { _, h, min -> startTime = LocalTime.of(h, min) },
                                    startTime.hour,
                                    startTime.minute,
                                    false
                                ).show()
                            }
                        )
                        Spacer(Modifier.height(screenHeightDp(8.dp)))
                        // 종료 일시 선택 (기능 추가)
                        DateTimeSelectorRow(
                            "종료", endDate, endTime,
                            onDateClick = {
                                DatePickerDialog(
                                    context,
                                    { _, y, m, d -> endDate = LocalDate.of(y, m + 1, d) },
                                    endDate.year,
                                    endDate.monthValue - 1,
                                    endDate.dayOfMonth
                                ).show()
                            },
                            onTimeClick = {
                                TimePickerDialog(
                                    context,
                                    { _, h, min -> endTime = LocalTime.of(h, min) },
                                    endTime.hour,
                                    endTime.minute,
                                    false
                                ).show()
                            }
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = navBarPadding)
                    ) {
                        SpotActivationButton(
                            buttonText = "추가",
                            isEnabled = isEnabled,
                            onClick = {
                                studyId?.let { id ->
                                    viewModel.createSchedule(
                                        id,
                                        title,
                                        locationMemo,
                                        startDateTime,
                                        endDateTime
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = screenHeightDp(13.dp))
                        )
                    }
                }
            }
        }
    }
}

// 나머지 컴포넌트는 기존과 동일 (위 코드에 DateTimeSelectorRow 인자 잘 확인해서 넣어주세요)
@Composable
private fun ScheduleInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    var isFocused by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (isFocused) SpotTheme.colors.B500 else SpotTheme.colors.gray200,
                RoundedCornerShape(6.dp)
            )
            .padding(horizontal = screenWidthDp(10.dp), vertical = screenHeightDp(7.dp)),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            BasicTextField(
                value = value, onValueChange = onValueChange,
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged { isFocused = it.isFocused },
                singleLine = true, textStyle = SpotTheme.typography.medium_500,
                cursorBrush = SolidColor(SpotTheme.colors.B500),
                decorationBox = {
                    if (value.isEmpty()) Text(
                        placeholder,
                        style = SpotTheme.typography.medium_500,
                        color = SpotTheme.colors.default
                    ); it()
                }
            )
            Text(
                "(${value.length}/20)",
                style = SpotTheme.typography.regular_500,
                color = SpotTheme.colors.gray400
            )
        }
    }
}

@Composable
private fun DateTimeSelectorRow(
    label: String,
    date: LocalDate,
    time: LocalTime,
    onDateClick: () -> Unit,
    onTimeClick: () -> Unit
) {
    val df = DateTimeFormatter.ofPattern("yyyy.MM.dd.")
    val tf = DateTimeFormatter.ofPattern("hh:mma", Locale.US)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                SpotTheme.colors.default,
                RoundedCornerShape(6.dp)
            )
            .padding(horizontal = screenWidthDp(10.dp), vertical = screenHeightDp(7.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = SpotTheme.typography.medium_500, color = SpotTheme.colors.gray400)
        Row {
            Box(
                Modifier
                    .border(
                        1.dp,
                        SpotTheme.colors.B100,
                        RoundedCornerShape(6.dp)
                    )
                    .background(SpotTheme.colors.B100)
                    .clickable { onDateClick() }
                    .padding(horizontal = screenWidthDp(9.dp), vertical = screenHeightDp(1.dp))
            ) {
                Text(
                    date.format(df),
                    style = SpotTheme.typography.regular_500,
                    color = SpotTheme.colors.black
                )
            }
            Spacer(Modifier.width(screenWidthDp(6.dp)))
            Box(
                Modifier
                    .border(
                        1.dp,
                        SpotTheme.colors.B100,
                        RoundedCornerShape(6.dp)
                    )
                    .background(SpotTheme.colors.B100)
                    .clickable { onTimeClick() }
                    .padding(horizontal = screenWidthDp(9.dp), vertical = screenHeightDp(1.dp))
            ) {
                Text(
                    time.format(tf).lowercase(),
                    style = SpotTheme.typography.regular_500,
                    color = SpotTheme.colors.black
                )
            }
        }
    }
}