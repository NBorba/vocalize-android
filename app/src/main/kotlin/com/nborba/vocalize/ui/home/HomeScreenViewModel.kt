package com.nborba.vocalize.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nborba.vocalize.data.GetRecordingFiles
import com.nborba.vocalize.ui.home.mapper.HomeUiStateMapper
import com.nborba.vocalize.ui.home.model.HomeEffect
import com.nborba.vocalize.ui.home.model.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
internal class HomeScreenViewModel
    @Inject
    constructor(
        private val homeUiStateMapper: HomeUiStateMapper,
        private val getRecordingFiles: GetRecordingFiles,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(homeUiStateMapper(isLoading = true))
        val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

        private var fileLoadingJob: Job? = null

        val effects = uiState.map { it.effect }

        fun loadData() {
            if (fileLoadingJob?.isActive == true) {
                return
            }

            _uiState.update { homeUiStateMapper(isLoading = true) }
            getRecordings()
        }

        fun onPullToRefresh() {
            if (fileLoadingJob?.isActive == true) {
                return
            }

            _uiState.update {
                homeUiStateMapper(
                    isRefreshing = true,
                    recordingUiState = it.recordings,
                )
            }
            getRecordings()
        }

        fun onRecordButtonClick() {
            _uiState.update { it.copy(effect = HomeEffect.NavigateToRecorder) }
        }

        fun onEffectConsumed() {
            _uiState.update { it.copy(effect = null) }
        }

        private fun getRecordings() {
            fileLoadingJob?.cancel()
            fileLoadingJob =
                viewModelScope.launch {
                    // Quality of life to UX, otherwise loads instantly and no feedback is shown
                    delay(500.milliseconds)

                    getRecordingFiles()
                        .onFailure { throwable -> _uiState.update { homeUiStateMapper(error = throwable) } }
                        .onSuccess { files -> _uiState.update { homeUiStateMapper(recordingFiles = files) } }
                }
            fileLoadingJob?.start()
        }
    }
