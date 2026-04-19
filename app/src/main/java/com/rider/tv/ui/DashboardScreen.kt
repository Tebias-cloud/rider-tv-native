package com.rider.tv.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.foundation.lazy.list.items
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.rider.tv.ui.components.MovieCard
import com.rider.tv.ui.theme.RiderBlack
import com.rider.tv.viewmodel.IptvViewModel

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun DashboardScreen(viewModel: IptvViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    var backgroundUri by remember { mutableStateOf<String?>(null) }

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
                alpha = 0.3f // Natural dimming
            )
        }
        
        // Gradient overlay for readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(RiderBlack.copy(alpha = 0.7f), RiderBlack)
                    )
                )
        )

        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text("Cargando Rider TV Nativo...", color = Color.White)
            }
        } else {
            TvLazyColumn(
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
                            
                            TvLazyRow(
                                horizontalArrangement = Arrangement.spacedBy(20.dp),
                                contentPadding = PaddingValues(horizontal = 4.dp)
                            ) {
                                items(streamsInCategory) { stream ->
                                    MovieCard(
                                        title = stream.name,
                                        imageUrl = stream.streamIcon,
                                        onClick = { /* Play Stream Logic */ }
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
