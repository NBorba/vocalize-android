package com.nborba.vocalize.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.nborba.vocalize.navigation.DetailRoute
import com.nborba.vocalize.ui.detail.mapper.DetailUiStateMapper
import com.nborba.vocalize.ui.detail.model.DetailEffect
import com.nborba.vocalize.ui.detail.model.DetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
internal class DetailScreenViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        detailUiStateMapper: DetailUiStateMapper,
    ) : ViewModel() {
        private val route = savedStateHandle.toRoute<DetailRoute>()
        private val id = route.id

        private val _uiState = MutableStateFlow(detailUiStateMapper(id))
        val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

        val effects = uiState.map { it.effect }

        fun onBackClick() {
            _uiState.update { it.copy(effect = DetailEffect.NavigateBack) }
        }

        fun onUpClick() {
            _uiState.update { it.copy(effect = DetailEffect.NavigateUp) }
        }

        fun onEffectConsumed() {
            _uiState.update { it.copy(effect = null) }
        }
    }
