package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalCalmPalette = staticCompositionLocalOf { LightCalmPalette }

val MaterialTheme.calm: CalmPalette
    @Composable
    @ReadOnlyComposable
    get() = LocalCalmPalette.current

private val LightColorScheme = lightColorScheme(
    primary = CalmAccent,                  // #0F766E deep teal
    onPrimary = Color.White,
    primaryContainer = CalmAccentContainerLight,
    onPrimaryContainer = CalmAccent,
    secondary = CalmSteelLight,            // #6E6A63
    onSecondary = Color.White,
    secondaryContainer = CalmDividerLight, // #E7E2D9
    onSecondaryContainer = CalmInkLight,   // #1C1B1A
    tertiary = CalmGoLight,                // #2F7D5B
    onTertiary = Color.White,
    background = CalmBgLight,              // #FAFAF8
    onBackground = CalmInkLight,           // #1C1B1A
    surface = CalmSurfaceLight,            // #FFFFFF
    onSurface = CalmInkLight,              // #1C1B1A
    surfaceVariant = CalmBgLight,          // #FAFAF8
    onSurfaceVariant = CalmSteelLight,     // #6E6A63
    outline = CalmDividerLight,            // #E7E2D9
    outlineVariant = CalmDividerLight,
    error = CalmWarnLight,                 // #B0483E
    onError = Color.White,
    errorContainer = CalmWarnContainerLight,
    onErrorContainer = CalmWarnLight
)

private val DarkColorScheme = darkColorScheme(
    primary = CalmAccent,                  // #0F766E (same teal — do NOT brighten buttons)
    onPrimary = Color.White,
    primaryContainer = CalmAccentContainerDark,
    onPrimaryContainer = CalmAccentBright, // #5EC8B8
    secondary = CalmAccentBright,          // #5EC8B8
    onSecondary = CalmBgDark,
    secondaryContainer = CalmDividerDark,  // #2C2D30
    onSecondaryContainer = CalmInkDark,    // #EDEAE4
    tertiary = CalmGoDark,                 // #6FBF92
    onTertiary = CalmBgDark,
    background = CalmBgDark,               // #17181A
    onBackground = CalmInkDark,            // #EDEAE4
    surface = CalmSurfaceDark,             // #1F2023
    onSurface = CalmInkDark,               // #EDEAE4
    surfaceVariant = CalmSurfaceDark,      // #1F2023
    onSurfaceVariant = CalmSteelDark,      // #9B968C
    outline = CalmDividerDark,             // #2C2D30
    outlineVariant = CalmDividerDark,
    error = CalmWarnDark,                  // #E08A7E
    onError = CalmBgDark,
    errorContainer = CalmWarnContainerDark,
    onErrorContainer = CalmWarnDark
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // Default to SYSTEM mode
    dynamicColor: Boolean = false, // Keep dynamicColor OFF per Brand Guide
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val calmPalette = if (darkTheme) DarkCalmPalette else LightCalmPalette

    CompositionLocalProvider(LocalCalmPalette provides calmPalette) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
