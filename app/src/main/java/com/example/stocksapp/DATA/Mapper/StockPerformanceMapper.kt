package com.example.stocksapp.DATA.Mapper

import com.example.stocksapp.DATA.local.CachedStockPerformanceEntity
import com.example.stocksapp.Domain.Model.StockPerformance

fun StockPerformance.toEntity(cachedAt: Long): CachedStockPerformanceEntity =
    CachedStockPerformanceEntity(id=0,
        ticker, price, changeAmount, changePercentage, volume, isGainer, isLoser, cachedAt
    )

fun CachedStockPerformanceEntity.toDomain(): StockPerformance =
    StockPerformance(ticker, price, changeAmount, changePercentage, volume, isGainer, isLoser)
