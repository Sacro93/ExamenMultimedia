package com.example.examenmultimedia.Model.Utils

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MediaPlayerController(context: Context) {

    private val exoPlayer: ExoPlayer = ExoPlayer.Builder(context).build()

    private val _playerState = MutableStateFlow(PlayerState.IDLE)

    private val _duration = MutableStateFlow(0L)
    val duration: StateFlow<Long> = _duration



    fun playMedia(uri: Uri) {
        try {
            val mediaItem = MediaItem.fromUri(uri)
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
            exoPlayer.play()
            _playerState.value = PlayerState.PLAYING
            _duration.value = exoPlayer.duration
        } catch (e: Exception) {
            Log.e("MediaPlayerController", "Error al reproducir: ${e.message}")
            _playerState.value = PlayerState.IDLE
        }
    }


    fun release() {
        exoPlayer.release()
    }

    fun getPlayer(): ExoPlayer {
        return exoPlayer
    }
}


enum class PlayerState {
    IDLE, PLAYING, PAUSED, STOPPED
}
