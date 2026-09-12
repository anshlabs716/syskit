package com.example.syskit.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Hacker / Terminal / SysKit Deep Dark Palette
val NeonGreen = Color(0xFF00E676)
val BrightCyan = Color(0xFF00E5FF)
val ElectricBlue = Color(0xFF2979FF)
val WarningAmber = Color(0xFFFFD600)
val DangerRed = Color(0xFFFF5252)

val BackgroundDark = Color(0xFF0B0F19)
val SurfaceDark = Color(0xFF131B2E)
val SurfaceVariantDark = Color(0xFF1B253D)
val OnSurfaceDark = Color(0xFFE3E8F4)
val OnSurfaceMutedDark = Color(0xFF90A0BE)
val BorderDark = Color(0xFF233252)

private val DarkColorScheme = darkColorScheme(
    primary = NeonGreen,
    onPrimary = Color(0xFF003816),
    primaryContainer = Color(0xFF005324),
    onPrimaryContainer = Color(0xFF7BFF9F),
    secondary = BrightCyan,
    onSecondary = Color(0xFF00363D),
    secondaryContainer = Color(0xFF004F59),
    onSecondaryContainer = Color(0xFF92F4FF),
    tertiary = ElectricBlue,
    background = BackgroundDark,
    surface = SurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onBackground = OnSurfaceDark,
    onSurface = OnSurfaceDark,
    onSurfaceVariant = OnSurfaceMutedDark,
    outline = BorderDark,
    error = DangerRed
)

@Composable
fun SysKitTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = BackgroundDark.toArgb()
            window.navigationBarColor = BackgroundDark.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
