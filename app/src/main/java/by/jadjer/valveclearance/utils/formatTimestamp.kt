package by.jadjer.valveclearance.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd.MM.yyyy HH:MM", Locale.getDefault())

    return sdf.format(Date(timestamp))
}
