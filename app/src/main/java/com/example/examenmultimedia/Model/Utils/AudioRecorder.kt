package com.example.examenmultimedia.Model.Utils


import android.media.MediaRecorder
import android.util.Log
import java.io.File

class AudioRecorder {
    private var mediaRecorder: MediaRecorder? = null
    private var outputFile: File? = null
    private var isRecording = false

    /**
     * Inicia la grabación de audio.
     * @param file Archivo donde se guardará la grabación.
     */
    fun startRecording(file: File) {
        if (isRecording) {
            Log.w("AudioRecorder", "Ya se está grabando audio")
            return
        }

        try {
            outputFile = file
            mediaRecorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC) // Fuente: Micrófono
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4) // Formato de salida
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)   // Codificación de audio
                setOutputFile(file.absolutePath)                  // Archivo de salida
                prepare()                                         // Prepara el MediaRecorder
                start()                                           // Inicia la grabación
            }

            isRecording = true
            Log.d("AudioRecorder", "Grabación iniciada. Archivo: ${file.absolutePath}")
        } catch (e: Exception) {
            Log.e("AudioRecorder", "Error al iniciar la grabación: ${e.message}")
            stopRecording()
        }
    }

    /**
     * Detiene la grabación de audio.
     * @return Archivo con el audio grabado o `null` si ocurrió un error.
     */
    fun stopRecording(): File? {
        if (!isRecording) {
            Log.w("AudioRecorder", "No se está grabando audio actualmente")
            return null
        }

        try {
            mediaRecorder?.apply {
                stop()    // Detiene la grabación
                release() // Libera recursos
            }
            Log.d("AudioRecorder", "Grabación detenida. Archivo guardado en: ${outputFile?.absolutePath}")
        } catch (e: Exception) {
            Log.e("AudioRecorder", "Error al detener la grabación: ${e.message}")
        } finally {
            mediaRecorder = null
            isRecording = false
        }

        return outputFile
    }
}
