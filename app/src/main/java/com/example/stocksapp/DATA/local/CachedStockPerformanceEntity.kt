package com.example.stocksapp.DATA.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stock_performance")
data class CachedStockPerformanceEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val ticker: String,
    val price: Double,
    val changeAmount: Double,
    val changePercentage: Double,
    val volume: Long,
    val isGainer: Boolean,
    val isLoser: Boolean,
    val cachedAt: Long // timestamp in millis
)
