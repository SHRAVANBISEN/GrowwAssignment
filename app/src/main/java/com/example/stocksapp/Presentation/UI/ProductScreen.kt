package com.example.stocksapp.Presentation.UI

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.stocksapp.Domain.Model.PricePoint
import com.example.stocksapp.Domain.Model.TimeRange
import com.example.stocksapp.Presentation.ViewModels.ProductViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.abs

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(
    symbol: String,
    viewModel: ProductViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val company by viewModel.companyState.collectAsState()
    val pricePoints by viewModel.pricePoints.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isChartLoading by viewModel.isChartLoading.collectAsState() // ADD THIS

    val error by viewModel.error.collectAsState()

    val selectedTimeRange by viewModel.selectedTimeRange.collectAsState() // ADD THIS

    LaunchedEffect(symbol) {
        viewModel.loadStockDetails(symbol)
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.surface,
                        CircleShape
                    )
            ) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Row {
                IconButton(
                    onClick = { /* History */ },
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.surface,
                            CircleShape
                        )
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "History",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = { viewModel.updateDialogVisibility(true) },
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.surface,
                            CircleShape
                        )
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Bookmark",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = { /* Search */ },
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.surface,
                            CircleShape
                        )
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Error: $error",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            company != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(horizontal = 16.dp)
                ) {
                    // Company Logo and Info
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .background(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                    RoundedCornerShape(12.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = symbol.take(2),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = company!!.name,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = company!!.sector,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    if (pricePoints.isNotEmpty()) {
                        val priceInfo = viewModel.getCurrentPriceInfo()

                        if (priceInfo != null) {
                            Text(
                                text = " $ ${String.format("%.2f", priceInfo.currentPrice)}",
                                style = MaterialTheme.typography.displaySmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "${if (priceInfo.isPositive) "+" else ""}${String.format("%.2f", priceInfo.priceChange)} (${String.format("%.2f", priceInfo.percentChange)}%)",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = if (priceInfo.isPositive) Color(0xFF00C853) else Color(0xFFFF5252),
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = selectedTimeRange.displayName,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Box {
                        EnhancedPriceChart(
                            pricePoints = pricePoints,
                            timeRange = selectedTimeRange,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(280.dp)
                        )


                        if (isChartLoading) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(280.dp)
                                    .background(
                                        MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                                        RoundedCornerShape(16.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(40.dp),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    TimeRangeSelector(
                        selectedRange = selectedTimeRange,
                        onRangeSelected = { timeRange ->
                            viewModel.onTimeRangeSelected(timeRange)
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))


                    CompanyDescriptionCard(description = company!!.description)

                    Spacer(modifier = Modifier.height(24.dp))


                    TabRow(selectedRange = selectedTimeRange.toString())

                    Spacer(modifier = Modifier.height(16.dp))


                    PerformanceCard(pricePoints = pricePoints)

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }


        if (viewModel.isDialogVisible) {
            AddToWatchlistDialog(
                stockSymbol = symbol,
                watchlists = viewModel.watchlists,
                selectedWatchlistIds = viewModel.selectedIds,
                onDismiss = { viewModel.updateDialogVisibility(false) },
                onToggleWatchlist = { watchlistId, shouldAdd ->
                    viewModel.onToggleWatchlist(watchlistId, shouldAdd, symbol)
                },
                onCreateWatchlist = viewModel::createWatchlist
            )
        }
    }
}

@Composable
fun CompanyDescriptionCard(description: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Info,
                    contentDescription = "About",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "About Company",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = if (description.isNotBlank()) description else "No description available for this company.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                lineHeight = 20.sp
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EnhancedPriceChart(
    pricePoints: List<PricePoint>,
    timeRange: TimeRange,
    modifier: Modifier = Modifier
) {
    if (pricePoints.isEmpty()) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text(
                "No chart data available",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        }
        return
    }

    val graphColor = Color(0xFFFF5722)
    val fillColor = graphColor.copy(alpha = 0.1f)

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            val chartWidth = size.width - 60.dp.toPx()
            val chartHeight = size.height - 40.dp.toPx()
            val chartLeft = 50.dp.toPx()
            val chartTop = 10.dp.toPx()

            val maxPrice = pricePoints.maxOf { it.close }
            val minPrice = pricePoints.minOf { it.close }
            val priceRange = if (maxPrice != minPrice) maxPrice - minPrice else 1.0
            val spacePerPoint = chartWidth / (pricePoints.size - 1).coerceAtLeast(1)


            val gridLines = 5
            for (i in 0..gridLines) {
                val y = chartTop + (chartHeight * i / gridLines)
                drawLine(
                    color = Color.Gray.copy(alpha = 0.2f),
                    start = androidx.compose.ui.geometry.Offset(chartLeft, y),
                    end = androidx.compose.ui.geometry.Offset(chartLeft + chartWidth, y),
                    strokeWidth = 0.5.dp.toPx()
                )


                val price = maxPrice - (priceRange * i / gridLines)
                drawContext.canvas.nativeCanvas.drawText(
                    "$${String.format("%.0f", price)}",
                    10f,
                    y + 5.dp.toPx(),
                    android.graphics.Paint().apply {
                        color = android.graphics.Color.GRAY
                        textSize = 24f
                        isAntiAlias = true
                    }
                )
            }


            val path = Path()
            val fillPath = Path()

            pricePoints.forEachIndexed { index, point ->
                val x = chartLeft + (index * spacePerPoint)
                val yRatio = (point.close - minPrice).toFloat() / priceRange.toFloat()
                val y = chartTop + chartHeight - (yRatio * chartHeight)

                if (index == 0) {
                    path.moveTo(x, y)
                    fillPath.moveTo(x, chartTop + chartHeight)
                    fillPath.lineTo(x, y)
                } else {
                    path.lineTo(x, y)
                    fillPath.lineTo(x, y)
                }
            }


            fillPath.lineTo(chartLeft + chartWidth, chartTop + chartHeight)
            fillPath.close()


            drawPath(
                path = fillPath,
                color = fillColor
            )


            drawPath(
                path = path,
                color = graphColor,
                style = Stroke(
                    width = 3.dp.toPx(),
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )


            val labelCount = 4
            for (i in 0..labelCount) {
                val index = (pricePoints.size - 1) * i / labelCount
                if (index < pricePoints.size) {
                    val x = chartLeft + (index * spacePerPoint)
                    val timeLabel = formatTimeLabel(pricePoints[index].timestamp, timeRange)

                    drawContext.canvas.nativeCanvas.drawText(
                        timeLabel,
                        x - 20.dp.toPx(),
                        size.height - 5.dp.toPx(),
                        android.graphics.Paint().apply {
                            color = android.graphics.Color.GRAY
                            textSize = 20f
                            isAntiAlias = true
                        }
                    )
                }
            }
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
fun formatTimeLabel(timestamp: LocalDateTime, timeRange: TimeRange): String {
    val formatter = when (timeRange) {
        TimeRange.ONE_DAY -> DateTimeFormatter.ofPattern("HH:mm")
        TimeRange.ONE_WEEK, TimeRange.ONE_MONTH -> DateTimeFormatter.ofPattern("MM/dd")
        TimeRange.ONE_YEAR, TimeRange.FIVE_YEARS -> DateTimeFormatter.ofPattern("MMM yy")
        TimeRange.ALL -> DateTimeFormatter.ofPattern("yyyy")
    }
    return timestamp.format(formatter)
}
@Composable
fun TimeRangeSelector(
    selectedRange: TimeRange,
    onRangeSelected: (TimeRange) -> Unit
) {
    val timeRanges = TimeRange.values()

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        item {

        }

        items(timeRanges) { range ->
            val isSelected = range == selectedRange

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        else Color.Transparent
                    )
                    .clickable { onRangeSelected(range) }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = range.displayName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isSelected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }

        item {
            Icon(
                Icons.Default.Info,
                contentDescription = "Chart",
                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp)
            )
        }
    }
}
@Composable
fun TabRow(selectedRange: String) {
    val tabs = listOf("Overview", "News", "Events")
    var selectedTab by remember { mutableStateOf(0) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        tabs.forEachIndexed { index, tab ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable { selectedTab = index }
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = tab,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (selectedTab == index) MaterialTheme.colorScheme.onBackground
                    else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                )

                if (selectedTab == index) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(3.dp)
                            .background(
                                MaterialTheme.colorScheme.primary,
                                RoundedCornerShape(2.dp)
                            )
                    )
                }
            }
        }
    }
}

@Composable
fun PerformanceCard(pricePoints: List<PricePoint>) {
    if (pricePoints.isEmpty()) return

    val currentPrice = pricePoints.last().close
    val maxPrice = pricePoints.maxOf { it.close }
    val minPrice = pricePoints.minOf { it.close }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Performance",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    Icons.Default.Info,
                    contentDescription = "Info",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                PerformanceItem(
                    label = "52 Week Low",
                    value = "₹${String.format("%.2f", minPrice)}"
                )
                PerformanceItem(
                    label = "Current price",
                    value = "₹${String.format("%.2f", currentPrice)}"
                )
                PerformanceItem(
                    label = "52 Week High",
                    value = "₹${String.format("%.2f", maxPrice)}"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MetricItem(label = "Market Cap", value = "₹1.50 Lac Cr")
                MetricItem(label = "Book Value", value = "₹48.50")
                MetricItem(label = "Dividend Yield", value = "0.45%")
                MetricItem(label = "Profit Margin", value = "8.39%")
            }
        }
    }
}

@Composable
fun PerformanceItem(
    label: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun MetricItem(
    label: String,
    value: String
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}




