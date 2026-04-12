package com.example.debt.app.data.repo

import com.example.debt.app.utils.LogUtils.debugLog
import com.example.debt.app.utils.LogUtils.errorLog
import com.example.debt.data.model.Debt
import com.example.debt.data.model.request.DebtRequest
import com.example.debt.data.model.request.DebtUpdateRequest
import com.example.debt.data.model.response.DebtResponse
import com.example.debt.data.model.response.toModel
import com.example.debt.data.remote.DebtApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

class DebtRepositoryImpl(
    private val apiService: DebtApi,
) : DebtRepository {

    override suspend fun getAllDebts(): Flow<List<Debt>> = flow {
        val response = apiService.getAllDebts()
        if (response.isSuccessful) {
            response.body()?.let { debtResponses ->
                emit(debtResponses.map { it.toModel() })
            } ?: emit(emptyList())
        } else {
            errorLog("Failed to load debts: ${response.code()}")
            emit(emptyList())
        }
    }

    override suspend fun createDebt(request: DebtRequest) {
        val response = apiService.createDebt(request)
        if (!response.isSuccessful) {
            errorLog("Failed to create debt: ${response.code()}")
        }
    }

    override suspend fun addDebt(id: String, amount: Double): List<Debt> {
        val response = apiService.addDebt(id, amount)
        return if (response.isSuccessful) {
            debugLog("")
            response.body()?.let { debtResponses ->
                debtResponses.map { it.toModel() }
            } ?: emptyList()
        } else {
            errorLog("Failed to add debt: ${response.code()}")
            emptyList()
        }
    }

    override suspend fun payDebt(id: String, amount: Double): List<Debt> {
        val response = apiService.payDebt(id, amount)
        return if (response.isSuccessful) {
            debugLog("")
            response.body()?.let { debtResponses ->
                debtResponses.map { it.toModel() }
            } ?: emptyList()
        } else {
            errorLog("Failed to pay debt: ${response.code()}")
            emptyList()
        }
    }

    override suspend fun updateDebt(id: String, request: DebtUpdateRequest) {
        val response = apiService.updateDebt(id, request)
        if (!response.isSuccessful) {
            errorLog("Failed to update debt: ${response.code()}")
        }
    }

    override suspend fun deleteDebt(id: String) {
        val response = apiService.deleteDebt(id)
        if (!response.isSuccessful) {
            errorLog("Failed to delete debt: ${response.code()}")
        }
    }

    override suspend fun getDebtById(id: String): Debt? {
        val response = apiService.getDebtById(id)
        return if (response.isSuccessful) {
            response.body()?.toModel()
        } else {
            errorLog("Failed to get debt by id $id: ${response.code()}")
            null
        }
    }
}