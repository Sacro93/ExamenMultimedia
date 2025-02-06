package com.example.examenmultimedia.View.MainScreen


import MediaViewModel
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController


/*---------hiltViewModel(): Obtiene automáticamente el MediaViewModel sin necesidad de instanciarlo manualmente.----

¿Qué hace este código?

Usa LazyColumn para mostrar la lista de archivos multimedia.
Cada archivo tiene un clickable para iniciar la reproducción

Ahora, cuando un usuario toca un archivo, lo llevamos a la pantalla de detalles.

Usa navController.navigate(...) para abrir DetailScreen con el URI y el título.
Se usa Uri.encode() para evitar errores en los nombres de archivo con espacios o caracteres especiales.
---*/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController, viewModel: MediaViewModel = hiltViewModel()) {
    val mediaFiles by viewModel.mediaFiles.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Videos del Dispositivo") })
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier.fillMaxSize()
        ) {
            items(mediaFiles) { media ->
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
        }
    }
}

