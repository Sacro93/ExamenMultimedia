package com.example.examenmultimedia.Model.Utils

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class MediaRepository @Inject constructor(@ApplicationContext private val context: Context) {

    //Obtener archivos multimedia desde el almacenamiento del dispositivo usando MediaStore.

    fun getMediaFilesFromMediaStore(): List<MediaFile> {
        val mediaList = mutableListOf<MediaFile>()

        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DURATION
        )

        // URI base de los videos en MediaStore.
        val uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI

        // Ordenamiento: Ordena los resultados por fecha de adición (descendente).
        val sortOrder = "${MediaStore.Video.Media.DATE_ADDED} DESC"

        // Ejecuta la consulta en MediaStore.
        val cursor = context.contentResolver.query(uri, projection, null, null, sortOrder)

        //Procesar los resultados.
        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val nameColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
            val durationColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)

            while (it.moveToNext()) {
                val id = it.getLong(idColumn)
                val name = it.getString(nameColumn)
                val duration = it.getLong(durationColumn)

                // Crear URI para el archivo.
                val contentUri = ContentUris.withAppendedId(uri, id)

                Log.d("MediaRepository", "Video encontrado: $name, URI: $contentUri")

                mediaList.add(MediaFile(contentUri, name, duration, null, ""))
            }
        }

        return mediaList
    }


    fun getAudioFilesFromMediaStore(): List<MediaFile> {
        val audioList = mutableListOf<MediaFile>()

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ARTIST
        )

        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val sortOrder = "${MediaStore.Audio.Media.DATE_ADDED} DESC"

        val cursor = context.contentResolver.query(uri, projection, null, null, sortOrder)

        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val nameColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
            val durationColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val artistColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)

            while (it.moveToNext()) {
                val id = it.getLong(idColumn)
                val name = it.getString(nameColumn)
                val duration = it.getLong(durationColumn)
                val artist = it.getString(artistColumn) ?: "Desconocido"

                val contentUri = ContentUris.withAppendedId(uri, id)
                audioList.add(MediaFile(contentUri, name, duration, null, artist))
            }
        }

        return audioList
    }

}


