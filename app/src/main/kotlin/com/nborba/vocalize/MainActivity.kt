package com.nborba.vocalize

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.nborba.vocalize.core.designsystem.theme.VocalizeTheme
import com.nborba.vocalize.ui.VocalizeApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VocalizeTheme {
                VocalizeApp()
            }
        }
    }
}
