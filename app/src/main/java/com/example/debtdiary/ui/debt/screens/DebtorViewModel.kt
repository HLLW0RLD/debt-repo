package com.example.debt.app.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.debt.app.data.Debtor
import com.example.debt.app.data.repo.LocalRepository
import com.example.debt.app.data.repo.LocalRepositoryImpl
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class DebtorViewModel(private val repository: LocalRepositoryImpl) : ViewModel(), KoinComponent {
    val debtors = repository.debtors

    fun insert(debtor: Debtor) = viewModelScope.launch {
        repository.insert(debtor)
    }

    fun update(debtor: Debtor) = viewModelScope.launch {
        repository.update(debtor)
    }

    fun getDebtorById(id: Long) = viewModelScope.launch {
        repository.getDebtorById(id)
    }

    fun deleteDebtor(id: Long) = viewModelScope.launch {
        repository.deleteDebtor(id)
    }
}