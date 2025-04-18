package com.example.debtdiary.utils

import android.widget.Toast
import com.example.debtdiary.App
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

val LENGTH_LONG = Toast.LENGTH_LONG
val LENGTH_SHORT = Toast.LENGTH_SHORT

fun Any.toast(msg: Any?, duration: Int = LENGTH_SHORT) {
    Toast.makeText(
        App.appInstance,
        if (msg == null) this.toString() else msg.toString(),
        duration)
        .show()
}

fun getCurrentDate(): String {
    val dateFormat = SimpleDateFormat("yyyy_MM_dd", Locale.getDefault())
    val date = Date()
    return dateFormat.format(date)
}

fun getCurrentDateTime(): String {
    val dateFormat = SimpleDateFormat("yyyy_MM_dd_HH.mm.ss", Locale.getDefault())
    val date = Date()
    return dateFormat.format(date)
}

fun String.capitalizeFirstLetter(): String {
    if (this.isEmpty()) return this
    return this.substring(0, 1).uppercase() + this.substring(1)
}