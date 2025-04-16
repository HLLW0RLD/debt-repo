package com.example.debt.app.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.debt.app.data.Debtor

private const val DB_NAME = "project_debt"

@Database(
    entities = [Debtor::class],
    version = 1
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