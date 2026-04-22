package com.rider.tv.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.rider.tv.ui.theme.GlowBlue
import com.rider.tv.ui.theme.RiderBlue

@Composable
fun MovieCard(
    title: String,
    imageUrl: String?,
    onFocus: () -> Unit = {},
    onClick: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isFocused) 1.1f else 1.0f)

    Box(
        modifier = Modifier
            .padding(8.dp)
            .width(160.dp)
            .height(240.dp)
            .scale(scale)
            .onFocusChanged { 
                isFocused = it.isFocused 
                if (it.isFocused) onFocus()
            }
            .clickable { onClick() }
            .shadow(
                elevation = if (isFocused) 20.dp else 4.dp,
                shape = RoundedCornerShape(12.dp),
                ambientColor = if (isFocused) GlowBlue else Color.Transparent,
                spotColor = if (isFocused) GlowBlue else Color.Transparent
            )
            .border(
                width = if (isFocused) 3.dp else 0.dp,
                color = if (isFocused) RiderBlue else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
            .background(Color.DarkGray)
            .focusable()
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Overlay for Title
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                        startY = 300f
                    )
                ),
            contentAlignment = Alignment.BottomCenter
        ) {
            Text(
                text = title,
                modifier = Modifier.padding(8.dp),
                color = Color.White,
                maxLines = 2
            )
        }
    }
}
