package com.example.stocksapp.Domain.Model

import java.time.LocalDateTime

data class PricePoint(
    val timestamp: LocalDateTime,
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double,
    val volume: Long? = null
)


enum class TimeRange(val displayName: String, val apiInterval: String?) {
    ONE_DAY("1D", "5min"),
    ONE_WEEK("1W", null),
    ONE_MONTH("1M", null),
    ONE_YEAR("1Y", null),
    FIVE_YEARS("5Y", null),
    ALL("ALL", null)
}