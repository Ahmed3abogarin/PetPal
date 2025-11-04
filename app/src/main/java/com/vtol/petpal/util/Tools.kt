package com.vtol.petpal.util

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun LocalDate.formatDate(): String {
    return this.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
}

fun Timestamp.formatDate(): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return sdf.format(this.toDate())
}
