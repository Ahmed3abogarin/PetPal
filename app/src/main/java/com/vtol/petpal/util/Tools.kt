package com.vtol.petpal.util

import android.content.Context
import android.widget.Toast
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vtol.petpal.domain.model.tasks.TaskType
import com.vtol.petpal.domain.model.tasks.TaskUi
import com.vtol.petpal.domain.model.tasks.details.FoodDetails
import com.vtol.petpal.domain.model.tasks.details.MedDetails
import com.vtol.petpal.domain.model.tasks.details.VetDetails
import com.vtol.petpal.domain.model.tasks.details.WalkDetails
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

fun Long.formatDate(): String {
    val date = Date(this)
    val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    return formatter.format(date)
}

fun Long?.toAgeString(): String {
    if (this == null) return "Unknown"
    val now = System.currentTimeMillis()
    val diffInMillis = now - this

    val totalDays = TimeUnit.MILLISECONDS.toDays(diffInMillis)
    val years = totalDays / 365
    val months = (totalDays % 365) / 30

    return when {
        years > 0 && months > 0 -> "$years years $months months"
        years > 0 -> "$years year${if (years > 1) "s" else ""}"
        months > 0 -> "$months month${if (months > 1) "s" else ""}"
        else -> "Less than a month"
    }
}

fun LocalTime.convertTime(): String{
    return this.format(DateTimeFormatter.ofPattern("hh:mm a"))
}
fun LocalDate.convertDate(): String{
    return this.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
}

fun Long.toLocalDate(): LocalDate =
    Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

fun Long.toTimeString(): String {
    val time = Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalTime()
    return time.format(DateTimeFormatter.ofPattern("h:mm a"))
}

fun Long.toDateTimeString(): String {
    val dateTime = Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()

    return dateTime.format(
        DateTimeFormatter.ofPattern("dd MMM yyyy, h:mm a")
    )
}

fun Long.toRelativeTime(): String {
    val now = System.currentTimeMillis()
    val diff = now - this

    val minute = 60_000
    val hour = 60 * minute
    val day = 24 * hour

    return when {
        diff < minute -> "Just now"
        diff < hour -> "${diff / minute} min ago"
        diff < day -> "${diff / hour} hour${if (diff / hour > 1) "s" else ""} ago"
        diff < day * 2 -> "Yesterday"
        else -> "${diff / day} days ago"
    }
}


fun Float.toFormattedDistance(): String {
    return if (this >= 1000) {
        String.format(Locale.US,"%.1f km", this / 1000) // e.g., "2.4 km"
    } else {
        String.format(Locale.US,"%.0f m", this) // e.g., "850 m"
    }
}


fun Context.showToast(text: String = "Not available yet") {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

}

fun Context.getVersionName(): String{
    val packageInfo = packageManager.getPackageInfo(packageName, 0)
    return packageInfo.versionName ?: "Unknown"
}

fun String.truncate(limit: Int) = if (length > limit) take(limit) + "…" else this

fun getGreeting(): String {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

    return when (hour) {
        in 5..11 -> "Good Morning️"
        in 12..16 -> "Good Afternoon"
        in 17..20 -> "Good Evening"
        else -> "Good Night"
    }
}

fun Modifier.dashedCircleBorder(
    color: Color,
    strokeWidth: Dp = 2.dp
) = drawBehind {
    drawCircle(
        color = color,
        radius = size.minDimension / 2 - strokeWidth.toPx() / 2,
        style = Stroke(
            width = strokeWidth.toPx(),
            pathEffect = PathEffect.dashPathEffect(
                floatArrayOf(18f, 18f)
            )
        )
    )
}

fun getPetTaskTitle(task: TaskUi, petName: String): Pair<String, String> {
    return when (task.type) {
        TaskType.FEED -> {
            val d = task.details as? FoodDetails
            "Feed $petName" to (d?.let { "${it.amount} of ${it.brand}" }
                ?: "")
        }

        TaskType.MEDICATION -> {
            val d = task.details as? MedDetails
            "$petName's Medication" to (d?.let {
                if (it.medicineName.isBlank()) return@let ""
                "${it.medicineName} • ${it.dosage}"
            } ?: "")
        }

        TaskType.WALK -> {
            val d = task.details as? WalkDetails
            "Walk with $petName" to (d?.let {
                "${it.durationMinutes} min • ${it.location}"
            } ?: ""
                    )
        }

        TaskType.VET -> {
            val d = task.details as? VetDetails
            "Vet Visit for $petName" to (d?.let { "${it.clinicName} • ${it.reason}" }
                ?: "")
        }
    }
}

fun Long.toFriendlyDate(): String {
    val taskDate = Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

    return when (taskDate) {
        LocalDate.now() -> "Today"
        LocalDate.now().plusDays(1) -> "Tomorrow"
        else -> taskDate.format(
            DateTimeFormatter.ofPattern("EEE, MMM d")
        )
    }
}