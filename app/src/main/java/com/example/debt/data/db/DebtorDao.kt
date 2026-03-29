package com.example.debt.app.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.debt.data.model.Debt
import kotlinx.coroutines.flow.Flow

@Dao
interface DebtorDao {
//    @Insert
//    suspend fun insert(debt: Debt): Long
//
//    @Query("SELECT * FROM debtors WHERE id = :id")
//    suspend fun getDebtorById(id: Long): Debt?
//
//    @Query("""
//        SELECT * FROM debtors
//        WHERE name = :name
//        AND telegramNick = :telegramNick
//        AND isMine = :isMine
//        LIMIT 1
//    """)
//    suspend fun findDebt(
//        name: String,
//        telegramNick: String,
//        isMine: Boolean
//    ): Debt?
//
//    @Query("""
//        SELECT * FROM debtors
//        WHERE name = :name
//        AND telegramNick = :telegramNick
//        LIMIT 1
//    """)
//    suspend fun findAnyDebt(
//        name: String,
//        telegramNick: String
//    ): Debt?
//
//    @Update
//    suspend fun updateDebt(debt: Debt)
//
//    @Query("UPDATE debtors SET debtAmount = debtAmount + :amountToAdd WHERE id = :debtorId")
//    suspend fun addDebt(debtorId: Long, amountToAdd: Double)
//
//    @Query("UPDATE debtors SET debtAmount = debtAmount - :amountToSubtract WHERE id = :debtorId")
//    suspend fun payDebt(debtorId: Long, amountToSubtract: Double)
//
//    @Query("SELECT * FROM debtors ORDER BY loanDate DESC")
//    fun getAllDebtors(): Flow<List<Debt>>
//
//    @Query("DELETE FROM debtors WHERE id = :id")
//    suspend fun deleteDebtor(id: Long)
//
//    @Query("DELETE FROM debtors WHERE debtAmount = 0")
//    suspend fun deleteZeroDebtors()
}