package com.nborba.vocalize.ui.detail.model

internal sealed interface DetailEffect {
    object NavigateUp : DetailEffect

    object NavigateBack : DetailEffect
}

internal data class DetailUiState(
    val title: String,
    val header: String,
    val buttonBack: String,
    val effect: DetailEffect? = null,
)
