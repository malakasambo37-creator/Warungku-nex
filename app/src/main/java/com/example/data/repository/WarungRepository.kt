package com.example.data.repository

import android.content.Context
import com.example.data.local.AppDatabase
import com.example.data.model.CartItem
import com.example.data.model.DebtRecord
import com.example.data.model.ExpenseRecord
import com.example.data.model.Product
import com.example.data.model.PromoRecord
import com.example.data.model.StoreAssetRecord
import com.example.data.model.StoreProfile
import com.example.data.model.SupplierRecord
import com.example.data.model.TransactionRecord
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WarungRepository(context: Context) {
    private val database = AppDatabase.getInstance(context)
    private val productDao = database.productDao()
    private val transactionDao = database.transactionDao()
    private val debtDao = database.debtDao()
    private val expenseDao = database.expenseDao()
    private val storeAssetDao = database.storeAssetDao()
    private val promoDao = database.promoDao()
    private val supplierDao = database.supplierDao()

    val products: Flow<List<Product>> = productDao.getAllProducts()
    val transactions: Flow<List<TransactionRecord>> = transactionDao.getAllTransactions()
    val debts: Flow<List<DebtRecord>> = debtDao.getAllDebts()
    val expenses: Flow<List<ExpenseRecord>> = expenseDao.getAllExpenses()
    val assets: Flow<List<StoreAssetRecord>> = storeAssetDao.getAllAssets()
    val promos: Flow<List<PromoRecord>> = promoDao.getAllPromos()
    val suppliers: Flow<List<SupplierRecord>> = supplierDao.getAllSuppliers()

    private val _storeProfile = MutableStateFlow(StoreProfile())
    val storeProfile: StateFlow<StoreProfile> = _storeProfile.asStateFlow()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            seedInitialDataIfNeeded()
        }
    }

    private suspend fun seedInitialDataIfNeeded() {
        if (productDao.getCount() == 0) {
            val initialProducts = listOf(
                Product(
                    code = "BRG-001",
                    name = "Beras Premium 5kg",
                    category = "Sembako",
                    price = 68000.0,
                    costPrice = 58000.0,
                    stock = 14,
                    minStock = 5,
                    unit = "karung",
                    barcode = "899123456001"
                ),
                Product(
                    code = "BRG-002",
                    name = "Minyak Goreng 2L",
                    category = "Sembako",
                    price = 34000.0,
                    costPrice = 29000.0,
                    stock = 8,
                    minStock = 5,
                    unit = "pouch",
                    barcode = "899123456002"
                ),
                Product(
                    code = "BRG-003",
                    name = "Gula Pasir 1kg",
                    category = "Sembako",
                    price = 15000.0,
                    costPrice = 12500.0,
                    stock = 25,
                    minStock = 8,
                    unit = "kg",
                    barcode = "899123456003"
                ),
                Product(
                    code = "BRG-008",
                    name = "Telur Ayam 1kg",
                    category = "Sembako",
                    price = 28000.0,
                    costPrice = 24000.0,
                    stock = 2,
                    minStock = 5,
                    unit = "kg",
                    barcode = "899123456008"
                ),
                Product(
                    code = "BRG-012",
                    name = "Kecap Manis 520ml",
                    category = "Bumbu",
                    price = 18000.0,
                    costPrice = 14000.0,
                    stock = 4,
                    minStock = 6,
                    unit = "botol",
                    barcode = "899123456012"
                ),
                Product(
                    code = "BRG-015",
                    name = "Mie Instan Goreng",
                    category = "Makanan",
                    price = 3500.0,
                    costPrice = 2800.0,
                    stock = 45,
                    minStock = 10,
                    unit = "pcs",
                    barcode = "899123456015"
                ),
                Product(
                    code = "BRG-019",
                    name = "Kopi Kapal Api 65g",
                    category = "Minuman",
                    price = 6500.0,
                    costPrice = 5000.0,
                    stock = 30,
                    minStock = 10,
                    unit = "bungkus",
                    barcode = "899123456019"
                ),
                Product(
                    code = "BRG-022",
                    name = "Teh Celup Sariwangi 25",
                    category = "Minuman",
                    price = 8000.0,
                    costPrice = 6200.0,
                    stock = 20,
                    minStock = 5,
                    unit = "kotak",
                    barcode = "899123456022"
                ),
                Product(
                    code = "BRG-026",
                    name = "Sabun Mandi Lifebuoy",
                    category = "Kebersihan",
                    price = 4500.0,
                    costPrice = 3500.0,
                    stock = 18,
                    minStock = 6,
                    unit = "pcs",
                    barcode = "899123456026"
                ),
                Product(
                    code = "BRG-030",
                    name = "Tepung Terigu Segitiga 1kg",
                    category = "Sembako",
                    price = 12500.0,
                    costPrice = 10200.0,
                    stock = 12,
                    minStock = 5,
                    unit = "kg",
                    barcode = "899123456030"
                )
            )
            productDao.insertAll(initialProducts)
        }
        // Ensure dummy pre-seeded transactions (246.000 sales & 41.000 profit) are removed
        transactionDao.deleteDummyTransactions()

        if (debtDao.getCount() == 0) {
            val initialDebts = listOf(
                DebtRecord(
                    customerName = "Pak Ahmad (Rt 03)",
                    phone = "081234567890",
                    amount = 250000.0,
                    dueDate = "30 Okt 2026",
                    notes = "Beras 1 karung, minyak 2 pouch, telur 2kg",
                    isPaid = false
                ),
                DebtRecord(
                    customerName = "Ibu Buwono",
                    phone = "081398765432",
                    amount = 200000.0,
                    dueDate = "02 Nov 2026",
                    notes = "Sembako mingguan keluarga",
                    isPaid = false
                )
            )
            debtDao.insertAll(initialDebts)
        }

        if (expenseDao.getCount() == 0) {
            val initialExpenses = listOf(
                ExpenseRecord(
                    title = "Bayar Listrik Toko & Wifi",
                    category = "Operasional",
                    amount = 450000.0,
                    dateText = "25 Okt 2026"
                ),
                ExpenseRecord(
                    title = "Plastik Kresek & Kertas Struk",
                    category = "Operasional",
                    amount = 65000.0,
                    dateText = "28 Okt 2026"
                )
            )
            expenseDao.insertAll(initialExpenses)
        }

        if (storeAssetDao.getCount() == 0) {
            val initialAssets = listOf(
                StoreAssetRecord(
                    name = "Freezer Showcase Minuman",
                    value = 3200000.0,
                    condition = "Kondisi Baik",
                    notes = "Merk Polytron 3 rak"
                ),
                StoreAssetRecord(
                    name = "Timbangan Digital 30kg",
                    value = 450000.0,
                    condition = "Kondisi Baik",
                    notes = "Akurasi 5 gram"
                ),
                StoreAssetRecord(
                    name = "Printer Bluetooth Thermal 58mm",
                    value = 280000.0,
                    condition = "Kondisi Baik",
                    notes = "Model RPP02N"
                )
            )
            storeAssetDao.insertAll(initialAssets)
        }

        if (promoDao.getCount() == 0) {
            val initialPromos = listOf(
                PromoRecord(
                    title = "Promo Jumat Berkah",
                    discountPercent = 5.0,
                    minSpend = 100000.0,
                    validUntil = "31 Okt 2026",
                    isActive = true
                ),
                PromoRecord(
                    title = "Diskon Pelanggan Setia",
                    discountAmount = 5000.0,
                    minSpend = 75000.0,
                    validUntil = "15 Nov 2026",
                    isActive = true
                )
            )
            promoDao.insertAll(initialPromos)
        }

        if (supplierDao.getCount() == 0) {
            val initialSuppliers = listOf(
                SupplierRecord(
                    name = "CV Sembako Jaya Utama",
                    category = "Suplier Beras & Minyak",
                    phone = "081299887766",
                    address = "Pasar Induk Kramat Jati Blok C-12"
                ),
                SupplierRecord(
                    name = "Agen Telur Berkah Barokah",
                    category = "Telur & Unggas",
                    phone = "085712345678",
                    address = "Jl. Kebon Jeruk No. 44"
                ),
                SupplierRecord(
                    name = "Distributor Grosir Sinar Terang",
                    category = "Bumbu & Kebutuhan Dapur",
                    phone = "087890123456",
                    address = "Komplek Pergudangan Daan Mogot"
                )
            )
            supplierDao.insertAll(initialSuppliers)
        }
    }

    suspend fun addProduct(product: Product) = withContext(Dispatchers.IO) {
        productDao.insertProduct(product)
    }

    suspend fun updateProduct(product: Product) = withContext(Dispatchers.IO) {
        productDao.updateProduct(product)
    }

    suspend fun deleteProduct(product: Product) = withContext(Dispatchers.IO) {
        productDao.deleteProduct(product)
    }

    suspend fun addStock(id: Long, qty: Int) = withContext(Dispatchers.IO) {
        productDao.addStock(id, qty)
    }

    suspend fun completeTransaction(
        items: List<CartItem>,
        trxNumber: String,
        paymentType: String,
        cashGiven: Double,
        changeAmount: Double,
        customerName: String = "Pelanggan Umum"
    ): TransactionRecord = withContext(Dispatchers.IO) {
        val totalAmount = items.sumOf { it.subtotal }
        val totalCost = items.sumOf { it.product.costPrice * it.quantity }
        val profit = totalAmount - totalCost
        val summary = items.joinToString(", ") { "${it.product.name} x ${it.quantity}" }

        // Deduct inventory stock
        for (item in items) {
            productDao.reduceStock(item.product.id, item.quantity)
        }

        val record = TransactionRecord(
            trxNumber = trxNumber,
            timestamp = System.currentTimeMillis(),
            totalAmount = totalAmount,
            totalCost = totalCost,
            profit = profit,
            paymentType = paymentType,
            customerName = customerName,
            itemsSummary = summary,
            cashGiven = cashGiven,
            changeAmount = changeAmount
        )
        val id = transactionDao.insertTransaction(record)
        record.copy(id = id)
    }

    suspend fun deleteTransaction(trx: TransactionRecord) = withContext(Dispatchers.IO) {
        transactionDao.deleteTransaction(trx)
    }

    suspend fun deleteTransactionById(id: Long) = withContext(Dispatchers.IO) {
        transactionDao.deleteTransactionById(id)
    }

    suspend fun clearAllTransactions() = withContext(Dispatchers.IO) {
        transactionDao.deleteAllTransactions()
    }

    suspend fun addDebt(debt: DebtRecord) = withContext(Dispatchers.IO) {
        debtDao.insertDebt(debt)
    }

    suspend fun markDebtPaid(id: Long) = withContext(Dispatchers.IO) {
        debtDao.markAsPaid(id)
    }

    suspend fun deleteDebt(debt: DebtRecord) = withContext(Dispatchers.IO) {
        debtDao.deleteDebt(debt)
    }

    suspend fun addExpense(expense: ExpenseRecord) = withContext(Dispatchers.IO) {
        expenseDao.insertExpense(expense)
    }

    suspend fun deleteExpense(expense: ExpenseRecord) = withContext(Dispatchers.IO) {
        expenseDao.deleteExpense(expense)
    }

    suspend fun addAsset(asset: StoreAssetRecord) = withContext(Dispatchers.IO) {
        storeAssetDao.insertAsset(asset)
    }

    suspend fun deleteAsset(asset: StoreAssetRecord) = withContext(Dispatchers.IO) {
        storeAssetDao.deleteAsset(asset)
    }

    suspend fun addPromo(promo: PromoRecord) = withContext(Dispatchers.IO) {
        promoDao.insertPromo(promo)
    }

    suspend fun togglePromo(promo: PromoRecord) = withContext(Dispatchers.IO) {
        promoDao.updatePromo(promo.copy(isActive = !promo.isActive))
    }

    suspend fun addSupplier(supplier: SupplierRecord) = withContext(Dispatchers.IO) {
        supplierDao.insertSupplier(supplier)
    }

    fun updateStoreProfile(profile: StoreProfile) {
        _storeProfile.value = profile
    }
}
