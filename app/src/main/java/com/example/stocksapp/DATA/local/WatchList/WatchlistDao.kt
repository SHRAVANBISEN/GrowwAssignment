package com.example.stocksapp.DATA.local.WatchList

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WatchlistDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWatchlist(watchlist: WatchlistEntity): Long

    @Query("SELECT * FROM watchlists")
    suspend fun getAllWatchlistss(): List<WatchlistEntity>

    @Query("SELECT * FROM watchlists WHERE id = :id")
    suspend fun getWatchlistById(id: Int): WatchlistEntity?

    @Delete
    suspend fun deleteWatchlist(watchlist: WatchlistEntity)

    // Stocks
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addStockToWatchlist(stock: WatchlistStockEntity)


    @Query("DELETE FROM watchlist_stocks WHERE watchlistId = :watchlistId AND symbol = :symbol")
    suspend fun removeStockFromWatchlist(watchlistId: Int, symbol: String)

    @Query("SELECT symbol FROM watchlist_stocks WHERE watchlistId = :watchlistId")
    suspend fun getSymbolsInWatchlist(watchlistId: Int): List<String>

    @Query("SELECT * FROM watchlist_stocks WHERE symbol = :symbol")
    suspend fun getWatchlistsContainingStock(symbol: String): List<WatchlistStockEntity>
}
