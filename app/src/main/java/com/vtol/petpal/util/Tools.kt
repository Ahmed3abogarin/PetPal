package com.vtol.petpal.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun LocalDate.formatDate(): String {
    return this.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
}