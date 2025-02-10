package com.example.examenmultimedia.ViewModel.VM.VideoCapture
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class CaptureViewModel : ViewModel() {

    fun saveMediaFile(context: Context, uri: Uri, fileName: String, mimeType: String, directory: String) {
        viewModelScope.launch {
            try {
                val resolver = context.contentResolver
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName) // ✅ Nombre elegido por el usuario
                    put(MediaStore.MediaColumns.MIME_TYPE, mimeType) // ✅ Tipo de archivo (video/audio)
                    put(MediaStore.MediaColumns.RELATIVE_PATH, directory) // ✅ Carpeta destino
                }

                val newUri = resolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues)
                newUri?.let { outputUri ->
                    resolver.openOutputStream(outputUri)?.use { output ->
                        resolver.openInputStream(uri)?.use { input ->
                            input.copyTo(output) // ✅ Copia el archivo correctamente
                        }
                    }
                    Toast.makeText(context, "Archivo guardado en $directory", Toast.LENGTH_SHORT).show()
                } ?: run {
                    Toast.makeText(context, "Error al guardar archivo", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
