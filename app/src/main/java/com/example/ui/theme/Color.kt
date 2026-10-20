package com.example.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

// ============================================================================
// Calm Pro Design System — Tokens (Brand Guide v0.2.5 §2.1)
// ============================================================================

// Light Mode Tokens
val CalmBgLight = Color(0xFFFAFAF8)       // warm paper
val CalmSurfaceLight = Color(0xFFFFFFFF)  // pure surface
val CalmInkLight = Color(0xFF1C1B1A)      // primary text
val CalmSteelLight = Color(0xFF6E6A63)    // secondary text, captions
val CalmDividerLight = Color(0xFFE7E2D9)  // hairline dividers (1dp)
val CalmAccent = Color(0xFF0F766E)        // deep teal — primary actions in both modes
val CalmGoLight = Color(0xFF2F7D5B)       // correct / mastery positive
val CalmWarnLight = Color(0xFFB0483E)     // incorrect / attention (muted)

// Dark Mode Tokens
val CalmBgDark = Color(0xFF17181A)        // warm charcoal
val CalmSurfaceDark = Color(0xFF1F2023)   // dark surface
val CalmInkDark = Color(0xFFEDEAE4)       // primary text
val CalmSteelDark = Color(0xFF9B968C)     // secondary text, captions
val CalmDividerDark = Color(0xFF2C2D30)   // hairline dividers (1dp)
val CalmAccentBright = Color(0xFF5EC8B8)  // dark-mode ONLY: accent-colored text, progress rings/bars, active tab
val CalmGoDark = Color(0xFF6FBF92)        // correct (muted)
val CalmWarnDark = Color(0xFFE08A7E)      // incorrect (muted)

// Muted container / tint helpers (derived from official tokens)
val CalmGoContainerLight = Color(0xFFEAF3EE)
val CalmGoContainerDark = Color(0xFF1B2E24)
val CalmWarnContainerLight = Color(0xFFF9EAE8)
val CalmWarnContainerDark = Color(0xFF331D1B)
val CalmAccentContainerLight = Color(0xFFE6F2F0)
val CalmAccentContainerDark = Color(0xFF152926)

@Immutable
data class CalmPalette(
    val bg: Color,
    val surface: Color,
    val ink: Color,
    val steel: Color,
    val divider: Color,
    val accent: Color,
    val accentBright: Color,
    val go: Color,
    val warn: Color,
    val goContainer: Color,
    val warnContainer: Color,
    val accentContainer: Color,
    val isDark: Boolean
)

val LightCalmPalette = CalmPalette(
    bg = CalmBgLight,
    surface = CalmSurfaceLight,
    ink = CalmInkLight,
    steel = CalmSteelLight,
    divider = CalmDividerLight,
    accent = CalmAccent,
    accentBright = CalmAccent,
    go = CalmGoLight,
    warn = CalmWarnLight,
    goContainer = CalmGoContainerLight,
    warnContainer = CalmWarnContainerLight,
    accentContainer = CalmAccentContainerLight,
    isDark = false
)

val DarkCalmPalette = CalmPalette(
    bg = CalmBgDark,
    surface = CalmSurfaceDark,
    ink = CalmInkDark,
    steel = CalmSteelDark,
    divider = CalmDividerDark,
    accent = CalmAccent,
    accentBright = CalmAccentBright,
    go = CalmGoDark,
    warn = CalmWarnDark,
    goContainer = CalmGoContainerDark,
    warnContainer = CalmWarnContainerDark,
    accentContainer = CalmAccentContainerDark,
    isDark = true
)
