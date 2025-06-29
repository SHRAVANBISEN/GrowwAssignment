package com.example.stocksapp.Domain.Model

data class SearchResult(
    val symbol: String,
    val name: String,
    val region: String,
    val score: Double
)
