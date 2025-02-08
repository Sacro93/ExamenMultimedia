package com.example.examenmultimedia.Model.Utils

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MediaPlayerController(context: Context) {

    private val exoPlayer: ExoPlayer = ExoPlayer.Builder(context).build()

    private val _playerState = MutableStateFlow(PlayerState.IDLE)

    private val _progress = MutableStateFlow(0L)

    private val _duration = MutableStateFlow(0L)
    val duration: StateFlow<Long> = _duration

    private val _isMuted = MutableStateFlow(false)

    init {
        setupPlayerListener()
    }

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



    private fun setupPlayerListener() {
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                when (state) {
                    Player.STATE_READY -> _duration.value = exoPlayer.duration
                    Player.STATE_ENDED -> _playerState.value = PlayerState.STOPPED
                }
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _playerState.value = if (isPlaying) PlayerState.PLAYING else PlayerState.PAUSED
            }
        })
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
