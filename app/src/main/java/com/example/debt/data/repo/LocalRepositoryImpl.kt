package com.example.debt.app.data.repo

import com.example.debt.data.model.Debtor
import com.example.debt.app.data.db.DebtorDao
import com.example.debt.app.utils.LogUtils.debugLog
import com.example.debt.app.utils.LogUtils.errorLog
import com.example.debt.data.model.Transaction
import com.example.debt.data.model.TransactionType
import com.example.debt.utils.PreferenceCache
import com.example.debt.utils.getCurrentDateTime
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow

class LocalRepositoryImpl(private val debtorDao: DebtorDao) : LocalRepository {

    override val debtors: Flow<List<Debtor>> = debtorDao.getAllDebtors()

    override suspend fun insertDebtor(debtor: Debtor): Long {
        return try {
            val existingDebt = debtorDao.findAnyDebt(
                name = debtor.name,
                telegramNick = debtor.telegramNick
            )

            var remainingAmount = debtor.debtAmount

            if (PreferenceCache.autoCountDebts && existingDebt != null) {
                when {
                    existingDebt.isMine == debtor.isMine -> {
                        val updatedDebt = existingDebt.addDebt(remainingAmount)
                        debtorDao.updateDebt(updatedDebt)
                        remainingAmount = 0.0
                        debugLog("Merged same-type debt for ${debtor.name}")
                        existingDebt.id
                    }

                    existingDebt.isMine != debtor.isMine -> {
                        val amountToSettle = minOf(remainingAmount, existingDebt.debtAmount)

                        val updatedDebt = if (existingDebt.isMine) {
                            existingDebt.addPayment(amountToSettle)
                        } else {
                            existingDebt.addDebt(amountToSettle)
                        }

                        if (updatedDebt.debtAmount == 0.0 && PreferenceCache.autoDeleteEmptyDebts) {
                            debtorDao.deleteDebtor(updatedDebt.id)
                            debugLog("Deleted zero-amount debt for ${debtor.name}")
                        } else {
                            debtorDao.updateDebt(updatedDebt)
                        }

                        remainingAmount -= amountToSettle
                        debugLog("Settled $amountToSettle for debt [name=${debtor.name}]")
                        existingDebt.id
                    }

                    else -> {
                        debtorDao.insert(debtor).also { id ->
                            debugLog("Inserted debtor with auto-settle on but no debt [name=${debtor.name}]")
                        }
                    }
                }
            } else {
                debtorDao.insert(debtor).also { id ->
                    debugLog("Inserted debtor [name=${debtor.name}] with auto-settle off")
                }
            }
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

    override suspend fun addDebt(
        debtorId: Long,
        amount: Double
    ) {
        val debtor = debtorDao.getDebtorById(debtorId) ?: return

        try {
            if (PreferenceCache.autoCountDebts && debtor.isMine) {
                val theirDebt = debtorDao.findDebt(
                    name = debtor.name,
                    telegramNick = debtor.telegramNick,
                    isMine = false
                )

                theirDebt?.let { mutualDebt ->
                    val amountToSettle = minOf(amount, mutualDebt.debtAmount)

                    debtorDao.updateDebt(mutualDebt.addPayment(amountToSettle))

                    val remaining = amount - amountToSettle
                    if (remaining > 0) {
                        debtorDao.updateDebt(debtor.addDebt(remaining))
                    }
                    return
                }
            }

            debtorDao.updateDebt(debtor.addDebt(amount))
        } catch (e: Exception) {
            errorLog("Failed to process debt addition: ${e.message}")
            throw e
        }
    }

    override suspend fun payDebt(
        debtorId: Long,
        amount: Double
    ) {
        val debtor = debtorDao.getDebtorById(debtorId) ?: return

        try {
            when {
                PreferenceCache.autoCountDebts && !debtor.isMine -> {
                    val matchDebt = debtorDao.findDebt(
                        name = debtor.name,
                        telegramNick = debtor.telegramNick,
                        isMine = true
                    )

                    matchDebt?.let { newDebt ->
                        val amountToSettle = minOf(amount, newDebt.debtAmount, debtor.debtAmount)

                        debtorDao.updateDebt(newDebt.addPayment(amountToSettle))
                        debtorDao.updateDebt(debtor.addPayment(amountToSettle))

                        val remaining = amount - amountToSettle
                        if (remaining > 0) {
                            handleRemainingPayment(debtor, remaining)
                        }
                        return
                    } ?: run {
                        handleRemainingPayment(debtor, amount)
                    }
                }

                amount > debtor.debtAmount -> {
                    handleRemainingPayment(debtor, amount)
                }

                else -> {
                    handleRemainingPayment(debtor, amount)
                }
            }
        } catch (e: Exception) {
            errorLog("Failed to process debt payment: ${e.message}")
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

    private suspend fun handleRemainingPayment(debtor: Debtor, amount: Double) {
        if (amount > debtor.debtAmount) {
            handleOverpayment(debtor, amount)
        } else {
            debtorDao.updateDebt(debtor.addPayment(amount))
        }
    }

    private suspend fun handleOverpayment(debtor: Debtor, amount: Double) {
        val overpayment = amount - debtor.debtAmount

        val updatedDebtor = debtor.addPayment(debtor.debtAmount)
        debtorDao.updateDebt(updatedDebtor)

        if (PreferenceCache.autoDeleteEmptyDebts) {
            deleteEmpty(updatedDebtor)
        }

        val reverseDebtor = Debtor(
            name = debtor.name,
            telegramNick = debtor.telegramNick,
            isMine = !debtor.isMine,
            debtAmount = overpayment,
            comment = "Автоматически создано при переплате\n${debtor.comment}",
            transactionsJson = Gson().toJson(mutableListOf(
                Transaction(
                    id = 0,
                    amount = overpayment,
                    date = getCurrentDateTime(),
                    type = TransactionType.DEBT,
                    comment = "Автоматически создано при переплате\n${debtor.comment}"
                )
            ))
        )

        // 3. Вставляем новую запись
        debtorDao.insert(reverseDebtor)
    }

    private suspend fun deleteEmpty(debtor: Debtor) {
        if (debtor.debtAmount == 0.0) {
            debtorDao.deleteDebtor(debtor.id)
        } else {
            debtorDao.deleteZeroDebtors()
        }
    }
}