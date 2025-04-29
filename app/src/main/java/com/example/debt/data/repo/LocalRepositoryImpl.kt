package com.example.debt.app.data.repo

import com.example.debt.data.model.Debtor
import com.example.debt.app.data.db.DebtorDao
import com.example.debt.app.utils.LogUtils.debugLog
import com.example.debt.app.utils.LogUtils.errorLog
import kotlinx.coroutines.flow.Flow

class LocalRepositoryImpl(private val debtorDao: DebtorDao): LocalRepository {

    override val debtors: Flow<List<Debtor>> = debtorDao.getAllDebtors()

    override suspend fun insertDebtor(debtor: Debtor): Long {
        return try {
            val id = debtorDao.insert(debtor)
            debugLog("Inserted debtor [id=$id]: ${debtor.name} (${debtor.telegramNick})")
            id
        } catch (e: Exception) {
            errorLog("Failed to insert debtor ${debtor.name}: ${e.message}")
            throw e
        }
    }

    override suspend fun updateDebt(debtor: Debtor) {
        try {
            debtorDao.updateDebt(debtor)
            debugLog("Updated debtor [id=${debtor.id}]: ${debtor.name}, new debt: ${debtor.debtAmount}")
        } catch (e: Exception) {
            errorLog("Failed to update debtor [id=${debtor.id}]: ${e.message}")
            throw e
        }
    }

    override suspend fun addDebt(debtorId: Long, newAmount: Double) {
        val debtor = debtorDao.getDebtorById(debtorId) ?: return
        try {
            debtorDao.updateDebt(debtor.addDebt(newAmount))
        } catch (e: Exception) {
            errorLog("Failed to add debt ${e.message}")
            throw e
        }
    }

    override suspend fun payDebt(debtorId: Long, newAmount: Double) {
        val debtor = debtorDao.getDebtorById(debtorId) ?: return
        try {
            debtorDao.updateDebt(debtor.addPayment(newAmount))
        } catch (e: Exception) {
            errorLog("Failed to pay debt ${e.message}")
            throw e
        }
    }

    override suspend fun getDebtorById(id: Long): Debtor? {
        return try {
            val debtor = debtorDao.getDebtorById(id)
            if (debtor == null) {
                debugLog("No debtor found with id: $id")
            } else {
                debugLog("Retrieved debtor [id=$id]: ${debtor.name}")
            }
            debtor
        } catch (e: Exception) {
            errorLog("Failed to get debtor by id $id: ${e.message}")
            throw e
        }
    }

    override suspend fun deleteDebtor(id: Long) {
        try {
            debugLog("Deleting debtor with id: $id")
            debtorDao.deleteDebtor(id)
            debugLog("Successfully deleted debtor with id: $id")
        } catch (e: Exception) {
            errorLog("Failed to delete debtor with id $id: ${e.message}")
            throw e
        }
    }
}