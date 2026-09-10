@file:OptIn(ExperimentalMaterial3Api::class)

package com.nborba.vocalize.ui.detail.compose

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nborba.vocalize.core.common.util.DefaultEffectHandler
import com.nborba.vocalize.core.designsystem.component.VocalizeButton
import com.nborba.vocalize.core.designsystem.component.VocalizeScaffold
import com.nborba.vocalize.core.designsystem.component.VocalizeTopAppBar
import com.nborba.vocalize.core.designsystem.theme.spacing
import com.nborba.vocalize.ui.detail.DetailScreenViewModel
import com.nborba.vocalize.ui.detail.model.DetailEffect
import com.nborba.vocalize.ui.detail.model.DetailEffect.NavigateBack
import com.nborba.vocalize.ui.detail.model.DetailEffect.NavigateUp
import com.nborba.vocalize.ui.detail.model.DetailUiState
import kotlinx.coroutines.flow.Flow

@Composable
internal fun DetailScreen(
    modifier: Modifier = Modifier,
    viewModel: DetailScreenViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    EffectHandler(
        effectFlow = viewModel.effects,
        onNavigateBack = onNavigateBack,
        onNavigateUp = onNavigateUp,
        onConsumeEffect = viewModel::onEffectConsumed,
    )

    DetailContent(
        modifier = modifier,
        uiState = uiState,
        onBackClick = viewModel::onBackClick,
        onUpClick = viewModel::onUpClick,
    )
}

@Composable
private fun EffectHandler(
    effectFlow: Flow<DetailEffect?>,
    onNavigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    onConsumeEffect: () -> Unit,
) {
    DefaultEffectHandler(
        effectFlow = effectFlow,
        onEffect = { effect ->
            when (effect) {
                NavigateBack -> onNavigateBack()
                NavigateUp -> onNavigateUp()
            }
        },
        onConsumeEffect = onConsumeEffect,
    )
}

@Composable
internal fun DetailContent(
    modifier: Modifier = Modifier,
    uiState: DetailUiState,
    onUpClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    VocalizeScaffold(
        topBar = {
            VocalizeTopAppBar(
                title = uiState.title,
                onNavigationClick = onUpClick,
            )
        },
    ) { innerPadding ->
        Column(
            modifier =
                modifier
                    .padding(innerPadding)
                    .padding(MaterialTheme.spacing.medium),
        ) {
            Text(
                text = uiState.header,
                style = MaterialTheme.typography.headlineMedium,
            )
            Spacer(modifier = Modifier.size(MaterialTheme.spacing.small))
            VocalizeButton(text = uiState.buttonBack, onClick = onBackClick)
        }
    }
}

@Preview
@Composable
private fun DetailContentPreview() {
    val context = LocalContext.current
    val toast: (String) -> Unit = { message ->
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    DetailContent(
        uiState =
            DetailUiState(
                title = "Detail #1",
                header = "Viewing detail",
                buttonBack = "Go back",
            ),
        onUpClick = { toast("onUpClick") },
        onBackClick = { toast("onBackClick") },
    )
}
