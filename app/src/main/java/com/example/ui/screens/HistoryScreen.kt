package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TransactionRecord
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.DarkEmeraldBg
import com.example.ui.theme.DarkEmeraldBorder
import com.example.ui.theme.DarkEmeraldSurface
import com.example.ui.theme.LocaleManager
import com.example.ui.theme.MintGreenLight
import com.example.ui.theme.MintGreenPrimary
import com.example.ui.theme.RoseDanger
import com.example.ui.theme.RoseDangerSoft
import com.example.ui.theme.SkyInfo
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.WarungViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(
    viewModel: WarungViewModel,
    modifier: Modifier = Modifier
) {
    val transactions by viewModel.transactions.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    var trxToDelete by remember { mutableStateOf<TransactionRecord?>(null) }
    var showClearAllDialog by remember { mutableStateOf(false) }

    val filteredTransactions = remember(transactions, searchQuery) {
        if (searchQuery.isBlank()) transactions
        else transactions.filter {
            it.trxNumber.contains(searchQuery, ignoreCase = true) ||
            it.customerName.contains(searchQuery, ignoreCase = true) ||
            it.paymentType.contains(searchQuery, ignoreCase = true) ||
            it.itemsSummary.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkEmeraldBg)
            .padding(16.dp)
            .testTag("history_screen")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = LocaleManager.tr("Riwayat Transaksi (Log Kasir)", "Transaction History (Cashier Log)"),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                Text(
                    text = LocaleManager.tr("Daftar seluruh transaksi yang telah dicatat", "All recorded store sales transactions"),
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 11.sp)
                )
            }

            if (transactions.isNotEmpty()) {
                OutlinedButton(
                    onClick = { showClearAllDialog = true },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = RoseDanger),
                    border = androidx.compose.foundation.BorderStroke(1.dp, RoseDanger.copy(alpha = 0.6f)),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("btn_clear_all_history")
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteSweep,
                        contentDescription = "Hapus Semua",
                        tint = RoseDanger,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = LocaleManager.tr("Hapus Semua", "Clear All"),
                        color = RoseDanger,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Search Bar for History
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = {
                Text(
                    text = LocaleManager.tr("Cari nomor nota, pelanggan, atau pembayaran...", "Search receipt #, customer, payment..."),
                    color = TextSecondary,
                    fontSize = 13.sp
                )
            },
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
                            contentDescription = "Hapus",
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
                .testTag("search_history_input")
        )

        Spacer(modifier = Modifier.height(10.dp))

        if (filteredTransactions.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (searchQuery.isNotEmpty()) {
                        LocaleManager.tr("Tidak ada transaksi cocok dengan \"$searchQuery\".", "No transactions matching \"$searchQuery\".")
                    } else {
                        LocaleManager.tr("Belum ada riwayat transaksi.", "No transaction history yet.")
                    },
                    color = TextMuted,
                    fontSize = 13.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredTransactions, key = { it.id }) { trx ->
                    val dateStr = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date(trx.timestamp))
                    Card(
                        colors = CardDefaults.cardColors(containerColor = DarkEmeraldSurface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, DarkEmeraldBorder),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.selectedTransactionForReceipt.value = trx
                                viewModel.showReceiptDialog.value = true
                            }
                            .testTag("history_item_${trx.trxNumber}")
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
                                        text = trx.trxNumber,
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            color = MintGreenLight,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Monospace
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    // Payment badge
                                    val (badgeBg, badgeColor) = when (trx.paymentType) {
                                        "TUNAI" -> Pair(Color(0xFF042F24), MintGreenLight)
                                        "QRIS" -> Pair(Color(0xFF0C4A6E), SkyInfo)
                                        else -> Pair(Color(0xFF451A03), AmberWarning)
                                    }
                                    Surface(
                                        color = badgeBg,
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = trx.paymentType,
                                            color = badgeColor,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Text(
                                    text = WarungViewModel.formatRupiah(trx.totalAmount),
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "${LocaleManager.tr("Pelanggan", "Customer")}: ${trx.customerName} • $dateStr",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 11.sp)
                            )

                            if (trx.itemsSummary.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = trx.itemsSummary,
                                    style = MaterialTheme.typography.labelSmall.copy(color = TextMuted, fontSize = 10.sp),
                                    maxLines = 1
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Bottom actions row: Delete option on the left, Receipt on the right
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Opsi Hapus Transaksi (per item)
                                Row(
                                    modifier = Modifier
                                        .clickable { trxToDelete = trx }
                                        .padding(vertical = 4.dp, horizontal = 4.dp)
                                        .testTag("btn_delete_trx_${trx.trxNumber}"),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DeleteOutline,
                                        contentDescription = "Hapus Transaksi",
                                        tint = RoseDanger,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = LocaleManager.tr("Hapus", "Delete"),
                                        color = RoseDanger,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }

                                Row(
                                    modifier = Modifier
                                        .clickable {
                                            viewModel.selectedTransactionForReceipt.value = trx
                                            viewModel.showReceiptDialog.value = true
                                        }
                                        .padding(vertical = 4.dp, horizontal = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Receipt,
                                        contentDescription = "Lihat Struk",
                                        tint = MintGreenPrimary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = LocaleManager.tr("Lihat & Cetak Struk", "View & Print Receipt"),
                                        color = MintGreenLight,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal Konfirmasi Hapus Transaksi Satuan
    if (trxToDelete != null) {
        val target = trxToDelete!!
        AlertDialog(
            onDismissRequest = { trxToDelete = null },
            title = {
                Text(
                    text = LocaleManager.tr("Hapus Transaksi?", "Delete Transaction?"),
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            },
            text = {
                Text(
                    text = LocaleManager.tr(
                        "Apakah Anda yakin ingin menghapus transaksi ${target.trxNumber} (${WarungViewModel.formatRupiah(target.totalAmount)})? Data ini akan dihapus dari riwayat dan pencatatan laba.",
                        "Are you sure you want to delete transaction ${target.trxNumber} (${WarungViewModel.formatRupiah(target.totalAmount)})? This will be removed from history and profit calculations."
                    ),
                    color = TextSecondary,
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteTransaction(target)
                        trxToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RoseDanger),
                    modifier = Modifier.testTag("btn_confirm_delete_trx")
                ) {
                    Text(text = LocaleManager.tr("Hapus", "Delete"), color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { trxToDelete = null },
                    modifier = Modifier.testTag("btn_cancel_delete_trx")
                ) {
                    Text(text = LocaleManager.tr("Batal", "Cancel"), color = TextSecondary)
                }
            },
            containerColor = DarkEmeraldSurface,
            shape = RoundedCornerShape(16.dp)
        )
    }

    // Modal Konfirmasi Hapus Semua Transaksi
    if (showClearAllDialog) {
        AlertDialog(
            onDismissRequest = { showClearAllDialog = false },
            title = {
                Text(
                    text = LocaleManager.tr("Hapus Semua Riwayat?", "Delete All History?"),
                    fontWeight = FontWeight.Bold,
                    color = RoseDanger
                )
            },
            text = {
                Text(
                    text = LocaleManager.tr(
                        "Semua riwayat transaksi penjualan akan dihapus permanen. Total omset penjualan dan laba hari ini akan disesuaikan kembali.",
                        "All sales transaction history will be permanently deleted. Total sales revenue and today's profit will be adjusted accordingly."
                    ),
                    color = TextSecondary,
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.clearAllTransactions()
                        showClearAllDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RoseDanger),
                    modifier = Modifier.testTag("btn_confirm_clear_all_trx")
                ) {
                    Text(text = LocaleManager.tr("Hapus Semua", "Delete All"), color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showClearAllDialog = false },
                    modifier = Modifier.testTag("btn_cancel_clear_all_trx")
                ) {
                    Text(text = LocaleManager.tr("Batal", "Cancel"), color = TextSecondary)
                }
            },
            containerColor = DarkEmeraldSurface,
            shape = RoundedCornerShape(16.dp)
        )
    }
}
