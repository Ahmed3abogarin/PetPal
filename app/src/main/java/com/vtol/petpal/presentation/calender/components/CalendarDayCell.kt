package com.vtol.petpal.presentation.calender.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.vtol.petpal.domain.model.tasks.TaskUi
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme
import java.time.LocalDate

@Composable
fun CalendarDayCell(
    day: CalendarDay,
    selectedDate: LocalDate,
    tasks: List<TaskUi>,
    onDateClicked: (LocalDate) -> Unit
) {
    val isSelected = day.date == selectedDate
    Column(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(4.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(color = if (isSelected) MainPurple else Color.Transparent)
            .clickable {
                // show day's tasks highlight
                onDateClicked(day.date)
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),
            color = if (isSelected) Color.White else Color.Black,
            fontSize = 14.sp
        )

        if (tasks.isNotEmpty()) {
            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                repeat(minOf(tasks.size, 3)) {
                    Box(
                        Modifier
                            .size(4.dp)
                            .clip(CircleShape)
                            .background(
                                if (tasks.any { it.isCompleted }) Color.Gray
                                else Color.Green
                            )
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun CellPreview(){
    PetPalTheme {
        CalendarDayCell(
            day = CalendarDay(LocalDate.now(), DayPosition.MonthDate),
            selectedDate = LocalDate.now(),
            tasks = listOf(),
            onDateClicked = {}
        )
    }
}