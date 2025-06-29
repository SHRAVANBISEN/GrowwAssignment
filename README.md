# Groww Android SDE Intern Assessment

**Candidate:** Shravan Bisen  
**Assignment:** Android Stocks/ETFs Broking Platform  
**Tech Stack:** Kotlin, Jetpack Compose, MVVM, Room, Retrofit, Hilt, Alpha Vantage API

---

## 🔗 Submission Links

* **GitHub Repository:** [Your Repository Link Here]
* **LinkedIn Profile:** [Your LinkedIn Profile Link Here]

---

## 📱 Screenshots

### Main Features Overview
<div align="center">
  <img src="screenshots/ss1.png" alt="Explore Screen" width="200"/>
  <img src="screenshots/product_screen.png" alt="Product Screen" width="200"/>
  <img src="screenshots/watchlist_screen.png" alt="Watchlist Screen" width="200"/>
  <img src="screenshots/view_all_screen.png" alt="View All Screen" width="200"/>
</div>

---

## 🗂️ Project Structure Overview

```
com.example.stocksapp/
├── data/
│   ├── api/StockApi.kt
│   ├── local/
│   │   ├── WatchList/WatchlistDao.kt
│   │   └── StockPerformanceDao.kt
│   └── dto/
│       ├── CompanyOverviewDto.kt
│       ├── SearchResultDto.kt
│       └── TimeSeriesDto.kt
│
├── di/
│   ├── DatabaseModule.kt
│   ├── NetworkModule.kt
│   └── RepositoryModule.kt
│
├── domain/
│   ├── model/ (e.g., StockPerformance.kt, Company.kt)
│   └── repository/ (interfaces)
│
├── presentation/
│   ├── ui/ (screens, composables)
│   └── viewmodels/
│       ├── ExploreViewModel.kt
│       └── ProductViewModel.kt
│
├── utils/
│   └── UiState.kt
```

---

## 🧩 Architecture Components and Dependency Explanation

### 📦 DTOs (Data Transfer Objects)

DTOs are models that represent raw data received from the Alpha Vantage API.

* Example: `CompanyOverviewDto`, `TimeSeriesDto`
* Purpose: Keep external API data separate from internal domain models.

### 🔄 Mappers

Used to convert DTOs into domain models (`.toDomain()`) and entities for Room (`.toEntity()`).

* Ensures UI and business logic never directly depend on raw API structures.

### 🧱 Dependency Injection (DI)

Managed using **Hilt** to keep code modular and testable.

* **DatabaseModule.kt**: Provides Room database and DAOs.
* **NetworkModule.kt**: Provides Retrofit and API instance.
* **RepositoryModule.kt**: Binds repository interfaces to implementations.

### 🧮 DAOs

Database Access Objects to fetch/store Room database data.

* Example: `WatchlistDao.kt`, `StockPerformanceDao.kt`

### 🔄 Example Flow

```
ExploreViewModel → StockRepositoryImpl → StockApi (Retrofit)
                                     ↘ Room (StockPerformanceDao)
```

---

## ✅ Feature-Wise Implementation & Code Mapping

### 1. 🔍 Explore Screen (Top Gainers & Losers)

* **Functionality:** Displays top gaining and losing stocks in two horizontal carousels.
* **Tech Used:** Alpha Vantage `TOP_GAINERS_LOSERS` API, Room caching, Jetpack Compose
* **States Handled:** Loading, Empty, Error, Success
* **Code Path:**
  * `ExploreViewModel.kt` – Business logic
  * `ExploreScreen.kt` – UI rendering
  * `StockRepositoryImpl.kt` – API + cache handling
  * `StockApi.kt` – Retrofit interface
  * `StockPerformanceCard.kt` – Reusable stock cards

### 2. ⭐ Watchlist Screen

* **Functionality:** Lists all user-created watchlists; empty state supported
* **Tech Used:** Room database, Compose UI, ViewModel
* **Code Path:**
  * `WatchlistScreen.kt`
  * `WatchlistDao.kt`, `WatchlistEntity.kt`, `WatchlistRepositoryImpl.kt`
  * `WatchlistCard.kt`

### 3. 📈 Product Screen (Stock Detail + Graph)

* **Functionality:** Displays detailed stock information with interactive price charts, company overview, and performance metrics across multiple time ranges.
* **Tech Used:** Alpha Vantage Time Series APIs, Canvas API for custom charts, Room caching, Jetpack Compose
* **States Handled:** Loading, Chart Loading, Empty Data, Error, Success
* **Code Path:**
  * `ProductViewModel.kt` – Business logic & state management
  * `ProductScreen.kt` – UI rendering & chart display
  * `StockRepositoryImpl.kt` – Multi-timeframe API + cache handling
  * `StockApi.kt` – Retrofit interface for time series data
  * `EnhancedPriceChart.kt` – Custom Canvas-based chart component

## Core Implementation

### Data Fetching Logic

```kotlin
// Smart data selection based on time range
suspend fun getPricesForTimeRange(symbol: String, timeRange: TimeRange): List<PricePoint> {
    return when (timeRange) {
        TimeRange.ONE_DAY -> getIntradayPrices(symbol, "5min")
        TimeRange.ONE_WEEK -> getDailyPrices(symbol).takeLast(7)
        TimeRange.ONE_MONTH -> getDailyPrices(symbol).takeLast(30)
        // ... other ranges
    }
}
```

**Data Sources:**
* Intraday (5min intervals) for 1D
* Daily prices for 1W/1M
* Weekly data for 1Y
* Monthly data for 5Y/All

### Graph Implementation

```kotlin
@Composable
fun EnhancedPriceChart(pricePoints: List<PricePoint>) {
    Canvas(modifier) {
        // Calculate scaling
        val maxPrice = pricePoints.maxOf { it.close }
        val minPrice = pricePoints.minOf { it.close }
        
        // Draw grid lines
        // Create price line path
        // Fill area under curve
        drawPath(fillPath, color = fillColor)
        drawPath(linePath, color = lineColor)
    }
}
```

**Graph Features:**
* Auto-scaling to price range
* Grid lines with price labels
* Fill gradient under line
* Time-based X-axis labels
* Responsive to time range changes

### Caching Strategy

```kotlin
// 30-minute cache for top gainers/losers
val cacheTimeLimit = 30 * 60 * 1000L
val isCacheValid = cachedData.isNotEmpty() && 
    (now - cachedData.first().cachedAt < cacheTimeLimit)
```

### Time Range Handling

```kotlin
enum class TimeRange { ONE_DAY("1D"), ONE_WEEK("1W"), ONE_MONTH("1M")... }

// Context-aware time formatting
fun formatTimeLabel(timestamp: LocalDateTime, timeRange: TimeRange): String {
    val formatter = when (timeRange) {
        ONE_DAY -> "HH:mm"
        ONE_WEEK -> "MM/dd" 
        ONE_YEAR -> "MMM yy"
    }
}
```

## Key Features

* **Real-time Price Updates** with % change indicators
* **Interactive Time Range Selector** (1D, 1W, 1M, 1Y, 5Y, All)
* **Performance Metrics** (52-week high/low, market cap)
* **Loading States** (main screen + chart overlay)
* **Error Handling** with graceful fallbacks
* **Material 3 Design** with proper theming

## Data Flow

```
Alpha Vantage API → Repository → ViewModel → UI State → Canvas Chart
                        ↓
                   Room Cache
```

### Performance Optimizations

* Canvas-based charts (better than library charts)
* Smart caching with expiration
* Lazy loading with pagination
* Offline support via Room
* State management with Compose

### 4. ➕ Add to Watchlist Popup

* **Functionality:** Modal popup to select or create watchlists; updates state instantly
* **Tech Used:** Compose `AlertDialog`, Room, ViewModel state sync
* **Code Path:**
  * `AddToWatchlistDialog.kt`
  * `ProductViewModel.kt -> onToggleWatchlist()`
  * `WatchlistRepositoryImpl.kt`

### 5. 📋 View All Screen (Paginated List)

* **Functionality:** Shows full list of stocks under a category (e.g., all gainers/losers) with client-side pagination.
* **Tech Used:** Jetpack Compose, `LazyVerticalGrid`, `UiState`, local state management.
* **Code Path:**
  * `ViewAllGainersScreen.kt`
  * `ExploreViewModel.kt -> getTopGainers()/getTopLosers()`
* **Highlights:**
  * Dynamic calculation of pages using item count
  * Paginated rendering using `subList(startIndex, endIndex)`
  * Smooth scroll-to-top effect on page switch
  * Ellipsis logic and highlighted page indicators in pagination bar

---

## 🌟 Additional Features

* 🟢 **Splash Screen** added to mimic Groww's launch screen ✨
* 🎯 **Custom App Icon** redesigned to match Groww branding
* 🎨 **Theme Switching** between Light & Dark Modes using Material3 color schemes
* 🧩 **Consistent Color Palette** applied throughout to maintain Groww's look and feel

---

## 🔐 API Keys & Alpha Vantage Integration

**Alpha Vantage API Free Tier Limitations:**
* 5 requests per minute
* 25 requests per day
* `TOP_GAINERS_LOSERS` costs 10 API calls!

### ⚠️ Why Some Data May Not Load:

Sometimes the graphs or data do not appear because the daily quota or per-minute rate limit has been exceeded. This is not a bug in the app but a known limitation of the Alpha Vantage free plan.

### 👇 Provided API Keys (for Testing)

Use any of the below keys in:
* `DATA/API/StockApi.kt`

```kotlin
object ApiKeyProvider {
    val ALPHA_API_KEY: String
        get() = "REPLACE_ME_WITH_ANY_VALID_KEY"
}
```

* `API_KEY_1`: YGAUA4QIJT79XKT3
* `API_KEY_2`: MV77X9L60L0W8CO6
* `API_KEY_3`: 3BU3IE38491KNRBF

### ✅ Handling in Code

* Cached Top Gainers & Losers in Room for 30 mins
* `try-catch` for API exceptions
* Empty state UI if chart or overview data fails

---

## 🛠️ Setup Instructions

### Prerequisites
* Android Studio Arctic Fox or later
* Kotlin 1.7+
* Minimum SDK 24

### Installation Steps

1. **Clone the repository**
   ```bash
   git clone [your-repository-url]
   cd groww-android-assessment
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an existing project"
   - Navigate to the cloned directory

3. **Add API Key**
   - Open `DATA/API/StockApi.kt`
   - Replace `REPLACE_ME_WITH_ANY_VALID_KEY` with one of the provided API keys

4. **Build and Run**
   - Sync project with Gradle files
   - Run the app on device/emulator

---

## 🧪 Testing

### Unit Tests
* Repository layer tests
* ViewModel state management tests
* Data mapping tests

### UI Tests
* Screen navigation tests
* User interaction tests
* State handling tests

Run tests with:
```bash
./gradlew test
./gradlew connectedAndroidTest
```

---

## 🚀 Key Technical Decisions

### Architecture Choice
- **MVVM with Clean Architecture**: Separates concerns and makes testing easier
- **Hilt for DI**: Reduces boilerplate and improves testability
- **Room for Caching**: Provides offline capability and improves performance

### UI Framework
- **Jetpack Compose**: Modern, declarative UI toolkit
- **Material 3**: Latest design system with dynamic theming
- **Custom Canvas Charts**: Better performance than library-based solutions

### Data Management
- **Repository Pattern**: Abstracts data sources from ViewModels
- **Smart Caching**: Balances data freshness with API limits
- **Error Handling**: Graceful degradation with user-friendly messages

---

## 🔄 Future Enhancements

- [ ] **Real-time WebSocket Updates** for live price feeds
- [ ] **Advanced Charting** with technical indicators
- [ ] **Portfolio Tracking** with P&L calculations
- [ ] **News Integration** for market updates
- [ ] **Notification System** for price alerts
- [ ] **Biometric Authentication** for security
- [ ] **Advanced Filtering** and search capabilities

---

## 🧠 Final Notes & Reflection

This assessment gave me valuable exposure to real-world architectural planning, API integration, and clean Compose-based UI development.

### 🚧 Challenges Faced

* Integrating **Hilt** and managing multi-layer DI for ViewModels and Repositories
* Building a **custom Compose graph** for line chart visualization (first time doing it from scratch)
* Handling **Alpha Vantage's strict rate limits** during development and testing

### 📚 Learning Outcomes

* Deep understanding of **Clean Architecture** principles
* Proficiency in **Jetpack Compose** for modern Android UI
* Experience with **Room Database** for local data persistence
* Knowledge of **API integration** and rate limiting strategies
* Skills in **custom Canvas drawing** for complex UI components

### 💬 Closing Note

This internship assessment has been a rewarding learning experience. While there may still be areas for improvement, I've ensured clean design, maintainable structure, and a strong willingness to grow and learn. I would be truly grateful if given the opportunity to grow further with the Groww engineering team.

Thank you for reviewing my solution. 🙏

---

## 📞 Contact

**Shravan Bisen**
- LinkedIn: [Your LinkedIn Profile]
- Email: [Your Email]
- GitHub: [Your GitHub Profile]

---

## 📄 License

This project is created for educational and assessment purposes.

---

*Made with ❤️ for Groww Android SDE Intern Assessment*
