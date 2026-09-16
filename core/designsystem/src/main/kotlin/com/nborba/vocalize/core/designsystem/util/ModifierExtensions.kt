package com.nborba.vocalize.core.designsystem.util

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nborba.vocalize.core.designsystem.theme.spacing

@Composable
fun Modifier.defaultPadding() = this.padding(MaterialTheme.spacing.medium)

@Composable
fun Modifier.defaultVerticalPadding() = this.padding(vertical = MaterialTheme.spacing.medium)

@Composable
fun Modifier.defaultHorizontalPadding() = this.padding(horizontal = MaterialTheme.spacing.medium)
