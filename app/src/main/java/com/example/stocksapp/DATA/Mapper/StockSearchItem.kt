package com.example.stocksapp.DATA.Mapper

import com.example.stocksapp.DATA.DTOs.StockSearchItemDto
import com.example.stocksapp.Domain.Model.SearchResult

fun StockSearchItemDto.toDomain(): SearchResult {
    return SearchResult(
        symbol = symbol.orEmpty(),
        name = name.orEmpty(),
        region = region.orEmpty(),
        score = score?.toDoubleOrNull() ?: 0.0
    )
}
