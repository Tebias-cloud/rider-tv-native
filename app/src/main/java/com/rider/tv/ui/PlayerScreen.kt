package com.rider.tv.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.*
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.ui.PlayerView
import androidx.activity.compose.BackHandler
import androidx.media3.common.AudioAttributes

@OptIn(UnstableApi::class)
@Composable
fun PlayerScreen(url: String, onBack: () -> Unit) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    var retryCount by remember { mutableIntStateOf(0) }

    // Audio configuration for best quality and focus
    val audioAttributes = AudioAttributes.Builder()
        .setUsage(C.USAGE_MEDIA)
        .setContentType(C.CONTENT_TYPE_MOVIE)
        .build()

    // Optimized buffering to handle micro-cuts in IPTV
    val loadControl = DefaultLoadControl.Builder()
        .setBufferDurationsMs(
            30_000, // Min buffer
            60_000, // Max buffer
            2_500,  // Buffer for playback
            5_000   // Buffer for rebuffering
        )
        .build()

    val dataSourceFactory = remember {
        DefaultHttpDataSource.Factory()
            .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
            .setAllowCrossProtocolRedirects(true)
    }

    val exoPlayer = remember(url, retryCount) {
        ExoPlayer.Builder(context)
            .setMediaSourceFactory(DefaultMediaSourceFactory(dataSourceFactory))
            .setAudioAttributes(audioAttributes, true) // Handles audio focus automatically
            .setLoadControl(loadControl)
            .build()
            .apply {
                setWakeMode(C.WAKE_MODE_NETWORK) // Keep network alive
                setMediaItem(MediaItem.fromUri(url))
                prepare()
                playWhenReady = true
                
                addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        isLoading = (playbackState == Player.STATE_BUFFERING)
                        if (playbackState == Player.STATE_READY) {
                            errorMsg = null
                        }
                    }

                    override fun onPlayerError(error: PlaybackException) {
                        errorMsg = "Error de señal: ${error.localizedMessage}"
                        isLoading = false
                    }
                })
            }
    }

    BackHandler {
        onBack()
    }

    DisposableEffect(exoPlayer) {
        onDispose {
            exoPlayer.release()
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                    useController = true
                    keepScreenOn = true // Vital for TV and Long sessions
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        AnimatedVisibility(
            visible = isLoading,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            CircularProgressIndicator(color = Color.Red, strokeWidth = 5.dp)
        }

        if (errorMsg != null) {
            Box(
                modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.9f)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "⚠️ SEÑAL NO DISPONIBLE",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = errorMsg!!,
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { 
                            errorMsg = null
                            isLoading = true
                            retryCount++ 
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                    ) {
                        Text("REINTENTAR CONEXIÓN", color = Color.White, style = MaterialTheme.typography.labelLarge)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    TextButton(onClick = onBack) {
                        Text("VOLVER AL INICIO", color = Color.Gray)
                    }
                }
            }
        }
    }
}
