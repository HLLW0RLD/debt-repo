package com.example.debt.utils

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.ui.graphics.Color
import com.example.debt.App
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.core.net.toUri
import com.example.debt.app.utils.LogUtils.errorLog
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

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
    val dateFormat = SimpleDateFormat("HH:mm:ss dd.MM.yyyy")
    val date = Date()
    return dateFormat
        .format(date)
}

fun String.capitalizeFirstLetter(): String {
    if (this.isEmpty()) return this
    return this.substring(0, 1).uppercase() + this.substring(1)
}

fun String.openTelegramChat() {
    val context = App.appInstance
    val nick = this.replace("@", "")
    val telegramUrl = "https://t.me/$nick"
    val intent = Intent(Intent.ACTION_VIEW, telegramUrl.toUri())

    try {
        context.packageManager.getPackageInfo("org.telegram.messenger", 0)
        intent.setPackage("org.telegram.messenger")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)

    } catch (e: Exception) {
        toast("Telegram не установлен")
        errorLog(e)
    }
}

fun String.setColorDate(): Color? {
    try {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

        if (!this.matches(Regex("\\d{2}\\.\\d{2}\\.\\d{4}"))) {
            return null
            throw IllegalArgumentException("Дата должна быть в формате dd.MM.yyyy")
        }

        val inputDate = LocalDate.parse(this, formatter)
        val currentDate = LocalDate.now()

        val daysDifference = ChronoUnit.DAYS.between(currentDate, inputDate)

        return when {
            daysDifference > 8 -> Color.Green
            daysDifference in 3..7 -> Color.Yellow
            daysDifference in 1..2 -> Color.hsl(35f, 1f, 0.5f)
            daysDifference == 0L -> Color.Red
            else -> Color.White
        }
    } catch (e : Exception) {
        errorLog("string: $this \nerror $e")
    }
    return Color.Red
}

internal fun Context.findActivity(): ComponentActivity {
    var context = this
    while (context is ContextWrapper) {
        if (context is ComponentActivity) return context
        context = context.baseContext
    }
    throw IllegalStateException("Picture in picture should be called in the context of an Activity")
}