import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.examenmultimedia.Model.MediaFile
import com.example.examenmultimedia.Model.MediaRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MediaViewModel(
    private val mediaRepository: MediaRepository,
    private val exoPlayer: ExoPlayer
) : ViewModel() {

    private val _mediaFiles = MutableStateFlow<List<MediaFile>>(emptyList())
    val mediaFiles: StateFlow<List<MediaFile>> = _mediaFiles.asStateFlow()

    private val _playerState = MutableStateFlow(PlayerState.IDLE)
    val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()

    private val _progress = MutableStateFlow(0L)
    val progress: StateFlow<Long> = _progress.asStateFlow()

    private val _duration = MutableStateFlow(0L)
    val duration: StateFlow<Long> = _duration.asStateFlow()

    private val _isMuted = MutableStateFlow(false)
    val isMuted: StateFlow<Boolean> = _isMuted.asStateFlow()

    private val _source = MutableStateFlow(MediaSource.DEVICE) // Inicialmente carga desde el dispositivo
    val source: StateFlow<MediaSource> = _source.asStateFlow()

    init {
        setSource(MediaSource.DEVICE) // Cargar desde MediaStore por defecto
        setupPlayerListener()
    }

    /**
     * Establece la fuente de datos (MediaStore o res/raw)
     */
    fun setSource(source: MediaSource) {
        _source.value = source
        when (source) {
            MediaSource.DEVICE -> loadMediaFilesFromDevice()
            MediaSource.RAW -> loadMediaFilesFromRaw()
        }
    }

    /**
     * Cargar archivos desde el almacenamiento del dispositivo.
     */
    fun loadMediaFilesFromDevice() {
        viewModelScope.launch {
            _mediaFiles.value = mediaRepository.getMediaFilesFromMediaStore()
        }
    }

    /**
     * Cargar archivos desde `res/raw`.
     */
    fun loadMediaFilesFromRaw() {
        _mediaFiles.value = mediaRepository.getMediaFilesFromRaw()
    }

    /**
     * Reproducir un video.
     */
    fun playMedia(uri: Uri) {
        try {
            val mediaItem = MediaItem.fromUri(uri)
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
            exoPlayer.play()

            _playerState.value = PlayerState.PLAYING

            startProgressUpdates()
        } catch (e: Exception) {
            Log.e("MediaViewModel", "Error al reproducir el archivo: ${e.message}")
            _playerState.value = PlayerState.IDLE
        }
    }

    /**
     * Pausar la reproducción.
     */
    fun pauseMedia() {
        exoPlayer.pause()
        _playerState.value = PlayerState.PAUSED
    }

    /**
     * Detener la reproducción.
     */
    fun stopMedia() {
        exoPlayer.stop()
        _playerState.value = PlayerState.STOPPED
        _progress.value = 0L
    }

    /**
     * Ir a una posición específica en el video.
     */
    fun seekTo(position: Long) {
        exoPlayer.seekTo(position)
        _progress.value = position
    }

    /**
     * Activar o desactivar el sonido.
     */
    fun toggleMute() {
        _isMuted.value = !_isMuted.value
        exoPlayer.volume = if (_isMuted.value) 0f else 1f
    }

    /**
     * Listener para monitorear cambios en ExoPlayer.
     */
    private fun setupPlayerListener() {
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                when (state) {
                    Player.STATE_READY -> {
                        _duration.value = exoPlayer.duration.takeIf { it > 0 } ?: 0L
                        Log.d("ExoPlayer", "Reproductor listo. Duración: ${_duration.value} ms")
                    }
                    Player.STATE_ENDED -> {
                        _playerState.value = PlayerState.STOPPED
                        Log.d("ExoPlayer", "Reproducción terminada.")
                    }
                }
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _playerState.value = if (isPlaying) PlayerState.PLAYING else PlayerState.PAUSED
                Log.d("ExoPlayer", "¿Está reproduciendo? $isPlaying")
            }
        })
    }

    /**
     * Actualiza el progreso de la reproducción cada 500ms.
     */
    private fun startProgressUpdates() {
        viewModelScope.launch {
            while (_playerState.value == PlayerState.PLAYING) {
                _progress.value = exoPlayer.currentPosition
                delay(500L) // Actualiza cada 500ms
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        exoPlayer.release()
    }
}

/**
 * Estados del reproductor.
 */
enum class PlayerState {
    IDLE, PLAYING, PAUSED, STOPPED
}

/**
 * Fuentes de datos posibles.
 */
enum class MediaSource {
    DEVICE, RAW
}
