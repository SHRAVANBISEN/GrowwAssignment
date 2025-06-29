package com.example.stocksapp.Presentation.UI

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.stocksapp.Domain.Model.StockPerformance

@Composable
fun StockPerformanceCard(
    stock: StockPerformance,
    onClick: () -> Unit,
    isGridLayout: Boolean = false
) {
    Card(
        modifier = Modifier
            .then(
                if (isGridLayout) {
                    Modifier.fillMaxWidth()
                } else {
                    Modifier
                        .padding(end = 8.dp)
                        .width(180.dp)
                }
            )
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = stock.ticker,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Price: $${stock.price}",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Change: ${stock.changePercentage}%",
                color = if (stock.changePercentage >= 0) Color(0xFF4CAF50) else Color(0xFFE53E3E),
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "Volume: ${formatVolume(stock.volume)}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


private fun formatVolume(volume: Long): String {
    return when {
        volume >= 1_000_000 -> "${(volume / 1_000_000.0).format(1)}M"
        volume >= 1_000 -> "${(volume / 1_000.0).format(1)}K"
        else -> volume.toString()
    }
}

private fun Double.format(digits: Int) = "%.${digits}f".format(this)