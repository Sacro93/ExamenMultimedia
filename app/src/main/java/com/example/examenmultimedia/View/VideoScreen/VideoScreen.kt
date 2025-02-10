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
    val hasSelectedSource = remember { mutableStateOf(false) } // Estado para controlar si se seleccionó "Videos" o "Audios"

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Selecciona una opción") })
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            if (!hasSelectedSource.value) {
                // Muestra los botones si no se seleccionó ninguna fuente
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Button(onClick = {
                        hasSelectedSource.value = true
                        viewModel.setSource(MediaSource.DEVICE) // Cambia a videos
                    }) {
                        Text("📽️ Videos")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {
                        hasSelectedSource.value = true
                        viewModel.setSource(MediaSource.RAW) // Cambia a audios
                    }) {
                        Text("🎵 Audios")
                    }
                }
            } else {
                // Muestra la lista de videos o audios
                when {
                    isLoading -> {
                        CircularProgressIndicator() // ⏳ Loader mientras se cargan los datos
                    }
                    mediaFiles.isEmpty() -> {
                        Text("📂 No hay archivos disponibles") // Muestra mensaje si no hay datos
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
