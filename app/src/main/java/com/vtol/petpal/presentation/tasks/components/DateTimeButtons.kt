package com.vtol.petpal.presentation.tasks.components

import android.app.TimePickerDialog
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.R
import com.vtol.petpal.presentation.home.components.TaskDatePicker
import com.vtol.petpal.ui.theme.LightPurple
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme
import com.vtol.petpal.util.convertDate
import com.vtol.petpal.util.convertTime
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun DateTimeButtons(
    selectedTime: LocalTime?,
    selectedDate: LocalDate?,
    onDateChanged: (LocalDate) -> Unit,
    onTimeChanged: (LocalTime) -> Unit
) {

    val context = LocalContext.current
    var showDatePicker by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = LightPurple,
                    shape = RoundedCornerShape(12.dp)
                )
                .clickable { showDatePicker = true }
                .padding(14.dp)
                .weight(1f)
            ,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center

        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(R.drawable.ic_calendar_outlined),
                contentDescription = null,
                tint = MainPurple
            )
            Spacer(modifier = Modifier.width(10.dp))


            Text(
                text = selectedDate?.convertDate() ?: "Set date",
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
        }

        Row(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = LightPurple,
                    shape = RoundedCornerShape(12.dp)
                )
                .clickable {
                    val baseTime = selectedTime ?: LocalTime.now()
                    TimePickerDialog(
                        context,
                        { _, hour, minute ->
                            onTimeChanged(LocalTime.of(hour, minute))
                        },
                        baseTime.hour,
                        baseTime.minute,
                        true
                    ).show()
                }
                .padding(14.dp)
                .weight(1f)
                ,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center

        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = Icons.Default.AccessTime,
                contentDescription = null,
                tint = MainPurple
            )
            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = selectedTime?.convertTime() ?: "Set time",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }

    }

    if (showDatePicker) {
        TaskDatePicker(
            onDateSelected = { date ->
                onDateChanged(date)
                showDatePicker = false
            },
            onDismiss = {
                showDatePicker = false
            }
        )
    }
}

@Preview
@Composable
fun MyPreview() {
    PetPalTheme {
        DateTimeButtons(onDateChanged = {}, selectedDate = LocalDate.now(), selectedTime = LocalTime.now(), onTimeChanged = {})
    }
}