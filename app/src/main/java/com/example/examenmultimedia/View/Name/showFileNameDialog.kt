package com.example.examenmultimedia.View.Name

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.widget.EditText
import android.widget.Toast
import com.example.examenmultimedia.ViewModel.VM.VideoCapture.CaptureViewModel

fun showFileNameDialog(
    context: Context,
    uri: Uri,
    mimeType: String,
    directory: String,
    viewModel: CaptureViewModel
) {
    val input = EditText(context).apply {
        hint = "Ingresale un nombre "
    }

    AlertDialog.Builder(context)
        .setTitle("Guardar archivo")
        .setView(input)
        .setPositiveButton("Guardar") { _, _ ->
            val fileName = input.text.toString()
            if (fileName.isNotBlank()) {
                viewModel.saveMediaFile(context, uri, fileName, mimeType, directory)
            } else {
                Toast.makeText(context, "Nombre inválido", Toast.LENGTH_SHORT).show()
            }
        }
        .setNegativeButton("Cancelar", null)
        .show()
}

