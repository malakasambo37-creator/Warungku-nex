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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DebtRecord
import com.example.ui.components.sendDebtWhatsAppReminder
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
fun DebtScreen(
    viewModel: WarungViewModel,
    modifier: Modifier = Modifier
) {
    val debts by viewModel.debts.collectAsState()
    val totalPiutang by viewModel.totalDebtAmount.collectAsState()
    val storeProfile by viewModel.storeProfile.collectAsState()
    val context = LocalContext.current

    var showAddDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredDebts = debts.filter { debt ->
        val matchesQuery = debt.customerName.contains(searchQuery, ignoreCase = true) ||
                debt.phone.contains(searchQuery, ignoreCase = true) ||
                debt.notes.contains(searchQuery, ignoreCase = true)
        matchesQuery
    }
    val unpaidDebts = filteredDebts.filter { !it.isPaid }
    val paidDebts = filteredDebts.filter { it.isPaid }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkEmeraldBg)
            .padding(16.dp)
            .testTag("debt_screen")
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Buku Bon Hutang & Piutang",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                Text(
                    text = "Catat bon pembeli & kirim tagihan WA",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 11.sp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Total Piutang Badge
            Surface(
                color = Color(0xFF321D05),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, AmberWarning),
                modifier = Modifier.testTag("badge_total_piutang")
            ) {
                Text(
                    text = "Total: ${WarungViewModel.formatRupiah(totalPiutang)}",
                    color = AmberWarning,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Search Bar for Debts
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Cari nama pelanggan / catatan bon...", color = TextSecondary, fontSize = 13.sp) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Cari",
                    tint = MintGreenPrimary
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear",
                            tint = TextSecondary
                        )
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
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("search_debt_input")
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Daftar Bon Aktif (${unpaidDebts.size})",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = MintGreenLight
                )
            )

            Surface(
                color = Color(0xFF042F24),
                shape = RoundedCornerShape(6.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MintGreenPrimary),
                modifier = Modifier
                    .clickable { showAddDialog = true }
                    .testTag("btn_catat_bon_baru")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Tambah", tint = MintGreenLight, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Catat Bon Baru",
                        color = MintGreenLight,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Debt items list
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(unpaidDebts) { debt ->
                DebtCard(
                    debt = debt,
                    onSendWhatsApp = {
                        sendDebtWhatsAppReminder(
                            context = context,
                            customerName = debt.customerName,
                            phone = debt.phone.ifEmpty { "081234567890" },
                            amount = debt.amount,
                            dueDate = debt.dueDate,
                            storeName = storeProfile.storeName
                        )
                    },
                    onPelunasan = {
                        viewModel.markDebtPaid(debt)
                        Toast.makeText(context, "Bon ${debt.customerName} berhasil DILUNASI!", Toast.LENGTH_SHORT).show()
                    }
                )
            }

            if (paidDebts.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Riwayat Bon Lunas (${paidDebts.size})",
                        style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                    )
                }
                items(paidDebts) { paidDebt ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = DarkEmeraldSurface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, DarkEmeraldBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = paidDebt.customerName,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = TextSecondary,
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                                Text(
                                    text = "Lunas • ${WarungViewModel.formatRupiah(paidDebt.amount)}",
                                    style = MaterialTheme.typography.bodySmall.copy(color = MintGreenLight)
                                )
                            }
                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "Lunas", tint = MintGreenPrimary)
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        com.example.ui.components.DebtCheckoutDialog(
            totalAmount = 50000.0,
            onConfirm = { name, phone, due, notes ->
                viewModel.addDebtRecord(name, phone, 50000.0, due, notes)
                showAddDialog = false
            },
            onDismiss = { showAddDialog = false }
        )
    }
}

@Composable
fun DebtCard(
    debt: DebtRecord,
    onSendWhatsApp: () -> Unit,
    onPelunasan: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = DarkEmeraldSurface),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, AmberWarning),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("debt_card_${debt.customerName.replace(" ", "_")}")
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
                Column {
                    Text(
                        text = debt.customerName,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            fontSize = 15.sp
                        )
                    )
                    Text(
                        text = "Jatuh Tempo: ${debt.dueDate}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    )
                }

                // Nominal Bon in bold red/amber
                Text(
                    text = WarungViewModel.formatRupiah(debt.amount),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = RoseDanger,
                        fontSize = 18.sp
                    )
                )
            }

            if (debt.notes.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Catatan: ${debt.notes}",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextMuted,
                        fontSize = 10.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons matching prototype: [Kirim WA Note] and [Pelunasan]
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onSendWhatsApp,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF042F24)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MintGreenPrimary),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp)
                        .testTag("btn_kirim_wa_${debt.id}")
                ) {
                    Icon(imageVector = Icons.Default.Send, contentDescription = "WA", tint = MintGreenLight, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Kirim WA Note",
                        color = MintGreenLight,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }

                Button(
                    onClick = onPelunasan,
                    colors = ButtonDefaults.buttonColors(containerColor = MintGreenPrimary),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp)
                        .testTag("btn_pelunasan_${debt.id}")
                ) {
                    Text(
                        text = "Pelunasan",
                        color = DarkEmeraldBg,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
