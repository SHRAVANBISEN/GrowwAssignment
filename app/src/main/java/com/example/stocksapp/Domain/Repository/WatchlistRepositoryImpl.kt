package com.example.stocksapp.Domain.Repository

import com.example.stocksapp.DATA.Mapper.toDomain
import com.example.stocksapp.DATA.Mapper.toEntity
import com.example.stocksapp.DATA.local.WatchList.WatchlistDao
import com.example.stocksapp.DATA.local.WatchList.WatchlistEntity
import com.example.stocksapp.DATA.local.WatchList.WatchlistStockEntity
import com.example.stocksapp.Domain.Model.Watchlist
import com.example.stocksapp.Domain.Model.WatchlistItem
import javax.inject.Inject

class WatchlistRepositoryImpl @Inject constructor(
    private val dao: WatchlistDao
) : WatchlistRepository {

    override suspend fun createWatchlist(name: String): Boolean {
        val id = dao.insertWatchlist(WatchlistEntity(name = name))
        return id != -1L
    }

    override suspend fun getAllWatchlists(): List<Watchlist> {
        return dao.getAllWatchlistss().map { it.toDomain() }
    }

    override suspend fun getWatchlistById(id: Int): Watchlist? {
        return dao.getWatchlistById(id)?.toDomain()
    }

    override suspend fun deleteWatchlist(watchlist: Watchlist) {
        dao.deleteWatchlist(watchlist.toEntity())
    }

    override suspend fun addStockToWatchlist(watchlistId: Int, symbol: String) {
        dao.addStockToWatchlist(
            WatchlistStockEntity(watchlistId = watchlistId, symbol = symbol)
        )
    }

    override suspend fun removeStockFromWatchlist(watchlistId: Int, symbol: String) {
        dao.removeStockFromWatchlist(watchlistId, symbol)
    }

    override suspend fun getSymbolsInWatchlist(watchlistId: Int): List<String> {
        return dao.getSymbolsInWatchlist(watchlistId)
    }

    override suspend fun getWatchlistsContainingStock(symbol: String): List<WatchlistItem> {
        return dao.getWatchlistsContainingStock(symbol).map { it.toDomain() }
    }
}
