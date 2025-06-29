package com.example.stocksapp.DATA.Mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.stocksapp.DATA.DTOs.TimeSeriesDto
import com.example.stocksapp.DATA.DTOs.StockPricePoint
import com.example.stocksapp.Domain.Model.PricePoint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun TimeSeriesDto.toDomainDaily(): List<PricePoint> {
    return dailySeries?.mapNotNull { (dateStr, point) ->
        point.toPricePoint(dateStr, "yyyy-MM-dd")
    }?.sortedBy { it.timestamp } ?: emptyList()
}

@RequiresApi(Build.VERSION_CODES.O)
fun TimeSeriesDto.toDomainIntraday(): List<PricePoint> {
    return intradaySeries5min?.mapNotNull { (dateTimeStr, point) ->
        point.toPricePoint(dateTimeStr, "yyyy-MM-dd HH:mm:ss")
    }?.sortedBy { it.timestamp } ?: emptyList()
}

@RequiresApi(Build.VERSION_CODES.O)
fun TimeSeriesDto.toDomainWeekly(): List<PricePoint> {
    return weeklySeries?.mapNotNull { (dateStr, point) ->
        point.toPricePoint(dateStr, "yyyy-MM-dd")
    }?.sortedBy { it.timestamp } ?: emptyList()
}

@RequiresApi(Build.VERSION_CODES.O)
fun TimeSeriesDto.toDomainMonthly(): List<PricePoint> {
    return monthlySeries?.mapNotNull { (dateStr, point) ->
        point.toPricePoint(dateStr, "yyyy-MM-dd")
    }?.sortedBy { it.timestamp } ?: emptyList()
}

@RequiresApi(Build.VERSION_CODES.O)
private fun StockPricePoint.toPricePoint(dateTimeStr: String, pattern: String): PricePoint? {
    return try {
        val timestamp = LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(pattern))
        val open = open?.toDoubleOrNull() ?: return null
        val high = high?.toDoubleOrNull() ?: return null
        val low = low?.toDoubleOrNull() ?: return null
        val close = close?.toDoubleOrNull() ?: return null
        val vol = volume?.toLongOrNull()

        PricePoint(
            timestamp = timestamp,
            open = open,
            high = high,
            low = low,
            close = close,
            volume = vol
        )
    } catch (e: Exception) {
        null
    }
}