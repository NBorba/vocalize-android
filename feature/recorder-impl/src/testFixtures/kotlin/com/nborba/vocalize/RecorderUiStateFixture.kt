package com.nborba.vocalize

import com.nborba.vocalize.feature.recorder.impl.ui.recorder.model.RecorderUiState

internal object RecorderUiStateFixture {
    fun default(): RecorderUiState =
        RecorderUiState(
            title = "Recorder",
        )
}
