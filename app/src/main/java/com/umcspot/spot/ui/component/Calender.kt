@file:OptIn(ExperimentalMaterial3Api::class)

package com.umcspot.spot.ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.umcspot.spot.R
import com.umcspot.spot.ui.shapes.SpotShapes
import com.umcspot.spot.ui.theme.B400
import com.umcspot.spot.ui.theme.B500
import com.umcspot.spot.ui.theme.Black
import com.umcspot.spot.ui.theme.G300
import com.umcspot.spot.ui.theme.SpotTypography
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Year
import java.time.YearMonth

@Composable
fun SpotMonthCalendar(
    modifier: Modifier = Modifier,
    onDateSelected: (LocalDate) -> Unit = {},
    eventsByDate: Map<LocalDate, Int> = emptyMap(),
    onMonthChanged: (YearMonth) -> Unit = {},
) {
    val nowMonth = YearMonth.now()
    val firstDow = DayOfWeek.MONDAY

    val state = rememberCalendarState(
        startMonth = nowMonth.minusMonths(12),
        endMonth   = nowMonth.plusMonths(12),
        firstDayOfWeek = firstDow
    )
    val scope = rememberCoroutineScope()
    var selected by remember { mutableStateOf<LocalDate?>(null) }

    val visibleMonth by remember {
        derivedStateOf { state.firstVisibleMonth.yearMonth }
    }

    LaunchedEffect(visibleMonth) {
        onMonthChanged(visibleMonth)
    }

    val currentYm = visibleMonth
    val canPrev = currentYm > state.startMonth
    val canNext = currentYm < state.endMonth

    Column(modifier = modifier.fillMaxWidth().padding(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { scope.launch { state.animateScrollToMonth(currentYm.minusMonths(1)) } },
                enabled = canPrev
            ) {
                Icon(painterResource(R.drawable.arrow_left), contentDescription = "이전 달", tint = B500)
            }

            Text(
                text = "${currentYm.year}년 ${currentYm.monthValue}월",
                style = SpotTypography.bodyMedium600,
                fontSize = 26.sp,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )

            IconButton(
                onClick = { scope.launch { state.animateScrollToMonth(currentYm.plusMonths(1)) } },
                enabled = canNext
            ) {
                Icon(painterResource(R.drawable.arrow_right), contentDescription = "다음 달", tint = B500)
            }
        }

        WeekdayRow()

        HorizontalCalendar(
            state = state,
            contentPadding = PaddingValues(horizontal = 2.dp),
            dayContent = { day ->
                DayCellStyled(
                    day = day,
                    isSelected = selected == day.date,
                    onClick = {
                        if (day.position == DayPosition.MonthDate) {
                            selected = day.date
                            onDateSelected(day.date)
                        }
                    },
                    eventCount = eventsByDate[day.date] ?: 0
                )
            }
        )
    }
}


@Composable
private fun WeekdayRow() {
    val labels = listOf("월","화","수","목","금","토","일")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 26.dp, bottom = 8.dp, start = 2.dp, end = 2.dp)
    ) {
        labels.forEachIndexed { idx, label ->
            Text(
                text = label,
                style = SpotTypography.bodyMedium600,
                fontSize = 16.sp,
                // ✅ 일요일 컬럼은 헤더도 B500
                color = if (idx == 6) B500 else Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
private fun DayCellStyled(
    day: CalendarDay,
    isSelected: Boolean,
    onClick: () -> Unit,
    eventCount: Int = 0,
) {
    val isThisMonth = day.position == DayPosition.MonthDate
    val isSunday = day.date.dayOfWeek == DayOfWeek.SUNDAY

    BoxWithConstraints(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(1.dp),
        contentAlignment = Alignment.Center
    ) {
        val cellSize = min(maxWidth, maxHeight)
        val circleSize = cellSize * 0.65f

        val interactionSource = remember { MutableInteractionSource() }
        val isPressed by interactionSource.collectIsPressedAsState()

        val clickableModifier = Modifier.clickable(
            enabled = isThisMonth,
            interactionSource = interactionSource,
            indication = null,
            onClick = onClick
        )

        if (isSelected) {
            Column(
                modifier = Modifier
                    .size(circleSize)
                    .clip(shape = SpotShapes.Hard)
                    .background(
                        color = if (isPressed)
                            B400.copy(alpha = 0.6f) // pressed
                        else
                            B400.copy(alpha = 0.10f) // normal
                    )
                    .then(clickableModifier),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = day.date.dayOfMonth.toString(),
                    style = SpotTypography.bodyMedium600,
                    fontSize = 18.sp,
                    color = when {
                        !isThisMonth -> G300
                        isSunday     -> B500
                        else         -> Black
                    }
                )
                if (eventCount > 0 && isThisMonth) {
                    Spacer(Modifier.size(2.dp))
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(B400)
                    )
                }
            }
        } else {
            // 일반 날짜
            Column(
                modifier = clickableModifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = day.date.dayOfMonth.toString(),
                    style = SpotTypography.bodyMedium600,
                    fontSize = 18.sp,
                    color = when {
                        !isThisMonth -> G300
                        isSunday     -> B500
                        else         -> Black
                    }
                )
                if (eventCount > 0 && isThisMonth) {
                    Spacer(Modifier.size(2.dp))
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(B400)
                    )
                }
            }
        }
    }
}



@Preview(showBackground = true, widthDp = 500)
@Composable
private fun SpotMonthCalendarPreview_WithEvents() {
    val y = Year.now().value           // 현재 연도 기준
    val events = mapOf(
        LocalDate.of(2024, 9, 5) to 1,    // ✅ 9월 5일
        LocalDate.of(2024, 9, 12) to 2    // ✅ 9월 12일 (개수 예시로 2)
    )

    SpotMonthCalendar(
        eventsByDate = events,
        modifier = Modifier.padding(10.dp),
        onMonthChanged = {}
    )
}
