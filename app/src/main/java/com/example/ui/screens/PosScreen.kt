package com.example.ui.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CartItem
import com.example.data.model.Product
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
fun PosScreen(
    viewModel: WarungViewModel,
    modifier: Modifier = Modifier
) {
    val products by viewModel.products.collectAsState()
    val cart by viewModel.cart.collectAsState()
    val trxNumber by viewModel.currentTrxNumber.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    val totalAmount = cart.sumOf { it.subtotal }

    val categories = listOf("Semua", "Sembako", "Bumbu", "Makanan", "Minuman", "Kebersihan")

    val filteredProducts = products.filter { prod ->
        val matchesCategory = selectedCategory == "Semua" || prod.category.equals(selectedCategory, ignoreCase = true)
        val matchesQuery = prod.name.contains(searchQuery, ignoreCase = true) ||
                prod.code.contains(searchQuery, ignoreCase = true) ||
                prod.barcode.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesQuery
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkEmeraldBg)
            .padding(16.dp)
            .testTag("pos_screen")
    ) {
        // Header with clean weight layout
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Kasir & Transaksi POS",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                Text(
                    text = "Mode kasir cepat & kontrol inventori",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 11.sp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Scan Active Pill Button (guaranteed horizontal layout)
            Surface(
                color = Color(0xFF042F24),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MintGreenPrimary),
                modifier = Modifier
                    .clickable { viewModel.showBarcodeScannerModal.value = true }
                    .testTag("btn_scan_active")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.QrCodeScanner,
                        contentDescription = "Scan",
                        tint = MintGreenLight,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Scan Active",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MintGreenLight,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.setSearchQuery(it) },
            placeholder = { Text("Pilih Produk / Scan Barcode...", color = TextSecondary, fontSize = 13.sp) },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Cari", tint = MintGreenPrimary)
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { viewModel.setSearchQuery("") }) {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear", tint = TextSecondary)
                    }
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                focusedContainerColor = DarkEmeraldSurface,
                unfocusedContainerColor = DarkEmeraldSurface,
                focusedBorderColor = MintGreenPrimary,
                unfocusedBorderColor = DarkEmeraldBorder
            ),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("search_barcode_input")
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Category Filter Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(categories) { cat ->
                val isSelected = selectedCategory == cat
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isSelected) MintGreenPrimary else DarkEmeraldSurface,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isSelected) MintGreenPrimary else DarkEmeraldBorder
                    ),
                    modifier = Modifier
                        .clickable { viewModel.setSelectedCategory(cat) }
                        .testTag("cat_chip_$cat")
                ) {
                    Text(
                        text = cat,
                        color = if (isSelected) DarkEmeraldBg else TextPrimary,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Main content: Product catalog (scrollable list)
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredProducts) { product ->
                ProductPosCard(
                    product = product,
                    onAddToCart = { viewModel.addToCart(product) }
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Bottom Keranjang Belanja Section matching Page 2
        KeranjangBelanjaCard(
            trxNumber = trxNumber,
            cart = cart,
            totalAmount = totalAmount,
            onIncrement = { viewModel.addToCart(it) },
            onDecrement = { viewModel.decrementCartItem(it) },
            onRemove = { viewModel.removeFromCart(it) },
            onPayCashOrQris = {
                if (cart.isNotEmpty()) {
                    viewModel.showCheckoutDialog.value = true
                }
            },
            onPayDebt = {
                if (cart.isNotEmpty()) {
                    viewModel.showDebtDialog.value = true
                }
            }
        )
    }
}

@Composable
fun ProductPosCard(
    product: Product,
    onAddToCart: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = DarkEmeraldSurface),
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkEmeraldBorder),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("product_pos_${product.code}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = WarungViewModel.formatRupiah(product.price),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MintGreenLight,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "(Stok: ${product.stock})",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = if (product.stock <= 2) RoseDanger else TextSecondary,
                            fontSize = 11.sp
                        )
                    )
                }
            }

            // + Tambah button
            Button(
                onClick = onAddToCart,
                enabled = product.stock > 0,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF042F24),
                    contentColor = MintGreenLight
                ),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MintGreenPrimary),
                modifier = Modifier.testTag("btn_add_to_cart_${product.code}")
            ) {
                Text(
                    text = if (product.stock > 0) "+ Tambah" else "Habis",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun KeranjangBelanjaCard(
    trxNumber: String,
    cart: List<CartItem>,
    totalAmount: Double,
    onIncrement: (Product) -> Unit,
    onDecrement: (Product) -> Unit,
    onRemove: (Product) -> Unit,
    onPayCashOrQris: () -> Unit,
    onPayDebt: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = DarkEmeraldSurface),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MintGreenPrimary),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("keranjang_belanja_card")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Keranjang Belanja (#$trxNumber)",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MintGreenLight
                    )
                )
                Text(
                    text = "${cart.sumOf { it.quantity }} Item",
                    style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))
            HorizontalDivider(color = DarkEmeraldBorder)
            Spacer(modifier = Modifier.height(6.dp))

            if (cart.isEmpty()) {
                Text(
                    text = "Keranjang masih kosong. Pilih produk atau scan barcode.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextMuted,
                        fontSize = 12.sp
                    ),
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            } else {
                // Cart item list preview (up to 3 or scroll)
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    cart.take(3).forEach { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${item.product.name} x ${item.quantity}",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary),
                                modifier = Modifier.weight(1f)
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = WarungViewModel.formatRupiah(item.subtotal),
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = MintGreenLight,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                IconButton(
                                    onClick = { onDecrement(item.product) },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Remove,
                                        contentDescription = "Kurang",
                                        tint = TextSecondary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                IconButton(
                                    onClick = { onIncrement(item.product) },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Tambah",
                                        tint = MintGreenPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                    if (cart.size > 3) {
                        Text(
                            text = "+ ${cart.size - 3} produk lainnya...",
                            style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = DarkEmeraldBorder)
            Spacer(modifier = Modifier.height(8.dp))

            // Total Bayar Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total Bayar:",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                Text(
                    text = WarungViewModel.formatRupiah(totalAmount),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = MintGreenPrimary
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 2 Primary Action Buttons: [Tunai / QRIS] and [Hutang/Bon]
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onPayCashOrQris,
                    enabled = cart.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MintGreenPrimary,
                        disabledContainerColor = DarkEmeraldCard
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("btn_pos_tunai_qris")
                ) {
                    Text(
                        text = "Tunai / QRIS",
                        color = if (cart.isNotEmpty()) DarkEmeraldBg else TextMuted,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }

                Button(
                    onClick = onPayDebt,
                    enabled = cart.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DarkEmeraldCard,
                        disabledContainerColor = DarkEmeraldCard
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AmberWarning),
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("btn_pos_hutang_bon")
                ) {
                    Text(
                        text = "Hutang / Bon",
                        color = AmberWarning,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}
