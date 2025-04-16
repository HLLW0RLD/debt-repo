package com.example.debt.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.debtdiary.utils.getCurrentDateTime
import java.util.Date

@Entity(tableName = "debtors")
data class Debtor(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val telegramNick: String,
    val debtAmount: Double,
    val loanDate: String = getCurrentDateTime(),
    val returnDate: String? = null, // опционально
    val comment: String? = null // опционально
)
