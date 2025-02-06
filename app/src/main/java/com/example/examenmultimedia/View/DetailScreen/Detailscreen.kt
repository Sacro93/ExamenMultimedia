package com.example.examenmultimedia.View.DetailScreen

import MediaViewModel
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    uri: String,
    title: String,
    navController: NavController,
    viewModel: MediaViewModel
) {
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(Uri.parse(uri))
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            // Usamos StyledPlayerView en lugar de PlayerView para que tenga controles nativos
            AndroidView(
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        player = exoPlayer
                        useController = true // Activa los controles
                    }
                },
                modifier = Modifier.fillMaxWidth().height(300.dp)
            )

        }
    }

    DisposableEffect(Unit) {
        onDispose { exoPlayer.release() }
    }
}
