package com.nborba.vocalize.feature.recorder.impl.ui.recorder.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nborba.vocalize.core.designsystem.theme.spacing

@Composable
fun RecorderBottomSheet(modifier: Modifier = Modifier) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(MaterialTheme.spacing.medium),
    ) {
        Text(
            text = "Recorder",
            style = MaterialTheme.typography.titleLarge,
        )
    }
}
