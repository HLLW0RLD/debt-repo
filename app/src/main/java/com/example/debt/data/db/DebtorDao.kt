package com.example.debt.app.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.debt.data.model.Debtor
import kotlinx.coroutines.flow.Flow

@Dao
interface DebtorDao {
    @Insert
    suspend fun insert(debtor: Debtor): Long

    @Query("SELECT * FROM debtors WHERE id = :id")
    suspend fun getDebtorById(id: Long): Debtor?

    @Query("""
        SELECT * FROM debtors 
        WHERE name = :name 
        AND telegramNick = :telegramNick
        AND isMine = :isMine
        LIMIT 1
    """)
    suspend fun findDebt(
        name: String,
        telegramNick: String,
        isMine: Boolean
    ): Debtor?

    @Query("""
        SELECT * FROM debtors 
        WHERE name = :name 
        AND telegramNick = :telegramNick
        LIMIT 1
    """)
    suspend fun findAnyDebt(
        name: String,
        telegramNick: String
    ): Debtor?

    @Update
    suspend fun updateDebt(debtor: Debtor)

    @Query("UPDATE debtors SET debtAmount = debtAmount + :amountToAdd WHERE id = :debtorId")
    suspend fun addDebt(debtorId: Long, amountToAdd: Double)

    @Query("UPDATE debtors SET debtAmount = debtAmount - :amountToSubtract WHERE id = :debtorId")
    suspend fun payDebt(debtorId: Long, amountToSubtract: Double)

    @Query("SELECT * FROM debtors ORDER BY loanDate DESC")
    fun getAllDebtors(): Flow<List<Debtor>>

    @Query("DELETE FROM debtors WHERE id = :id")
    suspend fun deleteDebtor(id: Long)

    @Query("DELETE FROM debtors WHERE debtAmount = 0")
    suspend fun deleteZeroDebtors()
}