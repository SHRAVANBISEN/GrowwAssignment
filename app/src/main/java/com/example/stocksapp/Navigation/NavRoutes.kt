package com.example.stocksapp.Navigation

sealed class NavRoutes(val route: String) {
    object Explore : NavRoutes("explore")
    object Watchlist : NavRoutes("watchlist")
    object Product : NavRoutes("product/{symbol}") {
        fun withArgs(symbol: String) = "product/$symbol"
    }

    object ViewAllGainers : NavRoutes("viewall_gainers")
    object ViewAllLosers : NavRoutes("viewall_losers")
    object WatchlistDetail : NavRoutes("watchlist_detail/{id}/{name}") {

    }
}