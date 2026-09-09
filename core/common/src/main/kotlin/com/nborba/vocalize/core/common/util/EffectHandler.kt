package com.nborba.vocalize.core.common.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow

/**
 * Collects one-off UI side effects emitted by a Flow (e.g., ViewModel events).
 */
@Composable
fun <E> DefaultEffectHandler(
    effectFlow: Flow<E?>,
    onEffect: suspend (E) -> Unit,
    onConsumeEffect: () -> Unit,
) {
    val currentEffect by effectFlow.collectAsStateWithLifecycle(null)
    val currentOnEffect by rememberUpdatedState(onEffect)
    val currentOnConsumeEffect by rememberUpdatedState(onConsumeEffect)

    LaunchedEffect(currentEffect) {
        currentEffect?.let {
            currentOnEffect(it)
            currentOnConsumeEffect()
        }
    }
}
