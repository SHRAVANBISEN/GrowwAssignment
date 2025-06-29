package com.example.stocksapp.DATA.DTOs

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CompanyOverviewDto(
    @Json(name = "Symbol") val symbol: String?,
    @Json(name = "Name") val name: String?,
    @Json(name = "Description") val description: String?,
    @Json(name = "Sector") val sector: String?,
    @Json(name = "MarketCapitalization") val marketCap: String?,
    @Json(name = "PERatio") val peRatio: String?,
    @Json(name = "DividendYield") val dividendYield: String?
)
