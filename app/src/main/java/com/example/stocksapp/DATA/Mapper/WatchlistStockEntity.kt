package com.example.stocksapp.DATA.Mapper

import com.example.stocksapp.DATA.local.WatchList.WatchlistStockEntity
import com.example.stocksapp.Domain.Model.WatchlistItem

fun WatchlistStockEntity.toDomain(): WatchlistItem =
    WatchlistItem(watchlistId = watchlistId, symbol = symbol)

fun WatchlistItem.toEntity(): WatchlistStockEntity =
    WatchlistStockEntity(watchlistId = watchlistId, symbol = symbol)
