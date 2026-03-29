package com.example.debt.data.model

import com.example.debt.utils.getCurrentDateTime
import java.util.UUID

data class Debt(
    val id: String = "",
    val name: String,
    val isMine: Boolean = false,
    val telegramNick: String?,
    val debtAmount: Double,
    var loanDate: String = getCurrentDateTime(),
    val returnDate: String? = "", // опционально
    var comment: String? = "", // опционально
    var transactions: List<Transaction> = listOf()
) {
    // Только для UI, без бизнес-логики
    val isPaid: Boolean get() = debtAmount == 0.0
}

data class Transaction(
    val id: String,
    val amount: Double,
    val date: String,
    val type: TransactionType,
    val comment: String?
)

enum class TransactionType {
    DEBT,
    PAYMENT
}