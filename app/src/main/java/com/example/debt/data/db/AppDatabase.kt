package com.example.debt.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.debt.data.model.Debtor
import com.example.debt.app.data.db.DebtorDao

private const val DB_NAME = "project_debt"

@Database(
    entities = [Debtor::class],
    version = 3
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun debtorDao(): DebtorDao

    companion object {
        fun create(context: Context) : AppDatabase{
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                DB_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}