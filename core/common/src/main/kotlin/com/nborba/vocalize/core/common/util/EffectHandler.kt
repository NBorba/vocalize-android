package com.nborba.vocalize.core.common.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull

/**
 * Collects one-off UI side effects emitted by a Flow (e.g., ViewModel events).
 */
@Composable
fun <E> DefaultEffectHandler(
    effectFlow: Flow<E?>,
    onEffect: suspend (E) -> Unit,
    onConsumeEffect: () -> Unit,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
) {
    val currentOnEffect by rememberUpdatedState(onEffect)
    val currentOnConsumeEffect by rememberUpdatedState(onConsumeEffect)

    LaunchedEffect(effectFlow, lifecycleOwner) {
        effectFlow
            .filterNotNull()
            .flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .collect { effect ->
                currentOnEffect(effect)
                currentOnConsumeEffect()
            }
    }
}
