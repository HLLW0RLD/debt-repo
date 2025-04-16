package com.example.debt.app.data.db

import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.debt.app.data.Debtor
import kotlinx.coroutines.flow.Flow

interface DebtorDao {
    @Insert
    suspend fun insert(debtor: Debtor): Long

    @Update
    suspend fun update(debtor: Debtor)

    @Query("SELECT * FROM debtors ORDER BY loanDate DESC")
    fun getAllDebtors(): Flow<List<Debtor>>

    @Query("SELECT * FROM debtors WHERE id = :id")
    suspend fun getDebtorById(id: Long): Debtor?

    @Query("DELETE FROM debtors WHERE id = :id")
    suspend fun deleteDebtor(id: Long)
}