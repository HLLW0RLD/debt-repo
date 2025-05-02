package com.example.debt.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.debt.app.utils.LogUtils.errorLog
import com.example.debt.utils.getCurrentDateTime
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

@Entity(tableName = "debtors")
data class Debtor(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val isMine: Boolean = false,
    val telegramNick: String,
    val debtAmount: Double,
    var loanDate: String = getCurrentDateTime(),
    @ColumnInfo(name = "transactions")
    var transactionsJson: String = "",
    val returnDate: String = "", // опционально
    var comment: String = "" // опционально
) {
    var transactions: MutableList<Transaction>
        get() = try {
            val type: Type = object : TypeToken<MutableList<Transaction>>() {}.type
            Gson().fromJson(transactionsJson, type) ?: mutableListOf()
        } catch (e: Exception) {
            errorLog(e)
            mutableListOf()
        }
        set(value) {
            transactionsJson = Gson().toJson(value)
        }

    fun addPayment(amount: Double): Debtor {
        val newTransaction = Transaction(
            amount = amount,
            type = TransactionType.PAYMENT,
            id = id,
            date = getCurrentDateTime(),
            comment = comment
        )
        val addTransactions = transactions + newTransaction
        return this.copy(
            debtAmount = debtAmount - amount,
            transactionsJson = Gson().toJson(addTransactions)
        )
    }

    fun addDebt(amount: Double): Debtor {
        val newTransaction = Transaction(
            amount = amount,
            type = TransactionType.DEBT,
            id = id,
            date = getCurrentDateTime(),
            comment = comment
        )
        val updatedTransactions = transactions + newTransaction
        return this.copy(
            debtAmount = debtAmount + amount,
            transactionsJson = Gson().toJson(updatedTransactions)
        )
    }
}