@file:OptIn(ExperimentalMaterial3Api::class)

package com.nborba.vocalize.ui.compose.detail

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

@Composable
internal fun DetailScreen(
    modifier: Modifier = Modifier,
    id: String,
    onUpClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    DetailContent(
        modifier = modifier,
        id = id,
        onUpClick = onUpClick,
        onBackClick = onBackClick,
    )
}

@Composable
private fun DetailContent(
    modifier: Modifier = Modifier,
    id: String,
    onUpClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    VocalizeScaffold(
        topBar = {
            VocalizeTopAppBar(
                title = "Detail #$id",
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
                text = "Viewing detail",
                style = MaterialTheme.typography.headlineMedium,
            )
            Spacer(modifier = Modifier.size(MaterialTheme.spacing.small))
            VocalizeButton(text = "Go back", onClick = onBackClick)
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
        id = "preview",
        onUpClick = { toast("onUpClick") },
        onBackClick = { toast("onBackClick") },
    )
}
