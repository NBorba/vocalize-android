package com.nborba.vocalize

import com.nborba.vocalize.ui.home.model.HomeUiState

internal object HomeUiStateFixture {
    fun default(): HomeUiState =
        HomeUiState(
            title = "Vocalize",
            header = "Welcome to the app!",
            buttonDetails = "See details",
            buttonRecord = "Record",
        )
}
