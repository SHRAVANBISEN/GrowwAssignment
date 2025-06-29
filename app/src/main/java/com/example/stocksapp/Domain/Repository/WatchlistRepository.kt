package com.example.stocksapp.Domain.Repository

import com.example.stocksapp.Domain.Model.Watchlist
import com.example.stocksapp.Domain.Model.WatchlistItem

interface WatchlistRepository {

    suspend fun createWatchlist(name: String): Boolean

    suspend fun getAllWatchlists(): List<Watchlist>

    suspend fun getWatchlistById(id: Int): Watchlist?

    suspend fun deleteWatchlist(watchlist: Watchlist)

    suspend fun addStockToWatchlist(watchlistId: Int, symbol: String)

    suspend fun removeStockFromWatchlist(watchlistId: Int, symbol: String)

    suspend fun getSymbolsInWatchlist(watchlistId: Int): List<String>

    suspend fun getWatchlistsContainingStock(symbol: String): List<WatchlistItem>
}
