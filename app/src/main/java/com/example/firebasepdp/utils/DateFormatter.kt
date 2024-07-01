package com.example.firebasepdp.utils

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

object DateFormatter {

    private val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    fun formatTimestamp(timestamp: Timestamp): String {
        return dateFormat.format(timestamp.toDate())
    }
}