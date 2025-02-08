package com.example.examenmultimedia.View.VideoScreen


import com.example.examenmultimedia.ViewModel.VM.Media.MediaViewModel
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.examenmultimedia.Model.Utils.MediaFile
import com.example.examenmultimedia.Model.Utils.MediaSource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoScreen(navController: NavController, viewModel: MediaViewModel) {
    val mediaFiles by viewModel.mediaFiles.collectAsState() // Lista de videos/audios
    val currentSource by viewModel.source.collectAsState() // Fuente actual (Videos/Audios)
    val isLoading by viewModel.isLoading.collectAsState() // Estado de carga

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (currentSource == MediaSource.DEVICE) "📽️ Videos" else "🎵 Audios") },
                actions = {
                    Row {
                        TextButton(onClick = {
                            if (currentSource != MediaSource.DEVICE) {
                                viewModel.setSource(MediaSource.DEVICE) // Cambia a videos
                            }
                        }) {
                            Text("Videos")
                        }
                        TextButton(onClick = {
                            if (currentSource != MediaSource.RAW) {
                                viewModel.setSource(MediaSource.RAW) // Cambia a audios
                            }
                        }) {
                            Text("Audios")
                        }
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
            when {
                isLoading -> {
                    CircularProgressIndicator() // ⏳ Muestra un loader mientras se cargan los datos
                }
                mediaFiles.isEmpty() -> {
                    Text("📂 No hay archivos disponibles") // Muestra un mensaje si no hay datos
                }
                else -> {
                    LazyColumn {
                        items(mediaFiles) { media ->
                            MediaItem(navController, media)
                        }
                    }
                }
            }
        }
    }
}

/**
 * Componente reutilizable para mostrar un video o audio
 */
@Composable
fun MediaItem(navController: NavController, media: MediaFile) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                val encodedUri = Uri.encode(media.uri.toString())
                navController.navigate("detailScreen/$encodedUri/${media.title}")
            }
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = media.title, style = MaterialTheme.typography.titleMedium)
                Text(text = "${media.duration / 1000} seg", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
