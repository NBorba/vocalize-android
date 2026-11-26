package com.nborba.vocalize.ui.home.compose

import android.media.MediaPlayer
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nborba.vocalize.core.common.util.DefaultEffectHandler
import com.nborba.vocalize.core.designsystem.component.VocalizeButton
import com.nborba.vocalize.core.designsystem.component.VocalizeExtendedFloatingActionButton
import com.nborba.vocalize.core.designsystem.component.VocalizeScaffold
import com.nborba.vocalize.core.designsystem.component.VocalizeTextButton
import com.nborba.vocalize.core.designsystem.component.VocalizeTopAppBar
import com.nborba.vocalize.core.designsystem.icon.VocalizeIcons
import com.nborba.vocalize.core.designsystem.theme.spacing
import com.nborba.vocalize.core.designsystem.util.defaultHorizontalPadding
import com.nborba.vocalize.ui.home.HomeScreenViewModel
import com.nborba.vocalize.ui.home.model.HomeEffect
import com.nborba.vocalize.ui.home.model.HomeEffect.NavigateToRecorder
import com.nborba.vocalize.ui.home.model.HomeUiState
import com.nborba.vocalize.ui.home.model.HomeUiState.RecordingUiState
import kotlinx.coroutines.flow.Flow

@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel = hiltViewModel(),
    onNavigateToRecorder: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // TODO: Implement Media3 exo player on :core:audio and call from VM!
    var mediaPlayer: MediaPlayer? = remember { null }

    EffectHandler(
        effectFlow = viewModel.effects,
        onNavigateToRecorder = onNavigateToRecorder,
        onEffectConsumed = viewModel::onEffectConsumed,
    )

    LifecycleResumeEffect(Unit) {
        viewModel.loadData()
        onPauseOrDispose { }
    }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.stop()
            mediaPlayer?.reset()
            mediaPlayer?.release()
        }
    }

    HomeContent(
        state = state,
        modifier = modifier,
        onPullToRefresh = viewModel::onPullToRefresh,
        onRetry = viewModel::loadData,
        onRecordButtonClick = viewModel::onRecordButtonClick,
        onPlayFile = { uri ->
            try {
                if (mediaPlayer == null) {
                    mediaPlayer = MediaPlayer()
                }

                mediaPlayer.run {
                    reset()
                    setDataSource(context, uri)
                    setOnPreparedListener { start() }
                    setOnCompletionListener { reset() }
                    setOnErrorListener { _, _, _ ->
                        reset()
                        true
                    }
                    prepareAsync()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                mediaPlayer?.reset()
            }
        },
    )
}

const val HOME_RECORD_FAB_TEST_TAG = "home_record_fab"

@Composable
internal fun HomeContent(
    state: HomeUiState,
    modifier: Modifier = Modifier,
    onPullToRefresh: () -> Unit,
    onRetry: () -> Unit,
    onRecordButtonClick: () -> Unit = {},
    onPlayFile: (Uri) -> Unit = {},
) {
    VocalizeScaffold(
        topBar = { VocalizeTopAppBar(title = state.title) },
        floatingActionButton = {
            VocalizeExtendedFloatingActionButton(
                icon = VocalizeIcons.Record,
                text = state.buttonRecord,
                onClick = onRecordButtonClick,
                modifier = Modifier.testTag(HOME_RECORD_FAB_TEST_TAG),
            )
        },
    ) { innerPadding ->
        with(state) {
            when {
                isLoading ->
                    DefaultContentWrapper(
                        modifier = modifier,
                        innerPadding = innerPadding,
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                error != null -> {
                    DefaultContentWrapper(
                        modifier = modifier,
                        innerPadding = innerPadding,
                        contentAlignment = Alignment.Center,
                    ) {
                        HomeErrorContent(
                            title = "Something went wrong",
                            description = "We could not load the recordings.",
                            onRetry = onRetry,
                        )
                    }
                }
                recordings.isEmpty() ->
                    DefaultContentWrapper(
                        modifier = modifier,
                        innerPadding = innerPadding,
                        contentAlignment = Alignment.Center,
                    ) {
                        HomeEmptyContent(
                            title = emptyTitle,
                            description = emptyDescription,
                        )
                    }
                else ->
                    DefaultRefreshableContentWrapper(
                        modifier = modifier,
                        innerPadding = innerPadding,
                        isRefreshing = isRefreshing,
                        onPullToRefresh = onPullToRefresh,
                    ) {
                        HomeListContent(
                            recordings = recordings,
                            onPlayFile = onPlayFile,
                        )
                    }
            }
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
            imageVector = VocalizeIcons.Record,
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

@Composable
private fun HomeErrorContent(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    onRetry: () -> Unit,
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
            imageVector = VocalizeIcons.Close,
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
        VocalizeButton(text = "Try again", onClick = onRetry)
    }
}

@Composable
private fun HomeListContent(
    modifier: Modifier = Modifier,
    recordings: List<RecordingUiState>,
    onPlayFile: (Uri) -> Unit,
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(
            count = recordings.size,
            key = { index -> recordings[index].uri.toString() },
        ) { index ->
            val item = recordings[index]
            VocalizeTextButton(
                text = "${item.title} - ${item.formattedDate}",
                onClick = { onPlayFile(item.uri) },
            )
        }
    }
}

@Composable
private fun DefaultContentWrapper(
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable () -> Unit,
) {
    Box(
        modifier =
            modifier
                .fillMaxSize()
                .padding(innerPadding)
                .defaultHorizontalPadding(),
        contentAlignment = contentAlignment,
    ) {
        content()
    }
}

@Composable
private fun DefaultRefreshableContentWrapper(
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier,
    isRefreshing: Boolean = false,
    onPullToRefresh: () -> Unit = {},
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable () -> Unit,
) {
    PullToRefreshBox(
        modifier =
            modifier
                .fillMaxSize()
                .padding(innerPadding)
                .defaultHorizontalPadding(),
        isRefreshing = isRefreshing,
        onRefresh = onPullToRefresh,
        contentAlignment = contentAlignment,
    ) {
        content()
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
