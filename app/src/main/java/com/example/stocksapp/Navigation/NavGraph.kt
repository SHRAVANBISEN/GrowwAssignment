package com.example.stocksapp.Navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.stocksapp.Presentation.UI.ExploreScreen
import com.example.stocksapp.Presentation.UI.ProductScreen
import com.example.stocksapp.Presentation.UI.ViewAllGainersScreen
import com.example.stocksapp.Presentation.UI.ViewAllLosersScreen
import com.example.stocksapp.Presentation.UI.WatchlistDetailScreen
import com.example.stocksapp.Presentation.UI.WatchlistScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = NavRoutes.Explore.route) {

        composable(NavRoutes.Explore.route) {
            ExploreScreen(
                onStockClick = { symbol ->
                    navController.navigate(NavRoutes.Product.withArgs(symbol))
                },
                onNavigateToWatchlist = {
                    navController.navigate(NavRoutes.Watchlist.route)
                },
                onNavigateToAllGainers = {
                    navController.navigate(NavRoutes.ViewAllGainers.route)
                },
                onNavigateToAllLosers = {
                    navController.navigate(NavRoutes.ViewAllLosers.route)
                }
            )
        }

        composable(NavRoutes.ViewAllGainers.route) {
            ViewAllGainersScreen(
                onBackClick = { navController.popBackStack() },
                onStockClick = { symbol ->
                    navController.navigate(NavRoutes.Product.withArgs(symbol))
                }
            )
        }

        composable(NavRoutes.ViewAllLosers.route) {
            ViewAllLosersScreen(
                onBackClick = { navController.popBackStack() },
                onStockClick = { symbol ->
                    navController.navigate(NavRoutes.Product.withArgs(symbol))
                }
            )
        }

        composable(NavRoutes.Watchlist.route) {
            WatchlistScreen(
                onWatchlistClick = { id, name ->
                    navController.navigate("watchlist_detail/$id/$name")
                },
                onNavigateToHome = {
                    navController.navigate(NavRoutes.Explore.route) {
                        popUpTo(NavRoutes.Explore.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = NavRoutes.Product.route,
            arguments = listOf(navArgument("symbol") { type = NavType.StringType })
        ) { backStackEntry ->
            val symbol = backStackEntry.arguments?.getString("symbol") ?: return@composable

            ProductScreen(
                symbol = symbol,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = NavRoutes.WatchlistDetail.route,
            arguments = listOf(
                navArgument("id") { type = NavType.IntType },
                navArgument("name") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            val name = backStackEntry.arguments?.getString("name") ?: "Watchlist"

            WatchlistDetailScreen(
                watchlistId = id,
                watchlistName = name,
                onStockClick = { symbol ->
                    navController.navigate(NavRoutes.Product.withArgs(symbol))
                },
                onNavigateToHome = {
                    navController.navigate(NavRoutes.Explore.route) {
                        popUpTo(NavRoutes.Explore.route) { inclusive = true }
                    }
                },
                onNavigateToWatchlist = {
                    navController.popBackStack()
                }
            )
        }


    }
}