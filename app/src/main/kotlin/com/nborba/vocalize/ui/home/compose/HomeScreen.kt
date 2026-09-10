@file:OptIn(ExperimentalMaterial3Api::class)

package com.nborba.vocalize.ui.home.compose

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
import com.nborba.vocalize.ui.home.HomeScreenViewModel
import com.nborba.vocalize.ui.home.model.HomeEffect
import com.nborba.vocalize.ui.home.model.HomeEffect.NavigateToDetail
import com.nborba.vocalize.ui.home.model.HomeEffect.NavigateToRecorder
import com.nborba.vocalize.ui.home.model.HomeUiState
import kotlinx.coroutines.flow.Flow
import kotlin.random.Random

@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel = hiltViewModel(),
    onNavigateToDetail: (String) -> Unit,
    onNavigateToRecorder: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    EffectHandler(
        effectFlow = viewModel.effects,
        onNavigateToRecorder = onNavigateToRecorder,
        onNavigateToDetail = onNavigateToDetail,
        onEffectConsumed = viewModel::onEffectConsumed,
    )

    HomeContent(
        state = state,
        modifier = modifier,
        onNavigateToDetail = viewModel::onDetailItemClick,
        onNavigateToRecorder = viewModel::onRecordButtonClick,
    )
}

@Composable
private fun EffectHandler(
    effectFlow: Flow<HomeEffect?>,
    onNavigateToRecorder: () -> Unit,
    onNavigateToDetail: (String) -> Unit,
    onEffectConsumed: () -> Unit,
) {
    DefaultEffectHandler(
        effectFlow = effectFlow,
        onEffect = { effect ->
            when (effect) {
                NavigateToRecorder -> onNavigateToRecorder()
                is NavigateToDetail -> onNavigateToDetail(effect.id)
            }
        },
        onConsumeEffect = onEffectConsumed,
    )
}

@Composable
internal fun HomeContent(
    state: HomeUiState,
    modifier: Modifier = Modifier,
    onNavigateToDetail: (String) -> Unit,
    onNavigateToRecorder: () -> Unit,
) {
    VocalizeScaffold(
        topBar = {
            VocalizeTopAppBar(title = state.title)
        },
    ) { innerPadding ->
        Column(
            modifier =
                modifier
                    .padding(innerPadding)
                    .padding(MaterialTheme.spacing.medium),
        ) {
            Text(
                text = state.header,
                style = MaterialTheme.typography.headlineMedium,
            )
            Spacer(modifier = Modifier.size(MaterialTheme.spacing.small))
            VocalizeButton(
                text = state.buttonDetails,
                onClick = { onNavigateToDetail(Random.nextInt().toString()) },
            )
            VocalizeButton(
                text = state.buttonRecord,
                onClick = { onNavigateToRecorder() },
            )
        }
    }
}

@Preview
@Composable
private fun HomeContentPreview() {
    val context = LocalContext.current
    val toast: (String) -> Unit = { message ->
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    HomeContent(
        state =
            HomeUiState(
                title = "Vocalize",
                header = "Welcome to the app!",
                buttonDetails = "See details",
                buttonRecord = "Record",
            ),
        onNavigateToDetail = { toast("onNavigateToDetail") },
        onNavigateToRecorder = { toast("onNavigateToRecorder") },
    )
}
