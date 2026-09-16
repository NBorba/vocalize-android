package com.nborba.vocalize.feature.recorder.impl.ui.recorder.compose

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nborba.vocalize.core.common.util.DefaultEffectHandler
import com.nborba.vocalize.core.designsystem.component.VocalizeCircularButton
import com.nborba.vocalize.core.designsystem.theme.spacing
import com.nborba.vocalize.core.designsystem.util.defaultPadding
import com.nborba.vocalize.core.permission.host.PermissionPromptHost
import com.nborba.vocalize.core.permission.host.rememberPermissionPromptHostState
import com.nborba.vocalize.feature.recorder.impl.ui.recorder.RecorderBottomSheetViewModel
import com.nborba.vocalize.feature.recorder.impl.ui.recorder.model.RecorderEffect
import com.nborba.vocalize.feature.recorder.impl.ui.recorder.model.RecorderUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RecorderBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RecorderBottomSheetViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val permissionHostState = rememberPermissionPromptHostState()

    val sheetState =
        rememberModalBottomSheetState(
            skipPartiallyExpanded = true,
        )

    EffectHandler(
        effectFlow = viewModel.effects,
        onRequestPermission = { permission ->
            scope.launch {
                val result = permissionHostState.requestPermission(permission)
                viewModel.onAudioPermissionRequestResult(result)
            }
        },
        onShowToast = { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        },
        onDismissRequest = onDismissRequest,
        onEffectConsumed = viewModel::onEffectConsumed,
    )

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = viewModel::onDismissRequest,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
    ) {
        RecorderContent(
            uiState = uiState,
            onMainButtonClick = viewModel::onMainButtonClick,
        )
    }

    PermissionPromptHost(hostState = permissionHostState)
}

@Composable
private fun EffectHandler(
    effectFlow: Flow<RecorderEffect?>,
    onRequestPermission: (String) -> Unit,
    onShowToast: (String) -> Unit,
    onEffectConsumed: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    DefaultEffectHandler(
        effectFlow = effectFlow,
        onEffect = { effect ->
            when (effect) {
                is RecorderEffect.RequestPermission -> onRequestPermission(effect.permission)
                is RecorderEffect.ShowToast -> onShowToast(effect.message)
                is RecorderEffect.Dismiss -> {
                    effect.message?.let { onShowToast(it) }
                    onDismissRequest()
                }
            }
        },
        onConsumeEffect = onEffectConsumed,
    )
}

@Composable
internal fun RecorderContent(
    uiState: RecorderUiState,
    onMainButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .defaultPadding()
                .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        VocalizeCircularButton(
            icon = uiState.mainButtonIcon,
            onClick = onMainButtonClick,
        )
    }
}

@Preview
@Composable
private fun RecorderContentPreview() {
    RecorderContent(
        uiState = RecorderUiState(),
        onMainButtonClick = {},
    )
}
