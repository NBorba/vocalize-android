package com.nborba.vocalize.feature.recorder.impl.ui.recorder.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nborba.vocalize.core.designsystem.theme.spacing
import com.nborba.vocalize.feature.recorder.impl.ui.recorder.RecorderBottomSheetViewModel
import com.nborba.vocalize.feature.recorder.impl.ui.recorder.model.RecorderUiState

@Composable
internal fun RecorderBottomSheet(
    modifier: Modifier = Modifier,
    viewModel: RecorderBottomSheetViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    RecorderContent(
        modifier = modifier,
        uiState = uiState,
    )
}

@Composable
internal fun RecorderContent(
    modifier: Modifier = Modifier,
    uiState: RecorderUiState,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .navigationBarsPadding()
                .padding(MaterialTheme.spacing.medium),
    ) {
        Text(
            text = uiState.title,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleLarge,
        )
    }
}

@Preview
@Composable
private fun RecorderContentPreview() {
    RecorderContent(
        uiState = RecorderUiState("Recorder"),
    )
}
