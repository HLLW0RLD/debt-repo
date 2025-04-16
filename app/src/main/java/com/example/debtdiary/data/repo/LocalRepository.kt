package com.example.debt.app.data.repo

import com.example.debt.app.data.Debtor
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent

interface LocalRepository : KoinComponent {
    val debtors: Flow<List<Debtor>>
    suspend fun insert(debtor: Debtor): Long
    suspend fun update(debtor: Debtor)
    suspend fun getDebtorById(id: Long): Debtor?
    suspend fun deleteDebtor(id: Long)
}