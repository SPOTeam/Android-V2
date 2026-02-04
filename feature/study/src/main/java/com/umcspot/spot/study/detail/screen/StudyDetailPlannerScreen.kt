package com.umcspot.spot.study.detail.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.daysOfWeek
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.SpotPlannerCalendar
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.study.detail.component.common.StudyDetailCreateButton
import com.umcspot.spot.study.detail.component.planner.StudyDetailScheduleItem
import com.umcspot.spot.study.detail.component.planner.StudyDetailToDoItem
import com.umcspot.spot.study.detail.component.common.StudyMemberItem
import com.umcspot.spot.study.detail.mapper.toUiTime
import com.umcspot.spot.study.detail.model.StudyPlannerState
import com.umcspot.spot.study.model.StudyMemberModel
import com.umcspot.spot.ui.extension.noRippleClickable
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp
import kotlinx.collections.immutable.ImmutableList
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields

@Composable
fun StudyDetailPlannerScreen(
    studyId: Long,
    plannerState: StudyPlannerState,
    members: ImmutableList<StudyMemberModel>,
    onDateSelected: (LocalDate) -> Unit,
    onMonthChanged: (Int, Int) -> Unit,
    onAddingTodo: () -> Unit,
    onTodoCreate: (Long, String) -> Unit,
    onTodoToggle: (Long, Long, Boolean) -> Unit,
    onTodoDelete: (Long, Long) -> Unit,
    onMemberSelected: (Long) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    var isAddingTodo by remember { mutableStateOf(false) }
    var newTodoText by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    val daysOfWeekList = remember { daysOfWeek(firstDayOfWeek = DayOfWeek.MONDAY) }
    val monthState = rememberCalendarState(
        startMonth = YearMonth.now().minusMonths(12),
        endMonth = YearMonth.now().plusMonths(12),
        firstVisibleMonth = YearMonth.from(plannerState.selectedDate),
        firstDayOfWeek = daysOfWeekList.first()
    )

    val isAvailableDate = remember(plannerState.selectedDate) {
        val today = LocalDate.now()
        !plannerState.selectedDate.isBefore(today)
    }

    LaunchedEffect(monthState.firstVisibleMonth) {
        val ym = monthState.firstVisibleMonth.yearMonth
        onMonthChanged(ym.year, ym.monthValue)
    }

    val monthTitle = plannerState.selectedDate.format(DateTimeFormatter.ofPattern("yyyy년 M월"))
    val weekNumber = plannerState.selectedDate.get(WeekFields.of(DayOfWeek.MONDAY, 1).weekOfMonth())
    val weekTitle = "$monthTitle ${weekNumber}주차"

    Column(modifier = Modifier
        .fillMaxWidth()
        .animateContentSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = screenWidthDp(13.dp)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = if (isExpanded) monthTitle else weekTitle, style = SpotTheme.typography.h4)
            Icon(
                painter = painterResource(id = if (isExpanded) R.drawable.arrow_up else R.drawable.arrow_down),
                contentDescription = null,
                tint = B500,
                modifier = Modifier
                    .size(screenWidthDp(14.dp))
                    .noRippleClickable { isExpanded = !isExpanded }
            )
        }

        Spacer(modifier = Modifier.height(screenHeightDp(6.dp)))

        SpotPlannerCalendar(
            isExpanded = isExpanded,
            monthState = monthState,
            selectedDate = plannerState.selectedDate,
            daysOfWeek = daysOfWeekList,
            onDateSelected = onDateSelected
        )

        Spacer(modifier = Modifier.height(screenHeightDp(16.dp)))

        plannerState.selectedDaySchedules.forEach { schedule ->
            StudyDetailScheduleItem(
                title = schedule.title,
                timeRange = "${schedule.startAt.toUiTime()} - ${schedule.endAt.toUiTime()}",
                isNow = schedule.isNow
            )
            Spacer(modifier = Modifier.height(screenHeightDp(12.dp)))
        }

        HorizontalDivider(thickness = 0.5.dp, color = SpotTheme.colors.gray300)
        Spacer(modifier = Modifier.height(screenHeightDp(16.dp)))

        StudyDetailCreateButton(
            text = "Todo",
            isStudyMember = true,
            enabled = isAvailableDate,
            onButtonClick = {
                if (isAvailableDate) {
                    onAddingTodo()
                    isAddingTodo = true
                }
            }
        )

        Spacer(modifier = Modifier.height(screenHeightDp(12.dp)))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(screenWidthDp(13.dp))
        ) {
            items(members) { member ->
                StudyMemberItem(
                    name = member.name,
                    profileUrl = member.profileUrl,
                    isSelected = plannerState.selectedMemberId == member.id.toString(),
                    onClick = { onMemberSelected(member.id) }
                )
            }
        }

        Spacer(modifier = Modifier.height(screenHeightDp(8.dp)))

        // Todo 리스트
        if (isAddingTodo) {
            StudyDetailToDoItem(
                text = newTodoText,
                isCompleted = false,
                isMyToDo = true,
                isEditing = true,
                onTextChange = { newTodoText = it },
                onEnterPressed = {
                    if (newTodoText.isNotBlank()) {
                        onTodoCreate(studyId, newTodoText)
                        newTodoText = ""
                        isAddingTodo = false
                        keyboardController?.hide()
                    }
                },
                onDeleteClick = { isAddingTodo = false }
            )
        }

        val filteredTodos =
            plannerState.todoList.filter { it.memberId == plannerState.selectedMemberId }

        if (filteredTodos.isEmpty() && !isAddingTodo) {
            Text(
                text = "아직 할 일이 작성되지 않았어요.",
                style = SpotTheme.typography.regular_400,
                color = SpotTheme.colors.gray400,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = screenHeightDp(40.dp)),
                textAlign = TextAlign.Center
            )
        } else {
            filteredTodos.forEach { todo ->
                StudyDetailToDoItem(
                    text = todo.content,
                    isCompleted = todo.isCompleted,
                    isMyToDo = true,
                    onCheckedChange = {
                        onTodoToggle(studyId, todo.id, todo.isCompleted)
                    },
                    onDeleteClick = { onTodoDelete(studyId, todo.id) }
                )
            }
        }
        Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))
    }
}