package com.example.examenmultimedia.Model.Utils

import android.graphics.Bitmap
import android.net.Uri

data class MediaFile(
    val uri: Uri,
    val title: String,
    val duration: Long,
    val thumbnail: Bitmap? = null // Portada opcional
)
