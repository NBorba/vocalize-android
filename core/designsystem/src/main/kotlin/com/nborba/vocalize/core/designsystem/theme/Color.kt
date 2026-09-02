package com.nborba.vocalize.core.designsystem.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Raw Palette Colors (Internal to designsystem)
internal val PrimaryLight = Color(0xFF6750A4)
internal val OnPrimaryLight = Color(0xFFFFFFFF)
internal val SurfaceLight = Color(0xFFFEF7FF)
internal val OnSurfaceLight = Color(0xFF1D1B20)

internal val PrimaryDark = Color(0xFFD0BCFF)
internal val OnPrimaryDark = Color(0xFF381E72)
internal val SurfaceDark = Color(0xFF141218)
internal val OnSurfaceDark = Color(0xFFE6E0E9)

// Semantic Color Schemes
internal val LightColorScheme =
    lightColorScheme(
        primary = PrimaryLight,
        onPrimary = OnPrimaryLight,
        surface = SurfaceLight,
        onSurface = OnSurfaceLight,
    )

internal val DarkColorScheme =
    darkColorScheme(
        primary = PrimaryDark,
        onPrimary = OnPrimaryDark,
        surface = SurfaceDark,
        onSurface = OnSurfaceDark,
    )
