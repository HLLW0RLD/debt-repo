package com.example.debt.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.debt.app.data.repo.LocalRepository
import com.example.debt.data.model.Debtor
import com.example.debt.data.model.Transaction
import com.example.debt.data.model.TransactionType
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class MainDebtorViewModel(private val repository: LocalRepository) : ViewModel(), KoinComponent {
    val debtors = repository.debtors

    fun insertDebtor(debtor: Debtor) {
        viewModelScope.launch {
            val initialTransaction = Transaction(
                amount = debtor.debtAmount,
                type = TransactionType.DEBT,
                date = debtor.loanDate,
                comment = "Первоначальный долг\n${debtor.comment}",
                id = debtor.id
            )

            debtor.apply {
                transactions = mutableListOf(initialTransaction)
            }
            repository.insertDebtor(debtor)
        }
    }

    fun payDebt(debtorId: Long, paymentAmount: Double) {
        viewModelScope.launch {
            try {
                repository.payDebt(debtorId, paymentAmount)
            } catch (e: Exception) {

            }
        }
    }

    fun addDebt(debtorId: Long, additionalAmount: Double) {
        viewModelScope.launch {
            repository.addDebt(debtorId, additionalAmount)
        }
    }

    fun updateDebt(debtor: Debtor) {
        viewModelScope.launch {
            repository.updateDebt(debtor)
        }
    }

    fun deleteDebtor(id: Long) = viewModelScope.launch {
        repository.deleteDebtor(id)
    }

    fun getDebtorById(id: Long) = viewModelScope.launch {
        repository.getDebtorById(id)
    }
}