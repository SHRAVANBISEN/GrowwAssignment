package com.example.stocksapp.Presentation.ViewModels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stocksapp.Domain.Model.*
import com.example.stocksapp.Domain.Repository.StockRepository
import com.example.stocksapp.Domain.Repository.StockRepositoryImpl
import com.example.stocksapp.Domain.Repository.WatchlistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repo: StockRepository,
    private val watchlistRepo: WatchlistRepository
) : ViewModel() {

    private val _companyState = MutableStateFlow<Company?>(null)
    val companyState: StateFlow<Company?> = _companyState

    private val _pricePoints = MutableStateFlow<List<PricePoint>>(emptyList())
    val pricePoints: StateFlow<List<PricePoint>> = _pricePoints

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _selectedTimeRange = MutableStateFlow(TimeRange.ONE_DAY)
    val selectedTimeRange: StateFlow<TimeRange> = _selectedTimeRange

    private val _isChartLoading = MutableStateFlow(false)
    val isChartLoading: StateFlow<Boolean> = _isChartLoading

    var isDialogVisible by mutableStateOf(false)
        private set

    var watchlists by mutableStateOf<List<Watchlist>>(emptyList())
        private set

    var selectedIds by mutableStateOf<List<Int>>(emptyList())
        private set

    private var currentSymbol: String = ""

    // Cache for different time ranges to avoid repeated API calls
    private val priceDataCache = mutableMapOf<Pair<String, TimeRange>, List<PricePoint>>()

    fun updateDialogVisibility(visible: Boolean) {
        isDialogVisible = visible
        if (visible && currentSymbol.isNotEmpty()) {
            loadWatchlistData()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadStockDetails(symbol: String) {
        currentSymbol = symbol
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {

                val overview = repo.getCompanyOverview(symbol)
                _companyState.value = overview


                loadPriceDataForTimeRange(TimeRange.ONE_DAY)

            } catch (e: Exception) {
                _error.value = e.localizedMessage
            } finally {
                _isLoading.value = false
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onTimeRangeSelected(timeRange: TimeRange) {
        if (_selectedTimeRange.value == timeRange) return

        _selectedTimeRange.value = timeRange
        loadPriceDataForTimeRange(timeRange)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadPriceDataForTimeRange(timeRange: TimeRange) {
        viewModelScope.launch {
            _isChartLoading.value = true
            _error.value = null

            try {
                val cacheKey = Pair(currentSymbol, timeRange)

                // Check cache first
                val cachedData = priceDataCache[cacheKey]
                if (cachedData != null) {
                    _pricePoints.value = cachedData
                    _isChartLoading.value = false
                    return@launch
                }


                val repoImpl = repo as StockRepositoryImpl
                val prices = repoImpl.getPricesForTimeRange(currentSymbol, timeRange)

                // Cache the data
                priceDataCache[cacheKey] = prices
                _pricePoints.value = prices

            } catch (e: Exception) {
                _error.value = e.localizedMessage
            } finally {
                _isChartLoading.value = false
            }
        }
    }

    private fun loadWatchlistData() {
        viewModelScope.launch {
            try {
                watchlists = watchlistRepo.getAllWatchlists()
                selectedIds =
                    watchlistRepo.getWatchlistsContainingStock(currentSymbol).map { it.watchlistId }
            } catch (e: Exception) {

            }
        }
    }

    fun onToggleWatchlist(watchlistId: Int, shouldAdd: Boolean, symbol: String) {
        viewModelScope.launch {
            try {
                if (shouldAdd) {
                    watchlistRepo.addStockToWatchlist(watchlistId, symbol)
                    selectedIds = selectedIds + watchlistId
                } else {
                    watchlistRepo.removeStockFromWatchlist(watchlistId, symbol)
                    selectedIds = selectedIds - watchlistId
                }
            } catch (e: Exception) {
                loadWatchlistData()
            }
        }
    }

    fun createWatchlist(name: String) {
        viewModelScope.launch {
            try {
                val created = watchlistRepo.createWatchlist(name)
                if (created) {
                    loadWatchlistData()
                }
            } catch (e: Exception) {

            }
        }
    }


    fun getCurrentPriceInfo(): PriceChangeInfo? {
        val prices = _pricePoints.value
        if (prices.isEmpty()) return null

        val currentPrice = prices.last().close
        val previousPrice = when (_selectedTimeRange.value) {
            TimeRange.ONE_DAY -> {

                if (prices.size > 1) prices.first().close else currentPrice
            }

            TimeRange.ONE_WEEK -> {

                if (prices.size > 7) prices[prices.size - 8].close
                else if (prices.size > 1) prices.first().close
                else currentPrice
            }

            TimeRange.ONE_MONTH -> {

                if (prices.size > 30) prices[prices.size - 31].close
                else if (prices.size > 1) prices.first().close
                else currentPrice
            }

            else -> {

                if (prices.size > 1) prices[prices.size - 2].close else currentPrice
            }
        }

        val priceChange = currentPrice - previousPrice
        val percentChange = if (previousPrice != 0.0) (priceChange / previousPrice) * 100 else 0.0

        return PriceChangeInfo(
            currentPrice = currentPrice,
            priceChange = priceChange,
            percentChange = percentChange,
            isPositive = priceChange >= 0
        )
    }

    data class PriceChangeInfo(
        val currentPrice: Double,
        val priceChange: Double,
        val percentChange: Double,
        val isPositive: Boolean
    )
}