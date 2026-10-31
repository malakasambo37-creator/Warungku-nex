package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocalAtm
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StockStatus
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.BrandTeal
import com.example.ui.theme.DarkEmeraldBg
import com.example.ui.theme.DarkEmeraldBorder
import com.example.ui.theme.DarkEmeraldCard
import com.example.ui.theme.DarkEmeraldSurface
import com.example.ui.theme.EmeraldMuted
import com.example.ui.theme.MintGreenLight
import com.example.ui.theme.MintGreenPrimary
import com.example.ui.theme.RoseDanger
import com.example.ui.theme.SkyInfo
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.SubScreen
import com.example.ui.viewmodel.WarungViewModel

@Composable
fun HomeScreen(
    viewModel: WarungViewModel,
    modifier: Modifier = Modifier
) {
    val summary by viewModel.dailySummary.collectAsState()
    val products by viewModel.products.collectAsState()
    val transactions by viewModel.transactions.collectAsState()
    val context = LocalContext.current

    var searchQuery by remember { mutableStateOf("") }
    val criticalCount = products.count { it.stockStatus == StockStatus.CRITICAL || it.stockStatus == StockStatus.LOW }
    val bestSellerName = products.firstOrNull()?.name ?: "Air Mineral 600ml"

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkEmeraldBg)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("home_screen")
    ) {
        // 1. Ringkasan Hari ini Card (matching Screenshot 1)
        Card(
            colors = CardDefaults.cardColors(containerColor = DarkEmeraldSurface),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkEmeraldBorder),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("ringkasan_hari_ini_card")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Header row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { }
                    ) {
                        Text(
                            text = "Ringkasan Hari ini",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                fontSize = 15.sp
                            )
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Expand",
                            tint = MintGreenLight,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 4 metrics horizontal row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    // Metric 1: Penjualan
                    MetricColumn(
                        icon = Icons.Default.CurrencyExchange,
                        label = "Penjualan",
                        value = WarungViewModel.formatRupiah(summary.totalSales),
                        modifier = Modifier.weight(1.2f)
                    )

                    // Metric 2: Transaksi
                    MetricColumn(
                        icon = Icons.Default.ShoppingCart,
                        label = "Transaksi",
                        value = "${summary.totalTransactions}",
                        modifier = Modifier.weight(0.8f)
                    )

                    // Metric 3: Laba Kotor
                    MetricColumn(
                        icon = Icons.Default.TrendingUp,
                        label = "Laba Kotor",
                        value = WarungViewModel.formatRupiah(summary.grossProfit),
                        modifier = Modifier.weight(1.1f)
                    )

                    // Metric 4: Produk Terjual
                    MetricColumn(
                        icon = Icons.Default.Inventory,
                        label = "Produk Terj...",
                        value = "${summary.itemsSold}",
                        modifier = Modifier.weight(0.9f)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Laporan Lengkap > Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Surface(
                        color = Color(0xFF04241E),
                        shape = RoundedCornerShape(20.dp),
                        border = androidx.compose.foundation.BorderStroke(0.8.dp, DarkEmeraldBorder),
                        modifier = Modifier
                            .clickable { viewModel.setTab(AppTab.LAPORAN) }
                            .testTag("btn_laporan_lengkap")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Laporan Lengkap",
                                color = TextPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = "Laporan",
                                tint = TextSecondary,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 2. Search Bar "Cari menu / fitur..." (matching Screenshot 1)
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = {
                Text(
                    text = "Cari menu / fitur...",
                    color = TextSecondary,
                    fontSize = 13.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = TextSecondary,
                    modifier = Modifier.size(20.dp)
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                focusedContainerColor = DarkEmeraldSurface,
                unfocusedContainerColor = DarkEmeraldSurface,
                focusedBorderColor = MintGreenPrimary,
                unfocusedBorderColor = DarkEmeraldBorder
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("search_menu_input")
        )

        Spacer(modifier = Modifier.height(18.dp))

        // 3. Menu Utama Section Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Menu Utama",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    fontSize = 16.sp
                )
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable { Toast.makeText(context, "Atur urutan menu utama", Toast.LENGTH_SHORT).show() }
            ) {
                Text(
                    text = "Kelola",
                    color = MintGreenLight,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = "Kelola",
                    tint = MintGreenLight,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 12 Menu Utama Grid Cards (4 columns x 3 rows) matching Screenshot 1
        val allMenuItems = listOf(
            MenuGridItem("pos", "Penjualan (P...", "Kasir & transa...", Icons.Default.PointOfSale) { viewModel.setTab(AppTab.TRANSAKSI) },
            MenuGridItem("stock", "Stok Barang", "Monitor stok", Icons.Default.Inventory) { viewModel.navigateToSubScreen(SubScreen.INVENTORY) },
            MenuGridItem("catalog", "Produk", "Kelola produk", Icons.Default.Widgets) { viewModel.navigateToSubScreen(SubScreen.CATALOG) },
            MenuGridItem("crm", "Pelanggan", "Data pelanggan", Icons.Default.People) { viewModel.navigateToSubScreen(SubScreen.DEBT) },

            MenuGridItem("history", "Riwayat Tran...", "Semua transa...", Icons.Default.History) { viewModel.navigateToSubScreen(SubScreen.HISTORY) },
            MenuGridItem("reports", "Laporan", "Penjualan & ke...", Icons.Default.Assessment) { viewModel.setTab(AppTab.LAPORAN) },
            MenuGridItem("debts", "Hutang Piut...", "Catat utang", Icons.Default.CreditCard) { viewModel.navigateToSubScreen(SubScreen.DEBT) },
            MenuGridItem("expenses", "Pengeluaran", "Catat biaya", Icons.Default.Receipt) { viewModel.navigateToSubScreen(SubScreen.EXPENSES_ASSETS) },

            MenuGridItem("promo", "Diskon & Pr...", "Buat promo", Icons.Default.LocalOffer) { viewModel.navigateToSubScreen(SubScreen.PROMO_SUPPLIER) },
            MenuGridItem("category", "Kategori", "Kelola kategori", Icons.Default.Category) { viewModel.navigateToSubScreen(SubScreen.CATALOG) },
            MenuGridItem("supplier", "Supplier", "Data supplier", Icons.Default.LocalShipping) { viewModel.navigateToSubScreen(SubScreen.PROMO_SUPPLIER) },
            MenuGridItem("asset", "Aset Toko", "Inventaris toko", Icons.Default.Storefront) { viewModel.navigateToSubScreen(SubScreen.EXPENSES_ASSETS) }
        )

        val filteredMenuItems = if (searchQuery.isBlank()) {
            allMenuItems
        } else {
            allMenuItems.filter {
                it.title.contains(searchQuery, ignoreCase = true) ||
                        it.subtitle.contains(searchQuery, ignoreCase = true)
            }
        }

        // Render in rows of 4
        for (i in filteredMenuItems.indices step 4) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (j in 0 until 4) {
                    val index = i + j
                    if (index < filteredMenuItems.size) {
                        MenuGridCard(
                            item = filteredMenuItems[index],
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // 4. Promo Banner Card (matching Screenshot 1)
        Card(
            colors = CardDefaults.cardColors(containerColor = DarkEmeraldSurface),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkEmeraldBorder),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("promo_banner_card")
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF042F24)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Storefront,
                            contentDescription = "Promo",
                            tint = MintGreenLight,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "Tingkatkan penjualan tokomu",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                fontSize = 13.sp
                            )
                        )
                        Text(
                            text = "Buat promo menarik untuk pelanggan setia",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Bright Mint Green Button
                Button(
                    onClick = { viewModel.navigateToSubScreen(SubScreen.PROMO_SUPPLIER) },
                    colors = ButtonDefaults.buttonColors(containerColor = MintGreenPrimary),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                    modifier = Modifier.testTag("btn_buat_promo_banner")
                ) {
                    Text(
                        text = "Buat Promo",
                        color = DarkEmeraldBg,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 5. Insight & Rekomendasi Section (matching Screenshot 1)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Insight & Rekomendasi",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    fontSize = 15.sp
                )
            )
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Buka",
                tint = TextSecondary,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Card 1: Produk Terlaris
            item {
                InsightCard(
                    title = "Produk Terlaris",
                    mainText = bestSellerName,
                    actionText = "Terjual 10 pcs",
                    isWarning = false,
                    onClick = { viewModel.setTab(AppTab.LAPORAN) },
                    testTag = "insight_card_terlaris"
                )
            }

            // Card 2: Stok Menipis
            item {
                InsightCard(
                    title = "Stok Menipis",
                    mainText = if (criticalCount > 0) "$criticalCount produk perlu restock" else "Semua stok terkendali",
                    actionText = "Lihat sekarang",
                    isWarning = criticalCount > 0,
                    onClick = { viewModel.navigateToSubScreen(SubScreen.INVENTORY) },
                    testTag = "insight_card_stok"
                )
            }

            // Card 3: Target Penjualan
            item {
                val targetAmount = 500000.0
                val remainingTarget = (targetAmount - summary.totalSales).coerceAtLeast(0.0)
                InsightCard(
                    title = "Target Penjualan",
                    mainText = "${WarungViewModel.formatRupiah(summary.totalSales)} / Rp 500k",
                    actionText = if (remainingTarget > 0) "${WarungViewModel.formatRupiah(remainingTarget)} lagi" else "Target Tercapai! 🎉",
                    isWarning = false,
                    onClick = { viewModel.setTab(AppTab.LAPORAN) },
                    testTag = "insight_card_target"
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 6. Akses Cepat Section (matching Screenshot 1)
        Text(
            text = "Akses Cepat",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                fontSize = 15.sp
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                QuickActionButton(
                    icon = Icons.Default.QrCodeScanner,
                    label = com.example.ui.theme.LocaleManager.tr("Scan Barcode", "Scan Barcode"),
                    onClick = { viewModel.showBarcodeScannerModal.value = true },
                    testTag = "quick_action_scan"
                )
            }

            item {
                QuickActionButton(
                    icon = Icons.Default.Print,
                    label = com.example.ui.theme.LocaleManager.tr("Cetak Struk", "Print Receipt"),
                    onClick = {
                        val lastTrx = transactions.firstOrNull()
                        if (lastTrx != null) {
                            viewModel.selectedTransactionForReceipt.value = lastTrx
                            viewModel.showReceiptDialog.value = true
                        } else {
                            Toast.makeText(context, com.example.ui.theme.LocaleManager.tr("Belum ada transaksi untuk dicetak", "No transactions to print yet"), Toast.LENGTH_SHORT).show()
                        }
                    },
                    testTag = "quick_action_cetak"
                )
            }

            item {
                QuickActionButton(
                    icon = Icons.Default.Widgets,
                    label = com.example.ui.theme.LocaleManager.tr("Tambah Produk", "Add Product"),
                    onClick = { viewModel.showAddProductDialog.value = true },
                    testTag = "quick_action_tambah_produk"
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun MetricColumn(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MintGreenLight,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = TextSecondary,
                fontSize = 10.sp
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                fontSize = 12.sp
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

data class MenuGridItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)

@Composable
fun MenuGridCard(
    item: MenuGridItem,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = DarkEmeraldSurface),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkEmeraldBorder),
        modifier = modifier
            .height(104.dp)
            .clickable { item.onClick() }
            .testTag("menu_card_${item.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF042F24)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    tint = MintGreenLight,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = item.title,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(1.dp))
            Text(
                text = item.subtitle,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TextMuted,
                    fontSize = 9.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun InsightCard(
    title: String,
    mainText: String,
    actionText: String,
    isWarning: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = DarkEmeraldSurface),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, if (isWarning) AmberWarning else DarkEmeraldBorder),
        modifier = Modifier
            .width(160.dp)
            .height(96.dp)
            .clickable { onClick() }
            .testTag(testTag)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TextSecondary,
                    fontSize = 11.sp
                )
            )
            Text(
                text = mainText,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = actionText,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = if (isWarning) AmberWarning else MintGreenLight,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
            )
        }
    }
}

@Composable
fun QuickActionButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    testTag: String
) {
    Surface(
        color = DarkEmeraldSurface,
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkEmeraldBorder),
        modifier = Modifier
            .clickable { onClick() }
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF042F24)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = MintGreenLight,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp
                )
            )
        }
    }
}
