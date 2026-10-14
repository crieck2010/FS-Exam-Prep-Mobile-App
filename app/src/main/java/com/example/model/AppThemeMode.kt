package com.example.model

enum class AppThemeMode(val title: String, val subtitle: String) {
    SYSTEM("System default", "Follows device appearance settings"),
    LIGHT("Light mode", "Crisp, bright daylight interface"),
    DARK("Dark mode", "High-contrast dark mode for low glare")
}
