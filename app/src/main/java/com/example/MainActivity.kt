package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.StockStatus
import com.example.ui.components.AddEditProductDialog
import com.example.ui.components.BarcodeScannerDialog
import com.example.ui.components.DebtCheckoutDialog
import com.example.ui.components.EditProfileDialog
import com.example.ui.components.FastCheckoutDialog
import com.example.ui.components.PinDialog
import com.example.ui.components.PrinterTestDialog
import com.example.ui.components.QrisModal
import com.example.ui.components.QuickRestockDialog
import com.example.ui.components.StaffDialog
import com.example.ui.components.ThermalReceiptDialog
import com.example.ui.components.TopHeaderBar
import com.example.ui.components.WarungBottomNavBar
import com.example.ui.screens.CatalogScreen
import com.example.ui.screens.DebtScreen
import com.example.ui.screens.ExpensesAssetsScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.InventoryScreen
import com.example.ui.screens.PosScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.PromoSupplierScreen
import com.example.ui.screens.ReportScreen
import com.example.ui.theme.DarkEmeraldBg
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.SubScreen
import com.example.ui.viewmodel.WarungViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val viewModel: WarungViewModel = viewModel()
                WarungApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun WarungApp(viewModel: WarungViewModel) {
    val currentTab by viewModel.currentTab.collectAsState()
    val currentSubScreen by viewModel.currentSubScreen.collectAsState()
    val storeProfile by viewModel.storeProfile.collectAsState()
    val cart by viewModel.cart.collectAsState()
    val products by viewModel.products.collectAsState()
    val context = androidx.compose.ui.platform.LocalContext.current

    // Dialog states
    val showCheckout by viewModel.showCheckoutDialog.collectAsState()
    val showDebt by viewModel.showDebtDialog.collectAsState()
    val showReceipt by viewModel.showReceiptDialog.collectAsState()
    val selectedTrxForReceipt by viewModel.selectedTransactionForReceipt.collectAsState()
    val showAddProduct by viewModel.showAddProductDialog.collectAsState()
    val editingProd by viewModel.editingProduct.collectAsState()
    val showRestock by viewModel.showRestockDialog.collectAsState()
    val restockTarget by viewModel.restockTargetProduct.collectAsState()
    val showQris by viewModel.showQrisDialog.collectAsState()
    val showPrinter by viewModel.showPrinterDialog.collectAsState()
    val showStaff by viewModel.showStaffDialog.collectAsState()
    val showPin by viewModel.showPinDialog.collectAsState()
    val showEditProfile by viewModel.showEditProfileDialog.collectAsState()
    val showScanner by viewModel.showBarcodeScannerModal.collectAsState()

    val totalAmount = cart.sumOf { it.subtotal }

    // Handle back button smoothly
    BackHandler(enabled = currentSubScreen != SubScreen.NONE || currentTab != AppTab.BERANDA) {
        if (!viewModel.popSubScreen()) {
            viewModel.setTab(AppTab.BERANDA)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkEmeraldBg)
            .statusBarsPadding(),
        topBar = {
            val isProfileTab = currentTab == AppTab.PROFIL && currentSubScreen == SubScreen.NONE
            val isSubScreen = currentSubScreen != SubScreen.NONE
            val canBack = isSubScreen || isProfileTab

            TopHeaderBar(
                title = when {
                    isProfileTab -> com.example.ui.theme.LocaleManager.tr("Pengaturan Warungku", "Warungku Settings")
                    isSubScreen -> when (currentSubScreen) {
                        SubScreen.INVENTORY -> com.example.ui.theme.LocaleManager.tr("Stok Barang", "Inventory Stock")
                        SubScreen.CATALOG -> com.example.ui.theme.LocaleManager.tr("Katalog Produk", "Product Catalog")
                        SubScreen.DEBT -> com.example.ui.theme.LocaleManager.tr("Hutang & Piutang", "Customer Debts")
                        SubScreen.EXPENSES_ASSETS -> com.example.ui.theme.LocaleManager.tr("Pengeluaran & Aset", "Expenses & Assets")
                        SubScreen.PROMO_SUPPLIER -> com.example.ui.theme.LocaleManager.tr("Promo & Supplier", "Promos & Suppliers")
                        SubScreen.HISTORY -> com.example.ui.theme.LocaleManager.tr("Riwayat Transaksi", "Transaction History")
                        SubScreen.NONE -> "WARUNGKU"
                    }
                    currentTab == AppTab.TRANSAKSI -> com.example.ui.theme.LocaleManager.tr("Kasir POS", "Point of Sale")
                    currentTab == AppTab.LAPORAN -> com.example.ui.theme.LocaleManager.tr("Laporan Keuangan", "Financial Reports")
                    else -> "WARUNGKU"
                },
                subtitle = when {
                    isProfileTab -> com.example.ui.theme.LocaleManager.tr("Kelola preferensi dan pengaturan toko Anda", "Manage your store preferences and settings")
                    isSubScreen -> when (currentSubScreen) {
                        SubScreen.INVENTORY -> com.example.ui.theme.LocaleManager.tr("Monitor stok & peringatan restock", "Monitor stock & restock alerts")
                        SubScreen.CATALOG -> com.example.ui.theme.LocaleManager.tr("Kelola master data produk toko", "Manage store product master data")
                        SubScreen.DEBT -> com.example.ui.theme.LocaleManager.tr("Buku bon hutang & piutang pelanggan", "Customer credit book & receivables")
                        SubScreen.EXPENSES_ASSETS -> com.example.ui.theme.LocaleManager.tr("Catatan biaya operasional & inventaris", "Operational expenses & equipment")
                        SubScreen.PROMO_SUPPLIER -> com.example.ui.theme.LocaleManager.tr("Diskon promosi & kontak pemasok", "Discounts & supplier contacts")
                        SubScreen.HISTORY -> com.example.ui.theme.LocaleManager.tr("Log riwayat kasir lengkap", "Complete cashier transaction log")
                        SubScreen.NONE -> com.example.ui.theme.LocaleManager.tr("Kelola toko, stok & keuangan lebih mudah", "Manage store, stock & finances easier")
                    }
                    currentTab == AppTab.TRANSAKSI -> com.example.ui.theme.LocaleManager.tr("Mode kasir cepat & pembayaran", "Fast cashier checkout & payments")
                    currentTab == AppTab.LAPORAN -> com.example.ui.theme.LocaleManager.tr("Analisis omset dan laba rugi", "Revenue & gross profit analytics")
                    else -> com.example.ui.theme.LocaleManager.tr("Kelola toko, stok & keuangan lebih mudah", "Manage store, stock & finances easier")
                },
                canNavigateBack = canBack,
                onBackClick = {
                    if (isSubScreen) {
                        viewModel.popSubScreen()
                    } else if (isProfileTab) {
                        viewModel.setTab(AppTab.BERANDA)
                    }
                },
                onNotificationClick = {
                    val count = products.count { it.stockStatus == StockStatus.CRITICAL }
                    val debtCount = viewModel.debts.value.count { !it.isPaid }
                    android.widget.Toast.makeText(
                        context,
                        "Notifikasi: $count produk perlu restock, $debtCount bon aktif.",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                },
                onProfileClick = {
                    viewModel.setTab(AppTab.PROFIL)
                }
            )
        },
        bottomBar = {
            if (currentSubScreen == SubScreen.NONE) {
                WarungBottomNavBar(
                    currentTab = currentTab,
                    onTabSelected = { tab ->
                        viewModel.setTab(tab)
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentSubScreen) {
                SubScreen.INVENTORY -> InventoryScreen(viewModel = viewModel)
                SubScreen.CATALOG -> CatalogScreen(viewModel = viewModel)
                SubScreen.DEBT -> DebtScreen(viewModel = viewModel)
                SubScreen.EXPENSES_ASSETS -> ExpensesAssetsScreen(viewModel = viewModel)
                SubScreen.PROMO_SUPPLIER -> PromoSupplierScreen(viewModel = viewModel)
                SubScreen.HISTORY -> HistoryScreen(viewModel = viewModel)
                SubScreen.NONE -> {
                    when (currentTab) {
                        AppTab.BERANDA -> HomeScreen(viewModel = viewModel)
                        AppTab.TRANSAKSI -> PosScreen(viewModel = viewModel)
                        AppTab.QR_SCAN -> PosScreen(viewModel = viewModel) // Dialog scanner will overlay
                        AppTab.LAPORAN -> ReportScreen(viewModel = viewModel)
                        AppTab.PROFIL -> ProfileScreen(viewModel = viewModel)
                    }
                }
            }
        }
    }

    // Modal Dialogs
    if (showCheckout) {
        FastCheckoutDialog(
            totalAmount = totalAmount,
            onConfirmCash = { cash, custName ->
                viewModel.completeCheckout(paymentType = "TUNAI", cashGiven = cash, customerName = custName)
            },
            onConfirmQris = { custName ->
                viewModel.completeCheckout(paymentType = "QRIS", cashGiven = totalAmount, customerName = custName)
            },
            onDismiss = { viewModel.showCheckoutDialog.value = false }
        )
    }

    if (showDebt) {
        DebtCheckoutDialog(
            totalAmount = totalAmount,
            onConfirm = { name, phone, due, notes ->
                viewModel.completeDebtCheckout(name, phone, due, notes)
            },
            onDismiss = { viewModel.showDebtDialog.value = false }
        )
    }

    if (showReceipt && selectedTrxForReceipt != null) {
        ThermalReceiptDialog(
            transaction = selectedTrxForReceipt!!,
            storeName = storeProfile.storeName,
            storeAddress = storeProfile.address,
            storePhone = storeProfile.phone,
            onDismiss = {
                viewModel.showReceiptDialog.value = false
                viewModel.selectedTransactionForReceipt.value = null
            }
        )
    }

    if (showScanner) {
        BarcodeScannerDialog(
            products = products,
            onProductScanned = { prod ->
                viewModel.addToCart(prod)
            },
            onDismiss = {
                viewModel.showBarcodeScannerModal.value = false
                if (currentTab == AppTab.QR_SCAN) {
                    viewModel.setTab(AppTab.TRANSAKSI)
                }
            }
        )
    }

    if (showQris) {
        QrisModal(
            storeName = storeProfile.storeName,
            nmid = storeProfile.qrisNmid,
            onDismiss = { viewModel.showQrisDialog.value = false }
        )
    }

    if (showRestock && restockTarget != null) {
        QuickRestockDialog(
            product = restockTarget!!,
            onConfirm = { addedQty ->
                viewModel.quickRestock(restockTarget!!, addedQty)
            },
            onDismiss = {
                viewModel.showRestockDialog.value = false
                viewModel.restockTargetProduct.value = null
            }
        )
    }

    if (showAddProduct) {
        AddEditProductDialog(
            initialProduct = editingProd,
            onSave = { code, name, category, price, costPrice, stock, minStock, unit, barcode ->
                viewModel.saveProduct(
                    id = editingProd?.id ?: 0L,
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
            },
            onDismiss = {
                viewModel.showAddProductDialog.value = false
                viewModel.editingProduct.value = null
            }
        )
    }

    if (showPrinter) {
        PrinterTestDialog(
            printerName = storeProfile.printerName,
            onDismiss = { viewModel.showPrinterDialog.value = false }
        )
    }

    if (showStaff) {
        StaffDialog(
            staffCount = storeProfile.staffCount,
            onDismiss = { viewModel.showStaffDialog.value = false }
        )
    }

    if (showPin) {
        PinDialog(
            onDismiss = { viewModel.showPinDialog.value = false }
        )
    }

    if (showEditProfile) {
        EditProfileDialog(
            profile = storeProfile,
            onSave = { updated ->
                viewModel.updateProfile(updated)
            },
            onDismiss = { viewModel.showEditProfileDialog.value = false }
        )
    }
}
