package com.example.stocksapp.DATA.DTOs

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchResultDto(
    @Json(name = "bestMatches")
    val matches: List<StockSearchItemDto>?
)

@JsonClass(generateAdapter = true)
data class StockSearchItemDto(
    @Json(name = "1. symbol") val symbol: String?,
    @Json(name = "2. name") val name: String?,
    @Json(name = "4. region") val region: String?,
    @Json(name = "9. matchScore") val score: String?
)
