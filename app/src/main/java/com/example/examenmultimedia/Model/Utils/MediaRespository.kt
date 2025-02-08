package com.example.examenmultimedia.Model.Utils

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.example.examenmultimedia.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class MediaRepository @Inject constructor(@ApplicationContext private val context: Context) {

    /**
     * Obtener archivos multimedia desde el almacenamiento del dispositivo usando MediaStore.
     */
    fun getMediaFilesFromMediaStore(): List<MediaFile> {
        val mediaList = mutableListOf<MediaFile>()

        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DURATION
        )

        val uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val sortOrder = "${MediaStore.Video.Media.DATE_ADDED} DESC"

        val cursor = context.contentResolver.query(uri, projection, null, null, sortOrder)

        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val nameColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
            val durationColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)

            while (it.moveToNext()) {
                val id = it.getLong(idColumn)
                val name = it.getString(nameColumn)
                val duration = it.getLong(durationColumn)

                val contentUri = ContentUris.withAppendedId(uri, id)

                Log.d("MediaRepository", "Video encontrado: $name, URI: $contentUri")

                mediaList.add(MediaFile(contentUri, name, duration, null, ""))
            }
        }

        return mediaList
    }



    /**
     * Obtener archivos de audio desde el almacenamiento del dispositivo.
     */
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


/*
*
*  /**
     * Obtener archivos multimedia desde la carpeta res/raw.
     */
    fun getMediaFilesFromRaw(): List<MediaFile> {
        val rawFiles = listOf(
            Pair(R.raw.video_1, "Video 1"),
            Pair(R.raw.video_2, "Video 2"),
            Pair(R.raw.video_3, "Video 3"),
            Pair(R.raw.video_4, "Video 4")
        )

        return rawFiles.map { (resId, title) ->
            val uri = Uri.parse("android.resource://${context.packageName}/$resId")
            MediaFile(uri, title, getDurationFromRaw(uri), null, "")
        }
    }

    /**
     * Obtener la duración de un video en res/raw.
     */
    private fun getDurationFromRaw(uri: Uri): Long {
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(context, uri) // ✅ FIX: setDataSource correcto
            val duration =
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong()
            duration ?: 0L
        } catch (e: Exception) {
            Log.e("MediaRepository", "Error obteniendo duración: ${e.message}")
            0L
        } finally {
            retriever.release()
        }
    }

    /**
     * Obtener miniatura de un video.
     */
    fun getThumbnail(uri: Uri): Bitmap? {
        return try {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(context, uri)
            retriever.frameAtTime // O usa un frame específico
        } catch (e: Exception) {
            null // Usa una imagen genérica si es necesario
        }
    }
*/