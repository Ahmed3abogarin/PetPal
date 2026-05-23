package com.vtol.petpal.presentation.calender

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kizitonwose.calendar.compose.ContentHeightMode
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.vtol.petpal.presentation.calender.components.CalendarDayCell
import com.vtol.petpal.presentation.calender.components.HighlightCard
import com.vtol.petpal.presentation.components.AppIconButton
import com.vtol.petpal.ui.theme.BackgroundColor
import com.vtol.petpal.ui.theme.PetPalTheme
import com.vtol.petpal.util.AppColors.petPalGradient
import com.vtol.petpal.util.showToast
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun CalenderScreen(
    modifier: Modifier = Modifier,
    state: CalendarState,
) {

    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    val context = LocalContext.current

    val calendarState = rememberCalendarState(
        firstDayOfWeek = DayOfWeek.SUNDAY,
        startMonth = YearMonth.now().minusMonths(6),
        firstVisibleMonth = YearMonth.now(),
        endMonth = YearMonth.now().plusMonths(12)
    )

    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .background(BackgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(petPalGradient)
                .statusBarsPadding()
                .padding(top = 20.dp, bottom = 32.dp)
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Calendar",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    AppIconButton(modifier = Modifier.padding(start = 16.dp)) {
                        scope.launch {
                            val targetMonth = calendarState.firstVisibleMonth.yearMonth.minusMonths(1)
                            calendarState.animateScrollToMonth(targetMonth)
                        }
                    }

                    Text(
                        text = calendarState.firstVisibleMonth.yearMonth.month.name.lowercase()
                            .replaceFirstChar { it.uppercase() },
                        modifier = Modifier.weight(1f), // Takes up all middle space forcing text to center
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp, // Bumped slightly for better header visibility
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )

                    AppIconButton(modifier = Modifier.rotate(180f).padding(start = 16.dp)) {
                        scope.launch {
                            val targetMonth = calendarState.firstVisibleMonth.yearMonth.plusMonths(1)
                            calendarState.animateScrollToMonth(targetMonth)
                        }
                    }
                }
            }
        }


        // calendar days
        Box(
            modifier = Modifier
                .offset(y = (-22).dp)
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color.White)
                .padding(8.dp)
                .fillMaxWidth()
                .weight(1f)
        ) {
            HorizontalCalendar(
                contentHeightMode = ContentHeightMode.Fill,
                dayContent = { day ->
                    CalendarDayCell(
                        day = day,
                        selectedDate = selectedDate,
                        tasks = state.tasks[day.date].orEmpty(),
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
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = month.yearMonth.month.name,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // week days
                            val weekDays =
                                listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

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

        // Highlight card for the selected date
        Box(modifier = Modifier.weight(0.8f)) {
            HighlightCard(
                tasks = state.tasks[selectedDate],
                date = selectedDate,
                petMap = state.petMap,
                showToast = {context.showToast("No tasks")}
            )
        }
    }
}

@Preview
@Composable
fun CalendarPreview() {
    PetPalTheme {
        CalenderScreen(state = CalendarState())
    }
}