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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.nborba.vocalize.core.designsystem.component.VocalizeButton
import com.nborba.vocalize.core.designsystem.component.VocalizeScaffold
import com.nborba.vocalize.core.designsystem.component.VocalizeTopAppBar
import com.nborba.vocalize.core.designsystem.theme.spacing
import kotlin.random.Random

@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavigateToDetail: (String) -> Unit,
    onNavigateToRecorder: () -> Unit,
) {
    HomeContent(
        modifier = modifier,
        onNavigateToDetail = onNavigateToDetail,
        onNavigateToRecorder = onNavigateToRecorder,
    )
}

@Composable
private fun HomeContent(
    modifier: Modifier = Modifier,
    onNavigateToDetail: (String) -> Unit,
    onNavigateToRecorder: () -> Unit,
) {
    VocalizeScaffold(
        topBar = {
            VocalizeTopAppBar(title = "Vocalize")
        },
    ) { innerPadding ->
        Column(
            modifier =
                modifier
                    .padding(innerPadding)
                    .padding(MaterialTheme.spacing.medium),
        ) {
            Text(
                text = "Welcome to the app!",
                style = MaterialTheme.typography.headlineMedium,
            )
            Spacer(modifier = Modifier.size(MaterialTheme.spacing.small))
            VocalizeButton(
                text = "See details",
                onClick = { onNavigateToDetail(Random.nextInt().toString()) },
            )
            VocalizeButton(
                text = "Record",
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
        onNavigateToDetail = { toast("onNavigateToDetail") },
        onNavigateToRecorder = { toast("onNavigateToRecorder") },
    )
}
