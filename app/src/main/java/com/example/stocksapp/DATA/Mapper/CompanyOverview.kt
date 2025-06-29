package com.example.stocksapp.DATA.Mapper

import com.example.stocksapp.DATA.DTOs.CompanyOverviewDto
import com.example.stocksapp.Domain.Model.Company

fun CompanyOverviewDto.toDomain(): Company {
    return Company(
        symbol = symbol.orEmpty(),
        name = name.orEmpty(),
        description = description.orEmpty(),
        sector = sector.orEmpty(),
        marketCap = marketCap.orEmpty(),
        peRatio = peRatio.orEmpty(),
        dividendYield = dividendYield.orEmpty()
    )
}
