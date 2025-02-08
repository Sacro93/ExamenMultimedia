package com.example.examenmultimedia.Model.Utils


import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File

/**
 * Crea un archivo de video en un directorio seguro y devuelve su URI.
 */
fun createVideoFileUri(context: Context): Uri {
    // Directorio seguro dentro de la carpeta de videos de la app
    val videoDir = File(context.getExternalFilesDir(Environment.DIRECTORY_MOVIES), "MyVideos")
    if (!videoDir.exists()) {
        videoDir.mkdirs() // ✅ Asegura que la carpeta existe
    }

    // Nombre de archivo único
    val videoFile = File(videoDir, "video_${System.currentTimeMillis()}.mp4")

    // Devuelve la URI utilizando FileProvider
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider", // Asegúrate de que coincide con AndroidManifest.xml
        videoFile
    )
}
