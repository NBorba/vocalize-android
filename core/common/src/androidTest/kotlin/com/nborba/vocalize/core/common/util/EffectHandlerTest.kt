package com.nborba.vocalize.core.common.util

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.testing.TestLifecycleOwner
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import kotlin.time.Duration.Companion.milliseconds

class EffectHandlerTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun effectEmittedInForeground_invokesOnEffectAndOnConsumeEffect() {
        val effectFlow = MutableStateFlow<String?>(null)
        var capturedEffect: String? = null
        var effectConsumed = false

        composeTestRule.setContent {
            DefaultEffectHandler(
                effectFlow = effectFlow,
                onEffect = { effect -> capturedEffect = effect },
                onConsumeEffect = { effectConsumed = true },
            )
        }

        effectFlow.value = "NavigateToDetail"
        composeTestRule.waitForIdle()

        assertEquals("NavigateToDetail", capturedEffect)
        assertTrue("Expected onConsumeEffect to be called", effectConsumed)
    }

    @Test
    fun nullEffect_doesNotInvokeCallbacks() {
        val effectFlow = MutableStateFlow<String?>(null)
        var effectInvoked = false
        var consumeInvoked = false

        composeTestRule.setContent {
            DefaultEffectHandler(
                effectFlow = effectFlow,
                onEffect = { effectInvoked = true },
                onConsumeEffect = { consumeInvoked = true },
            )
        }

        composeTestRule.waitForIdle()

        assertFalse("onEffect should not be called for null state", effectInvoked)
        assertFalse("onConsumeEffect should not be called for null state", consumeInvoked)
    }

    @Test
    fun multipleSequentialEffects_areProcessedInOrder() {
        val effectFlow = MutableStateFlow<String?>(null)
        val processedEffects = mutableListOf<String>()

        composeTestRule.setContent {
            DefaultEffectHandler(
                effectFlow = effectFlow,
                onEffect = { effect -> processedEffects.add(effect) },
                onConsumeEffect = { effectFlow.value = null },
            )
        }

        effectFlow.value = "Effect1"
        composeTestRule.waitForIdle()

        effectFlow.value = "Effect2"
        composeTestRule.waitForIdle()

        assertEquals(listOf("Effect1", "Effect2"), processedEffects)
    }

    @Test
    fun suspendingOnEffect_invokesOnConsumeEffectOnlyAfterCompletion() {
        val effectFlow = MutableStateFlow<String?>(null)
        val executionOrder = mutableListOf<String>()

        composeTestRule.setContent {
            DefaultEffectHandler(
                effectFlow = effectFlow,
                onEffect = {
                    executionOrder.add("startOnEffect")
                    delay(100.milliseconds)
                    executionOrder.add("endOnEffect")
                },
                onConsumeEffect = {
                    executionOrder.add("onConsumeEffect")
                    effectFlow.value = null
                },
            )
        }

        effectFlow.value = "SlowEffect"
        composeTestRule.waitUntil(timeoutMillis = 3000) { executionOrder.size == 3 }

        assertEquals(
            listOf("startOnEffect", "endOnEffect", "onConsumeEffect"),
            executionOrder,
        )
    }

    @Test
    fun effectEmittedInBackground_isPaused_andProcessedWhenResumed() {
        val lifecycleOwner = TestLifecycleOwner(initialState = Lifecycle.State.STARTED)
        val effectFlow = MutableStateFlow<String?>(null)
        var effectHandled = false

        composeTestRule.setContent {
            CompositionLocalProvider(LocalLifecycleOwner provides lifecycleOwner) {
                DefaultEffectHandler(
                    effectFlow = effectFlow,
                    onEffect = { effectHandled = true },
                    onConsumeEffect = { effectFlow.value = null },
                )
            }
        }

        // 1. Move lifecycle to background (STOPPED)
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_STOP)

        // 2. Emit an effect while the app is in the background
        effectFlow.value = "NavigateToDetail"
        composeTestRule.waitForIdle()

        // 3. Assert the effect was NOT processed while backgrounded
        assertFalse("Effect should NOT be processed while app is in background", effectHandled)

        // 4. Move lifecycle back to STARTED (user re-opens the app)
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        composeTestRule.waitForIdle()

        // 5. Assert the effect WAS processed once back in the foreground
        assertTrue("Effect SHOULD be processed when returning to STARTED", effectHandled)
    }

    @Test
    fun updatedCallbackReferences_areReflectedViaRememberUpdatedState() {
        val effectFlow = MutableStateFlow<String?>(null)
        var currentLabel by mutableStateOf("InitialHandler")
        var invokerLabel: String? = null

        composeTestRule.setContent {
            DefaultEffectHandler(
                effectFlow = effectFlow,
                onEffect = { invokerLabel = currentLabel },
                onConsumeEffect = {},
            )
        }

        // Change the label state (recomposing with a new lambda closure)
        currentLabel = "UpdatedHandler"
        composeTestRule.waitForIdle()

        // Emit effect
        effectFlow.value = "TestEffect"
        composeTestRule.waitForIdle()

        // Verify updated lambda reference was captured via rememberUpdatedState
        assertEquals("UpdatedHandler", invokerLabel)
    }
}
