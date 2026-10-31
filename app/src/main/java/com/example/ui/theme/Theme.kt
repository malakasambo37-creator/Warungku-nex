package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkEmeraldColorScheme = darkColorScheme(
    primary = Color(0xFF10B981),
    onPrimary = Color(0xFF042F24),
    primaryContainer = Color(0xFF064E3B),
    onPrimaryContainer = MintGreenSoft,
    secondary = Color(0xFF1FB496),
    onSecondary = Color(0xFF022E22),
    secondaryContainer = Color(0xFF134E48),
    onSecondaryContainer = EmeraldMuted,
    tertiary = AmberWarning,
    onTertiary = Color(0xFF451A03),
    tertiaryContainer = Color(0xFF78350F),
    onTertiaryContainer = AmberWarningSoft,
    background = Color(0xFF0B1E1C),
    onBackground = Color(0xFFF8FAFC),
    surface = Color(0xFF112A27),
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = Color(0xFF163A36),
    onSurfaceVariant = Color(0xFF94A3B8),
    outline = Color(0xFF20544E),
    outlineVariant = Color(0xFF2B6B63),
    error = RoseDanger,
    onError = Color.White,
    errorContainer = Color(0xFF7F1D1D),
    onErrorContainer = RoseDangerSoft
)

private val StandardLightColorScheme = lightColorScheme(
    primary = Color(0xFF059669),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD1FAE5),
    onPrimaryContainer = Color(0xFF065F46),
    secondary = Color(0xFF0D9488),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFCCFBF1),
    onSecondaryContainer = Color(0xFF115E59),
    tertiary = AmberWarning,
    onTertiary = Color.White,
    tertiaryContainer = AmberWarningSoft,
    onTertiaryContainer = Color(0xFF78350F),
    background = Color(0xFFF1F5F9),
    onBackground = Color(0xFF0F172A),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFF8FAFC),
    onSurfaceVariant = Color(0xFF475569),
    outline = Color(0xFFE2E8F0),
    outlineVariant = Color(0xFFCBD5E1),
    error = RoseDanger,
    onError = Color.White,
    errorContainer = RoseDangerSoft,
    onErrorContainer = Color(0xFF7F1D1D)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = ThemeManager.isDark,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (ThemeManager.isStandard) StandardLightColorScheme else DarkEmeraldColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
