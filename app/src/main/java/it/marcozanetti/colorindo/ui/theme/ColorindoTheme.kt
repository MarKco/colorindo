package it.marcozanetti.colorindo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF6200EE), // purple_500
    onPrimary = Color(0xFFFFFFFF), // white
    primaryContainer = Color(0xFFBB86FC), // purple_200
    secondary = Color(0xFF03DAC5), // teal_200
    onSecondary = Color(0xFF000000), // black
    background = Color(0xFFFFFFFF), // white
    onBackground = Color(0xFF000000), // black
    surface = Color(0xFFFFFFFF), // white
    onSurface = Color(0xFF000000), // black
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFBB86FC), // purple_200
    onPrimary = Color(0xFF000000), // black
    primaryContainer = Color(0xFF3700B3), // purple_700
    secondary = Color(0xFF03DAC5), // teal_200
    onSecondary = Color(0xFF000000), // black
    background = Color(0xFF000000), // black
    onBackground = Color(0xFFFFFFFF), // white
    surface = Color(0xFF121212),
    onSurface = Color(0xFFFFFFFF), // white
)

@Composable
fun ColorindoTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (useDarkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        typography = androidx.compose.material3.Typography(),
        content = content
    )
} 