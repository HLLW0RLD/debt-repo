package com.example.debt.app.data.repo

import com.example.debt.data.model.Debtor
import com.example.debt.app.data.db.DebtorDao
import kotlinx.coroutines.flow.Flow

class LocalRepositoryImpl(private val debtorDao: DebtorDao): LocalRepository {

    override val debtors: Flow<List<Debtor>> = debtorDao.getAllDebtors()

    override suspend fun insert(debtor: Debtor): Long {
        return debtorDao.insert(debtor)
    }

    override suspend fun updateDebt(debtor: Debtor) {
        debtorDao.updateDebt(debtor)
    }

    override suspend fun addDebt(debtorId: Long, newAmount: Double) {
        val debtor = debtorDao.getDebtorById(debtorId) ?: return
        debtorDao.updateDebt(debtor.addDebt(newAmount))
    }

    override suspend fun payDebt(debtorId: Long, newAmount: Double) {
        val debtor = debtorDao.getDebtorById(debtorId) ?: return
        debtorDao.updateDebt(debtor.addPayment(newAmount))
    }

    override suspend fun getDebtorById(id: Long): Debtor? {
        return debtorDao.getDebtorById(id)
    }

    override suspend fun deleteDebtor(id: Long) {
        debtorDao.deleteDebtor(id)
    }
}