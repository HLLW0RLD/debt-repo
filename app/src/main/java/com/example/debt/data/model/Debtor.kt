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
    val telegramNick: String,
    val debtAmount: Double,
    val loanDate: String = getCurrentDateTime(),
    @ColumnInfo(name = "transactions")
    private val _transactions: String = "",
    val returnDate: String? = null, // опционально
    var comment: String? = null // опционально
) {
    fun get_transactions(): String = _transactions
    val transactions: MutableList<Transaction>
        get() = try {
            val type: Type = object : TypeToken<MutableList<Transaction>>() {}.type
            Gson().fromJson(_transactions, type) ?: mutableListOf()
        } catch (e: Exception) {
            errorLog(e)
            mutableListOf()
        }

    fun addPayment(amount: Double): Debtor {
        val newTransaction = Transaction(
            amount = amount,
            type = TransactionType.PAYMENT,
            id = id,
            date = loanDate,
            comment = comment
        )
        val addTransactions = transactions + newTransaction
        return this.copy(
            debtAmount = debtAmount - amount,
            _transactions = Gson().toJson(addTransactions)
        )
    }

    fun addDebt(amount: Double): Debtor {
        val newTransaction = Transaction(
            amount = amount,
            type = TransactionType.DEBT,
            id = id,
            date = loanDate,
            comment = comment
        )
        val updatedTransactions = transactions + newTransaction
        return this.copy(
            debtAmount = debtAmount + amount,
            _transactions = Gson().toJson(updatedTransactions)
        )
    }
}