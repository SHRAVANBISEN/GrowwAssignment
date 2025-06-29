package com.example.stocksapp.Presentation.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stocksapp.Domain.Repository.StockRepository
import com.example.stocksapp.Domain.Model.SearchResult
import com.example.stocksapp.Domain.Model.StockPerformance
import com.example.stocksapp.Utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val repository: StockRepository
) : ViewModel() {



    private val _gainersState = MutableStateFlow<UiState<List<StockPerformance>>>(UiState.Loading)
    val gainersState: StateFlow<UiState<List<StockPerformance>>> = _gainersState.asStateFlow()

    private val _losersState = MutableStateFlow<UiState<List<StockPerformance>>>(UiState.Loading)
    val losersState: StateFlow<UiState<List<StockPerformance>>> = _losersState.asStateFlow()

    init {
        loadGainersAndLosers()
    }


    fun retry() {
        loadGainersAndLosers()
    }

    private fun loadGainersAndLosers() {
        viewModelScope.launch {
            _gainersState.value = UiState.Loading
            _losersState.value = UiState.Loading
            repository.getTopGainersAndLosers().fold(
                onSuccess = { (gainers, losers) ->
                    _gainersState.value = UiState.Success(gainers)
                    _losersState.value = UiState.Success(losers)
                },
                onFailure = { e ->
                    _gainersState.value = UiState.Error(e.message ?: "Error loading gainers")
                    _losersState.value = UiState.Error(e.message ?: "Error loading losers")
                }
            )
        }
    }
}

