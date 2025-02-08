package com.example.examenmultimedia.ViewModel.VM.Media

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.examenmultimedia.Model.Utils.MediaFile
import com.example.examenmultimedia.Model.Utils.MediaRepository
import com.example.examenmultimedia.Model.Utils.MediaSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


/*✅ com.example.examenmultimedia.ViewModel.VM.Media.MediaViewModel maneja:
1️⃣ Carga de archivos multimedia desde el almacenamiento del dispositivo o res/raw.
2️⃣ Gestión del estado de los archivos (mantiene una lista de videos y audios).
3️⃣ Interacción con MediaRepository para obtener datos de MediaStore y res/raw.*/


class MediaViewModel(private val mediaRepository: MediaRepository) : ViewModel() {

    private val _mediaFiles = MutableStateFlow<List<MediaFile>>(emptyList())
    val mediaFiles: StateFlow<List<MediaFile>> = _mediaFiles.asStateFlow()

    private val _source = MutableStateFlow(MediaSource.DEVICE) // Fuente actual (videos o audios)
    val source: StateFlow<MediaSource> = _source.asStateFlow()

    private val _isLoading = MutableStateFlow(false) // Estado de carga
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()


    /**
     * Cambia la fuente de medios (videos/audios) y carga los datos.
     */
    fun setSource(source: MediaSource) {
        if (_source.value == source) return // Evita recargar si ya está en la misma fuente
        _source.value = source
        loadMediaFiles(source)
    }

    /**
     * Carga los archivos multimedia en segundo plano.
     */
    private fun loadMediaFiles(source: MediaSource) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true // 🔄 Muestra el indicador de carga

            val files = try {
                when (source) {
                    MediaSource.DEVICE -> mediaRepository.getMediaFilesFromMediaStore() // 🔹 Obtiene videos
                    MediaSource.RAW -> mediaRepository.getAudioFilesFromMediaStore() // 🔹 Obtiene audios
                }
            } catch (e: Exception) {
                Log.e(
                    "com.example.examenmultimedia.ViewModel.VM.Media.MediaViewModel",
                    "Error cargando archivos: ${e.message}"
                )
                emptyList()
            }

            _mediaFiles.value = files // 📁 Actualiza la lista en la UI
            _isLoading.value = false // ✅ Oculta el indicador de carga
        }
    }


}