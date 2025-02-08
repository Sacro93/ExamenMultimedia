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

        val resolver: ContentResolver = context.contentResolver
        val cursor = resolver.query(uri, projection, null, null, sortOrder)

        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val nameColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
            val durationColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)

            while (it.moveToNext()) {
                val id = it.getLong(idColumn)
                val name = it.getString(nameColumn)
                val duration = it.getLong(durationColumn)

                val contentUri = ContentUris.withAppendedId(uri, id)
                val thumbnail = getThumbnail(contentUri)

                Log.d("MediaRepository", "Archivo encontrado: $name, URI: $contentUri")

                mediaList.add(MediaFile(contentUri, name, duration, null))
            }
        }

        return mediaList
    }

    /**
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
            MediaFile(uri, title, getDurationFromRaw(uri), null)
        }
    }

    /**
     * Obtener la duración de un video en res/raw.
     */
    private fun getDurationFromRaw(uri: Uri): Long {
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(context, uri)
            val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong()
            duration ?: 0L
        } catch (e: Exception) {
            0L
        } finally {
            retriever.release()
        }
    }

    /**
     * Obtener miniatura de un video.
     */
    private fun getThumbnail(uri: Uri): Bitmap? {
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(context, uri)
            retriever.embeddedPicture?.let {
                BitmapFactory.decodeByteArray(it, 0, it.size)
            }
        } catch (e: Exception) {
            null
        } finally {
            retriever.release()
        }
    }
}



