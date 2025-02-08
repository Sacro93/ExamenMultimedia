package com.example.examenmultimedia.View.HomScreen



import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Examen Multimedia") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = { navController.navigate("mainScreen") }) {
                Text("📂 Acceder a Videos y Audios")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate("captureScreen") }) {
                Text("🎥 Capturar Audio/Video")
            }
        }
    }
}
