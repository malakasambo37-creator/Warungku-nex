package com.example.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

enum class AppThemeMode(val titleId: String, val titleEn: String) {
    DARK("Tema Gelap", "Dark Theme"),
    STANDARD("Tema Standar / Biasa", "Standard / Light Theme")
}

enum class AppLanguage(val label: String, val code: String) {
    INDONESIAN("Bahasa Indonesia", "id"),
    ENGLISH("English", "en")
}

object ThemeManager {
    var currentTheme by mutableStateOf(AppThemeMode.DARK)
    val isStandard: Boolean get() = currentTheme == AppThemeMode.STANDARD
    val isDark: Boolean get() = currentTheme == AppThemeMode.DARK
}

object LocaleManager {
    var language by mutableStateOf(AppLanguage.INDONESIAN)
    val isEnglish: Boolean get() = language == AppLanguage.ENGLISH

    fun tr(id: String, en: String): String = if (isEnglish) en else id
}

// Dynamic Adaptive Colors based on ThemeManager
val DarkEmeraldBg: Color
    get() = if (ThemeManager.isStandard) Color(0xFFF1F5F9) else Color(0xFF0B1E1C)

val DarkEmeraldSurface: Color
    get() = if (ThemeManager.isStandard) Color(0xFFFFFFFF) else Color(0xFF112A27)

val DarkEmeraldCard: Color
    get() = if (ThemeManager.isStandard) Color(0xFFFFFFFF) else Color(0xFF163A36)

val DarkEmeraldCardHover: Color
    get() = if (ThemeManager.isStandard) Color(0xFFF8FAFC) else Color(0xFF1C4641)

val DarkEmeraldBorder: Color
    get() = if (ThemeManager.isStandard) Color(0xFFE2E8F0) else Color(0xFF20544E)

val DarkEmeraldBorderLight: Color
    get() = if (ThemeManager.isStandard) Color(0xFFCBD5E1) else Color(0xFF2B6B63)

val TextPrimary: Color
    get() = if (ThemeManager.isStandard) Color(0xFF0F172A) else Color(0xFFF8FAFC)

val TextSecondary: Color
    get() = if (ThemeManager.isStandard) Color(0xFF475569) else Color(0xFF94A3B8)

val TextMuted: Color
    get() = if (ThemeManager.isStandard) Color(0xFF94A3B8) else Color(0xFF64748B)

val MintGreenPrimary: Color
    get() = if (ThemeManager.isStandard) Color(0xFF059669) else Color(0xFF10B981)

val BrandTeal: Color
    get() = if (ThemeManager.isStandard) Color(0xFF0D9488) else Color(0xFF1FB496)

val MintGreenLight: Color
    get() = if (ThemeManager.isStandard) Color(0xFF059669) else Color(0xFF34D399)

val MintGreenSoft = Color(0xFFD1FAE5)
val EmeraldMuted = Color(0xFF6EE7B7)

val AmberWarning = Color(0xFFF59E0B)
val AmberWarningSoft = Color(0xFFFEF3C7)
val RoseDanger = Color(0xFFEF4444)
val RoseDangerSoft = Color(0xFFFEE2E2)
val SkyInfo = Color(0xFF38BDF8)
val PurpleAccent = Color(0xFFA855F7)
val GoldYellow = Color(0xFFFBBF24)
