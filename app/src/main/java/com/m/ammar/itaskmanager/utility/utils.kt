package com.m.ammar.itaskmanager.utility

import java.text.SimpleDateFormat
import java.util.*

fun Long.toReadableDate(): String {
    val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    return formatter.format(Date(this))
}
