package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.TrendingUp
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.BrandTeal
import com.example.ui.theme.DarkEmeraldBg
import com.example.ui.theme.DarkEmeraldBorder
import com.example.ui.theme.DarkEmeraldCard
import com.example.ui.theme.DarkEmeraldSurface
import com.example.ui.theme.EmeraldMuted
import com.example.ui.theme.MintGreenLight
import com.example.ui.theme.MintGreenPrimary
import com.example.ui.theme.RoseDanger
import com.example.ui.theme.SkyInfo
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.SubScreen
import com.example.ui.viewmodel.WarungViewModel

@Composable
fun ReportScreen(
    viewModel: WarungViewModel,
    modifier: Modifier = Modifier
) {
    val reportPeriod by viewModel.reportPeriod.collectAsState()
    val transactions by viewModel.transactions.collectAsState()
    val expenses by viewModel.expenses.collectAsState()
    val storeProfile by viewModel.storeProfile.collectAsState()
    val context = LocalContext.current

    val totalOmset = transactions.sumOf { it.totalAmount }
    val totalExpense = expenses.sumOf { it.amount } + transactions.sumOf { it.totalCost }
    val netProfit = totalOmset - totalExpense

    val periods = listOf("Hari Ini", "Minggu Ini", "Bulan Ini", "Tahun Ini")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkEmeraldBg)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("report_screen")
    ) {
        // Feature Header matching Page 3
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Fitur 4: Laporan Penjualan & Analisis Laba Rugi",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                Text(
                    text = "Sistem Laporan Keuangan, Otomatisasi Promosi, dan Supplier",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 11.sp)
                )
            }

            // Export PDF button
            Surface(
                color = MintGreenPrimary,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .clickable {
                        exportFinancialReport(context, storeProfile.storeName, totalOmset, totalExpense, netProfit)
                    }
                    .testTag("btn_export_pdf")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Download, contentDescription = "Export", tint = DarkEmeraldBg, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Export PDF",
                        color = DarkEmeraldBg,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Period filter pills
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(periods) { p ->
                val isSelected = reportPeriod == p
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isSelected) MintGreenPrimary else DarkEmeraldSurface,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isSelected) MintGreenPrimary else DarkEmeraldBorder
                    ),
                    modifier = Modifier
                        .clickable { viewModel.setReportPeriod(p) }
                        .testTag("period_chip_$p")
                ) {
                    Text(
                        text = if (p == "Bulan Ini") "Filter: Bulan Ini" else p,
                        color = if (isSelected) DarkEmeraldBg else TextPrimary,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Chart Card: Tren Omset vs Laba (Oktober 2026)
        Card(
            colors = CardDefaults.cardColors(containerColor = DarkEmeraldSurface),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkEmeraldBorder),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("chart_trend_omset_laba")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Tren Omset vs Laba (Oktober 2026)",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(MintGreenPrimary))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Omset", color = MintGreenLight, fontSize = 10.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(SkyInfo))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Laba", color = SkyInfo, fontSize = 10.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Canvas Trend Chart
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val w = size.width
                        val h = size.height

                        // Grid lines
                        val gridCount = 4
                        for (i in 0..gridCount) {
                            val y = h * (i.toFloat() / gridCount)
                            drawLine(
                                color = Color(0x2210B981),
                                start = Offset(0f, y),
                                end = Offset(w, y),
                                strokeWidth = 1f
                            )
                        }

                        // Omset smooth curve points (Minggu 1 -> 4)
                        val omsetPoints = listOf(
                            Offset(w * 0.1f, h * 0.75f),
                            Offset(w * 0.38f, h * 0.65f),
                            Offset(w * 0.68f, h * 0.25f),
                            Offset(w * 0.92f, h * 0.20f)
                        )

                        // Laba smooth curve points
                        val labaPoints = listOf(
                            Offset(w * 0.1f, h * 0.88f),
                            Offset(w * 0.38f, h * 0.82f),
                            Offset(w * 0.68f, h * 0.50f),
                            Offset(w * 0.92f, h * 0.45f)
                        )

                        // Draw Omset Path
                        val omsetPath = Path().apply {
                            moveTo(omsetPoints[0].x, omsetPoints[0].y)
                            for (k in 1 until omsetPoints.size) {
                                val prev = omsetPoints[k - 1]
                                val cur = omsetPoints[k]
                                cubicTo(
                                    (prev.x + cur.x) / 2, prev.y,
                                    (prev.x + cur.x) / 2, cur.y,
                                    cur.x, cur.y
                                )
                            }
                        }
                        drawPath(
                            path = omsetPath,
                            color = Color(0xFF10B981),
                            style = Stroke(width = 4f)
                        )

                        // Draw Laba Path (dashed sky blue)
                        val labaPath = Path().apply {
                            moveTo(labaPoints[0].x, labaPoints[0].y)
                            for (k in 1 until labaPoints.size) {
                                val prev = labaPoints[k - 1]
                                val cur = labaPoints[k]
                                cubicTo(
                                    (prev.x + cur.x) / 2, prev.y,
                                    (prev.x + cur.x) / 2, cur.y,
                                    cur.x, cur.y
                                )
                            }
                        }
                        drawPath(
                            path = labaPath,
                            color = Color(0xFF38BDF8),
                            style = Stroke(
                                width = 3f,
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                            )
                        )

                        // Draw Point dots
                        for (pt in omsetPoints) {
                            drawCircle(color = Color(0xFF10B981), radius = 5f, center = pt)
                            drawCircle(color = Color.White, radius = 2.5f, center = pt)
                        }
                        for (pt in labaPoints) {
                            drawCircle(color = Color(0xFF38BDF8), radius = 4f, center = pt)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // X-Axis labels matching prototype
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf("Minggu 1", "Minggu 2", "Minggu 3", "Minggu 4").forEach { week ->
                        Text(
                            text = week,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TextSecondary,
                                fontSize = 10.sp
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 3 Summary Cards matching Page 3: Total Omset, Total Pengeluaran, Laba Bersih
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SummarySmallCard(
                title = "Total Omset",
                value = WarungViewModel.formatRupiah(totalOmset),
                color = MintGreenLight,
                modifier = Modifier.weight(1f).testTag("summary_total_omset")
            )
            SummarySmallCard(
                title = "Total Pengeluaran",
                value = WarungViewModel.formatRupiah(totalExpense),
                color = RoseDanger,
                modifier = Modifier.weight(1f).testTag("summary_total_pengeluaran")
            )
            SummarySmallCard(
                title = "Laba Bersih",
                value = WarungViewModel.formatRupiah(netProfit),
                color = SkyInfo,
                modifier = Modifier.weight(1f).testTag("summary_laba_bersih")
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Navigation Shortcuts to Complementary Modules from Prototype Page 3:
        Text(
            text = "MODUL KEUANGAN & PARTNER TOKO",
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = MintGreenLight,
                letterSpacing = 1.sp
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Fitur 5 & 6 Quick Navigation
        Card(
            colors = CardDefaults.cardColors(containerColor = DarkEmeraldSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkEmeraldBorder),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { viewModel.navigateToSubScreen(SubScreen.PROMO_SUPPLIER) }
                .testTag("nav_promo_supplier")
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF042F24)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.LocalOffer, contentDescription = "Promo", tint = MintGreenLight, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("Fitur 5 & 6: Diskon Promo & Data Supplier", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 13.sp)
                        Text("Kelola promo toko & kontak suplier sembako", color = TextSecondary, fontSize = 11.sp)
                    }
                }
                Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Buka", tint = MintGreenPrimary)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Fitur 7 & 8 Quick Navigation
        Card(
            colors = CardDefaults.cardColors(containerColor = DarkEmeraldSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkEmeraldBorder),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { viewModel.navigateToSubScreen(SubScreen.EXPENSES_ASSETS) }
                .testTag("nav_expenses_assets")
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF450A0A)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.ReceiptLong, contentDescription = "Aset", tint = RoseDanger, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("Fitur 7 & 8: Catat Pengeluaran & Aset Toko", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 13.sp)
                        Text("Biaya operasional & inventaris perlengkapan warung", color = TextSecondary, fontSize = 11.sp)
                    }
                }
                Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Buka", tint = MintGreenPrimary)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun SummarySmallCard(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = DarkEmeraldSurface),
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkEmeraldBorder),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TextSecondary,
                    fontSize = 10.sp
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall.copy(
                    color = color,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                ),
                maxLines = 1
            )
        }
    }
}

fun exportFinancialReport(context: Context, storeName: String, omset: Double, pengeluaran: Double, laba: Double) {
    val reportText = """
        *LAPORAN KEUANGAN & LABA RUGI - $storeName*
        Periode: Oktober 2026
        ------------------------------------------
        Total Omset: ${WarungViewModel.formatRupiah(omset)}
        Total Pengeluaran: ${WarungViewModel.formatRupiah(pengeluaran)}
        *LABA BERSIH: ${WarungViewModel.formatRupiah(laba)}*
        ------------------------------------------
        Generated via WARUNGKU POS System v2.4
    """.trimIndent()

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "Laporan Keuangan $storeName")
        putExtra(Intent.EXTRA_TEXT, reportText)
    }
    try {
        context.startActivity(Intent.createChooser(intent, "Bagikan Laporan Keuangan"))
    } catch (e: Exception) {
        Toast.makeText(context, "Laporan siap dibagikan", Toast.LENGTH_SHORT).show()
    }
}
