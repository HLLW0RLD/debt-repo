package com.example.debt.utils

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import com.example.debt.App
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import androidx.core.net.toUri
import com.example.debt.app.utils.LogUtils.errorLog

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

        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            errorLog(e)
            toast("Telegram не установлен")
        }

    } catch (e: PackageManager.NameNotFoundException) {
        errorLog(e)
    }
}