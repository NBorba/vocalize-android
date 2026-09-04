@file:Suppress("UsingMaterialAndMaterial3Libraries")

package com.nborba.vocalize.feature.recorder.impl.ui

import androidx.compose.material.navigation.bottomSheet
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.nborba.vocalize.feature.recorder.api.navigation.RecorderGraphRoute
import com.nborba.vocalize.feature.recorder.impl.navigation.RecorderRoute
import com.nborba.vocalize.feature.recorder.impl.ui.recorder.compose.RecorderBottomSheetScreen

fun NavGraphBuilder.recorderNavGraph(onDismiss: () -> Unit) {
    navigation<RecorderGraphRoute>(
        startDestination = RecorderRoute,
    ) {
        bottomSheet<RecorderRoute>(
            deepLinks =
                listOf(
                    navDeepLink<RecorderRoute>(basePath = "https://vocalize.app/recorder"),
                    navDeepLink<RecorderRoute>(basePath = "vocalize://recorder"),
                ),
        ) {
            RecorderBottomSheetScreen(onDismissRequest = onDismiss)
        }
    }
}
