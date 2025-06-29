package com.example.stocksapp.Domain.Repository

import com.example.stocksapp.Domain.Model.Company
import com.example.stocksapp.Domain.Model.PricePoint
import com.example.stocksapp.Domain.Model.SearchResult
import com.example.stocksapp.Domain.Model.StockPerformance

interface StockRepository {
    suspend fun searchStocks(query: String): List<SearchResult>
    suspend fun getCompanyOverview(symbol: String): Company


    suspend fun getIntradayPrices(symbol: String, interval: String = "5min"): List<PricePoint>
    suspend fun getDailyPrices(symbol: String): List<PricePoint>
    suspend fun getWeeklyPrices(symbol: String): List<PricePoint>
    suspend fun getMonthlyPrices(symbol: String): List<PricePoint>

    suspend fun getTopGainersAndLosers(): Result<Pair<List<StockPerformance>, List<StockPerformance>>>
    suspend fun getTopGainers(): Result<List<StockPerformance>>
    suspend fun getTopLosers(): Result<List<StockPerformance>>
}