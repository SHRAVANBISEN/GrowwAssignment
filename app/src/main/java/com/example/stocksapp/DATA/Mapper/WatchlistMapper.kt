package com.example.stocksapp.DATA.Mapper

import com.example.stocksapp.DATA.local.WatchList.WatchlistEntity
import com.example.stocksapp.Domain.Model.Watchlist

fun WatchlistEntity.toDomain(): Watchlist =
    Watchlist(id = id, name = name)

fun Watchlist.toEntity(): WatchlistEntity =
    WatchlistEntity(id = id, name = name)
