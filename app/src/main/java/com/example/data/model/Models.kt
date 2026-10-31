package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val code: String,
    val name: String,
    val category: String,
    val price: Double,
    val costPrice: Double,
    val stock: Int,
    val minStock: Int = 5,
    val unit: String = "pcs",
    val barcode: String = ""
) {
    val stockStatus: StockStatus
        get() = when {
            stock <= 2 -> StockStatus.CRITICAL
            stock <= minStock -> StockStatus.LOW
            else -> StockStatus.SAFE
        }
}

enum class StockStatus(val label: String) {
    SAFE("Aman"),
    LOW("Menipis"),
    CRITICAL("Kritis / Restock")
}

data class CartItem(
    val product: Product,
    val quantity: Int
) {
    val subtotal: Double get() = product.price * quantity
    val profit: Double get() = (product.price - product.costPrice) * quantity
}

@Entity(tableName = "transactions")
data class TransactionRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val trxNumber: String,
    val timestamp: Long = System.currentTimeMillis(),
    val totalAmount: Double,
    val totalCost: Double,
    val profit: Double,
    val paymentType: String, // "TUNAI", "QRIS", "BON"
    val customerName: String = "",
    val itemsSummary: String = "",
    val cashGiven: Double = 0.0,
    val changeAmount: Double = 0.0
)

@Entity(tableName = "debts")
data class DebtRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val customerName: String,
    val phone: String = "",
    val amount: Double,
    val dueDate: String,
    val notes: String = "",
    val isPaid: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "expenses")
data class ExpenseRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val category: String, // "Operasional", "Listrik & Wifi", "Sewa", "Gaji", "Lainnya"
    val amount: Double,
    val dateText: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "store_assets")
data class StoreAssetRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val value: Double,
    val condition: String = "Kondisi Baik", // "Kondisi Baik", "Perlu Servis"
    val notes: String = ""
)

@Entity(tableName = "promos")
data class PromoRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val discountPercent: Double = 0.0,
    val discountAmount: Double = 0.0,
    val minSpend: Double = 0.0,
    val validUntil: String,
    val isActive: Boolean = true
)

@Entity(tableName = "suppliers")
data class SupplierRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val category: String,
    val phone: String,
    val address: String = ""
)

data class StoreProfile(
    val storeName: String = "Toko Kelontong Barokah",
    val ownerName: String = "H. Ahmad Subagjo",
    val storeId: String = "#WRG-99482",
    val address: String = "Jl. Raya Mawar No. 12, Kebayoran - Jakarta Selatan",
    val phone: String = "08123456789",
    val isVerifiedPro: Boolean = true,
    val printerName: String = "RPP02N Thermal",
    val isPrinterConnected: Boolean = true,
    val qrisNmid: String = "ID1029384",
    val isQrisActive: Boolean = true,
    val staffCount: Int = 2,
    val pinEnabled: Boolean = true
)
