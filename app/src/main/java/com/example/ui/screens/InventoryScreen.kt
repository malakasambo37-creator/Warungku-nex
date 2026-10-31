package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Product
import com.example.data.model.StockStatus
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.BrandTeal
import com.example.ui.theme.DarkEmeraldBg
import com.example.ui.theme.DarkEmeraldBorder
import com.example.ui.theme.DarkEmeraldCard
import com.example.ui.theme.DarkEmeraldSurface
import com.example.ui.theme.MintGreenLight
import com.example.ui.theme.MintGreenPrimary
import com.example.ui.theme.RoseDanger
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.WarungViewModel

@Composable
fun InventoryScreen(
    viewModel: WarungViewModel,
    modifier: Modifier = Modifier
) {
    val products by viewModel.products.collectAsState()
    val suppliers by viewModel.suppliers.collectAsState()
    val context = LocalContext.current

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Semua") }
    val filters = listOf("Semua", "Kritis / Restock", "Menipis", "Aman")

    val filteredProducts = products.filter { prod ->
        val matchesFilter = when (selectedFilter) {
            "Kritis / Restock" -> prod.stockStatus == StockStatus.CRITICAL
            "Menipis" -> prod.stockStatus == StockStatus.LOW
            "Aman" -> prod.stockStatus == StockStatus.SAFE
            else -> true
        }
        val matchesQuery = prod.name.contains(searchQuery, ignoreCase = true) ||
                prod.code.contains(searchQuery, ignoreCase = true) ||
                prod.category.contains(searchQuery, ignoreCase = true) ||
                prod.barcode.contains(searchQuery, ignoreCase = true)
        matchesFilter && matchesQuery
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkEmeraldBg)
            .padding(16.dp)
            .testTag("inventory_screen")
    ) {
        // Header with weight on text column
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Stok Barang & Peringatan Restock",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                Text(
                    text = "Monitor inventori & kontrol restock otomatis",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 11.sp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // + Restock Stok button (guaranteed horizontal width)
            Button(
                onClick = {
                    viewModel.showAddProductDialog.value = true
                },
                colors = ButtonDefaults.buttonColors(containerColor = MintGreenPrimary),
                shape = RoundedCornerShape(8.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                modifier = Modifier.testTag("btn_restock_stok")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Tambah",
                    tint = DarkEmeraldBg,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "+ Restock Stok",
                    color = DarkEmeraldBg,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Search Bar for Inventory
        androidx.compose.material3.OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { androidx.compose.material3.Text("Cari nama barang / kode / barcode...", color = TextSecondary, fontSize = 13.sp) },
            leadingIcon = {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Cari",
                    tint = MintGreenPrimary
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    androidx.compose.material3.IconButton(onClick = { searchQuery = "" }) {
                        androidx.compose.material3.Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear",
                            tint = TextSecondary
                        )
                    }
                }
            },
            colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                focusedContainerColor = DarkEmeraldSurface,
                unfocusedContainerColor = DarkEmeraldSurface,
                focusedBorderColor = MintGreenPrimary,
                unfocusedBorderColor = DarkEmeraldBorder
            ),
            shape = RoundedCornerShape(10.dp),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("search_inventory_input")
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Status filter chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(filters) { filter ->
                val isSelected = selectedFilter == filter
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isSelected) MintGreenPrimary else DarkEmeraldSurface,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isSelected) MintGreenPrimary else DarkEmeraldBorder
                    ),
                    modifier = Modifier
                        .clickable { selectedFilter = filter }
                        .testTag("filter_chip_$filter")
                ) {
                    Text(
                        text = filter,
                        color = if (isSelected) DarkEmeraldBg else TextPrimary,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Inventory Items List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredProducts) { item ->
                InventoryItemCard(
                    product = item,
                    onEdit = {
                        viewModel.editingProduct.value = item
                        viewModel.showAddProductDialog.value = true
                    },
                    onQuickRestock = {
                        viewModel.restockTargetProduct.value = item
                        viewModel.showRestockDialog.value = true
                    },
                    onOrderSupplier = {
                        val supplier = suppliers.firstOrNull { it.category.contains(item.category, ignoreCase = true) }
                            ?: suppliers.firstOrNull()
                        val phone = supplier?.phone ?: "081299887766"
                        orderStockViaWhatsApp(context, item, phone, supplier?.name ?: "Supplier Sembako")
                    }
                )
            }
        }
    }
}

@Composable
fun InventoryItemCard(
    product: Product,
    onEdit: () -> Unit,
    onQuickRestock: () -> Unit,
    onOrderSupplier: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = DarkEmeraldSurface),
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            when (product.stockStatus) {
                StockStatus.CRITICAL -> RoseDanger
                StockStatus.LOW -> AmberWarning
                StockStatus.SAFE -> DarkEmeraldBorder
            }
        ),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("inventory_item_${product.code}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = product.code,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MintGreenLight,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            fontSize = 14.sp
                        )
                    )
                }

                // Status Reorder badge
                val (badgeBg, badgeText, badgeColor) = when (product.stockStatus) {
                    StockStatus.CRITICAL -> Triple(Color(0xFF450A0A), "Kritis / Restock", RoseDanger)
                    StockStatus.LOW -> Triple(Color(0xFF422006), "Menipis", AmberWarning)
                    StockStatus.SAFE -> Triple(Color(0xFF042F24), "Aman", MintGreenLight)
                }

                Surface(
                    color = badgeBg,
                    shape = RoundedCornerShape(6.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, badgeColor)
                ) {
                    Text(
                        text = badgeText,
                        color = badgeColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Kategori: ${product.category}",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 11.sp)
                )
                Text(
                    text = "Sisa: ${product.stock} ${product.unit}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = if (product.stockStatus == StockStatus.CRITICAL) RoseDanger else TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action Buttons matching prototype: Edit or Pesan Lagi
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = Color(0xFF042F24),
                    shape = RoundedCornerShape(6.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MintGreenPrimary),
                    modifier = Modifier
                        .clickable { onQuickRestock() }
                        .testTag("btn_quick_restock_${product.code}")
                ) {
                    Text(
                        text = "+ Tambah Stok",
                        color = MintGreenLight,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                if (product.stockStatus == StockStatus.CRITICAL) {
                    // Pesan Lagi via WA
                    Surface(
                        color = Color(0xFF450A0A),
                        shape = RoundedCornerShape(6.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, RoseDanger),
                        modifier = Modifier
                            .clickable { onOrderSupplier() }
                            .testTag("btn_pesan_lagi_${product.code}")
                    ) {
                        Text(
                            text = "Pesan Lagi",
                            color = RoseDanger,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                } else {
                    Surface(
                        color = DarkEmeraldCard,
                        shape = RoundedCornerShape(6.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, DarkEmeraldBorder),
                        modifier = Modifier
                            .clickable { onEdit() }
                            .testTag("btn_edit_${product.code}")
                    ) {
                        Text(
                            text = "Edit",
                            color = TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }
            }
        }
    }
}

fun orderStockViaWhatsApp(context: Context, product: Product, supplierPhone: String, supplierName: String) {
    val msg = """
        Halo $supplierName,
        Saya dari Toko Kelontong Barokah ingin memesan restock barang:
        Barang: *${product.name}* (Kode: ${product.code})
        Jumlah pesan: 20 ${product.unit}
        Mohon info harga dan estimasi pengiriman ya. Terima kasih!
    """.trimIndent()

    val cleanPhone = if (supplierPhone.startsWith("0")) "62" + supplierPhone.substring(1) else supplierPhone
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse("https://api.whatsapp.com/send?phone=$cleanPhone&text=" + Uri.encode(msg))
    }
    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Membuka WhatsApp ke Supplier $supplierName...", Toast.LENGTH_SHORT).show()
    }
}
