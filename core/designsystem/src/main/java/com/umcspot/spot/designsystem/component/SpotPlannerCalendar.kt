package com.umcspot.spot.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.core.DayPosition
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.B100
import com.umcspot.spot.designsystem.theme.B400
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.Black
import com.umcspot.spot.designsystem.theme.G300
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.ui.extension.screenHeightDp
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun SpotPlannerCalendar(
    isExpanded: Boolean,
    monthState: CalendarState,
    selectedDate: LocalDate,
    daysOfWeek: List<DayOfWeek>,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = screenHeightDp(4.dp))
        ) {
            for (dayOfWeek in daysOfWeek) {
                Text(
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN),
                    style = SpotTheme.typography.regular_500,
                    color = if (dayOfWeek == DayOfWeek.SUNDAY) SpotTheme.colors.B500 else SpotTheme.colors.Black
                )
            }
        }

        if (isExpanded) {
            HorizontalCalendar(
                state = monthState,
                userScrollEnabled = true,
                dayContent = { day ->
                    DayCell(
                        date = day.date,
                        isCurrentMonth = day.position == DayPosition.MonthDate,
                        isSelected = selectedDate == day.date,
                        onClick = onDateSelected
                    )
                }
            )
        } else {
            CustomWeekRow(
                selectedDate = selectedDate,
                onDateSelected = onDateSelected
            )
        }
    }
}

@Composable
fun CustomWeekRow(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val daysFromMonday = (selectedDate.dayOfWeek.value - 1).toLong()
    val startOfWeek = selectedDate.minusDays(daysFromMonday)

    Row(modifier = Modifier.fillMaxWidth()) {
        for (i in 0..6) {
            val date = startOfWeek.plusDays(i.toLong())
            Box(modifier = Modifier.weight(1f)) {
                DayCell(
                    date = date,
                    isCurrentMonth = true,
                    isSelected = selectedDate == date,
                    onClick = onDateSelected
                )
            }
        }
    }
}

@Composable
private fun DayCell(
    date: LocalDate,
    isCurrentMonth: Boolean,
    isSelected: Boolean,
    onClick: (LocalDate) -> Unit
) {
    val isSunday = date.dayOfWeek == DayOfWeek.SUNDAY
    val isToday = date == LocalDate.now()

    Column(
        modifier = Modifier
            .aspectRatio(1.3f)
            .clickable(
                enabled = isCurrentMonth,
                onClick = { onClick(date) },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(29.dp)
                .clip(SpotShapes.Hard)
                .background(if (isSelected) SpotTheme.colors.B100 else Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = date.dayOfMonth.toString(),
                    style = SpotTheme.typography.regular_500.copy(fontSize = 16.sp),
                    color = when {
                        !isCurrentMonth -> SpotTheme.colors.G300
                        isSunday -> SpotTheme.colors.B500
                        else -> SpotTheme.colors.Black
                    }
                )

                if (isToday && isCurrentMonth) {
                    Box(
                        modifier = Modifier
                            .padding(top = 2.dp)
                            .size(4.dp)
                            .background(SpotTheme.colors.B400, CircleShape)
                    )
                } else {
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }
        }
    }
}