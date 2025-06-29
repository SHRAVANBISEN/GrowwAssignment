package com.example.stocksapp.DATA.DTOs
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TopGainersLosersDto(
    @Json(name = "metadata")
    val metadata: String? = null,

    @Json(name = "last_updated")
    val lastUpdated: String? = null,

    @Json(name = "top_gainers")
    val topGainers: List<StockPerformanceDto> = emptyList(),

    @Json(name = "top_losers")
    val topLosers: List<StockPerformanceDto> = emptyList(),

    @Json(name = "most_actively_traded")
    val mostActivelyTraded: List<StockPerformanceDto> = emptyList()
)

@JsonClass(generateAdapter = true)
data class StockPerformanceDto(
    @Json(name = "ticker") val ticker: String,
    @Json(name = "price") val price: String,
    @Json(name = "change_amount") val changeAmount: String,
    @Json(name = "change_percentage") val changePercentage: String,
    @Json(name = "volume") val volume: String
)
