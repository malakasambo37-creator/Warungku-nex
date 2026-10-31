package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.data.model.TransactionRecord
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
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.WarungViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TopHeaderBar(
    title: String = "WARUNGKU",
    subtitle: String = "Kelola toko, stok & keuangan lebih mudah",
    canNavigateBack: Boolean = false,
    onBackClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    Surface(
        color = DarkEmeraldBg,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                if (canNavigateBack) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(DarkEmeraldSurface)
                            .border(1.dp, DarkEmeraldBorder, RoundedCornerShape(10.dp))
                            .clickable { onBackClick() }
                            .testTag("top_bar_back_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            tint = TextPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                }

                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (canNavigateBack) TextPrimary else MintGreenLight,
                            fontSize = 20.sp
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

            if (!canNavigateBack) {
                // Two circular action buttons matching Screenshot 1
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Notification Bell with green badge dot
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(DarkEmeraldSurface)
                            .clickable { onNotificationClick() }
                            .testTag("btn_top_notifications"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifikasi",
                            tint = MintGreenLight,
                            modifier = Modifier.size(20.dp)
                        )
                        // Green indicator dot
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(MintGreenPrimary)
                                .align(Alignment.TopEnd)
                                .offset(x = (-8).dp, y = 8.dp)
                        )
                    }

                    // Profile avatar circle
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(DarkEmeraldSurface)
                            .clickable { onProfileClick() }
                            .testTag("btn_top_profile"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profil",
                            tint = MintGreenLight,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WarungBottomNavBar(
    currentTab: AppTab,
    onTabSelected: (AppTab) -> Unit
) {
    Surface(
        color = if (com.example.ui.theme.ThemeManager.isStandard) Color(0xFFFFFFFF) else Color(0xFF071412),
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .border(width = 0.5.dp, color = DarkEmeraldBorder)
            .testTag("bottom_nav_bar")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            // 1. Beranda
            BottomNavTabItem(
                label = com.example.ui.theme.LocaleManager.tr("Beranda", "Home"),
                icon = Icons.Default.Home,
                isSelected = currentTab == AppTab.BERANDA,
                onClick = { onTabSelected(AppTab.BERANDA) },
                testTag = "nav_item_beranda"
            )

            // 2. Transaksi (Cart)
            BottomNavTabItem(
                label = com.example.ui.theme.LocaleManager.tr("Transaksi", "Cashier"),
                icon = Icons.Default.ShoppingCart,
                isSelected = currentTab == AppTab.TRANSAKSI,
                onClick = { onTabSelected(AppTab.TRANSAKSI) },
                testTag = "nav_item_transaksi"
            )

            // 3. Center QR Button (matching Screenshot 1: rounded square teal button)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable { onTabSelected(AppTab.QR_SCAN) }
                    .testTag("nav_item_qr_scan")
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MintGreenPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.QrCode2,
                        contentDescription = "QR",
                        tint = if (com.example.ui.theme.ThemeManager.isStandard) Color.White else DarkEmeraldBg,
                        modifier = Modifier.size(26.dp)
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "QR",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = if (currentTab == AppTab.QR_SCAN) MintGreenPrimary else TextSecondary,
                        fontSize = 10.sp,
                        fontWeight = if (currentTab == AppTab.QR_SCAN) FontWeight.Bold else FontWeight.Normal
                    )
                )
            }

            // 4. Laporan (BarChart)
            BottomNavTabItem(
                label = com.example.ui.theme.LocaleManager.tr("Laporan", "Reports"),
                icon = Icons.Default.BarChart,
                isSelected = currentTab == AppTab.LAPORAN,
                onClick = { onTabSelected(AppTab.LAPORAN) },
                testTag = "nav_item_laporan"
            )

            // 5. Profil (Person)
            BottomNavTabItem(
                label = com.example.ui.theme.LocaleManager.tr("Profil", "Settings"),
                icon = Icons.Default.Person,
                isSelected = currentTab == AppTab.PROFIL,
                onClick = { onTabSelected(AppTab.PROFIL) },
                testTag = "nav_item_profil"
            )
        }
    }
}

@Composable
fun BottomNavTabItem(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .testTag(testTag)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) MintGreenPrimary else TextSecondary,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = if (isSelected) MintGreenPrimary else TextSecondary,
                fontSize = 10.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        )
    }
}

@Composable
fun ThermalReceiptDialog(
    transaction: TransactionRecord,
    storeName: String = "WARUNGKU",
    storeAddress: String = "Jl. Raya Mawar No. 12, Kebayoran - Jakarta Selatan",
    storePhone: String = "08123456789",
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val dateStr = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(transaction.timestamp))

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFFBFBFB),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(vertical = 16.dp)
                .testTag("thermal_receipt_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = storeName.uppercase(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    ),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = storeAddress,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = FontFamily.Monospace,
                        color = Color(0xFF333333),
                        fontSize = 10.sp
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Text(
                    text = "Telp: $storePhone",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = FontFamily.Monospace,
                        color = Color(0xFF333333),
                        fontSize = 10.sp
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))
                DashedDivider()
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = transaction.trxNumber,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    )
                    Text(
                        text = dateStr,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontFamily = FontFamily.Monospace,
                            color = Color(0xFF444444)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                DashedDivider()
                Spacer(modifier = Modifier.height(8.dp))

                val itemLines = transaction.itemsSummary.split(",")
                for (line in itemLines) {
                    val trimmed = line.trim()
                    if (trimmed.isNotEmpty()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = trimmed,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontFamily = FontFamily.Monospace,
                                    color = Color.Black
                                ),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                DashedDivider()
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "TOTAL BAYAR",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    )
                    Text(
                        text = WarungViewModel.formatRupiah(transaction.totalAmount),
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Metode: ${transaction.paymentType}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontFamily = FontFamily.Monospace,
                            color = Color(0xFF444444)
                        )
                    )
                    if (transaction.paymentType == "TUNAI") {
                        Text(
                            text = "Tunai: ${WarungViewModel.formatRupiah(transaction.cashGiven)}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFF444444)
                            )
                        )
                    }
                }

                if (transaction.paymentType == "TUNAI" && transaction.changeAmount > 0) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Kembalian:",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        )
                        Text(
                            text = WarungViewModel.formatRupiah(transaction.changeAmount),
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                DashedDivider()
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "||| || ||||| | |||| |||| ||| ||||| |||",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 2.sp,
                        color = Color.Black
                    ),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = transaction.trxNumber,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontFamily = FontFamily.Monospace,
                        color = Color(0xFF666666)
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Terima Kasih Atas Kunjungan Anda!\nBarang yang sudah dibeli tidak dapat ditukar.",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontFamily = FontFamily.Monospace,
                        color = Color(0xFF666666),
                        fontSize = 9.sp
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { shareReceiptViaWhatsApp(context, transaction, storeName) },
                        colors = ButtonDefaults.buttonColors(containerColor = MintGreenPrimary),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("btn_share_receipt_wa")
                    ) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = "Bagikan", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Kirim WA", fontSize = 12.sp, color = DarkEmeraldBg, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            Toast.makeText(context, "Mencetak ke printer thermal...", Toast.LENGTH_SHORT).show()
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BrandTeal),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("btn_print_receipt")
                    ) {
                        Icon(imageVector = Icons.Default.Print, contentDescription = "Cetak", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Cetak Struk", fontSize = 12.sp, color = DarkEmeraldBg, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("btn_close_receipt")
                ) {
                    Text("Tutup", color = Color.Black)
                }
            }
        }
    }
}

@Composable
fun DashedDivider() {
    Text(
        text = "------------------------------------------",
        maxLines = 1,
        fontFamily = FontFamily.Monospace,
        color = Color(0xFF888888),
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}

fun shareReceiptViaWhatsApp(context: Context, trx: TransactionRecord, storeName: String) {
    val dateStr = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(trx.timestamp))
    val msg = """
        *STRUK BELANJA - $storeName*
        Nomor: ${trx.trxNumber}
        Waktu: $dateStr
        Pelanggan: ${trx.customerName}
        ------------------------------
        Rincian:
        ${trx.itemsSummary.replace(", ", "\n")}
        ------------------------------
        *TOTAL: ${WarungViewModel.formatRupiah(trx.totalAmount)}*
        Pembayaran: ${trx.paymentType}
        ${if (trx.changeAmount > 0) "Kembalian: " + WarungViewModel.formatRupiah(trx.changeAmount) else ""}
        
        Terima kasih telah berbelanja di $storeName!
    """.trimIndent()

    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse("https://api.whatsapp.com/send?text=" + Uri.encode(msg))
    }
    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Membuka WhatsApp...", Toast.LENGTH_SHORT).show()
    }
}

fun sendDebtWhatsAppReminder(context: Context, customerName: String, phone: String, amount: Double, dueDate: String, storeName: String) {
    val amountStr = WarungViewModel.formatRupiah(amount)
    val msg = """
        Halo Yth. $customerName,
        Salam dari $storeName 🙏
        
        Kami ingin menginfokan catatan bon belanja Anda:
        Nominal: *$amountStr*
        Jatuh Tempo: *$dueDate*
        
        Pembayaran dapat melalui Kasir Toko atau transfer via QRIS kami.
        Terima kasih banyak atas kerjasamanya! 😊
    """.trimIndent()

    val cleanPhone = if (phone.startsWith("0")) "62" + phone.substring(1) else phone
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse("https://api.whatsapp.com/send?phone=$cleanPhone&text=" + Uri.encode(msg))
    }
    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Membuka WhatsApp ke $customerName...", Toast.LENGTH_SHORT).show()
    }
}
