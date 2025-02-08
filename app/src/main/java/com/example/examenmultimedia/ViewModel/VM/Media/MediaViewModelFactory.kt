package com.example.examenmultimedia.ViewModel.VM.Media

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.examenmultimedia.Model.Utils.MediaRepository

class MediaViewModelFactory(
    private val mediaRepository: MediaRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MediaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MediaViewModel(mediaRepository) as T // ExoPlayer eliminado
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
