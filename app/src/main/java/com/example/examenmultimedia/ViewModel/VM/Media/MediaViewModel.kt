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
import kotlinx.coroutines.withContext



class MediaViewModel(private val mediaRepository: MediaRepository) : ViewModel() {

    // Lista reactiva de archivos multimedia.
    private val _mediaFiles = MutableStateFlow<List<MediaFile>>(emptyList())
    val mediaFiles: StateFlow<List<MediaFile>> = _mediaFiles.asStateFlow()

   //  Fuente actual seleccionada (Videos o Audios).
    private val _source = MutableStateFlow<MediaSource?>(null)
    val source: StateFlow<MediaSource?> = _source.asStateFlow()

    // 3 Estado de carga.
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    // 🔹 Indica si los datos se están cargando para mostrar un spinner en la UI.


    //Cambia la fuente de medios (videos/audios) y carga los datos.

    fun setSource(source: MediaSource) {
        if (_source.value == source) return // Evita recargar si ya está en la misma fuente

        viewModelScope.launch {
            _isLoading.value = true // Activa el estado de carga
            _source.value = source  //  Se actualiza  para evitar recomposición antes de tiempo

            val files = withContext(Dispatchers.IO) { // 🔹 Ejecutar en segundo plano
                try {
                    when (source) {
                        MediaSource.DEVICE -> mediaRepository.getMediaFilesFromMediaStore()
                        MediaSource.RAW -> mediaRepository.getAudioFilesFromMediaStore()
                    }
                } catch (e: Exception) {
                    Log.e("MediaViewModel", "Error cargando archivos: ${e.message}")
                    emptyList()
                }
            }

            _mediaFiles.value = files // Actualiza la lista en la UI
            _isLoading.value = false //  Oculta el indicador de carga
        }
    }
}
