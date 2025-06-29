package com.example.stocksapp.DATA.local.WatchList

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index



@Entity(
    tableName = "watchlist_stocks",
    primaryKeys = ["watchlistId", "symbol"],
    foreignKeys = [
        ForeignKey(
            entity = WatchlistEntity::class,
            parentColumns = ["id"],
            childColumns = ["watchlistId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["watchlistId"]),
        Index(value = ["symbol"])
    ]
)
data class WatchlistStockEntity(
    val watchlistId: Int,
    val symbol: String,
    val addedAt: Long = System.currentTimeMillis()
)
