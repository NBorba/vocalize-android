@file:OptIn(ExperimentalMaterial3Api::class)

package com.nborba.vocalize.ui.compose.detail

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.nborba.vocalize.core.designsystem.component.VocalizeScaffold

@Composable
fun DetailScreen(
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
            TopAppBar(
                title = { Text("Detail #$id") },
                navigationIcon = {
                    IconButton(onClick = onUpClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = modifier.padding(innerPadding),
        ) {
            Text(text = "Viewing detail")
            Button(onClick = onBackClick) { Text("Go back") }
        }
    }
}

@Preview
@Composable
fun DetailContentPreview() {
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
