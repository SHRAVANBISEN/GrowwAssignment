package com.example.stocksapp.Domain.Repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.stocksapp.DATA.Mapper.*
import com.example.stocksapp.DATA.API.StockApi
import com.example.stocksapp.DATA.local.StockPerformanceDao
import com.example.stocksapp.Domain.Model.*
import java.time.LocalDateTime

class StockRepositoryImpl(
    private val api: StockApi,
    private val stockDao: StockPerformanceDao
) : StockRepository {

    override suspend fun searchStocks(query: String): List<SearchResult> {
        val response = api.searchStocks(keywords = query)
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!.matches?.map { it.toDomain() } ?: emptyList()
        } else {
            throw Exception(response.errorBody()?.string() ?: "Search failed")
        }
    }

    override suspend fun getCompanyOverview(symbol: String): Company {
        val response = api.getCompanyOverview(symbol = symbol)
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!.toDomain()
        } else {
            throw Exception(response.errorBody()?.string() ?: "Failed to fetch company overview")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getIntradayPrices(symbol: String, interval: String): List<PricePoint> {
        val response = api.getTimeSeriesIntraday(symbol = symbol, interval = interval)
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!.toDomainIntraday()
        } else {
            throw Exception(response.errorBody()?.string() ?: "Failed to fetch intraday data")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getDailyPrices(symbol: String): List<PricePoint> {
        val response = api.getTimeSeriesDaily(symbol = symbol)
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!.toDomainDaily()
        } else {
            throw Exception(response.errorBody()?.string() ?: "Failed to fetch daily data")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getWeeklyPrices(symbol: String): List<PricePoint> {
        val response = api.getTimeSeriesWeekly(symbol = symbol)
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!.toDomainWeekly()
        } else {
            throw Exception(response.errorBody()?.string() ?: "Failed to fetch weekly data")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getMonthlyPrices(symbol: String): List<PricePoint> {
        val response = api.getTimeSeriesMonthly(symbol = symbol)
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!.toDomainMonthly()
        } else {
            throw Exception(response.errorBody()?.string() ?: "Failed to fetch monthly data")
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getPricesForTimeRange(symbol: String, timeRange: TimeRange): List<PricePoint> {
        return when (timeRange) {
            TimeRange.ONE_DAY -> {
                getIntradayPrices(symbol, "5min")
            }
            TimeRange.ONE_WEEK -> {
                getDailyPrices(symbol).takeLast(7)
            }
            TimeRange.ONE_MONTH -> {
                getDailyPrices(symbol).takeLast(30)
            }
            TimeRange.ONE_YEAR -> {
                getWeeklyPrices(symbol).takeLast(52)
            }
            TimeRange.FIVE_YEARS -> {
                getMonthlyPrices(symbol).takeLast(60)
            }
            TimeRange.ALL -> {
                getMonthlyPrices(symbol)
            }
        }
    }

    override suspend fun getTopGainersAndLosers(): Result<Pair<List<StockPerformance>, List<StockPerformance>>> {
        val cacheTimeLimit = 30 * 60 * 1000L
        val now = System.currentTimeMillis()

        val rawGainers = stockDao.getTopGainers()
        val rawLosers = stockDao.getTopLosers()
        val cachedGainers = rawGainers.map { it.toDomain() }
        val cachedLosers = rawLosers.map { it.toDomain() }

        val isCacheValid = rawGainers.isNotEmpty() &&
                (now - rawGainers.first().cachedAt < cacheTimeLimit)

        if (isCacheValid) {
            return Result.success(Pair(cachedGainers, cachedLosers))
        }

        return try {
            val response = api.getTopGainersLosers()
            if (response.isSuccessful && response.body() != null) {
                val data = response.body()!!
                val gainers = data.topGainers.map { it.toDomain(isGainer = true) }
                val losers = data.topLosers.map { it.toDomain(isLoser = true) }

                val all = (gainers + losers).map { it.toEntity(now) }
                stockDao.clearAll()
                stockDao.insertAll(all)

                Result.success(Pair(gainers, losers))
            } else {
                Result.failure(Exception("API Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            if (cachedGainers.isNotEmpty()) {
                Result.success(Pair(cachedGainers, cachedLosers))
            } else {
                Result.failure(e)
            }
        }
    }

    override suspend fun getTopGainers(): Result<List<StockPerformance>> {
        return getTopGainersAndLosers().map { it.first }
    }

    override suspend fun getTopLosers(): Result<List<StockPerformance>> {
        return getTopGainersAndLosers().map { it.second }
    }
}