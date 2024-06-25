package com.example.firebasepdp.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

private const val DATE_FORMAT = "dd.MM.yyyy"

object DateFormatter {
    fun formatDate(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern(DATE_FORMAT)
        return date.format(formatter)
    }
}