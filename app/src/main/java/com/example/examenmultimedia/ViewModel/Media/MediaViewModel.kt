import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.examenmultimedia.Model.Utils.MediaFile
import com.example.examenmultimedia.Model.Utils.MediaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


/*✅ MediaViewModel maneja:
1️⃣ Carga de archivos multimedia desde el almacenamiento del dispositivo o res/raw.
2️⃣ Gestión del estado de los archivos (mantiene una lista de videos y audios).
3️⃣ Interacción con MediaRepository para obtener datos de MediaStore y res/raw.*/


class MediaViewModel(private val mediaRepository: MediaRepository) : ViewModel() {

    private val _mediaFiles = MutableStateFlow<List<MediaFile>>(emptyList())
    val mediaFiles: StateFlow<List<MediaFile>> = _mediaFiles.asStateFlow()

    private val _source = MutableStateFlow(MediaSource.DEVICE)
    val source: StateFlow<MediaSource> = _source.asStateFlow()

    init {
        setSource(MediaSource.DEVICE)
    }

    fun setSource(source: MediaSource) {
        _source.value = source
        when (source) {
            MediaSource.DEVICE -> loadMediaFilesFromDevice()
            MediaSource.RAW -> loadMediaFilesFromRaw()
        }
    }

    private fun loadMediaFilesFromDevice() {
        viewModelScope.launch {
            _mediaFiles.value = mediaRepository.getMediaFilesFromMediaStore()
        }
    }

    private fun loadMediaFilesFromRaw() {
        _mediaFiles.value = mediaRepository.getMediaFilesFromRaw()
    }
}

enum class MediaSource {
    DEVICE, RAW
}
