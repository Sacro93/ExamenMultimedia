package com.example.examenmultimedia.View.Capture

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.example.examenmultimedia.Model.Utils.AudioRecorder
import com.example.examenmultimedia.Model.Utils.createVideoFileUri
import com.example.examenmultimedia.View.Name.showFileNameDialog
import com.example.examenmultimedia.ViewModel.VM.VideoCapture.CaptureViewModel
import java.io.File


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaptureScreen(
    navController: NavController?,
    viewModel: CaptureViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val context = LocalContext.current
    val audioRecorder = remember { AudioRecorder() }
    val isRecording = remember { mutableStateOf(false) }
    val videoUri = remember { mutableStateOf<Uri?>(null) }

    // Lanzador de permisos
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (!isGranted) {
            Toast.makeText(context, "Permiso para grabar audio denegado", Toast.LENGTH_SHORT).show()
        }
    }

    // Solicita el permiso al iniciar
    LaunchedEffect(Unit) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    // Lanzador para capturar videos
    val videoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.CaptureVideo()) { success ->
        if (success) {
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
            // Botón para grabar video
            Button(onClick = {
                val uri = createVideoFileUri(context)
                videoUri.value = uri
                videoLauncher.launch(uri)
            }) {
                Text("🎥 Grabar Video")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para grabar audio
            Button(onClick = {
                if (!isRecording.value) {
                    // Crear archivo de audio en la carpeta de Música
                    val audioFile = File(
                        context.getExternalFilesDir(Environment.DIRECTORY_MUSIC),
                        "audio_${System.currentTimeMillis()}.mp4"
                    )

                    // Inicia la grabación
                    audioRecorder.startRecording(audioFile)
                    isRecording.value = true
                } else {
                    // Detiene la grabación
                    audioRecorder.stopRecording()?.let { file ->
                        Toast.makeText(context, "Audio guardado en: ${file.absolutePath}", Toast.LENGTH_SHORT).show()
                    }
                    isRecording.value = false
                }
            }) {
                Text(if (isRecording.value) "⏹️ Detener Audio" else "🎙️ Grabar Audio")
            }
        }
    }
}

