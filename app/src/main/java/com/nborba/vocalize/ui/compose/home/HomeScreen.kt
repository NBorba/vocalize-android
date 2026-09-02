@file:OptIn(ExperimentalMaterial3Api::class)

package com.nborba.vocalize.ui.compose.home

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.nborba.vocalize.core.designsystem.component.VocalizeScaffold
import kotlin.random.Random

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavigateToDetail: (String) -> Unit,
) {
    HomeContent(
        modifier = modifier,
        onNavigateToDetail = onNavigateToDetail,
    )
}

@Composable
private fun HomeContent(
    modifier: Modifier = Modifier,
    onNavigateToDetail: (String) -> Unit,
) {
    VocalizeScaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Vocalize")
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = modifier.padding(innerPadding),
        ) {
            Text(text = "Welcome to the app!")
            Button(onClick = { onNavigateToDetail(Random.nextInt().toString()) }) {
                Text("See details")
            }
        }
    }
}

@Preview
@Composable
fun HomeContentPreview() {
    val context = LocalContext.current
    val toast: (String) -> Unit = { message ->
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    HomeContent(
        onNavigateToDetail = { toast("onNavigateToDetail") },
    )
}
