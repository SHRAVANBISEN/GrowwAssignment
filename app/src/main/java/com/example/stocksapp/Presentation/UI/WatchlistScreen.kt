package com.example.stocksapp.Presentation.UI

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.stocksapp.Presentation.ViewModels.WatchlistViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.ui.text.font.FontWeight

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WatchlistScreen(
    onWatchlistClick: (Int, String) -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: WatchlistViewModel = hiltViewModel()
) {
    val watchlists by viewModel.watchlists.collectAsState()
    Scaffold(
        bottomBar = {
            BottomBar(
                onHomeClick = onNavigateToHome,
                onWatchlistClick = { }
            )
        }
    )
    {
        innerPadding->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp)
        ) {

            Text(
                text = "Your Watchlists",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(vertical = 24.dp),
                color = MaterialTheme.colorScheme.onSurface
            )


            if (watchlists.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No watchlists created yet",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    items(watchlists) { watchlist ->
                        WatchlistCard(
                            watchlist = watchlist,
                            onClick = { onWatchlistClick(watchlist.id, watchlist.name) }
                        )
                    }
                }
            }
        }
    }

}