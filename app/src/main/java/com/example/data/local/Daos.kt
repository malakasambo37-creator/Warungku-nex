package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.DebtRecord
import com.example.data.model.ExpenseRecord
import com.example.data.model.Product
import com.example.data.model.PromoRecord
import com.example.data.model.StoreAssetRecord
import com.example.data.model.SupplierRecord
import com.example.data.model.TransactionRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products ORDER BY name ASC")
    fun getAllProducts(): Flow<List<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<Product>)

    @Update
    suspend fun updateProduct(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)

    @Query("UPDATE products SET stock = stock + :qty WHERE id = :id")
    suspend fun addStock(id: Long, qty: Int)

    @Query("UPDATE products SET stock = CASE WHEN stock >= :qty THEN stock - :qty ELSE 0 END WHERE id = :id")
    suspend fun reduceStock(id: Long, qty: Int)

    @Query("SELECT COUNT(*) FROM products")
    suspend fun getCount(): Int
}

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<TransactionRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(trx: TransactionRecord): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(trxs: List<TransactionRecord>)

    @Query("SELECT COUNT(*) FROM transactions")
    suspend fun getCount(): Int

    @Query("DELETE FROM transactions WHERE trxNumber IN ('TRX-8840', 'TRX-8839', 'TRX-8838')")
    suspend fun deleteDummyTransactions()

    @Delete
    suspend fun deleteTransaction(trx: TransactionRecord)

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteTransactionById(id: Long)

    @Query("DELETE FROM transactions")
    suspend fun deleteAllTransactions()
}

@Dao
interface DebtDao {
    @Query("SELECT * FROM debts ORDER BY isPaid ASC, dueDate ASC")
    fun getAllDebts(): Flow<List<DebtRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDebt(debt: DebtRecord): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(debts: List<DebtRecord>)

    @Update
    suspend fun updateDebt(debt: DebtRecord)

    @Query("UPDATE debts SET isPaid = 1 WHERE id = :id")
    suspend fun markAsPaid(id: Long)

    @Delete
    suspend fun deleteDebt(debt: DebtRecord)

    @Query("SELECT COUNT(*) FROM debts")
    suspend fun getCount(): Int
}

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses ORDER BY timestamp DESC")
    fun getAllExpenses(): Flow<List<ExpenseRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: ExpenseRecord): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(expenses: List<ExpenseRecord>)

    @Delete
    suspend fun deleteExpense(expense: ExpenseRecord)

    @Query("SELECT COUNT(*) FROM expenses")
    suspend fun getCount(): Int
}

@Dao
interface StoreAssetDao {
    @Query("SELECT * FROM store_assets ORDER BY id ASC")
    fun getAllAssets(): Flow<List<StoreAssetRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAsset(asset: StoreAssetRecord): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(assets: List<StoreAssetRecord>)

    @Delete
    suspend fun deleteAsset(asset: StoreAssetRecord)

    @Query("SELECT COUNT(*) FROM store_assets")
    suspend fun getCount(): Int
}

@Dao
interface PromoDao {
    @Query("SELECT * FROM promos ORDER BY id ASC")
    fun getAllPromos(): Flow<List<PromoRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPromo(promo: PromoRecord): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(promos: List<PromoRecord>)

    @Update
    suspend fun updatePromo(promo: PromoRecord)

    @Delete
    suspend fun deletePromo(promo: PromoRecord)

    @Query("SELECT COUNT(*) FROM promos")
    suspend fun getCount(): Int
}

@Dao
interface SupplierDao {
    @Query("SELECT * FROM suppliers ORDER BY name ASC")
    fun getAllSuppliers(): Flow<List<SupplierRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSupplier(supplier: SupplierRecord): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(suppliers: List<SupplierRecord>)

    @Delete
    suspend fun deleteSupplier(supplier: SupplierRecord)

    @Query("SELECT COUNT(*) FROM suppliers")
    suspend fun getCount(): Int
}
