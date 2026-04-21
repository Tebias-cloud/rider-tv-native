package com.rider.tv.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.collectAsState
import coil.compose.AsyncImage
import com.rider.tv.ui.components.MovieCard
import com.rider.tv.ui.theme.RiderBlack
import com.rider.tv.viewmodel.MovieViewModel

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun DashboardScreen(viewModel: MovieViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    var backgroundUri by remember { mutableStateOf<String?>(null) }
    var activeStreamUrl by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(RiderBlack)
    ) {
        // Dynamic Blur Background Layer
        Crossfade(targetState = backgroundUri) { uri ->
            AsyncImage(
                model = uri,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.3f
            )
        }
        
        // Gradient overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(RiderBlack.copy(alpha = 0.7f), RiderBlack)
                    )
                )
        )

        when {
            uiState.isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    CircularProgressIndicator(color = Color.Red)
                }
            }
            uiState.error != null -> {
                Box(Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    Text("Error: ${uiState.error}", color = Color.Red)
                }
            }
            else -> {
                LazyColumn(
                    contentPadding = PaddingValues(top = 48.dp, start = 32.dp, end = 32.dp, bottom = 32.dp),
                    verticalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    items(uiState.categories) { category ->
                        val streamsInCategory = uiState.streams.filter { it.categoryId == category.id }
                        
                        if (streamsInCategory.isNotEmpty()) {
                            Column {
                                Text(
                                    text = category.name,
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(bottom = 12.dp)
                                )
                                
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                                    contentPadding = PaddingValues(horizontal = 4.dp)
                                ) {
                                    items(streamsInCategory) { stream ->
                                        MovieCard(
                                            title = stream.name,
                                            imageUrl = stream.streamIcon,
                                            onFocus = { backgroundUri = stream.streamIcon },
                                            onClick = { 
                                                // Construct the final HLS/TS URL
                                                activeStreamUrl = "http://api.movieplatform.pro/live/user/pass/${stream.streamId}.ts"
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Overlay player if active
    if (activeStreamUrl != null) {
        PlayerScreen(
            url = activeStreamUrl!!,
            onBack = { activeStreamUrl = null }
        )
    }
}
