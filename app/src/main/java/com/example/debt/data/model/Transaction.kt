package com.example.debt.data.model

data class Transaction(
    val id: Long,
    val amount: Double,
    val date: String,
    val type: TransactionType,
    val comment: String? = null
)

enum class TransactionType(val v: String) {
    DEBT("займ"),
    PAYMENT("оплата")
}
