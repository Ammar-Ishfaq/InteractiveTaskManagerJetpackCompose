package com.m.ammar.itaskmanager.utility

import java.text.SimpleDateFormat
import java.util.*

/**
 *  To convert the date from long to human-readable format
 */
fun Long.toReadableDate(): String {
    val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    return formatter.format(Date(this))
}
