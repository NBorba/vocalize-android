package com.nborba.vocalize.ui.home.model

internal sealed interface HomeEffect {
    data object NavigateToRecorder : HomeEffect

    data class NavigateToDetail(
        val id: String,
    ) : HomeEffect
}

internal data class HomeUiState(
    val title: String,
    val header: String,
    val buttonDetails: String,
    val buttonRecord: String,
    val effect: HomeEffect? = null,
)
