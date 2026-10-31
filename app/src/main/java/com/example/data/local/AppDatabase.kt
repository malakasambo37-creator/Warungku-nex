package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.DebtRecord
import com.example.data.model.ExpenseRecord
import com.example.data.model.Product
import com.example.data.model.PromoRecord
import com.example.data.model.StoreAssetRecord
import com.example.data.model.SupplierRecord
import com.example.data.model.TransactionRecord

@Database(
    entities = [
        Product::class,
        TransactionRecord::class,
        DebtRecord::class,
        ExpenseRecord::class,
        StoreAssetRecord::class,
        PromoRecord::class,
        SupplierRecord::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun transactionDao(): TransactionDao
    abstract fun debtDao(): DebtDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun storeAssetDao(): StoreAssetDao
    abstract fun promoDao(): PromoDao
    abstract fun supplierDao(): SupplierDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "warungku_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
