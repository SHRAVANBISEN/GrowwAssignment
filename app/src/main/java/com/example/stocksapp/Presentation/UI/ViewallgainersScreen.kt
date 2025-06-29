package com.example.stocksapp.Presentation.UI

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.stocksapp.Domain.Model.StockPerformance
import com.example.stocksapp.Presentation.ViewModels.ExploreViewModel
import com.example.stocksapp.Utils.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewAllGainersScreen(
    onBackClick: () -> Unit,
    onStockClick: (String) -> Unit,
    viewModel: ExploreViewModel = hiltViewModel()
) {
    val gainersState by viewModel.gainersState.collectAsState()
    var currentPage by remember { mutableIntStateOf(1) }
    val itemsPerPage = 10
    val gridState = rememberLazyGridState()


    LaunchedEffect(currentPage) {
        gridState.animateScrollToItem(0)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Top Gainers",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { innerPadding ->
        when (gainersState) {
            is UiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CircularProgressIndicator()
                        Text(
                            text = "Loading top gainers...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            is UiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Failed to load gainers",
                            color = Color.Red,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = (gainersState as UiState.Error).message,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Button(
                            onClick = { viewModel.retry() }
                        ) {
                            Text("Retry")
                        }
                    }
                }
            }

            is UiState.Empty -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "📈",
                            style = MaterialTheme.typography.displayMedium
                        )
                        Text(
                            text = "No gainers found",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "Check back later for market updates",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            is UiState.Success -> {
                val allStocks = (gainersState as UiState.Success<List<StockPerformance>>).data
                if (allStocks.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No gainers data available",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                } else {
                    val totalPages = (allStocks.size + itemsPerPage - 1) / itemsPerPage
                    val startIndex = (currentPage - 1) * itemsPerPage
                    val endIndex = minOf(startIndex + itemsPerPage, allStocks.size)
                    val currentPageStocks = allStocks.subList(startIndex, endIndex)

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (totalPages > 1) {
                                Text(
                                    text = "Page $currentPage of $totalPages",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Text(
                                text = "${allStocks.size} stocks",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Medium
                            )
                        }


                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(minSize = 160.dp),
                            state = gridState,
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(
                                items = currentPageStocks,
                                key = { it.ticker }
                            ) { stock ->
                                StockPerformanceCard(
                                    stock = stock,
                                    onClick = { onStockClick(stock.ticker) },
                                    isGridLayout = true
                                )
                            }
                        }


                        if (totalPages > 1) {
                            PaginationControls(
                                currentPage = currentPage,
                                totalPages = totalPages,
                                onPageChange = { newPage ->
                                    currentPage = newPage
                                },
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PaginationControls(
    currentPage: Int,
    totalPages: Int,
    onPageChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedButton(
            onClick = { onPageChange(currentPage - 1) },
            enabled = currentPage > 1
        ) {
            Text("Previous")
        }

        // Page numbers display
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            val startPage = maxOf(1, currentPage - 2)
            val endPage = minOf(totalPages, currentPage + 2)

            if (startPage > 1) {
                PageButton(
                    pageNumber = 1,
                    isSelected = currentPage == 1,
                    onClick = { onPageChange(1) }
                )
                if (startPage > 2) {
                    Text(
                        text = "...",
                        modifier = Modifier.padding(horizontal = 4.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            for (pageNum in startPage..endPage) {
                PageButton(
                    pageNumber = pageNum,
                    isSelected = currentPage == pageNum,
                    onClick = { onPageChange(pageNum) }
                )
            }

            if (endPage < totalPages) {
                if (endPage < totalPages - 1) {
                    Text(
                        text = "...",
                        modifier = Modifier.padding(horizontal = 4.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                PageButton(
                    pageNumber = totalPages,
                    isSelected = currentPage == totalPages,
                    onClick = { onPageChange(totalPages) }
                )
            }
        }

        OutlinedButton(
            onClick = { onPageChange(currentPage + 1) },
            enabled = currentPage < totalPages
        ) {
            Text("Next")
        }
    }
}

@Composable
private fun PageButton(
    pageNumber: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    if (isSelected) {
        Button(
            onClick = onClick,
            modifier = Modifier.width(48.dp)
        ) {
            Text(
                text = pageNumber.toString(),
                fontWeight = FontWeight.Bold
            )
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            modifier = Modifier.width(48.dp)
        ) {
            Text(pageNumber.toString())
        }
    }
}