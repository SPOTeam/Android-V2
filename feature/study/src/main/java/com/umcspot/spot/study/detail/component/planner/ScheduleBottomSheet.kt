package com.umcspot.spot.study.detail.component.planner

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.button.SpotActivationButton
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.B100
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ScheduleBottomSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
    studyId: Long?,
    onCreateSchedule: (String, String, LocalDateTime, LocalDateTime) -> Unit
) {
    if (!visible) return

    val context = LocalContext.current
    val keyboard = LocalSoftwareKeyboardController.current

    var title by remember { mutableStateOf("") }
    var locationMemo by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf(LocalDate.now()) }
    var startTime by remember { mutableStateOf(LocalTime.now()) }
    var endDate by remember { mutableStateOf(LocalDate.now()) }
    var endTime by remember { mutableStateOf(LocalTime.now().plusHours(1)) }

    val startDateTime = LocalDateTime.of(startDate, startTime)
    val endDateTime = LocalDateTime.of(endDate, endTime)
    val isEnabled = title.isNotBlank() && !endDateTime.isBefore(startDateTime)

    val navBarPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false,
            dismissOnClickOutside = false
        )
    ) {
        BackHandler { onDismiss() }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(SpotTheme.colors.black.copy(alpha = 0.4f))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onDismiss() }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeightDp(533.dp) + navBarPadding)
                    .clip(SpotShapes.RoundTop)
                    .background(SpotTheme.colors.white)
                    .imePadding()
                    .padding(horizontal = screenWidthDp(17.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = screenHeightDp(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "새로운 일정",
                        style = SpotTheme.typography.h4,
                        color = SpotTheme.colors.black
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .size(screenWidthDp(24.dp))
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.dismiss),
                            contentDescription = null,
                            tint = SpotTheme.colors.black
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(Modifier.height(screenHeightDp(24.dp)))
                    Text(
                        text = "일정",
                        style = SpotTheme.typography.h5,
                        color = SpotTheme.colors.black
                    )
                    Spacer(Modifier.height(screenHeightDp(7.dp)))

                    ScheduleInputField(
                        value = title,
                        onValueChange = { if (it.length <= 20) title = it },
                        placeholder = "제목"
                    )
                    Spacer(Modifier.height(screenHeightDp(8.dp)))

                    ScheduleInputField(
                        value = locationMemo,
                        onValueChange = { if (it.length <= 20) locationMemo = it },
                        placeholder = "위치 메모"
                    )
                    Spacer(Modifier.height(screenHeightDp(32.dp)))

                    Text(
                        text = "일시",
                        style = SpotTheme.typography.h4,
                        color = SpotTheme.colors.black
                    )
                    Spacer(Modifier.height(screenHeightDp(12.dp)))

                    DateTimeSelectorRow(
                        label = "시작",
                        date = startDate,
                        time = startTime,
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

                    DateTimeSelectorRow(
                        label = "종료",
                        date = endDate,
                        time = endTime,
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
                            studyId?.let {
                                keyboard?.hide()
                                onCreateSchedule(title, locationMemo, startDateTime, endDateTime)
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
                width = 1.dp,
                color = if (isFocused) SpotTheme.colors.B500 else SpotTheme.colors.gray200,
                shape = RoundedCornerShape(6.dp)
            )
            .padding(horizontal = screenWidthDp(10.dp), vertical = screenHeightDp(7.dp)),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged { isFocused = it.isFocused },
                singleLine = true,
                textStyle = SpotTheme.typography.medium_500.copy(color = SpotTheme.colors.black),
                cursorBrush = SolidColor(SpotTheme.colors.B500),
                decorationBox = { innerTextField ->
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = SpotTheme.typography.medium_500,
                            color = SpotTheme.colors.gray400
                        )
                    }
                    innerTextField()
                }
            )
            Text(
                text = "(${value.length}/20)",
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
    val dateFormatter = remember { DateTimeFormatter.ofPattern("yyyy.MM.dd.") }
    val timeFormatter = remember { DateTimeFormatter.ofPattern("hh:mma", Locale.US) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, SpotTheme.colors.gray200, RoundedCornerShape(6.dp))
            .padding(horizontal = screenWidthDp(10.dp), vertical = screenHeightDp(7.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = SpotTheme.typography.medium_500,
            color = SpotTheme.colors.gray400
        )
        Row {
            DateTimeBadge(text = date.format(dateFormatter), onClick = onDateClick)
            Spacer(Modifier.width(screenWidthDp(6.dp)))
            DateTimeBadge(text = time.format(timeFormatter).lowercase(), onClick = onTimeClick)
        }
    }
}

@Composable
private fun DateTimeBadge(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .background(SpotTheme.colors.B100, RoundedCornerShape(6.dp))
            .clickable { onClick() }
            .padding(horizontal = screenWidthDp(9.dp), vertical = screenHeightDp(1.dp))
    ) {
        Text(
            text = text,
            style = SpotTheme.typography.regular_500,
            color = SpotTheme.colors.black
        )
    }
}