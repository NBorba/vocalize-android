package com.nborba.vocalize.ui.home.model

import android.net.Uri

internal sealed interface HomeEffect {
    data object NavigateToRecorder : HomeEffect
}

internal data class HomeUiState(
    val title: String,
    val emptyTitle: String,
    val emptyDescription: String,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: Throwable? = null,
    val recordings: List<RecordingUiState> = emptyList(),
    val buttonRecord: String,
    val effect: HomeEffect? = null,
) {
    internal data class RecordingUiState(
        val title: String,
        val formattedDate: String,
        val uri: Uri,
    )
}
