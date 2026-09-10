package com.nborba.com.nborba.vocalize.feature.recorder.impl.ui.recorder

import com.nborba.vocalize.RecorderUiStateFixture
import com.nborba.vocalize.core.common.util.MainDispatcherExtension
import com.nborba.vocalize.feature.recorder.impl.ui.recorder.RecorderBottomSheetViewModel
import com.nborba.vocalize.feature.recorder.impl.ui.recorder.mapper.RecorderUiStateMapper
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

internal class RecorderBottomSheetViewModelTest {
    @JvmField
    @RegisterExtension
    val mainCoroutinesDispatcher = MainDispatcherExtension()

    private val recorderUiStateMapper: RecorderUiStateMapper = mockk()
    private lateinit var viewModel: RecorderBottomSheetViewModel

    @BeforeEach
    fun setUp() {
        every { recorderUiStateMapper() } returns RecorderUiStateFixture.default()

        viewModel =
            RecorderBottomSheetViewModel(
                recorderUiStateMapper = recorderUiStateMapper,
            )
    }

    @Test
    fun `initial state is correctly set from mapper`() {
        assertEquals(RecorderUiStateFixture.default(), viewModel.uiState.value)
    }
}
