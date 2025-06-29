package com.example.stocksapp.DI

import com.example.stocksapp.DATA.API.StockApi
import com.example.stocksapp.DATA.local.StockPerformanceDao
import com.example.stocksapp.DATA.local.WatchList.WatchlistDao
import com.example.stocksapp.Domain.Repository.StockRepository
import com.example.stocksapp.Domain.Repository.StockRepositoryImpl
import com.example.stocksapp.Domain.Repository.WatchlistRepository
import com.example.stocksapp.Domain.Repository.WatchlistRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideStockRepository(
        api: StockApi,
        stockDao: StockPerformanceDao
    ): StockRepository {
        return StockRepositoryImpl(api, stockDao)
    }

    @Provides
    @Singleton
    fun provideWatchlistRepository(
        dao: WatchlistDao
    ): WatchlistRepository = WatchlistRepositoryImpl(dao)

}

