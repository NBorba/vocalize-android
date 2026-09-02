package com.nborba.vocalize.core.designsystem.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Vocalize Shape tokens.
 * Defines the corner radius scale for components across the app.
 */
val VocalizeShapes =
    Shapes(
        // Text inputs, tooltips, badges
        extraSmall = RoundedCornerShape(4.dp),
        // Chips, small buttons, snackbars
        small = RoundedCornerShape(8.dp),
        // Cards, popups, dropdown menus
        medium = RoundedCornerShape(12.dp),
        // Dialogs, Floating Action Buttons (FABs)
        large = RoundedCornerShape(16.dp),
        // Bottom sheets, full-screen cards, modals
        extraLarge = RoundedCornerShape(28.dp),
    )
