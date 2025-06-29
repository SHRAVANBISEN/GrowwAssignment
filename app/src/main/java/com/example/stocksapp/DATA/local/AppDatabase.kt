package com.example.stocksapp.DATA.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.stocksapp.DATA.local.WatchList.WatchlistDao

import com.example.stocksapp.DATA.local.WatchList.WatchlistEntity
import com.example.stocksapp.DATA.local.WatchList.WatchlistStockEntity

@Database(
    entities = [
        CachedStockPerformanceEntity::class,
        WatchlistEntity::class,
        WatchlistStockEntity::class
    ],
    version = 3
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun stockPerformanceDao(): StockPerformanceDao
    abstract fun watchlistDao(): WatchlistDao
}


