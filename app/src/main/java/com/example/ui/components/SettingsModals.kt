package com.example.ui.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Print
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.StoreProfile
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.BrandTeal
import com.example.ui.theme.DarkEmeraldBg
import com.example.ui.theme.DarkEmeraldBorder
import com.example.ui.theme.DarkEmeraldCard
import com.example.ui.theme.DarkEmeraldSurface
import com.example.ui.theme.MintGreenLight
import com.example.ui.theme.MintGreenPrimary
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.WarungViewModel

@Composable
fun PrinterTestDialog(
    printerName: String = "RPP02N Thermal",
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var isTesting by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = DarkEmeraldSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, MintGreenPrimary),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp)
                .testTag("printer_test_dialog")
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
                        text = "Tes Cetak Struk Bluetooth",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MintGreenLight
                        )
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Icon(
                    imageVector = Icons.Default.Print,
                    contentDescription = "Printer",
                    tint = MintGreenPrimary,
                    modifier = Modifier.size(56.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = printerName,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                Text(
                    text = "Status: Terhubung (Bluetooth SPP 58mm)",
                    style = MaterialTheme.typography.bodySmall.copy(color = MintGreenLight)
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Simulated receipt sample paper
                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "*** TES PRINTER SUKSES ***",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontSize = 11.sp
                        )
                        Text(
                            text = "WARUNGKU - POS UMKM",
                            fontFamily = FontFamily.Monospace,
                            color = Color.Black,
                            fontSize = 10.sp
                        )
                        Text(
                            text = "Karakter: ABCDEFGHIJKLMNOPQRSTUVWXYZ",
                            fontFamily = FontFamily.Monospace,
                            color = Color.DarkGray,
                            fontSize = 9.sp
                        )
                        Text(
                            text = "Angka: 0123456789 - OK!",
                            fontFamily = FontFamily.Monospace,
                            color = Color.DarkGray,
                            fontSize = 9.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        Toast.makeText(context, "Sinyal tes berhasil dikirim ke $printerName!", Toast.LENGTH_SHORT).show()
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MintGreenPrimary),
                    modifier = Modifier.fillMaxWidth().testTag("btn_execute_print_test")
                ) {
                    Text("Cetak Kertas Tes Sekarang", color = DarkEmeraldBg, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun StaffDialog(
    staffCount: Int,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = DarkEmeraldSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkEmeraldBorder),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp)
                .testTag("staff_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Manajemen Staf / Kasir",
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

                // Staff 1
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkEmeraldCard),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(MintGreenPrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("A", fontWeight = FontWeight.Bold, color = DarkEmeraldBg)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("H. Ahmad (Owner)", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 13.sp)
                                Text("Akses: Penuh (Owner & Keuangan)", color = MintGreenLight, fontSize = 11.sp)
                            }
                        }
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "Aktif", tint = MintGreenPrimary)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Staff 2
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkEmeraldCard),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(BrandTeal),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("B", fontWeight = FontWeight.Bold, color = DarkEmeraldBg)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("Budi Santoso", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 13.sp)
                                Text("Akses: Kasir POS Terbatas", color = TextSecondary, fontSize = 11.sp)
                            }
                        }
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "Aktif", tint = MintGreenPrimary)
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        Toast.makeText(context, "Hak akses kasir diperbarui", Toast.LENGTH_SHORT).show()
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MintGreenPrimary),
                    modifier = Modifier.fillMaxWidth().testTag("btn_save_staff")
                ) {
                    Text("Simpan Hak Akses", color = DarkEmeraldBg, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun PinDialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var pin by remember { mutableStateOf("1234") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = DarkEmeraldSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkEmeraldBorder),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp)
                .testTag("pin_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Keamanan & PIN Kasir",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MintGreenLight
                        )
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "PIN 4-digit digunakan untuk otorisasi pembatalan transaksi, hapus barang, dan buka laporan keuangan.",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = pin,
                    onValueChange = { if (it.length <= 4) pin = it },
                    label = { Text("PIN Transaksi (4 Angka)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    visualTransformation = PasswordVisualTransformation(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = MintGreenPrimary,
                        unfocusedBorderColor = DarkEmeraldBorder
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("input_pin")
                )

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        Toast.makeText(context, "PIN Kasir berhasil diperbarui!", Toast.LENGTH_SHORT).show()
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MintGreenPrimary),
                    modifier = Modifier.fillMaxWidth().testTag("btn_save_pin")
                ) {
                    Text("Simpan PIN", color = DarkEmeraldBg, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun EditProfileDialog(
    profile: StoreProfile,
    onSave: (StoreProfile) -> Unit,
    onDismiss: () -> Unit
) {
    var storeName by remember { mutableStateOf(profile.storeName) }
    var ownerName by remember { mutableStateOf(profile.ownerName) }
    var address by remember { mutableStateOf(profile.address) }
    var phone by remember { mutableStateOf(profile.phone) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = DarkEmeraldSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, MintGreenPrimary),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp)
                .testTag("edit_profile_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Edit Profil Toko & Usaha",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MintGreenLight
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = storeName,
                    onValueChange = { storeName = it },
                    label = { Text("Nama Toko") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = MintGreenPrimary,
                        unfocusedBorderColor = DarkEmeraldBorder
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("input_profile_store_name")
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = ownerName,
                    onValueChange = { ownerName = it },
                    label = { Text("Nama Pemilik") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = MintGreenPrimary,
                        unfocusedBorderColor = DarkEmeraldBorder
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("input_profile_owner_name")
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Alamat Toko (Muncul di Struk)") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = MintGreenPrimary,
                        unfocusedBorderColor = DarkEmeraldBorder
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("input_profile_address")
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Nomor Telepon Toko") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = MintGreenPrimary,
                        unfocusedBorderColor = DarkEmeraldBorder
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("input_profile_phone")
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
                            onSave(
                                profile.copy(
                                    storeName = storeName,
                                    ownerName = ownerName,
                                    address = address,
                                    phone = phone
                                )
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MintGreenPrimary),
                        modifier = Modifier.weight(1f).testTag("btn_save_profile")
                    ) {
                        Text("Simpan", color = DarkEmeraldBg, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun AddExpenseDialog(
    onSave: (title: String, category: String, amount: Double, date: String) -> Unit,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Operasional") }
    var amountText by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("31 Okt 2026") }

    val categories = listOf("Operasional", "Listrik & Wifi", "Sewa", "Gaji", "Transport", "Lainnya")

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = DarkEmeraldSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, MintGreenPrimary),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp)
                .testTag("add_expense_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Catat Pengeluaran Operasional",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MintGreenLight
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Keterangan Biaya (e.g. Bayar Listrik Toko)") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = MintGreenPrimary,
                        unfocusedBorderColor = DarkEmeraldBorder
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("input_expense_title")
                )

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

                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = { Text("Nominal Biaya (Rp)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = MintGreenPrimary,
                        unfocusedBorderColor = DarkEmeraldBorder
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("input_expense_amount")
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("Tanggal") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = MintGreenPrimary,
                        unfocusedBorderColor = DarkEmeraldBorder
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("input_expense_date")
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
                            val amount = amountText.toDoubleOrNull() ?: 0.0
                            if (title.isNotBlank() && amount > 0) {
                                onSave(title, category, amount, date)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MintGreenPrimary),
                        modifier = Modifier.weight(1f).testTag("btn_save_expense")
                    ) {
                        Text("Simpan Log", color = DarkEmeraldBg, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun AddAssetDialog(
    onSave: (name: String, value: Double, condition: String, notes: String) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var valueText by remember { mutableStateOf("") }
    var condition by remember { mutableStateOf("Kondisi Baik") }
    var notes by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = DarkEmeraldSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, MintGreenPrimary),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp)
                .testTag("add_asset_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Tambah Aset & Inventaris Toko",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MintGreenLight
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nama Aset (e.g. Freezer Showcase Minuman)") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = MintGreenPrimary,
                        unfocusedBorderColor = DarkEmeraldBorder
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("input_asset_name")
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = valueText,
                    onValueChange = { valueText = it },
                    label = { Text("Estimasi Nilai Aset (Rp)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = MintGreenPrimary,
                        unfocusedBorderColor = DarkEmeraldBorder
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("input_asset_value")
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = condition,
                    onValueChange = { condition = it },
                    label = { Text("Kondisi (e.g. Kondisi Baik / Perlu Servis)") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = MintGreenPrimary,
                        unfocusedBorderColor = DarkEmeraldBorder
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("input_asset_condition")
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
                            val value = valueText.toDoubleOrNull() ?: 0.0
                            if (name.isNotBlank()) {
                                onSave(name, value, condition, notes)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MintGreenPrimary),
                        modifier = Modifier.weight(1f).testTag("btn_save_asset")
                    ) {
                        Text("Simpan Aset", color = DarkEmeraldBg, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
