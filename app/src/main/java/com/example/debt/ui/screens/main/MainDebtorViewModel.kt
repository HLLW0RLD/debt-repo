package com.example.debt.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.debt.app.data.repo.DebtRepository
import com.example.debt.data.model.Debt
import com.example.debt.data.model.Transaction
import com.example.debt.data.model.TransactionType
import com.example.debt.data.model.request.DebtRequest
import com.example.debt.data.model.request.DebtUpdateRequest
import com.example.debt.data.model.request.TransactionRequest
import com.example.debt.utils.getCurrentDateTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

sealed class DebtUiState {
    object Loading : DebtUiState()
    data class Success(val debtors: List<Debt>) : DebtUiState()
    data class Error(val message: String) : DebtUiState()
}

class MainDebtorViewModel(private val repository: DebtRepository) : ViewModel() {

    private val _debtors = MutableStateFlow<DebtUiState>(DebtUiState.Loading)
    val debtors = _debtors.asStateFlow()

    private val _debtor = MutableStateFlow<Debt?>(null)
    val debtor = _debtor.asStateFlow()

    fun loadAllDebts() {
        viewModelScope.launch {
            _debtors.value = DebtUiState.Loading
            repository.getAllDebts().collect { debts ->
                _debtors.value = DebtUiState.Success(debts)
            }
        }
    }

    fun createDebt(
        name: String,
        isMine: Boolean,
        telegramNick: String?,
        debtAmount: Double,
        returnDate: String?,
        comment: String?
    ) {
        viewModelScope.launch {
            _debtors.value = DebtUiState.Loading

            val request = DebtRequest(
                name = name,
                isMine = isMine,
                telegramNick = telegramNick,
                debtAmount = debtAmount,
                loanDate = getCurrentDateTime(),
                returnDate = returnDate,
                comment = comment,
                initialTransaction = TransactionRequest(
                    amount = debtAmount,
                    date = getCurrentDateTime(),
                    type = if (isMine) "DEBT" else "PAYMENT",
                    comment = comment
                )
            )

            repository.createDebt(request)
            loadAllDebts()
        }
    }

    fun payDebt(debtorId: String, paymentAmount: Double) {
        viewModelScope.launch {
            _debtors.value = DebtUiState.Loading
            repository.payDebt(debtorId, paymentAmount)
            loadAllDebts()
        }
    }

    fun addDebt(debtorId: String, additionalAmount: Double) {
        viewModelScope.launch {
            _debtors.value = DebtUiState.Loading
            repository.addDebt(debtorId, additionalAmount)
            loadAllDebts()
        }
    }

    fun updateDebt(
        id: String,
        name: String? = null,
        isMine: Boolean? = null,
        telegramNick: String? = null,
        debtAmount: Double? = null,
        returnDate: String? = null,
        comment: String? = null
    ) {
        viewModelScope.launch {
            _debtors.value = DebtUiState.Loading

            val request = DebtUpdateRequest(
                name = name,
                isMine = isMine,
                telegramNick = telegramNick,
                debtAmount = debtAmount,
                returnDate = returnDate,
                comment = comment
            )

            repository.updateDebt(id, request)
            loadAllDebts()
        }
    }

    fun deleteDebtor(id: String) {
        viewModelScope.launch {
            _debtors.value = DebtUiState.Loading
            repository.deleteDebt(id)
            loadAllDebts()
        }
    }

    suspend fun getDebtorById(id: String) {
        viewModelScope.launch {
            _debtor.value = repository.getDebtById(id)
        }
    }
}