package com.example.examenmultimedia.ViewModel

import MediaViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.media3.exoplayer.ExoPlayer
import com.example.examenmultimedia.Model.MediaRepository

class MediaViewModelFactory(
    private val mediaRepository: MediaRepository,
    private val exoPlayer: ExoPlayer
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MediaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MediaViewModel(mediaRepository, exoPlayer) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
