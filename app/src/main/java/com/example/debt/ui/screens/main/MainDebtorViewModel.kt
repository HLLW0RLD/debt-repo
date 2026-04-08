package com.example.debt.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.debt.app.data.repo.DebtRepository
import com.example.debt.app.utils.LogUtils.errorLog
import com.example.debt.data.model.Debt
import com.example.debt.data.model.Transaction
import com.example.debt.data.model.TransactionType
import com.example.debt.data.model.request.DebtRequest
import com.example.debt.data.model.request.DebtUpdateRequest
import com.example.debt.data.model.request.TransactionRequest
import com.example.debt.utils.getCurrentDateTime
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

sealed class DebtUiState {
    object Loading : DebtUiState()
    data class Success(val debtors: List<Debt>) : DebtUiState()
    data class Error(val message: String) : DebtUiState()
}

class MainDebtorViewModel(
    private val repository: DebtRepository
) : ViewModel() {

    private val _debtors = MutableStateFlow<DebtUiState>(DebtUiState.Loading)
    val debtors = _debtors.asStateFlow()

    private val _debtor = MutableStateFlow<Debt?>(null)
    val debtor = _debtor.asStateFlow()

    init {
        loadAllDebts()
    }

    fun loadAllDebts() {
        viewModelScope.launch {
            _debtors.value = DebtUiState.Loading
            delay(1500)

            try {
//                repository.getAllDebts().collect { debts ->
//                    _debtors.value = DebtUiState.Success(debts)
//                }

                val emptList = listOf<Debt>()
                val list = List(12) {
                    Debt(
                        id = "id_${it}_${Random.nextInt(1000, 9999)}",
                        name = listOf("Алексей", "Мария", "Иван", "Елена", "Дмитрий", "Анна", "Сергей", "Ольга", "Павел", "Татьяна").random(),
                        isMine = Random.nextBoolean(),
                        telegramNick = null,
                        debtAmount = 10000.0,
                        comment = listOf("За обед", "За билеты", "За подарок", "Долг", "Возврат", "").random(),
                        transactions = listOf()
//                        transactions = List(Random.nextInt(1, 5)) { index ->
//                            Transaction(
//                                id = "trans_${it}_${index}_${Random.nextInt(1000)}",
//                                amount = Random.nextDouble(50.0, 1000.0).let { String.format("%.2f", it).toDouble() },
//                                date = "2024-${Random.nextInt(1, 13)}-${Random.nextInt(1, 29)}",
//                                type = if (Random.nextBoolean()) TransactionType.PAYMENT else TransactionType.DEBT,
//                                comment = listOf("Частичный возврат", "Полный расчёт", "Аванс", "").random()
//                            )
//                        }
                    )
                }

                _debtors.value = DebtUiState.Success(list)
//                _debtors.value = DebtUiState.Success(emptList)

            } catch (e: Exception) {
                _debtors.value = DebtUiState.Error(e.message ?: "loadAllDebts error")
                errorLog(e.message ?: "loadAllDebts error")
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

            try {
                repository.createDebt(request)
                loadAllDebts()
            } catch (e: Exception) {
                _debtors.value = DebtUiState.Error(e.message ?: "createDebt Error")
            }
        }
    }

    fun payDebt(debtorId: String, paymentAmount: Double) {
        viewModelScope.launch {
            _debtors.value = DebtUiState.Loading

            try {
                repository.payDebt(debtorId, paymentAmount)
                loadAllDebts()
            } catch (e: Exception) {
                _debtors.value = DebtUiState.Error(e.message ?: "payDebt Error")
            }
        }
    }

    fun addDebt(debtorId: String, additionalAmount: Double) {
        viewModelScope.launch {
            _debtors.value = DebtUiState.Loading

            try {
                repository.addDebt(debtorId, additionalAmount)
                loadAllDebts()
            } catch (e: Exception) {
                _debtors.value = DebtUiState.Error(e.message ?: "addDebt Error")
            }
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

            try {
                repository.updateDebt(id, request)
                loadAllDebts()
            } catch (e: Exception) {
                _debtors.value = DebtUiState.Error(e.message ?: "updateDebt Error")
            }
        }
    }

    fun deleteDebtor(id: String) {
        viewModelScope.launch {
            _debtors.value = DebtUiState.Loading

            try {
                repository.deleteDebt(id)
                loadAllDebts()
            } catch (e: Exception) {
                _debtors.value = DebtUiState.Error(e.message ?: "deleteDebtor Error")
            }
        }
    }

    fun getDebtorById(id: String) {
        viewModelScope.launch {
            try {
                _debtor.value = repository.getDebtById(id)
            } catch (e: Exception) {
                _debtors.value = DebtUiState.Error(e.message ?: "getDebtorById Error")
            }
        }
    }
}