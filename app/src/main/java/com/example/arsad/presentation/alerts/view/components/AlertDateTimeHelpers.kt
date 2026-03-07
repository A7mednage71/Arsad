package com.example.arsad.presentation.alerts.view.components

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


internal fun Long.toDisplayDate(): String {
    val sdf = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
    return sdf.format(Date(this))
}

internal fun formatTime(hour: Int, minute: Int): String {
    val cal = Calendar.getInstance()
        .apply { set(Calendar.HOUR_OF_DAY, hour); set(Calendar.MINUTE, minute) }
    return SimpleDateFormat("hh:mm a", Locale.getDefault()).format(cal.time)
}

