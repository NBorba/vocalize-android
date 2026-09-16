package com.nborba.vocalize

import com.nborba.vocalize.ui.home.model.HomeUiState

internal object HomeUiStateFixture {
    fun default(): HomeUiState =
        HomeUiState(
            title = "Vocalize",
            emptyTitle = "No recordings yet",
            emptyDescription = "Tap <b>Record</b> below to start your first recording.",
            buttonRecord = "Record",
        )
}
