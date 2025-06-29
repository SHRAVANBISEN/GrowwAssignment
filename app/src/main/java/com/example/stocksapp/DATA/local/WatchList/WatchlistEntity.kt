package com.example.stocksapp.DATA.local.WatchList

import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "watchlists")
data class WatchlistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val createdAt: Long = System.currentTimeMillis()
)
