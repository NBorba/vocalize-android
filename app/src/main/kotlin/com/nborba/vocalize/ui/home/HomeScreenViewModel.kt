package com.nborba.vocalize.ui.home

import androidx.lifecycle.ViewModel
import com.nborba.vocalize.ui.home.mapper.HomeUiStateMapper
import com.nborba.vocalize.ui.home.model.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
internal class HomeScreenViewModel
    @Inject
    constructor(
        homeUiStateMapper: HomeUiStateMapper,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(homeUiStateMapper())
        val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    }
