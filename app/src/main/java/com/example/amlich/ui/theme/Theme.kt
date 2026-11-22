package com.example.amlich.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = VermilionRed,
    onPrimary = PapyrusSurfaceCard,
    primaryContainer = VermilionRedContainer,
    onPrimaryContainer = VermilionRed,
    secondary = VermilionRed,
    onSecondary = PapyrusSurfaceCard,
    secondaryContainer = VermilionRedContainer,
    onSecondaryContainer = VermilionRed,
    background = PapyrusBackground,
    onBackground = TextCharcoalPrimary,
    surface = PapyrusSurfaceCard,
    onSurface = TextCharcoalPrimary,
    surfaceVariant = PapyrusContainer,
    onSurfaceVariant = TextCharcoalSecondary,
    outline = PapyrusBorder
)

private val DarkColorScheme = darkColorScheme(
    primary = AccentGold,
    onPrimary = DarkPapyrusSurfaceCard,
    primaryContainer = AccentGoldContainer,
    onPrimaryContainer = AccentGold,
    secondary = AccentRed,
    onSecondary = DarkPapyrusSurfaceCard,
    secondaryContainer = AccentRedContainer,
    onSecondaryContainer = AccentRed,
    background = DarkPapyrusBackground,
    onBackground = DarkTextPrimary,
    surface = DarkPapyrusSurfaceCard,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkPapyrusContainer,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkPapyrusBorder
)

@Composable
fun AmLichTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                // Đặt màu thanh trạng thái (Status bar) đồng bộ màu nền ứng dụng
                window.statusBarColor = colorScheme.background.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
