@file:Suppress("UsingMaterialAndMaterial3Libraries")

package com.nborba.vocalize.ui

import androidx.compose.foundation.background
import androidx.compose.material.navigation.ModalBottomSheetLayout
import androidx.compose.material.navigation.rememberBottomSheetNavigator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.nborba.vocalize.feature.recorder.api.navigation.RecorderGraphRoute
import com.nborba.vocalize.feature.recorder.impl.ui.recorderNavGraph
import com.nborba.vocalize.navigation.DetailRoute
import com.nborba.vocalize.navigation.HomeRoute
import com.nborba.vocalize.ui.detail.compose.DetailScreen
import com.nborba.vocalize.ui.home.compose.HomeScreen

@Composable
internal fun VocalizeApp() {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberNavController(bottomSheetNavigator)

    ModalBottomSheetLayout(bottomSheetNavigator) {
        NavHost(
            navController = navController,
            startDestination = HomeRoute,
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
        ) {
            composable<HomeRoute>(
                deepLinks =
                    listOf(
                        navDeepLink<HomeRoute>(basePath = "https://vocalize.app"),
                        navDeepLink<HomeRoute>(basePath = "vocalize://app"),
                    ),
            ) {
                HomeScreen(
                    onNavigateToDetail = { id ->
                        navController.navigate(DetailRoute(id))
                    },
                    onNavigateToRecorder = { navController.navigate(RecorderGraphRoute) },
                )
            }

            composable<DetailRoute>(
                deepLinks =
                    listOf(
                        navDeepLink<DetailRoute>(basePath = "https://vocalize.app/detail"),
                        navDeepLink<DetailRoute>(basePath = "vocalize://detail"),
                    ),
            ) { backStackEntry ->
                val route: DetailRoute = backStackEntry.toRoute()
                DetailScreen(
                    id = route.id,
                    onUpClick = navController::navigateUp,
                    onBackClick = navController::popBackStack,
                )
            }

            recorderNavGraph()
        }
    }
}
