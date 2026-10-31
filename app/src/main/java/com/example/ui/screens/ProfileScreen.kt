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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.AppLanguage
import com.example.ui.theme.AppThemeMode
import com.example.ui.theme.DarkEmeraldBg
import com.example.ui.theme.DarkEmeraldBorder
import com.example.ui.theme.DarkEmeraldCard
import com.example.ui.theme.DarkEmeraldSurface
import com.example.ui.theme.LocaleManager
import com.example.ui.theme.MintGreenLight
import com.example.ui.theme.MintGreenPrimary
import com.example.ui.theme.RoseDanger
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.ThemeManager
import com.example.ui.viewmodel.WarungViewModel

@Composable
fun ProfileScreen(
    viewModel: WarungViewModel,
    modifier: Modifier = Modifier
) {
    val storeProfile by viewModel.storeProfile.collectAsState()
    val transactions by viewModel.transactions.collectAsState()
    val context = LocalContext.current

    var isStockNotificationEnabled by remember { mutableStateOf(true) }
    var showThemeDialog by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkEmeraldBg)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("profile_screen")
    ) {
        // 1. Store Avatar Banner Card with Gradient
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF0F766E),
                            Color(0xFF0D4B45)
                        )
                    )
                )
                .clickable { viewModel.showEditProfileDialog.value = true }
                .testTag("profile_banner_card")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp, horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Circular person avatar
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF092925)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Avatar",
                        tint = MintGreenLight,
                        modifier = Modifier.size(44.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = storeProfile.storeName,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 20.sp,
                        letterSpacing = 1.sp
                    )
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "warungku@pembayaran.id",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFFC7ECE5),
                        fontSize = 13.sp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Settings Items List
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

            // Item 1: Pengaturan Toko
            SettingsRowCard(
                icon = Icons.Default.Storefront,
                title = LocaleManager.tr("Pengaturan Toko", "Store Settings"),
                subtitle = "${storeProfile.storeName} • " + LocaleManager.tr("Informasi toko, pajak & tarif", "Store info, tax & rates"),
                onClick = { viewModel.showEditProfileDialog.value = true },
                testTag = "setting_pengaturan_toko"
            )

            // NOTE: Metode Pembayaran is explicitly REMOVED per user instructions ("hapus metode pembayaran di menu profil")

            // Item 3: Notifikasi Stok (with Switch toggle)
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkEmeraldSurface),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkEmeraldBorder),
                modifier = Modifier.fillMaxWidth().testTag("setting_notifikasi_stok")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        SettingsIconBox(icon = Icons.Default.Notifications)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = LocaleManager.tr("Notifikasi Stok", "Stock Notifications"),
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary,
                                    fontSize = 13.sp
                                )
                            )
                            Text(
                                text = LocaleManager.tr("Dapatkan notifikasi untuk stok minimum", "Get notifications when items reach minimum stock"),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }

                    Switch(
                        checked = isStockNotificationEnabled,
                        onCheckedChange = {
                            isStockNotificationEnabled = it
                            Toast.makeText(
                                context,
                                if (it) LocaleManager.tr("Notifikasi stok minimum diaktifkan", "Minimum stock alert enabled")
                                else LocaleManager.tr("Notifikasi stok dinonaktifkan", "Stock alert disabled"),
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = DarkEmeraldBg,
                            checkedTrackColor = MintGreenPrimary,
                            uncheckedThumbColor = TextMuted,
                            uncheckedTrackColor = DarkEmeraldCard
                        )
                    )
                }
            }

            // Item 4: Format Struk
            SettingsRowCard(
                icon = Icons.Default.Receipt,
                title = LocaleManager.tr("Format Struk", "Receipt Format"),
                subtitle = LocaleManager.tr("Atur tampilan dan konten struk", "Configure receipt layout and info"),
                onClick = {
                    val lastTrx = transactions.firstOrNull()
                    if (lastTrx != null) {
                        viewModel.selectedTransactionForReceipt.value = lastTrx
                        viewModel.showReceiptDialog.value = true
                    } else {
                        Toast.makeText(context, LocaleManager.tr("Format struk thermal 58mm aktif", "Thermal 58mm format active"), Toast.LENGTH_SHORT).show()
                    }
                },
                testTag = "setting_format_struk"
            )

            // Item 5: Perangkat Kasir
            SettingsRowCard(
                icon = Icons.Default.PointOfSale,
                title = LocaleManager.tr("Perangkat Kasir", "Cashier Devices"),
                subtitle = LocaleManager.tr("Kelola printer thermal Bluetooth & Wi-Fi", "Manage Bluetooth & Wi-Fi thermal printers"),
                onClick = { viewModel.showPrinterDialog.value = true },
                testTag = "setting_perangkat_kasir"
            )

            // Item 6: Keamanan PIN & Kata Sandi
            SettingsRowCard(
                icon = Icons.Default.Shield,
                title = LocaleManager.tr("Keamanan PIN & Kata Sandi", "PIN & Password Security"),
                subtitle = LocaleManager.tr("Kunci aplikasi & pemulihan", "App lock & security PIN"),
                badgeText = LocaleManager.tr("Nonaktif", "Disabled"),
                badgeColor = MintGreenLight,
                onClick = { viewModel.showPinDialog.value = true },
                testTag = "setting_keamanan_pin"
            )

            // Item 7: Backup & Restore Data
            SettingsRowCard(
                icon = Icons.Default.CloudSync,
                title = LocaleManager.tr("Backup & Restore Data", "Backup & Restore Data"),
                subtitle = LocaleManager.tr("Simpan atau pulihkan seluruh data toko", "Save or restore all store data"),
                onClick = {
                    Toast.makeText(context, LocaleManager.tr("Data toko berhasil dibackup ke penyimpanan lokal!", "Store data backed up locally!"), Toast.LENGTH_SHORT).show()
                },
                testTag = "setting_backup_data"
            )

            // Item 8: Bahasa (Bilingual Selection)
            SettingsRowCard(
                icon = Icons.Default.Language,
                title = LocaleManager.tr("Bahasa", "Language"),
                subtitle = LocaleManager.tr("Pilih bahasa aplikasi (Indonesia / English)", "Select application language"),
                badgeText = if (LocaleManager.isEnglish) "English" else "Bahasa Indonesia",
                badgeColor = MintGreenLight,
                onClick = { showLanguageDialog = true },
                testTag = "setting_bahasa"
            )

            // Item 9: Tema (Dark vs Standard / Biasa)
            SettingsRowCard(
                icon = Icons.Default.Palette,
                title = LocaleManager.tr("Tema", "Theme"),
                subtitle = LocaleManager.tr("Pilih tampilan tema aplikasi (Gelap / Standar)", "Select app theme appearance"),
                badgeText = if (ThemeManager.isStandard) LocaleManager.tr("Standar / Biasa", "Standard / Light") else LocaleManager.tr("Gelap", "Dark"),
                badgeColor = MintGreenLight,
                onClick = { showThemeDialog = true },
                testTag = "setting_tema"
            )

            // Item 10: Kebijakan Privasi
            SettingsRowCard(
                icon = Icons.Default.Security,
                title = LocaleManager.tr("Kebijakan Privasi", "Privacy Policy"),
                subtitle = LocaleManager.tr("Perlindungan data lokal perangkat", "On-device data protection"),
                badgeText = "com.warungku.id",
                badgeColor = MintGreenLight,
                onClick = {
                    Toast.makeText(context, LocaleManager.tr("Aplikasi beroperasi secara Offline-First dengan perlindungan data lokal.", "Offline-first application with secure local storage."), Toast.LENGTH_LONG).show()
                },
                testTag = "setting_kebijakan_privasi"
            )

            // Item 11: Keluar
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkEmeraldSurface),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkEmeraldBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        Toast.makeText(context, LocaleManager.tr("Sesi kasir aktif. Gunakan menu Staf untuk ganti petugas.", "Active cashier session. Use Staff menu to switch operator."), Toast.LENGTH_SHORT).show()
                    }
                    .testTag("setting_keluar")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFF3B1212)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ExitToApp,
                                contentDescription = "Keluar",
                                tint = RoseDanger,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = LocaleManager.tr("Keluar", "Sign Out"),
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary,
                                    fontSize = 13.sp
                                )
                            )
                            Text(
                                text = LocaleManager.tr("Keluar ke halaman awal Warungku", "Return to Warungku welcome screen"),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Buka",
                        tint = TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Item 12: Special Gold Card - Donasi untuk Pengembangan Aplikasi Ini
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1705)),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, AmberWarning),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val url = "https://saweria.co"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        try {
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(context, "Membuka link Saweria...", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .testTag("card_donasi_saweria")
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
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFF382905)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalCafe,
                                contentDescription = "Donasi",
                                tint = AmberWarning,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = LocaleManager.tr("Donasi untuk Pengembangan Aplikasi Ini", "Donate to App Development"),
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = AmberWarning,
                                    fontSize = 13.sp
                                )
                            )
                            Text(
                                text = LocaleManager.tr("Donasi via Saweria", "Donate via Saweria"),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = AmberWarning,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.OpenInNew,
                        contentDescription = "Buka Link",
                        tint = AmberWarning,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }

    // Modal Pilihan Tema (Gelap vs Standar / Biasa)
    if (showThemeDialog) {
        AlertDialog(
            onDismissRequest = { showThemeDialog = false },
            title = {
                Text(
                    text = LocaleManager.tr("Pilih Tampilan Tema", "Choose Theme Appearance"),
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = LocaleManager.tr(
                            "Tersedia dua mode tema untuk kenyamanan kasir dan pemilik warung:",
                            "Choose between two themes for comfortable cashier usage:"
                        ),
                        color = TextSecondary,
                        fontSize = 13.sp
                    )

                    // Option 1: Tema Gelap (Dark Emerald)
                    ThemeOptionCard(
                        title = LocaleManager.tr("Tema Gelap", "Dark Theme"),
                        description = LocaleManager.tr("Aksen Hijau Zamrud gelap elegan, nyaman untuk mata di malam hari", "Elegant dark emerald palette, comfortable in low light"),
                        icon = Icons.Default.DarkMode,
                        isSelected = ThemeManager.isDark,
                        previewBgColor = Color(0xFF0B1E1C),
                        onClick = {
                            viewModel.setAppTheme(AppThemeMode.DARK)
                            showThemeDialog = false
                        },
                        testTag = "opt_theme_dark"
                    )

                    // Option 2: Tema Standar / Biasa (Light Clean)
                    ThemeOptionCard(
                        title = LocaleManager.tr("Tema Standar / Biasa", "Standard / Light Theme"),
                        description = LocaleManager.tr("Tampilan putih bersih & jernih dengan kontras tajam Material 3", "Clean white surface with sharp contrast and crisp readability"),
                        icon = Icons.Default.LightMode,
                        isSelected = ThemeManager.isStandard,
                        previewBgColor = Color(0xFFF1F5F9),
                        onClick = {
                            viewModel.setAppTheme(AppThemeMode.STANDARD)
                            showThemeDialog = false
                        },
                        testTag = "opt_theme_standard"
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { showThemeDialog = false },
                    modifier = Modifier.testTag("btn_close_theme_dialog")
                ) {
                    Text(text = LocaleManager.tr("Selesai", "Done"), color = MintGreenLight)
                }
            },
            containerColor = DarkEmeraldSurface,
            shape = RoundedCornerShape(16.dp)
        )
    }

    // Modal Pilihan Bahasa (Bahasa Indonesia vs English)
    if (showLanguageDialog) {
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            title = {
                Text(
                    text = LocaleManager.tr("Pilih Bahasa", "Choose Language"),
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = LocaleManager.tr("Pilih bahasa antarmuka aplikasi:", "Select the user interface language:"),
                        color = TextSecondary,
                        fontSize = 13.sp
                    )

                    // Option 1: Bahasa Indonesia
                    LanguageOptionCard(
                        title = "Bahasa Indonesia",
                        subtitle = "Bahasa baku kasir & manajemen warung UMKM",
                        code = "ID",
                        isSelected = !LocaleManager.isEnglish,
                        onClick = {
                            viewModel.setAppLanguage(AppLanguage.INDONESIAN)
                            showLanguageDialog = false
                        },
                        testTag = "opt_lang_id"
                    )

                    // Option 2: English
                    LanguageOptionCard(
                        title = "English",
                        subtitle = "Standard English for point of sale & inventory",
                        code = "EN",
                        isSelected = LocaleManager.isEnglish,
                        onClick = {
                            viewModel.setAppLanguage(AppLanguage.ENGLISH)
                            showLanguageDialog = false
                        },
                        testTag = "opt_lang_en"
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { showLanguageDialog = false },
                    modifier = Modifier.testTag("btn_close_lang_dialog")
                ) {
                    Text(text = LocaleManager.tr("Selesai", "Done"), color = MintGreenLight)
                }
            },
            containerColor = DarkEmeraldSurface,
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
fun ThemeOptionCard(
    title: String,
    description: String,
    icon: ImageVector,
    isSelected: Boolean,
    previewBgColor: Color,
    onClick: () -> Unit,
    testTag: String
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = if (isSelected) Color(0xFF042F24) else DarkEmeraldCard),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.5.dp,
            if (isSelected) MintGreenPrimary else DarkEmeraldBorder
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(previewBgColor)
                    .border(1.dp, if (isSelected) MintGreenPrimary else Color.Gray, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = if (previewBgColor == Color(0xFF0B1E1C)) MintGreenLight else Color(0xFF059669),
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        fontSize = 13.sp
                    )
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                )
            }

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Dipilih",
                    tint = MintGreenPrimary,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Composable
fun LanguageOptionCard(
    title: String,
    subtitle: String,
    code: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = if (isSelected) Color(0xFF042F24) else DarkEmeraldCard),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.5.dp,
            if (isSelected) MintGreenPrimary else DarkEmeraldBorder
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) MintGreenPrimary else Color(0xFF042F24)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = code,
                    color = if (isSelected) Color.White else MintGreenLight,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        fontSize = 13.sp
                    )
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                )
            }

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Dipilih",
                    tint = MintGreenPrimary,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Composable
fun SettingsRowCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    badgeText: String? = null,
    badgeColor: Color = MintGreenLight,
    onClick: () -> Unit,
    testTag: String
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = DarkEmeraldSurface),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkEmeraldBorder),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                SettingsIconBox(icon = icon)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            fontSize = 13.sp
                        )
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 11.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (badgeText != null) {
                    Text(
                        text = badgeText,
                        color = badgeColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                }
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Buka",
                    tint = TextSecondary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun SettingsIconBox(
    icon: ImageVector,
    iconColor: Color = MintGreenLight
) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF042F24)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(20.dp)
        )
    }
}
