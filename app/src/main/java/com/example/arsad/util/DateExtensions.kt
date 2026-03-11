package com.example.arsad.util


import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


fun Long.toDisplayDate(): String {
    val sdf = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
    return sdf.format(Date(this))
}

fun formatTime(hour: Int, minute: Int): String {
    val cal = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
    }
    val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return sdf.format(cal.time)
}

fun Long.formatTimestamp(pattern: String = "EEEE, dd MMM yyyy"): String {
    val date = Date(this * 1000)
    val locale = Locale.getDefault()
    val formatter = SimpleDateFormat(pattern, locale)
    val dateString = formatter.format(date)

    return if (locale.language == "ar") {
        dateString.toArabicNumbers()
    } else {
        dateString
    }
}

fun String.toArabicNumbers(): String {
    val arabicDigits = charArrayOf('٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩')
    return this.map { char ->
        if (char in '0'..'9') arabicDigits[char - '0'] else char
    }.joinToString("")
}

fun formatTo12Hour(dtTxt: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        // "h:mm a" -> 3:00 PM
        val outputFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
        val date = inputFormat.parse(dtTxt)
        val isArabic = Locale.getDefault().language == "ar"
        if (isArabic) {
            outputFormat.format(date!!).toArabicNumbers()
        } else {
            outputFormat.format(date!!)
        }
    } catch (e: Exception) {
        dtTxt.substring(11, 16)
    }
}


fun Long.formatToOnlyDayName(isAbbreviated: Boolean = false): String {
    val date = Date(this * 1000)
    val pattern = if (isAbbreviated) "EEE" else "EEEE"
    val sdf = SimpleDateFormat(pattern, Locale.getDefault())
    return sdf.format(date)
}

fun Number.localize(): String {
    val numberStr = this.toString()
    val isArabic = Locale.getDefault().language == "ar"
    return if (isArabic) {
        numberStr.toArabicNumbers()
    } else {
        numberStr
    }
}

