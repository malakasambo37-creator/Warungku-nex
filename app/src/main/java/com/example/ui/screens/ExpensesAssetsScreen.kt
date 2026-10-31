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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.ui.components.AddAssetDialog
import com.example.ui.components.AddExpenseDialog
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.BrandTeal
import com.example.ui.theme.DarkEmeraldBg
import com.example.ui.theme.DarkEmeraldBorder
import com.example.ui.theme.DarkEmeraldCard
import com.example.ui.theme.DarkEmeraldSurface
import com.example.ui.theme.MintGreenLight
import com.example.ui.theme.MintGreenPrimary
import com.example.ui.theme.RoseDanger
import com.example.ui.theme.SkyInfo
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.WarungViewModel

@Composable
fun ExpensesAssetsScreen(
    viewModel: WarungViewModel,
    modifier: Modifier = Modifier
) {
    val expenses by viewModel.expenses.collectAsState()
    val assets by viewModel.assets.collectAsState()
    val context = LocalContext.current

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Pengeluaran Operasional", "Inventaris / Aset Toko")

    var showAddExpense by remember { mutableStateOf(false) }
    var showAddAsset by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredExpenses = remember(expenses, searchQuery) {
        if (searchQuery.isBlank()) expenses
        else expenses.filter {
            it.title.contains(searchQuery, ignoreCase = true) ||
            it.category.contains(searchQuery, ignoreCase = true) ||
            it.dateText.contains(searchQuery, ignoreCase = true)
        }
    }

    val filteredAssets = remember(assets, searchQuery) {
        if (searchQuery.isBlank()) assets
        else assets.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
            it.condition.contains(searchQuery, ignoreCase = true) ||
            it.notes.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkEmeraldBg)
            .padding(16.dp)
            .testTag("expenses_assets_screen")
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Fitur 7 & 8: Catat Pengeluaran & Aset Toko",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                Text(
                    text = "Pencatatan kas keluar harian & nilai barang inventori",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 11.sp)
                )
            }

            Surface(
                color = Color(0xFF042F24),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MintGreenPrimary),
                modifier = Modifier
                    .clickable {
                        if (selectedTabIndex == 0) showAddExpense = true else showAddAsset = true
                    }
                    .testTag("btn_tambah_log")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Tambah", tint = MintGreenLight, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "+ Tambah Log",
                        color = MintGreenLight,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Tabs
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = DarkEmeraldSurface,
            contentColor = MintGreenPrimary,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    color = MintGreenPrimary
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            color = if (selectedTabIndex == index) MintGreenPrimary else TextSecondary,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.sp
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Search Bar for Expenses / Assets
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = {
                Text(
                    text = if (selectedTabIndex == 0) "Cari pengeluaran operasional..." else "Cari inventaris / aset toko...",
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
                .testTag("search_expense_input")
        )

        Spacer(modifier = Modifier.height(10.dp))

        if (selectedTabIndex == 0) {
            // Tab 1: Pengeluaran Operasional
            val totalExpense = filteredExpenses.sumOf { it.amount }
            Text(
                text = "Total Pengeluaran: ${WarungViewModel.formatRupiah(totalExpense)}",
                style = MaterialTheme.typography.labelSmall.copy(color = RoseDanger, fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredExpenses) { exp ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = DarkEmeraldSurface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, DarkEmeraldBorder),
                        modifier = Modifier.fillMaxWidth().testTag("expense_card_${exp.id}")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "[Pengeluaran Operasional]",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = RoseDanger,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = exp.title,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = "Nominal: ${WarungViewModel.formatRupiah(exp.amount)} (Tgl ${exp.dateText})",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextSecondary,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                            IconButton(
                                onClick = {
                                    viewModel.deleteExpense(exp)
                                    Toast.makeText(context, "Log pengeluaran dihapus", Toast.LENGTH_SHORT).show()
                                }
                            ) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = "Hapus", tint = TextMuted)
                            }
                        }
                    }
                }
            }
        } else {
            // Tab 2: Inventaris / Aset Toko
            val totalAssetValue = filteredAssets.sumOf { it.value }
            Text(
                text = "Total Estimasi Aset: ${WarungViewModel.formatRupiah(totalAssetValue)}",
                style = MaterialTheme.typography.labelSmall.copy(color = SkyInfo, fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredAssets) { ast ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = DarkEmeraldSurface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, DarkEmeraldBorder),
                        modifier = Modifier.fillMaxWidth().testTag("asset_card_${ast.id}")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "[Inventaris / Aset Toko]",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = SkyInfo,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = ast.name,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = "Nilai Aset: ${WarungViewModel.formatRupiah(ast.value)} (${ast.condition})",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextSecondary,
                                        fontSize = 11.sp
                                    )
                                )
                                if (ast.notes.isNotEmpty()) {
                                    Text(
                                        text = ast.notes,
                                        style = MaterialTheme.typography.labelSmall.copy(color = TextMuted, fontSize = 10.sp)
                                    )
                                }
                            }
                            IconButton(
                                onClick = {
                                    viewModel.deleteAsset(ast)
                                    Toast.makeText(context, "Aset dihapus", Toast.LENGTH_SHORT).show()
                                }
                            ) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = "Hapus", tint = TextMuted)
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddExpense) {
        AddExpenseDialog(
            onSave = { title, cat, amt, date ->
                viewModel.addExpense(title, cat, amt, date)
                showAddExpense = false
            },
            onDismiss = { showAddExpense = false }
        )
    }

    if (showAddAsset) {
        AddAssetDialog(
            onSave = { name, valAmt, cond, notes ->
                viewModel.addStoreAsset(name, valAmt, cond, notes)
                showAddAsset = false
            },
            onDismiss = { showAddAsset = false }
        )
    }
}
