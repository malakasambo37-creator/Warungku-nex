package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.CartItem
import com.example.data.model.DebtRecord
import com.example.data.model.ExpenseRecord
import com.example.data.model.Product
import com.example.data.model.PromoRecord
import com.example.data.model.StoreAssetRecord
import com.example.data.model.StoreProfile
import com.example.data.model.SupplierRecord
import com.example.data.model.TransactionRecord
import com.example.data.repository.WarungRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

enum class AppTab(val title: String) {
    BERANDA("Beranda"),
    TRANSAKSI("Transaksi"),
    QR_SCAN("QR Scan"),
    LAPORAN("Laporan"),
    PROFIL("Profil")
}

enum class SubScreen {
    NONE,
    INVENTORY,
    CATALOG,
    DEBT,
    EXPENSES_ASSETS,
    PROMO_SUPPLIER,
    HISTORY
}

data class DailySummary(
    val totalSales: Double = 0.0,
    val totalTransactions: Int = 0,
    val grossProfit: Double = 0.0,
    val itemsSold: Int = 0
)

class WarungViewModel(application: Application) : AndroidViewModel(application) {
    val repository = WarungRepository(application)

    val products: StateFlow<List<Product>> = repository.products
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val transactions: StateFlow<List<TransactionRecord>> = repository.transactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val debts: StateFlow<List<DebtRecord>> = repository.debts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val expenses: StateFlow<List<ExpenseRecord>> = repository.expenses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val assets: StateFlow<List<StoreAssetRecord>> = repository.assets
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val promos: StateFlow<List<PromoRecord>> = repository.promos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val suppliers: StateFlow<List<SupplierRecord>> = repository.suppliers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val storeProfile: StateFlow<StoreProfile> = repository.storeProfile

    // UI State Navigation
    private val _currentTab = MutableStateFlow(AppTab.BERANDA)
    val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

    private val _currentSubScreen = MutableStateFlow(SubScreen.NONE)
    val currentSubScreen: StateFlow<SubScreen> = _currentSubScreen.asStateFlow()

    // POS Cart State
    private val _cart = MutableStateFlow<List<CartItem>>(emptyList())
    val cart: StateFlow<List<CartItem>> = _cart.asStateFlow()

    private val _currentTrxNumber = MutableStateFlow(generateTrxNumber())
    val currentTrxNumber: StateFlow<String> = _currentTrxNumber.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("Semua")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _reportPeriod = MutableStateFlow("Bulan Ini")
    val reportPeriod: StateFlow<String> = _reportPeriod.asStateFlow()

    // Active Dialogs / BottomSheets
    var showCheckoutDialog = MutableStateFlow(false)
    var showDebtDialog = MutableStateFlow(false)
    var showReceiptDialog = MutableStateFlow(false)
    var selectedTransactionForReceipt = MutableStateFlow<TransactionRecord?>(null)
    var showAddProductDialog = MutableStateFlow(false)
    var editingProduct = MutableStateFlow<Product?>(null)
    var showRestockDialog = MutableStateFlow(false)
    var restockTargetProduct = MutableStateFlow<Product?>(null)
    var showQrisDialog = MutableStateFlow(false)
    var showPrinterDialog = MutableStateFlow(false)
    var showStaffDialog = MutableStateFlow(false)
    var showPinDialog = MutableStateFlow(false)
    var showEditProfileDialog = MutableStateFlow(false)
    var showBarcodeScannerModal = MutableStateFlow(false)

    // Daily Operational Stats calculated reactively from actual transactions
    val dailySummary: StateFlow<DailySummary> = combine(transactions) { trxListArray ->
        val trxList = trxListArray[0]
        val totalSales = trxList.sumOf { it.totalAmount }
        val totalCount = trxList.size
        val grossProfit = trxList.sumOf { it.profit }
        val itemsSold = trxList.sumOf { trx ->
            var count = 0
            trx.itemsSummary.split(",").forEach { line ->
                val qtyStr = line.substringAfter("x", "").trim().toIntOrNull() ?: 1
                count += qtyStr
            }
            if (count == 0) 1 else count
        }
        DailySummary(
            totalSales = totalSales,
            totalTransactions = totalCount,
            grossProfit = grossProfit,
            itemsSold = itemsSold
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DailySummary(0.0, 0, 0.0, 0))

    val totalDebtAmount: StateFlow<Double> = combine(debts) { debtListArray ->
        val list = debtListArray[0]
        list.filter { !it.isPaid }.sumOf { it.amount }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 450000.0)

    fun setTab(tab: AppTab) {
        _currentTab.value = tab
        if (tab == AppTab.QR_SCAN) {
            showBarcodeScannerModal.value = true
        }
    }

    fun navigateToSubScreen(sub: SubScreen) {
        _currentSubScreen.value = sub
    }

    fun popSubScreen(): Boolean {
        if (_currentSubScreen.value != SubScreen.NONE) {
            _currentSubScreen.value = SubScreen.NONE
            return true
        }
        return false
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedCategory(cat: String) {
        _selectedCategory.value = cat
    }

    fun setReportPeriod(period: String) {
        _reportPeriod.value = period
    }

    // Cart Operations
    fun addToCart(product: Product) {
        val currentList = _cart.value.toMutableList()
        val index = currentList.indexOfFirst { it.product.id == product.id }
        if (index != -1) {
            val item = currentList[index]
            if (item.quantity < product.stock) {
                currentList[index] = item.copy(quantity = item.quantity + 1)
            }
        } else {
            if (product.stock > 0) {
                currentList.add(CartItem(product = product, quantity = 1))
            }
        }
        _cart.value = currentList
    }

    fun decrementCartItem(product: Product) {
        val currentList = _cart.value.toMutableList()
        val index = currentList.indexOfFirst { it.product.id == product.id }
        if (index != -1) {
            val item = currentList[index]
            if (item.quantity > 1) {
                currentList[index] = item.copy(quantity = item.quantity - 1)
            } else {
                currentList.removeAt(index)
            }
            _cart.value = currentList
        }
    }

    fun removeFromCart(product: Product) {
        _cart.value = _cart.value.filter { it.product.id != product.id }
    }

    fun clearCart() {
        _cart.value = emptyList()
        _currentTrxNumber.value = generateTrxNumber()
    }

    fun completeCheckout(paymentType: String, cashGiven: Double, customerName: String = "Pelanggan Umum") {
        val items = _cart.value
        if (items.isEmpty()) return

        val total = items.sumOf { it.subtotal }
        val change = if (paymentType == "TUNAI") (cashGiven - total).coerceAtLeast(0.0) else 0.0

        viewModelScope.launch {
            val trx = repository.completeTransaction(
                items = items,
                trxNumber = _currentTrxNumber.value,
                paymentType = paymentType,
                cashGiven = cashGiven,
                changeAmount = change,
                customerName = customerName
            )
            selectedTransactionForReceipt.value = trx
            showReceiptDialog.value = true
            clearCart()
            showCheckoutDialog.value = false
        }
    }

    fun completeDebtCheckout(customerName: String, phone: String, dueDate: String, notes: String) {
        val items = _cart.value
        if (items.isEmpty()) return

        val total = items.sumOf { it.subtotal }
        val summary = items.joinToString(", ") { "${it.product.name} x ${it.quantity}" }

        viewModelScope.launch {
            // Deduct stock and save as transaction with BON payment type
            val trx = repository.completeTransaction(
                items = items,
                trxNumber = _currentTrxNumber.value,
                paymentType = "BON",
                cashGiven = 0.0,
                changeAmount = 0.0,
                customerName = customerName
            )

            // Add debt record
            repository.addDebt(
                DebtRecord(
                    customerName = customerName,
                    phone = phone,
                    amount = total,
                    dueDate = dueDate,
                    notes = if (notes.isNotBlank()) notes else summary
                )
            )

            selectedTransactionForReceipt.value = trx
            showReceiptDialog.value = true
            clearCart()
            showDebtDialog.value = false
        }
    }

    fun quickRestock(product: Product, addedStock: Int) {
        viewModelScope.launch {
            repository.addStock(product.id, addedStock)
            showRestockDialog.value = false
        }
    }

    fun saveProduct(
        id: Long,
        code: String,
        name: String,
        category: String,
        price: Double,
        costPrice: Double,
        stock: Int,
        minStock: Int,
        unit: String,
        barcode: String
    ) {
        viewModelScope.launch {
            val prod = Product(
                id = id,
                code = code,
                name = name,
                category = category,
                price = price,
                costPrice = costPrice,
                stock = stock,
                minStock = minStock,
                unit = unit,
                barcode = barcode
            )
            if (id == 0L) {
                repository.addProduct(prod)
            } else {
                repository.updateProduct(prod)
            }
            showAddProductDialog.value = false
            editingProduct.value = null
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            repository.deleteProduct(product)
        }
    }

    fun deleteTransaction(trx: TransactionRecord) {
        viewModelScope.launch {
            repository.deleteTransaction(trx)
        }
    }

    fun clearAllTransactions() {
        viewModelScope.launch {
            repository.clearAllTransactions()
        }
    }

    fun setAppTheme(mode: com.example.ui.theme.AppThemeMode) {
        com.example.ui.theme.ThemeManager.currentTheme = mode
    }

    fun setAppLanguage(lang: com.example.ui.theme.AppLanguage) {
        com.example.ui.theme.LocaleManager.language = lang
    }

    fun markDebtPaid(debt: DebtRecord) {
        viewModelScope.launch {
            repository.markDebtPaid(debt.id)
        }
    }

    fun addDebtRecord(customerName: String, phone: String, amount: Double, dueDate: String, notes: String) {
        viewModelScope.launch {
            repository.addDebt(
                DebtRecord(
                    customerName = customerName,
                    phone = phone,
                    amount = amount,
                    dueDate = dueDate,
                    notes = notes
                )
            )
        }
    }

    fun addExpense(title: String, category: String, amount: Double, dateText: String) {
        viewModelScope.launch {
            repository.addExpense(
                ExpenseRecord(
                    title = title,
                    category = category,
                    amount = amount,
                    dateText = dateText
                )
            )
        }
    }

    fun deleteExpense(expense: ExpenseRecord) {
        viewModelScope.launch {
            repository.deleteExpense(expense)
        }
    }

    fun addStoreAsset(name: String, value: Double, condition: String, notes: String) {
        viewModelScope.launch {
            repository.addAsset(
                StoreAssetRecord(
                    name = name,
                    value = value,
                    condition = condition,
                    notes = notes
                )
            )
        }
    }

    fun deleteAsset(asset: StoreAssetRecord) {
        viewModelScope.launch {
            repository.deleteAsset(asset)
        }
    }

    fun addPromo(title: String, discountPercent: Double, minSpend: Double, validUntil: String) {
        viewModelScope.launch {
            repository.addPromo(
                PromoRecord(
                    title = title,
                    discountPercent = discountPercent,
                    minSpend = minSpend,
                    validUntil = validUntil,
                    isActive = true
                )
            )
        }
    }

    fun togglePromo(promo: PromoRecord) {
        viewModelScope.launch {
            repository.togglePromo(promo)
        }
    }

    fun addSupplier(name: String, category: String, phone: String, address: String) {
        viewModelScope.launch {
            repository.addSupplier(
                SupplierRecord(
                    name = name,
                    category = category,
                    phone = phone,
                    address = address
                )
            )
        }
    }

    fun updateProfile(profile: StoreProfile) {
        repository.updateStoreProfile(profile)
        showEditProfileDialog.value = false
    }

    private fun generateTrxNumber(): String {
        val randomNum = (1000..9999).random()
        return "TRX-$randomNum"
    }

    companion object {
        fun formatRupiah(amount: Double): String {
            val format = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
            format.maximumFractionDigits = 0
            val formatted = format.format(amount)
            return formatted.replace("Rp", "Rp ")
        }
    }
}
