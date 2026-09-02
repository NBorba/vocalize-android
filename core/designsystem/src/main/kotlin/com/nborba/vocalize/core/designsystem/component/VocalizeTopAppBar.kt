@file:OptIn(ExperimentalMaterial3Api::class)

package com.nborba.vocalize.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.nborba.vocalize.core.designsystem.icon.VocalizeIcons
import com.nborba.vocalize.core.designsystem.preview.ThemePreviews
import com.nborba.vocalize.core.designsystem.theme.VocalizeTheme

/**
 * Standard Vocalize Top App Bar (left-aligned title).
 */
@Composable
fun VocalizeTopAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    colors: TopAppBarColors =
        TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
        ),
) {
    TopAppBar(
        title = title,
        modifier = modifier,
        navigationIcon = navigationIcon,
        actions = actions,
        colors = colors,
    )
}

/**
 * Standard Vocalize Top App Bar - Convenience overload with String title and optional back navigation.
 */
@Composable
fun VocalizeTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: ImageVector = VocalizeIcons.ArrowBack,
    navigationIconContentDescription: String? = "Back",
    onNavigationClick: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    VocalizeTopAppBar(
        title = { Text(text = title) },
        modifier = modifier,
        navigationIcon = {
            if (onNavigationClick != null) {
                IconButton(onClick = onNavigationClick) {
                    Icon(
                        imageVector = navigationIcon,
                        contentDescription = navigationIconContentDescription,
                    )
                }
            }
        },
        actions = actions,
    )
}

/**
 * Center-aligned Vocalize Top App Bar (useful for detail or main feature screens).
 */
@Composable
fun VocalizeCenterAlignedTopAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    colors: TopAppBarColors =
        TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
        ),
) {
    CenterAlignedTopAppBar(
        title = title,
        modifier = modifier,
        navigationIcon = navigationIcon,
        actions = actions,
        colors = colors,
    )
}

/**
 * Standard Vocalize Top App Bar - Convenience overload with String title and optional back navigation.
 */
@Composable
fun VocalizeCenterAlignedTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: ImageVector = VocalizeIcons.ArrowBack,
    navigationIconContentDescription: String? = "Back",
    onNavigationClick: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    VocalizeCenterAlignedTopAppBar(
        title = { Text(text = title) },
        modifier = modifier,
        navigationIcon = {
            if (onNavigationClick != null) {
                IconButton(onClick = onNavigationClick) {
                    Icon(
                        imageVector = navigationIcon,
                        contentDescription = navigationIconContentDescription,
                    )
                }
            }
        },
        actions = actions,
    )
}

@ThemePreviews
@Composable
private fun VocalizeTopAppBarPreview() {
    VocalizeTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            // Standard Title Only
            VocalizeTopAppBar(
                title = "Vocalize",
            )

            // With Back Navigation & Action Icon
            VocalizeTopAppBar(
                title = "Home Screen",
                onNavigationClick = {},
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = VocalizeIcons.Settings,
                            contentDescription = "Settings",
                        )
                    }
                },
            )

            // Modal Style with Close Icon
            VocalizeTopAppBar(
                title = "Settings Modal",
                navigationIcon = VocalizeIcons.Close,
                navigationIconContentDescription = "Close",
                onNavigationClick = {},
            )
        }
    }
}

@ThemePreviews
@Composable
private fun VocalizeCenterAlignedTopAppBarPreview() {
    VocalizeTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            VocalizeCenterAlignedTopAppBar(
                title = "Detail Screen",
                onNavigationClick = {},
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = VocalizeIcons.Settings,
                            contentDescription = "Settings",
                        )
                    }
                },
            )
        }
    }
}
