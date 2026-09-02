package com.nborba.vocalize.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.nborba.vocalize.navigation.DetailRoute
import com.nborba.vocalize.navigation.HomeRoute
import com.nborba.vocalize.ui.compose.detail.DetailScreen
import com.nborba.vocalize.ui.compose.home.HomeScreen
import com.nborba.vocalize.ui.theme.VocalizeTheme

@Composable
fun VocalizeApp(navController: NavHostController = rememberNavController()) {
    VocalizeTheme {
        NavHost(
            navController = navController,
            startDestination = HomeRoute,
        ) {
            // TODO: Set up actual feature module with inner nav graphs.
            //  For now, these screens are just placeholders
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
        }
    }
}
