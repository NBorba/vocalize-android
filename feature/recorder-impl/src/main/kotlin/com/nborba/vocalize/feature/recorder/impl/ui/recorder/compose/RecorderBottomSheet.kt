package com.nborba.vocalize.feature.recorder.impl.ui.recorder.compose

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nborba.vocalize.core.common.util.DefaultEffectHandler
import com.nborba.vocalize.core.designsystem.component.VocalizeButton
import com.nborba.vocalize.core.designsystem.theme.spacing
import com.nborba.vocalize.core.permission.host.PermissionPromptHost
import com.nborba.vocalize.core.permission.host.rememberPermissionPromptHostState
import com.nborba.vocalize.feature.recorder.impl.ui.recorder.RecorderBottomSheetViewModel
import com.nborba.vocalize.feature.recorder.impl.ui.recorder.model.RecorderEffect
import com.nborba.vocalize.feature.recorder.impl.ui.recorder.model.RecorderUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Composable
internal fun RecorderBottomSheet(
    modifier: Modifier = Modifier,
    viewModel: RecorderBottomSheetViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val permissionHostState = rememberPermissionPromptHostState()

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
        onEffectConsumed = viewModel::onEffectConsumed,
    )

    RecorderContent(
        modifier = modifier,
        uiState = uiState,
        onRecordButtonClick = viewModel::onRecordButtonClick,
    )

    PermissionPromptHost(hostState = permissionHostState)
}

@Composable
private fun EffectHandler(
    effectFlow: Flow<RecorderEffect?>,
    onRequestPermission: (String) -> Unit,
    onShowToast: (String) -> Unit,
    onEffectConsumed: () -> Unit,
) {
    DefaultEffectHandler(
        effectFlow = effectFlow,
        onEffect = { effect ->
            when (effect) {
                is RecorderEffect.RequestPermission -> onRequestPermission(effect.permission)
                is RecorderEffect.ShowToast -> onShowToast(effect.message)
            }
        },
        onConsumeEffect = onEffectConsumed,
    )
}

@Composable
internal fun RecorderContent(
    modifier: Modifier = Modifier,
    uiState: RecorderUiState,
    onRecordButtonClick: () -> Unit,
) {
    Column(
        modifier =
            modifier
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(MaterialTheme.spacing.medium)
                .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
    ) {
        Text(
            text = uiState.title,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleLarge,
        )

        HorizontalDivider()

        Row(
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        ) {
            VocalizeButton(
                text = uiState.buttonRecord,
                onClick = onRecordButtonClick,
            )
        }
    }
}

@Preview
@Composable
private fun RecorderContentPreview() {
    RecorderContent(
        uiState =
            RecorderUiState(
                title = "Recorder Test",
                buttonRecord = "Record",
            ),
        onRecordButtonClick = {},
    )
}
