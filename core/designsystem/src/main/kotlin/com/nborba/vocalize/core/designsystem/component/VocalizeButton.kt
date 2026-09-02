package com.nborba.vocalize.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.nborba.vocalize.core.designsystem.icon.VocalizeIcons
import com.nborba.vocalize.core.designsystem.preview.ThemePreviews
import com.nborba.vocalize.core.designsystem.theme.VocalizeTheme
import com.nborba.vocalize.core.designsystem.theme.spacing

/**
 * Default button.
 */
@Composable
fun VocalizeButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: (@Composable () -> Unit)? = null,
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = MaterialTheme.shapes.medium,
        contentPadding =
            PaddingValues(
                horizontal = MaterialTheme.spacing.medium,
                vertical = MaterialTheme.spacing.small,
            ),
    ) {
        if (leadingIcon != null) {
            leadingIcon()
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
        }
        content()
    }
}

/**
 * Default button - Convenience overload.
 */
@Composable
fun VocalizeButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
) {
    VocalizeButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
    ) {
        if (leadingIcon != null) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null, // Decorative icon inside button
            )
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
        }
        Text(text = text)
    }
}

/**
 * Secondary Outlined Vocalize Button.
 */
@Composable
fun VocalizeOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: (@Composable () -> Unit)? = null,
    content: @Composable RowScope.() -> Unit,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = MaterialTheme.shapes.medium,
        contentPadding =
            PaddingValues(
                horizontal = MaterialTheme.spacing.medium,
                vertical = MaterialTheme.spacing.small,
            ),
    ) {
        if (leadingIcon != null) {
            leadingIcon()
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
        }
        content()
    }
}

/**
 * Secondary Outlined Vocalize Button - Convenience overload.
 */
@Composable
fun VocalizeOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
) {
    VocalizeOutlinedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
    ) {
        if (leadingIcon != null) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null, // Decorative icon inside button
            )
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
        }
        Text(text = text)
    }
}

/**
 * Ghost/Text Vocalize Button.
 */
@Composable
fun VocalizeTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = MaterialTheme.shapes.medium,
        content = content,
    )
}

/**
 * Ghost/Text Vocalize Button - Convenience Overload.
 */
@Composable
fun VocalizeTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    VocalizeTextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
    ) {
        Text(text = text)
    }
}

@ThemePreviews
@Composable
private fun VocalizeButtonsPreview() {
    VocalizeTheme {
        Surface {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                // Primary Filled Buttons
                VocalizeButton(
                    text = "Primary Button",
                    onClick = {},
                )
                VocalizeButton(
                    text = "Primary with Icon",
                    leadingIcon = VocalizeIcons.Check,
                    onClick = {},
                )
                VocalizeButton(
                    text = "Primary Disabled",
                    enabled = false,
                    onClick = {},
                )

                // Secondary Outlined Buttons
                VocalizeOutlinedButton(
                    text = "Outlined Button",
                    onClick = {},
                )
                VocalizeOutlinedButton(
                    text = "Outlined with Icon",
                    leadingIcon = VocalizeIcons.Settings,
                    onClick = {},
                )
                VocalizeOutlinedButton(
                    text = "Outlined Disabled",
                    enabled = false,
                    onClick = {},
                )

                // Text / Ghost Buttons
                VocalizeTextButton(
                    text = "Text Button",
                    onClick = {},
                )
                VocalizeTextButton(
                    text = "Text Disabled",
                    enabled = false,
                    onClick = {},
                )
            }
        }
    }
}
