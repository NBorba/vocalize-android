@file:OptIn(ExperimentalMaterial3Api::class)

package com.nborba.vocalize.ui.home.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nborba.vocalize.core.common.util.DefaultEffectHandler
import com.nborba.vocalize.core.designsystem.component.VocalizeExtendedFloatingActionButton
import com.nborba.vocalize.core.designsystem.component.VocalizeScaffold
import com.nborba.vocalize.core.designsystem.component.VocalizeTopAppBar
import com.nborba.vocalize.core.designsystem.icon.VocalizeIcons
import com.nborba.vocalize.core.designsystem.theme.spacing
import com.nborba.vocalize.ui.home.HomeScreenViewModel
import com.nborba.vocalize.ui.home.model.HomeEffect
import com.nborba.vocalize.ui.home.model.HomeEffect.NavigateToRecorder
import com.nborba.vocalize.ui.home.model.HomeUiState
import kotlinx.coroutines.flow.Flow

@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel = hiltViewModel(),
    onNavigateToRecorder: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    EffectHandler(
        effectFlow = viewModel.effects,
        onNavigateToRecorder = onNavigateToRecorder,
        onEffectConsumed = viewModel::onEffectConsumed,
    )

    HomeContent(
        state = state,
        modifier = modifier,
        onRecordButtonClick = viewModel::onRecordButtonClick,
    )
}

@Composable
internal fun HomeContent(
    state: HomeUiState,
    modifier: Modifier = Modifier,
    onRecordButtonClick: () -> Unit = {},
) {
    VocalizeScaffold(
        topBar = { VocalizeTopAppBar(title = state.title) },
        floatingActionButton = {
            VocalizeExtendedFloatingActionButton(
                icon = VocalizeIcons.RecordCircle,
                text = state.buttonRecord,
                onClick = onRecordButtonClick,
            )
        },
    ) { _ ->
        Box(
            modifier =
                modifier
                    .fillMaxSize()
                    .padding(horizontal = MaterialTheme.spacing.medium),
            contentAlignment = Alignment.Center,
        ) {
            HomeEmptyContent(
                title = state.emptyTitle,
                description = state.emptyDescription,
            )
        }
    }
}

@Composable
private fun EffectHandler(
    effectFlow: Flow<HomeEffect?>,
    onNavigateToRecorder: () -> Unit,
    onEffectConsumed: () -> Unit,
) {
    DefaultEffectHandler(
        effectFlow = effectFlow,
        onEffect = { effect ->
            when (effect) {
                NavigateToRecorder -> onNavigateToRecorder()
            }
        },
        onConsumeEffect = onEffectConsumed,
    )
}

@Composable
private fun HomeEmptyContent(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement =
            Arrangement.spacedBy(
                space = MaterialTheme.spacing.medium,
                alignment = Alignment.CenterVertically,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = VocalizeIcons.RecordCircle,
            contentDescription = null, // Decorative empty state icon
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )
        Text(
            text = AnnotatedString.fromHtml(description),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@Composable
private fun HomeEmptyContentPreview() {
    HomeEmptyContent(
        title = "No recordings yet",
        description = "Tap <b>Record</b> below to start your first recording.",
    )
}
