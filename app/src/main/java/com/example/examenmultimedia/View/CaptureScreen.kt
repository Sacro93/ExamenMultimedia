package com.example.examenmultimedia.View

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import com.example.examenmultimedia.Model.Utils.createVideoFileUri
import com.example.examenmultimedia.ViewModel.Media.CaptureViewModel
import java.io.File



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaptureScreen(
    navController: NavController?,
    viewModel: CaptureViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val context = LocalContext.current
    val videoUri = remember { mutableStateOf<Uri?>(null) }

    val videoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.CaptureVideo()) { success ->
        if (success) { // ✅ Si el video se grabó correctamente
            videoUri.value?.let { uri ->
                showFileNameDialog(
                    context = context,
                    uri = uri,
                    mimeType = "video/mp4",
                    directory = Environment.DIRECTORY_MOVIES,
                    viewModel = viewModel
                )
            }
        } else {
            Toast.makeText(context, "Error al capturar el video", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Capturar Audio/Video") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = {
                val uri = createVideoFileUri(context) // ✅ Genera URI segura antes de grabar
                videoUri.value = uri
                videoLauncher.launch(uri)
            }) {
                Text("🎥 Grabar Video")
            }
        }
    }
}


