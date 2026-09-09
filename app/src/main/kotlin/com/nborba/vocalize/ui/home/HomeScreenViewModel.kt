package com.nborba.vocalize.ui.home

import androidx.lifecycle.ViewModel
import com.nborba.vocalize.ui.home.mapper.HomeUiStateMapper
import com.nborba.vocalize.ui.home.model.HomeEffect
import com.nborba.vocalize.ui.home.model.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
internal class HomeScreenViewModel
    @Inject
    constructor(
        homeUiStateMapper: HomeUiStateMapper,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(homeUiStateMapper())
        val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

        val effects = uiState.map { it.effect }

        fun onRecordButtonClick() {
            _uiState.update { it.copy(effect = HomeEffect.NavigateToRecorder) }
        }

        fun onDetailItemClick(id: String) {
            _uiState.update { it.copy(effect = HomeEffect.NavigateToDetail(id)) }
        }

        fun onEffectHandled() {
            _uiState.update { it.copy(effect = null) }
        }
    }
