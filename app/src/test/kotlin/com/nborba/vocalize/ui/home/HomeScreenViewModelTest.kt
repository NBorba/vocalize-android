package com.nborba.vocalize.ui.home

import app.cash.turbine.test
import com.nborba.vocalize.HomeUiStateFixture
import com.nborba.vocalize.core.common.util.MainDispatcherExtension
import com.nborba.vocalize.ui.home.mapper.HomeUiStateMapper
import com.nborba.vocalize.ui.home.model.HomeEffect
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

internal class HomeScreenViewModelTest {
    @JvmField
    @RegisterExtension
    val mainDispatcherExtension = MainDispatcherExtension()

    private val homeUiStateMapper: HomeUiStateMapper = mockk()
    private lateinit var viewModel: HomeScreenViewModel

    @BeforeEach
    fun setUp() {
        every { homeUiStateMapper() } returns HomeUiStateFixture.default()
        viewModel = HomeScreenViewModel(homeUiStateMapper)
    }

    @Test
    fun `initial state is correctly set from mapper`() {
        assertEquals("Vocalize", viewModel.uiState.value.title)
        assertEquals("Welcome to the app!", viewModel.uiState.value.header)
    }

    @Test
    fun `when onRecordButtonClick, updates effect to NavigateToRecorder`() =
        runTest {
            viewModel.effects.test {
                assertEquals(null, awaitItem())

                viewModel.onRecordButtonClick()

                assertEquals(HomeEffect.NavigateToRecorder, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `when onDetailItemClick, updates effect to NavigateToDetail`() =
        runTest {
            viewModel.effects.test {
                assertEquals(null, awaitItem())

                viewModel.onDetailItemClick(DETAIL_ID)

                assertEquals(HomeEffect.NavigateToDetail(DETAIL_ID), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `when onEffectConsumed, clears effect`() =
        runTest {
            viewModel.effects.test {
                assertEquals(null, awaitItem())

                viewModel.onRecordButtonClick()
                assertEquals(HomeEffect.NavigateToRecorder, awaitItem())

                viewModel.onEffectConsumed()
                assertEquals(null, awaitItem())

                cancelAndIgnoreRemainingEvents()
            }
        }

    private companion object {
        const val DETAIL_ID = "detailId"
    }
}
