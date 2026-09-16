package com.nborba.vocalize.ui.home.model

internal sealed interface HomeEffect {
    data object NavigateToRecorder : HomeEffect
}

internal data class HomeUiState(
    val title: String,
    val emptyTitle: String,
    val emptyDescription: String,
    val buttonRecord: String,
    val effect: HomeEffect? = null,
)
