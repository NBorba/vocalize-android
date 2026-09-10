package com.nborba.vocalize.ui.detail.mapper

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import app.cash.turbine.test
import com.nborba.vocalize.DetailUiStateFixture
import com.nborba.vocalize.core.common.util.MainDispatcherExtension
import com.nborba.vocalize.navigation.DetailRoute
import com.nborba.vocalize.ui.detail.DetailScreenViewModel
import com.nborba.vocalize.ui.detail.model.DetailEffect
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull
import org.junit.jupiter.api.extension.RegisterExtension

internal class DetailScreenViewModelTest {
    @JvmField
    @RegisterExtension
    val mainDispatcherExtension = MainDispatcherExtension()

    private val detailUiStateMapper: DetailUiStateMapper = mockk()
    private lateinit var viewModel: DetailScreenViewModel

    @BeforeEach
    fun setUp() {
        mockkStatic("androidx.navigation.SavedStateHandleKt")
        val savedStateHandle: SavedStateHandle = mockk()
        every { savedStateHandle.toRoute<DetailRoute>() } returns DetailRoute(id = DETAIL_ID)
        every { detailUiStateMapper(DETAIL_ID) } returns DetailUiStateFixture.default()

        viewModel =
            DetailScreenViewModel(
                savedStateHandle = savedStateHandle,
                detailUiStateMapper = detailUiStateMapper,
            )
    }

    @AfterEach
    fun tearDown() {
        unmockkStatic("androidx.navigation.SavedStateHandleKt")
    }

    @Test
    fun `initial state is correctly set from mapper using id from SavedStateHandle`() {
        assertEquals(DetailUiStateFixture.default(), viewModel.uiState.value)
    }

    @Test
    fun `when onBackClick, then updates effect to NavigateBack`() =
        runTest {
            viewModel.effects.test {
                assertEquals(null, awaitItem())

                viewModel.onBackClick()

                assertEquals(DetailEffect.NavigateBack, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `when onUpClick, then updates effect to NavigateUp`() =
        runTest {
            viewModel.effects.test {
                assertEquals(null, awaitItem())

                viewModel.onUpClick()

                assertEquals(DetailEffect.NavigateUp, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `when onEffectConsumed, then clears effect`() =
        runTest {
            viewModel.effects.test {
                assertEquals(null, awaitItem())

                viewModel.onBackClick()
                assertEquals(DetailEffect.NavigateBack, awaitItem())

                viewModel.onEffectConsumed()
                assertNull(awaitItem())

                cancelAndIgnoreRemainingEvents()
            }
        }

    private companion object {
        const val DETAIL_ID = "detailId"
    }
}
