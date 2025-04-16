package com.example.debt.app.data.repo

import com.example.debt.app.data.Debtor
import com.example.debt.app.data.db.DebtorDao
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent

class LocalRepositoryImpl(private val debtorDao: DebtorDao) {

    val debtors: Flow<List<Debtor>> = debtorDao.getAllDebtors()

    suspend fun insert(debtor: Debtor): Long {
        return debtorDao.insert(debtor)
    }

    suspend fun update(debtor: Debtor) {
        debtorDao.update(debtor)
    }

    suspend fun getDebtorById(id: Long): Debtor? {
        return debtorDao.getDebtorById(id)
    }

    suspend fun deleteDebtor(id: Long) {
        debtorDao.deleteDebtor(id)
    }
}