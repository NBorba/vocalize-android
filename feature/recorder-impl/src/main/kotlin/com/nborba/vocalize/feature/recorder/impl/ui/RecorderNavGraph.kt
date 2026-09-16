package com.nborba.vocalize.feature.recorder.impl.ui

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.dialog
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.nborba.vocalize.feature.recorder.api.navigation.RecorderGraphRoute
import com.nborba.vocalize.feature.recorder.impl.navigation.RecorderRoute
import com.nborba.vocalize.feature.recorder.impl.ui.recorder.compose.RecorderBottomSheet

fun NavGraphBuilder.recorderNavGraph(onDismissRequest: () -> Unit) {
    navigation<RecorderGraphRoute>(
        startDestination = RecorderRoute,
    ) {
        dialog<RecorderRoute>(
            deepLinks =
                listOf(
                    navDeepLink<RecorderRoute>(basePath = "https://vocalize.app/recorder"),
                    navDeepLink<RecorderRoute>(basePath = "vocalize://recorder"),
                ),
        ) {
            RecorderBottomSheet(
                onDismissRequest = onDismissRequest,
            )
        }
    }
}
