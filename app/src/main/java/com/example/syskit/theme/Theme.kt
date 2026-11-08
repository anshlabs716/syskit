package com.example.syskit.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily

// ---- Semantic accents (used directly by feature screens) ----
val AccentCyan = Color(0xFF53D8EE) // primary accent — calm cyan
val NeonGreen = Color(0xFF5BD98A)  // success / positive
val BrightCyan = AccentCyan        // legacy name kept for existing call sites
val ElectricBlue = Color(0xFF7C9EFF)
val WarningAmber = Color(0xFFF5C15C)
val DangerRed = Color(0xFFF5716B)

// ---- Clean dark surfaces ----
val BackgroundDark = Color(0xFF0B0F14)
val SurfaceDark = Color(0xFF131922)
val SurfaceVariantDark = Color(0xFF1A2230)
val OnSurfaceDark = Color(0xFFE6EBF2)
val OnSurfaceMutedDark = Color(0xFF93A0B4)
val BorderDark = Color(0xFF222C3A)

private val DarkColorScheme = darkColorScheme(
    primary = AccentCyan,
    onPrimary = Color(0xFF002830),
    primaryContainer = Color(0xFF0E3A42),
    onPrimaryContainer = Color(0xFFB8ECF5),
    secondary = NeonGreen,
    onSecondary = Color(0xFF0A2E1A),
    secondaryContainer = Color(0xFF16402A),
    onSecondaryContainer = Color(0xFFBDF0CC),
    tertiary = ElectricBlue,
    background = BackgroundDark,
    surface = SurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onBackground = OnSurfaceDark,
    onSurface = OnSurfaceDark,
    onSurfaceVariant = OnSurfaceMutedDark,
    outline = BorderDark,
    outlineVariant = BorderDark,
    error = DangerRed
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF0E7C8F),
    secondary = Color(0xFF2E7D4F),
    tertiary = Color(0xFF4A66C4),
    error = Color(0xFFC04841)
)

/** Default (sans-serif) typography — monospace is reserved for commands and IDs. */
private val AppTypography = Typography()

@Composable
fun SysKitTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}

/** Where a terminal aesthetic is genuinely correct (package ids, shell commands). */
val Mono: FontFamily = FontFamily.Monospace
