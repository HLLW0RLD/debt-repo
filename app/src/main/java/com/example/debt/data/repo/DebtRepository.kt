package com.example.debt.app.data.repo

import com.example.debt.data.model.Debt
import com.example.debt.data.model.request.DebtRequest
import com.example.debt.data.model.request.DebtUpdateRequest
import kotlinx.coroutines.flow.Flow

interface DebtRepository {
    suspend fun getAllDebts(): Flow<List<Debt>>
    suspend fun createDebt(request: DebtRequest)
    suspend fun addDebt(id: String, amount: Double): List<Debt>
    suspend fun payDebt(id: String, amount: Double): List<Debt>
    suspend fun updateDebt(id: String, request: DebtUpdateRequest)
    suspend fun deleteDebt(id: String)
    suspend fun getDebtById(id: String): Debt?
}