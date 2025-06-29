package com.example.stocksapp.DI

import android.content.Context
import androidx.room.Room
import com.example.stocksapp.DATA.local.AppDatabase
import com.example.stocksapp.DATA.local.StockPerformanceDao
import com.example.stocksapp.DATA.local.WatchList.WatchlistDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "stocks_db")
            .fallbackToDestructiveMigration()
            .build()
    @Provides
    fun provideStockPerformanceDao(db: AppDatabase): StockPerformanceDao =
        db.stockPerformanceDao()

    @Provides
    fun provideWatchlistDao(db: AppDatabase): WatchlistDao =
        db.watchlistDao()

}
