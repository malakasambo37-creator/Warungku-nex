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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
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
import com.example.data.model.PromoRecord
import com.example.data.model.SupplierRecord
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
fun PromoSupplierScreen(
    viewModel: WarungViewModel,
    modifier: Modifier = Modifier
) {
    val promos by viewModel.promos.collectAsState()
    val suppliers by viewModel.suppliers.collectAsState()
    val context = LocalContext.current

    var searchQuery by remember { mutableStateOf("") }

    val filteredPromos = remember(promos, searchQuery) {
        if (searchQuery.isBlank()) promos
        else promos.filter { it.title.contains(searchQuery, ignoreCase = true) }
    }

    val filteredSuppliers = remember(suppliers, searchQuery) {
        if (searchQuery.isBlank()) suppliers
        else suppliers.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
            it.category.contains(searchQuery, ignoreCase = true) ||
            it.phone.contains(searchQuery, ignoreCase = true) ||
            it.address.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkEmeraldBg)
            .padding(16.dp)
            .testTag("promo_supplier_screen")
    ) {
        // Search Bar for Promo & Supplier
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = {
                Text(
                    text = "Cari promo atau nama kontak supplier...",
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
                .testTag("search_promo_supplier_input")
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Section: Fitur 5: Diskon & Promo
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Fitur 5: Diskon & Promo",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                Text(
                    text = "Otomatisasi diskon transaksi kasir",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 11.sp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Surface(
                color = Color(0xFF042F24),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MintGreenPrimary),
                modifier = Modifier
                    .clickable {
                        viewModel.addPromo(
                            title = "Promo Spesial UMKM",
                            discountPercent = 10.0,
                            minSpend = 50000.0,
                            validUntil = "10 Nov 2026"
                        )
                        Toast.makeText(context, "Promo baru ditambahkan!", Toast.LENGTH_SHORT).show()
                    }
                    .testTag("btn_buat_promo")
            ) {
                Text(
                    text = "+ Buat Promo",
                    color = MintGreenLight,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Promo list
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            filteredPromos.forEach { promo ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkEmeraldSurface),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (promo.isActive) MintGreenPrimary else DarkEmeraldBorder),
                    modifier = Modifier.fillMaxWidth().testTag("promo_card_${promo.id}")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = promo.title,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (promo.isActive) AmberWarning else TextSecondary
                                )
                            )
                            val desc = if (promo.discountPercent > 0) {
                                "Diskon ${promo.discountPercent.toInt()}% Min. Belanja ${WarungViewModel.formatRupiah(promo.minSpend)}"
                            } else {
                                "Potongan ${WarungViewModel.formatRupiah(promo.discountAmount)} Min. Belanja ${WarungViewModel.formatRupiah(promo.minSpend)}"
                            }
                            Text(text = desc, style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary, fontSize = 11.sp))
                            Text(
                                text = "Status: ${if (promo.isActive) "Aktif sampai " + promo.validUntil else "Non-Aktif"}",
                                style = MaterialTheme.typography.labelSmall.copy(color = if (promo.isActive) MintGreenLight else TextMuted, fontSize = 10.sp)
                            )
                        }

                        Switch(
                            checked = promo.isActive,
                            onCheckedChange = { viewModel.togglePromo(promo) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = DarkEmeraldBg,
                                checkedTrackColor = MintGreenPrimary
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Section: Fitur 6: Data Supplier
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Fitur 6: Data Supplier",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                Text(
                    text = "Daftar kontak pemasok & grosir barang",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 11.sp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Surface(
                color = Color(0xFF042F24),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MintGreenPrimary),
                modifier = Modifier
                    .clickable {
                        viewModel.addSupplier(
                            name = "Grosir Plastik & Kemasan Makmur",
                            category = "Plastik & Wadah",
                            phone = "081233445566",
                            address = "Pasar Pagi Blok D-5"
                        )
                        Toast.makeText(context, "Supplier baru berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
                    }
                    .testTag("btn_tambah_kontak_supplier")
            ) {
                Text(
                    text = "+ Kontak",
                    color = MintGreenLight,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredSuppliers) { sup ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkEmeraldSurface),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkEmeraldBorder),
                    modifier = Modifier.fillMaxWidth().testTag("supplier_card_${sup.id}")
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
                                text = sup.name,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            )
                            Text(
                                text = sup.category,
                                style = MaterialTheme.typography.bodySmall.copy(color = MintGreenLight, fontSize = 11.sp)
                            )
                            Text(
                                text = "WA: ${sup.phone} • ${sup.address}",
                                style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontSize = 10.sp)
                            )
                        }

                        Button(
                            onClick = {
                                val cleanPhone = if (sup.phone.startsWith("0")) "62" + sup.phone.substring(1) else sup.phone
                                val url = "https://api.whatsapp.com/send?phone=$cleanPhone"
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                try {
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Chat WA ke ${sup.name}...", Toast.LENGTH_SHORT).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF042F24)),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MintGreenPrimary),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Chat WA", color = MintGreenLight, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
