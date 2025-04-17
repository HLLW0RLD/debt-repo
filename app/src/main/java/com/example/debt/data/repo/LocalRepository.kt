package com.example.debt.app.data.repo

import com.example.debt.app.data.Debtor
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent

interface LocalRepository : KoinComponent {
    val debtors: Flow<List<Debtor>>
    suspend fun insert(debtor: Debtor): Long
    suspend fun updateDebt(debtor: Debtor)
    suspend fun addDebt(debtorId: Long, newAmount: Double)
    suspend fun payDebt(debtorId: Long, newAmount: Double)
    suspend fun getDebtorById(id: Long): Debtor?
    suspend fun deleteDebtor(id: Long)
}