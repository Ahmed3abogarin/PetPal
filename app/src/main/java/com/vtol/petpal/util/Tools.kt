package com.vtol.petpal.util

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
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

fun LocalDate.convertDate(): String{
    return this.format(DateTimeFormatter.ofPattern("EEE, dd MMM yyyy"))
}