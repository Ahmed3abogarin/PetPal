package com.vtol.petpal.presentation.calender

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.vtol.petpal.domain.model.tasks.Task
import com.vtol.petpal.ui.theme.MainPurple
import java.time.DayOfWeek
import java.time.YearMonth

@Composable
fun CalenderScreen(modifier: Modifier = Modifier, viewModel: CalenderViewModel = hiltViewModel()) {

    val calendarTasks by viewModel.calendarTasks.collectAsState()


    val calendarState = rememberCalendarState(
        firstDayOfWeek = DayOfWeek.SUNDAY,
        startMonth = YearMonth.now(),
        endMonth = YearMonth.now().plusMonths(3)
    )

    Column(modifier = modifier
        .fillMaxWidth()
        .statusBarsPadding()) {


        // Weekday Header


        // calendar days
        HorizontalCalendar(
            dayContent = { day ->
                CalendarDayCell(
                    day = day,
                    tasks = calendarTasks[day.date].orEmpty()
                )
            },
            monthHeader = { month ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp)
                ) {
                    Text(
                        text = month.yearMonth.month.name,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // week days
                        val weekDays = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

                        weekDays.forEach { day ->
                            Text(
                                text = day,
                                fontSize = 12.sp,
                                color = Color.Gray,
                            )
                        }
                    }
                }
            },
            state = calendarState

        )
    }
}

@Composable
fun CalendarDayCell(
    day: CalendarDay,
    tasks: List<Task>,
) {
    Column(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(4.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable { /* show bottom sheet */ },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),
            fontSize = 14.sp
        )

        if (tasks.isNotEmpty()) {
            Spacer(Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                repeat(minOf(tasks.size, 3)) {
                    Box(
                        Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(
                                if (tasks.any { it.isCompleted }) Color.Gray
                                else MainPurple
                            )
                    )
                }
            }
        }
    }
}
