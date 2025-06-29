package com.example.stocksapp.DATA.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StockPerformanceDao {

    @Query("SELECT * FROM stock_performance WHERE isGainer = 1")
    suspend fun getTopGainers(): List<CachedStockPerformanceEntity>

    @Query("SELECT * FROM stock_performance WHERE isLoser = 1")
    suspend fun getTopLosers(): List<CachedStockPerformanceEntity>

    @Query("DELETE FROM stock_performance")
    suspend fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(stocks: List<CachedStockPerformanceEntity>)
}
