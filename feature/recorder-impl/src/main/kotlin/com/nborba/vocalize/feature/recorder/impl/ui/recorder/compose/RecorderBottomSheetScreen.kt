@file:OptIn(ExperimentalMaterial3Api::class)

package com.nborba.vocalize.feature.recorder.impl.ui.recorder.compose

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun RecorderBottomSheetScreen(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
) {
    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
    ) {
        Text("Recorder")
    }
}
