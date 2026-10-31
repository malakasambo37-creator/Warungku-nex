package com.example.ui.components

import android.content.Context
import android.os.Vibrator
import android.widget.Toast
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.Product
import com.example.data.model.StoreProfile
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.BrandTeal
import com.example.ui.theme.DarkEmeraldBg
import com.example.ui.theme.DarkEmeraldBorder
import com.example.ui.theme.DarkEmeraldCard
import com.example.ui.theme.DarkEmeraldSurface
import com.example.ui.theme.EmeraldMuted
import com.example.ui.theme.MintGreenLight
import com.example.ui.theme.MintGreenPrimary
import com.example.ui.theme.MintGreenSoft
import com.example.ui.theme.RoseDanger
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.WarungViewModel

@Composable
fun FastCheckoutDialog(
    totalAmount: Double,
    onConfirmCash: (Double, String) -> Unit,
    onConfirmQris: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var paymentMethod by remember { mutableStateOf("TUNAI") } // TUNAI or QRIS
    var cashGivenText by remember { mutableStateOf(totalAmount.toInt().toString()) }
    var customerName by remember { mutableStateOf("Pelanggan Umum") }

    val cashGiven = cashGivenText.toDoubleOrNull() ?: totalAmount
    val change = (cashGiven - totalAmount).coerceAtLeast(0.0)

    val quickAmounts = listOf(
        totalAmount,
        50000.0,
        100000.0,
        200000.0
    ).filter { it >= totalAmount }.distinct()

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = DarkEmeraldSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkEmeraldBorder),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp)
                .testTag("checkout_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Pembayaran Kasir",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MintGreenLight
                        )
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Total Bill Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkEmeraldCard),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkEmeraldBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "TOTAL TAGIHAN",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TextMuted,
                                letterSpacing = 1.sp
                            )
                        )
                        Text(
                            text = WarungViewModel.formatRupiah(totalAmount),
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = MintGreenPrimary
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Payment Method Selector
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { paymentMethod = "TUNAI" },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (paymentMethod == "TUNAI") MintGreenPrimary else DarkEmeraldCard
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("btn_payment_tunai")
                    ) {
                        Text(
                            text = "💵 Tunai",
                            color = if (paymentMethod == "TUNAI") DarkEmeraldBg else TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Button(
                        onClick = { paymentMethod = "QRIS" },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (paymentMethod == "QRIS") MintGreenPrimary else DarkEmeraldCard
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("btn_payment_qris")
                    ) {
                        Text(
                            text = "📱 QRIS",
                            color = if (paymentMethod == "QRIS") DarkEmeraldBg else TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Customer Name
                OutlinedTextField(
                    value = customerName,
                    onValueChange = { customerName = it },
                    label = { Text("Nama Pelanggan") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = MintGreenPrimary,
                        unfocusedBorderColor = DarkEmeraldBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_customer_name")
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (paymentMethod == "TUNAI") {
                    Text(
                        text = "Uang Diterima:",
                        style = MaterialTheme.typography.labelMedium.copy(color = TextSecondary)
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    // Quick Cash Chips
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        item {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (cashGiven == totalAmount) MintGreenPrimary else DarkEmeraldCard,
                                border = androidx.compose.foundation.BorderStroke(1.dp, DarkEmeraldBorder),
                                modifier = Modifier
                                    .clickable { cashGivenText = totalAmount.toInt().toString() }
                                    .testTag("chip_uang_pas")
                            ) {
                                Text(
                                    text = "Uang Pas",
                                    color = if (cashGiven == totalAmount) DarkEmeraldBg else TextPrimary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                        items(quickAmounts) { amount ->
                            if (amount != totalAmount) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (cashGiven == amount) MintGreenPrimary else DarkEmeraldCard,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkEmeraldBorder),
                                    modifier = Modifier
                                        .clickable { cashGivenText = amount.toInt().toString() }
                                        .testTag("chip_quick_amount_${amount.toInt()}")
                                ) {
                                    Text(
                                        text = WarungViewModel.formatRupiah(amount),
                                        color = if (cashGiven == amount) DarkEmeraldBg else TextPrimary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = cashGivenText,
                        onValueChange = { cashGivenText = it },
                        label = { Text("Nominal Tunai (Rp)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = MintGreenPrimary,
                            unfocusedBorderColor = DarkEmeraldBorder
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_cash_given")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Kembalian calculation
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (change >= 0) Color(0xFF042F24) else Color(0xFF450A0A)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (change >= 0) "Kembalian:" else "Uang Kurang:",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = if (change >= 0) MintGreenLight else RoseDanger,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                            Text(
                                text = WarungViewModel.formatRupiah(if (change >= 0) change else (totalAmount - cashGiven)),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = if (change >= 0) MintGreenLight else RoseDanger,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                } else {
                    // QRIS Preview info
                    Card(
                        colors = CardDefaults.cardColors(containerColor = DarkEmeraldCard),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MintGreenPrimary),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.QrCode,
                                contentDescription = "QRIS",
                                tint = MintGreenPrimary,
                                modifier = Modifier.size(60.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "QRIS Dinamis Siap Dipindai",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    color = MintGreenLight,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = "NMID: ID1029384 (Toko Kelontong Barokah)",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 11.sp)
                            )
                            Text(
                                text = "Pelanggan langsung scan nominal otomatis terisi",
                                style = MaterialTheme.typography.labelSmall.copy(color = TextMuted, fontSize = 10.sp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        if (paymentMethod == "TUNAI") {
                            onConfirmCash(cashGiven, customerName)
                        } else {
                            onConfirmQris(customerName)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MintGreenPrimary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("btn_confirm_payment")
                ) {
                    Text(
                        text = "Konfirmasi & Cetak Struk",
                        color = DarkEmeraldBg,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}

@Composable
fun DebtCheckoutDialog(
    totalAmount: Double,
    onConfirm: (customerName: String, phone: String, dueDate: String, notes: String) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf("05 Nov 2026") }
    var notes by remember { mutableStateOf("") }

    val context = LocalContext.current

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = DarkEmeraldSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, AmberWarning),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp)
                .testTag("debt_checkout_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Catat Hutang / Bon Pembeli",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = AmberWarning
                        )
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF321D05)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "NOMINAL BON",
                            style = MaterialTheme.typography.labelSmall.copy(color = AmberWarning)
                        )
                        Text(
                            text = WarungViewModel.formatRupiah(totalAmount),
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = AmberWarning
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nama Pelanggan (e.g. Pak Ahmad)") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = AmberWarning,
                        unfocusedBorderColor = DarkEmeraldBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_debt_name")
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("No. WhatsApp (untuk kirim tagihan)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = AmberWarning,
                        unfocusedBorderColor = DarkEmeraldBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_debt_phone")
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = dueDate,
                    onValueChange = { dueDate = it },
                    label = { Text("Jatuh Tempo (e.g. 05 Nov 2026)") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = AmberWarning,
                        unfocusedBorderColor = DarkEmeraldBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_debt_due_date")
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Catatan Tambahan (Opsional)") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = AmberWarning,
                        unfocusedBorderColor = DarkEmeraldBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_debt_notes")
                )

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        if (name.isBlank()) {
                            Toast.makeText(context, "Nama pelanggan wajib diisi", Toast.LENGTH_SHORT).show()
                        } else {
                            onConfirm(name, phone, dueDate, notes)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AmberWarning),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("btn_confirm_debt")
                ) {
                    Text(
                        text = "Simpan ke Buku Bon",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}

@Composable
fun BarcodeScannerDialog(
    products: List<Product>,
    onProductScanned: (Product) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var lastScannedName by remember { mutableStateOf<String?>(null) }

    // Laser scanning animation
    val infiniteTransition = rememberInfiniteTransition(label = "laser")
    val laserY by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "laser_pos"
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = DarkEmeraldSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, MintGreenPrimary),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp)
                .testTag("barcode_scanner_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.QrCodeScanner,
                            contentDescription = "Scan",
                            tint = MintGreenPrimary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Scanner Barcode Produk",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MintGreenLight
                            )
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Camera Viewfinder Box with Laser
                Box(
                    modifier = Modifier
                        .size(240.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF021714))
                        .border(2.dp, MintGreenPrimary, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val y = size.height * laserY
                        drawLine(
                            color = Color(0xFFFF3333),
                            start = Offset(20f, y),
                            end = Offset(size.width - 20f, y),
                            strokeWidth = 4f
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.QrCodeScanner,
                            contentDescription = "Focus",
                            tint = Color(0x5510B981),
                            modifier = Modifier.size(100.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Arahkan Barcode ke Garis Merah",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (lastScannedName != null) {
                    Surface(
                        color = Color(0xFF042F24),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MintGreenPrimary)
                    ) {
                        Text(
                            text = "✓ Berhasil Scan: $lastScannedName",
                            color = MintGreenLight,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }

                Text(
                    text = "Pilih Produk Cepat untuk Simulasi Scan:",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextSecondary,
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(6.dp))

                // Test barcode chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(products.take(6)) { prod ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = DarkEmeraldCard,
                            border = androidx.compose.foundation.BorderStroke(1.dp, DarkEmeraldBorder),
                            modifier = Modifier
                                .clickable {
                                    lastScannedName = prod.name
                                    Toast.makeText(context, "Beep! ${prod.name} ditambahkan ke keranjang", Toast.LENGTH_SHORT).show()
                                    onProductScanned(prod)
                                }
                                .testTag("scan_chip_${prod.code}")
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = prod.name,
                                    color = TextPrimary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = prod.barcode.ifEmpty { prod.code },
                                    color = MintGreenLight,
                                    fontSize = 9.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth().testTag("btn_close_scanner")
                ) {
                    Text("Selesai Scan", color = TextPrimary)
                }
            }
        }
    }
}

@Composable
fun QrisModal(
    storeName: String = "Toko Kelontong Barokah",
    nmid: String = "ID1029384",
    amount: Double? = null,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp)
                .testTag("qris_modal")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "QRIS STANDAR PEMBAYARAN",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFFC00000),
                            letterSpacing = 1.sp
                        )
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup", tint = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = storeName.uppercase(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    ),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "NMID: $nmid",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = FontFamily.Monospace,
                        color = Color(0xFF555555)
                    )
                )

                if (amount != null && amount > 0) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = WarungViewModel.formatRupiah(amount),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF047857)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Realistic QR Graphic Container
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .background(Color.White)
                        .border(2.dp, Color(0xFFCCCCCC), RoundedCornerShape(8.dp))
                        .padding(10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.QrCode,
                        contentDescription = "QRIS Code",
                        tint = Color.Black,
                        modifier = Modifier.size(180.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Dapat dibayar menggunakan GoPay, OVO, Dana, ShopeePay, BCA, Mandiri, BRI, BNI & Seluruh Bank.",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color(0xFF666666),
                        fontSize = 10.sp
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        Toast.makeText(context, "QRIS tersimpan / siap dibagikan", Toast.LENGTH_SHORT).show()
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF047857)),
                    modifier = Modifier.fillMaxWidth().testTag("btn_close_qris")
                ) {
                    Text("Bagikan / Tutup", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun QuickRestockDialog(
    product: Product,
    onConfirm: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var addedQtyText by remember { mutableStateOf("10") }
    val addedQty = addedQtyText.toIntOrNull() ?: 10

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = DarkEmeraldSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, MintGreenPrimary),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp)
                .testTag("quick_restock_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Tambah Stok Masuk",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MintGreenLight
                    )
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "${product.name} (${product.code})",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Text(
                    text = "Stok saat ini: ${product.stock} ${product.unit}",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Quick buttons (+5, +10, +25, +50)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(5, 10, 25, 50).forEach { qty ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (addedQty == qty) MintGreenPrimary else DarkEmeraldCard,
                            border = androidx.compose.foundation.BorderStroke(1.dp, DarkEmeraldBorder),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { addedQtyText = qty.toString() }
                        ) {
                            Text(
                                text = "+$qty",
                                color = if (addedQty == qty) DarkEmeraldBg else TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = addedQtyText,
                    onValueChange = { addedQtyText = it },
                    label = { Text("Jumlah Tambahan (${product.unit})") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = MintGreenPrimary,
                        unfocusedBorderColor = DarkEmeraldBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_restock_qty")
                )

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Total stok setelah restock: ${product.stock + addedQty} ${product.unit}",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MintGreenLight,
                        fontWeight = FontWeight.Medium
                    )
                )

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Batal", color = TextSecondary)
                    }

                    Button(
                        onClick = { onConfirm(addedQty) },
                        colors = ButtonDefaults.buttonColors(containerColor = MintGreenPrimary),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("btn_confirm_restock")
                    ) {
                        Text("Simpan", color = DarkEmeraldBg, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun AddEditProductDialog(
    initialProduct: Product? = null,
    onSave: (code: String, name: String, category: String, price: Double, costPrice: Double, stock: Int, minStock: Int, unit: String, barcode: String) -> Unit,
    onDismiss: () -> Unit
) {
    var code by remember { mutableStateOf(initialProduct?.code ?: "BRG-${(100..999).random()}") }
    var name by remember { mutableStateOf(initialProduct?.name ?: "") }
    var category by remember { mutableStateOf(initialProduct?.category ?: "Sembako") }
    var priceText by remember { mutableStateOf(initialProduct?.price?.toInt()?.toString() ?: "") }
    var costPriceText by remember { mutableStateOf(initialProduct?.costPrice?.toInt()?.toString() ?: "") }
    var stockText by remember { mutableStateOf(initialProduct?.stock?.toString() ?: "10") }
    var minStockText by remember { mutableStateOf(initialProduct?.minStock?.toString() ?: "5") }
    var unit by remember { mutableStateOf(initialProduct?.unit ?: "pcs") }
    var barcode by remember { mutableStateOf(initialProduct?.barcode ?: "") }

    val categories = listOf("Sembako", "Bumbu", "Makanan", "Minuman", "Kebersihan", "Lainnya")

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = DarkEmeraldSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, MintGreenPrimary),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp)
                .testTag("add_product_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = if (initialProduct == null) "Tambah Master Produk" else "Edit Data Produk",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MintGreenLight
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nama Barang (e.g. Beras Ramos 5kg)") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = MintGreenPrimary,
                        unfocusedBorderColor = DarkEmeraldBorder
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("input_product_name")
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = code,
                        onValueChange = { code = it },
                        label = { Text("Kode Barang") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = MintGreenPrimary,
                            unfocusedBorderColor = DarkEmeraldBorder
                        ),
                        modifier = Modifier.weight(1f).testTag("input_product_code")
                    )

                    OutlinedTextField(
                        value = unit,
                        onValueChange = { unit = it },
                        label = { Text("Satuan (pcs/kg)") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = MintGreenPrimary,
                            unfocusedBorderColor = DarkEmeraldBorder
                        ),
                        modifier = Modifier.weight(1f).testTag("input_product_unit")
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text("Kategori:", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(categories) { cat ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (category == cat) MintGreenPrimary else DarkEmeraldCard,
                            border = androidx.compose.foundation.BorderStroke(1.dp, DarkEmeraldBorder),
                            modifier = Modifier.clickable { category = cat }
                        ) {
                            Text(
                                text = cat,
                                color = if (category == cat) DarkEmeraldBg else TextPrimary,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = priceText,
                        onValueChange = { priceText = it },
                        label = { Text("Harga Jual (Rp)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = MintGreenPrimary,
                            unfocusedBorderColor = DarkEmeraldBorder
                        ),
                        modifier = Modifier.weight(1f).testTag("input_product_price")
                    )

                    OutlinedTextField(
                        value = costPriceText,
                        onValueChange = { costPriceText = it },
                        label = { Text("Modal/HPP (Rp)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = MintGreenPrimary,
                            unfocusedBorderColor = DarkEmeraldBorder
                        ),
                        modifier = Modifier.weight(1f).testTag("input_product_cost")
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = stockText,
                        onValueChange = { stockText = it },
                        label = { Text("Stok Awal") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = MintGreenPrimary,
                            unfocusedBorderColor = DarkEmeraldBorder
                        ),
                        modifier = Modifier.weight(1f).testTag("input_product_stock")
                    )

                    OutlinedTextField(
                        value = minStockText,
                        onValueChange = { minStockText = it },
                        label = { Text("Batas Kritis") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = MintGreenPrimary,
                            unfocusedBorderColor = DarkEmeraldBorder
                        ),
                        modifier = Modifier.weight(1f).testTag("input_product_min_stock")
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = barcode,
                    onValueChange = { barcode = it },
                    label = { Text("Kode Barcode (Opsional)") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = MintGreenPrimary,
                        unfocusedBorderColor = DarkEmeraldBorder
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("input_product_barcode")
                )

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                        Text("Batal", color = TextSecondary)
                    }

                    Button(
                        onClick = {
                            val price = priceText.toDoubleOrNull() ?: 0.0
                            val cost = costPriceText.toDoubleOrNull() ?: (price * 0.8)
                            val stock = stockText.toIntOrNull() ?: 0
                            val minStock = minStockText.toIntOrNull() ?: 5
                            if (name.isNotBlank() && price > 0) {
                                onSave(code, name, category, price, cost, stock, minStock, unit, barcode)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MintGreenPrimary),
                        modifier = Modifier.weight(1f).testTag("btn_save_product")
                    ) {
                        Text("Simpan", color = DarkEmeraldBg, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
