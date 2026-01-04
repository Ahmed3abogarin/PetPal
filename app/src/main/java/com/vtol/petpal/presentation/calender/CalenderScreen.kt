package com.vtol.petpal.presentation.calender

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.vtol.petpal.presentation.calender.components.CalendarDayCell
import com.vtol.petpal.presentation.calender.components.HighlightCard
import com.vtol.petpal.ui.theme.PetPalTheme
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun CalenderScreen(
    modifier: Modifier = Modifier,
    state: CalendarState,
    viewModel: CalenderViewModel = hiltViewModel(),
) {

    val calendarTasks by viewModel.calendarTasks.collectAsState()

    var selectedDate by remember { mutableStateOf(LocalDate.now()) }


    val calendarState = rememberCalendarState(
        firstDayOfWeek = DayOfWeek.SUNDAY,
        startMonth = YearMonth.now(),
        endMonth = YearMonth.now().plusMonths(3)
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .statusBarsPadding()
    ) {

        // calendar days
        Column (modifier = Modifier.weight(0.5f)){
            HorizontalCalendar(
                dayContent = { day ->
                    CalendarDayCell(
                        day = day,
                        selectedDate = selectedDate,
                        tasks = calendarTasks[day.date].orEmpty(),
                        onDateClicked = { date ->
                            selectedDate = date
                        }
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

        Spacer(modifier = modifier.height(16.dp))


        // Highlight card for the selected date
        HighlightCard(
            tasks = calendarTasks[selectedDate].orEmpty(),
            date = selectedDate,
            petMap = state.petMap
        )

    }
}


@Preview
@Composable
fun CalendarPreview() {
    PetPalTheme {

    }
}