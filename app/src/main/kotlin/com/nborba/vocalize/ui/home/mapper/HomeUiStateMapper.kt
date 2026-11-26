package com.nborba.vocalize.ui.home.mapper

import androidx.core.net.toUri
import com.nborba.vocalize.R
import com.nborba.vocalize.core.common.util.StringProvider
import com.nborba.vocalize.ui.home.model.HomeUiState
import com.nborba.vocalize.ui.home.model.HomeUiState.RecordingUiState
import java.io.File
import javax.inject.Inject
import com.nborba.vocalize.core.designsystem.R as DSR

internal class HomeUiStateMapper
    @Inject
    constructor(
        private val stringProvider: StringProvider,
    ) {
        operator fun invoke(
            recordingFiles: List<File> = emptyList(),
            recordingUiState: List<RecordingUiState>? = null,
            isLoading: Boolean = false,
            isRefreshing: Boolean = false,
            error: Throwable? = null,
        ): HomeUiState {
            val buttonRecord = stringProvider.getString(DSR.string.button_record)
            return HomeUiState(
                title = stringProvider.getString(R.string.app_name),
                emptyTitle = stringProvider.getString(R.string.home_empty_title),
                emptyDescription =
                    stringProvider.getString(
                        R.string.home_empty_description,
                        buttonRecord,
                    ),
                isLoading = isLoading,
                isRefreshing = isRefreshing,
                error = error,
                recordings = recordingUiState ?: recordingFiles.map(::mapRecording),
                buttonRecord = buttonRecord,
            )
        }

        private fun mapRecording(recordingFile: File): RecordingUiState =
            recordingFile.run {
                RecordingUiState(
                    title = nameWithoutExtension,
                    formattedDate = lastModified().toString(),
                    uri = toUri(),
                )
            }
    }
