package com.example.stocksapp.DATA.DTOs

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TimeSeriesDto(

    @Json(name = "Time Series (Daily)")
    val dailySeries: Map<String, StockPricePoint>? = null,


    @Json(name = "Time Series (5min)")
    val intradaySeries5min: Map<String, StockPricePoint>? = null,


    @Json(name = "Weekly Time Series")
    val weeklySeries: Map<String, StockPricePoint>? = null,


    @Json(name = "Monthly Time Series")
    val monthlySeries: Map<String, StockPricePoint>? = null,


    @Json(name = "Meta Data")
    val metaData: MetaData? = null
)

@JsonClass(generateAdapter = true)
data class MetaData(
    @Json(name = "1. Information") val information: String?,
    @Json(name = "2. Symbol") val symbol: String?,
    @Json(name = "3. Last Refreshed") val lastRefreshed: String?,
    @Json(name = "4. Time Zone") val timeZone: String?
)

@JsonClass(generateAdapter = true)
data class StockPricePoint(
    @Json(name = "1. open") val open: String?,
    @Json(name = "2. high") val high: String?,
    @Json(name = "3. low") val low: String?,
    @Json(name = "4. close") val close: String?,
    @Json(name = "5. volume") val volume: String?
)