package com.nborba.vocalize

import com.nborba.vocalize.ui.detail.model.DetailUiState

internal object DetailUiStateFixture {
    fun default(): DetailUiState =
        DetailUiState(
            title = "Detail #1",
            header = "Viewing Detail",
            buttonBack = "Go back",
        )
}
