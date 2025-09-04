@file:OptIn(ExperimentalMaterial3Api::class)

package com.umcspot.spot.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umcspot.spot.ui.shapes.SpotShapes
import com.umcspot.spot.ui.theme.Black
import com.umcspot.spot.ui.theme.G300
import com.umcspot.spot.ui.theme.G400
import com.umcspot.spot.ui.theme.SpotTypography
import com.umcspot.spot.ui.theme.White
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Year
import java.time.YearMonth
import java.time.format.DateTimeFormatter

// 포맷터
private val dateFmt = DateTimeFormatter.ofPattern("yyyy.MM.dd.")
private val timeFmt = DateTimeFormatter.ofPattern("hh:mma")

/** 한 줄 + 인라인 달력 토글 */
@Composable
fun DateHeader(
    label: String,
    dateTime: LocalDateTime,
    onDateChange: (LocalDateTime) -> Unit,
    // 달력 커스터마이즈 전달
    eventsByDate: Map<LocalDate, Int> = emptyMap(),
    onMonthChanged: (YearMonth) -> Unit = {},
    // 스타일
    containerColor: Color = White,
    borderColor: Color = G300,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    // 외곽 컨테이너 (스샷 느낌: 연한 보더 + 둥근 모서리)
    Surface(
        shape = SpotShapes.Hard,
        color = containerColor,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        border = BorderStroke(1.dp, borderColor),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(Modifier.fillMaxWidth()) {

            // ── 상단: 라벨 + 날짜칩 + 시간칩
            CompactDateTimeRow(
                label = label,
                dateText = dateTime.format(dateFmt),
                timeText = dateTime.format(timeFmt).lowercase(),
                onClickDate = { expanded = !expanded },   // ⬅️ 날짜칩 누르면 달력 토글
                onClickTime = { /* TODO: TimePicker 연결 예정 */ },
                modifier = Modifier.fillMaxWidth()
            )

            // ── 펼쳐지는 달력
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                // 달력 패딩 약간
                Column(Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                    SpotMonthCalendar(
                        eventsByDate = eventsByDate,
                        onMonthChanged = onMonthChanged,
                        onDateSelected = { picked ->
                            // 날짜만 변경, 시간은 유지
                            val updated = dateTime.withYear(picked.year)
                                .withMonth(picked.monthValue)
                                .withDayOfMonth(picked.dayOfMonth)
                            onDateChange(updated)
                            expanded = false           // 날짜 선택 후 닫기 (원하면 유지 가능)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CompactDateTimeRow(
    label: String,
    dateText: String,
    timeText: String,
    onClickDate: () -> Unit,
    onClickTime: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = SpotTypography.bodyMedium600,
            fontSize = 12.sp,
            color = G400,
            modifier = Modifier.weight(1f)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            // 날짜 칩
            PillChip(
                text = dateText,
                onClick = onClickDate
            )
            // 시간 칩
            PillChip(
                text = timeText,
                onClick = onClickTime
            )
        }
    }
}

@Composable
private fun PillChip(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = Color(0xFFE4EDFF),      // 연한 파랑 배경
        contentColor = Black,
        shape = SpotShapes.Soft,
        modifier = modifier
            .height(24.dp)
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = SpotTypography.bodyMedium500,
                fontSize = 12.sp
            )
        }
    }
}


/* ───────── Preview ───────── */

@Preview(showBackground = true, widthDp = 380)
@Composable
private fun InlineDateCalendarFieldPreview() {
    val y = Year.now().value
    var dt by remember {
        mutableStateOf(LocalDateTime.of(y, 2, 1, 0, 0))
    }
    val events = mapOf(
        LocalDate.of(y, 2, 5) to 1,
        LocalDate.of(y, 2, 12) to 2
    )

    DateHeader(
        label = "시작",
        dateTime = dt,
        onDateChange = { dt = it },
        eventsByDate = events,
        modifier = Modifier.padding(10.dp)
    )
}
